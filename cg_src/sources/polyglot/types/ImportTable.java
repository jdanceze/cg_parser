package polyglot.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import polyglot.main.Report;
import polyglot.util.CollectionUtil;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.StringUtil;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/ImportTable.class */
public class ImportTable extends ClassResolver {
    protected TypeSystem ts;
    protected Resolver resolver;
    protected List packageImports;
    protected Map map;
    protected List lazyImports;
    protected List classImports;
    protected String sourceName;
    protected Position sourcePos;
    protected Package pkg;
    private static final Object NOT_FOUND = "NOT FOUND";
    private static final Collection TOPICS = CollectionUtil.list(Report.types, Report.resolver, Report.imports);

    public ImportTable(TypeSystem ts, Resolver base, Package pkg) {
        this(ts, base, pkg, null);
    }

    public ImportTable(TypeSystem ts, Resolver base, Package pkg, String src) {
        this.resolver = base;
        this.ts = ts;
        this.sourceName = src;
        this.sourcePos = src != null ? new Position(src) : null;
        this.pkg = pkg;
        this.map = new HashMap();
        this.packageImports = new ArrayList();
        this.lazyImports = new ArrayList();
        this.classImports = new ArrayList();
    }

    public Package package_() {
        return this.pkg;
    }

    public void addClassImport(String className) {
        if (Report.should_report(TOPICS, 2)) {
            Report.report(2, new StringBuffer().append(this).append(": lazy import ").append(className).toString());
        }
        this.lazyImports.add(className);
        this.classImports.add(className);
    }

    public void addPackageImport(String pkgName) {
        if ((this.pkg != null && this.pkg.fullName().equals(pkgName)) || this.ts.defaultPackageImports().contains(pkgName) || this.packageImports.contains(pkgName)) {
            return;
        }
        this.packageImports.add(pkgName);
    }

    public List packageImports() {
        return this.packageImports;
    }

    public List classImports() {
        return this.classImports;
    }

    public String sourceName() {
        return this.sourceName;
    }

    protected Named cachedFind(String name) throws SemanticException {
        Object res = this.map.get(name);
        if (res != null) {
            return (Named) res;
        }
        Named t = this.resolver.find(name);
        this.map.put(name, t);
        return t;
    }

    @Override // polyglot.types.ClassResolver, polyglot.types.Resolver
    public Named find(String name) throws SemanticException {
        Named n;
        if (Report.should_report(TOPICS, 2)) {
            Report.report(2, new StringBuffer().append(this).append(".find(").append(name).append(")").toString());
        }
        lazyImport();
        if (!StringUtil.isNameShort(name)) {
            return this.resolver.find(name);
        }
        Object res = this.map.get(name);
        if (res != null) {
            if (res == NOT_FOUND) {
                throw new NoClassException(name, this.sourcePos);
            }
            return (Named) res;
        }
        try {
            if (this.pkg != null && (n = findInPkg(name, this.pkg.fullName())) != null) {
                if (Report.should_report(TOPICS, 3)) {
                    Report.report(3, new StringBuffer().append(this).append(".find(").append(name).append("): found in current package").toString());
                }
                this.map.put(name, n);
                return n;
            }
            List<String> imports = new ArrayList(this.packageImports.size() + 5);
            imports.addAll(this.ts.defaultPackageImports());
            imports.addAll(this.packageImports);
            Named resolved = null;
            for (String pkgName : imports) {
                Named n2 = findInPkg(name, pkgName);
                if (n2 != null) {
                    if (resolved == null) {
                        resolved = n2;
                    } else {
                        throw new SemanticException(new StringBuffer().append("Reference to \"").append(name).append("\" is ambiguous; both ").append(resolved.fullName()).append(" and ").append(n2.fullName()).append(" match.").toString());
                    }
                }
            }
            if (resolved == null) {
                resolved = this.resolver.find(name);
                if (!isVisibleFrom(resolved, "")) {
                    throw new NoClassException(name, this.sourcePos);
                }
            }
            if (Report.should_report(TOPICS, 3)) {
                Report.report(3, new StringBuffer().append(this).append(".find(").append(name).append("): found as ").append(resolved.fullName()).toString());
            }
            this.map.put(name, resolved);
            return resolved;
        } catch (NoClassException e) {
            if (Report.should_report(TOPICS, 3)) {
                Report.report(3, new StringBuffer().append(this).append(".find(").append(name).append("): didn't find it").toString());
            }
            this.map.put(name, NOT_FOUND);
            throw e;
        }
    }

    protected Named findInPkg(String name, String pkgName) throws SemanticException {
        String fullName = new StringBuffer().append(pkgName).append(".").append(name).toString();
        try {
            Named n = this.resolver.find(pkgName);
            if (n instanceof ClassType) {
                return this.ts.classContextResolver((ClassType) n).find(name);
            }
        } catch (NoClassException e) {
        }
        try {
            Named n2 = this.resolver.find(fullName);
            if (isVisibleFrom(n2, pkgName)) {
                return n2;
            }
            return null;
        } catch (NoClassException e2) {
            return null;
        }
    }

    protected boolean isVisibleFrom(Named n, String pkgName) {
        boolean isVisible;
        boolean inSamePackage = (this.pkg != null && this.pkg.fullName().equals(pkgName)) || (this.pkg == null && pkgName.equals(""));
        if (n instanceof Type) {
            Type t = (Type) n;
            isVisible = !t.isClass() || t.toClass().flags().isPublic() || inSamePackage;
        } else {
            isVisible = true;
        }
        return isVisible;
    }

    protected void lazyImport() throws SemanticException {
        if (this.lazyImports.isEmpty()) {
            return;
        }
        loop0: for (int i = 0; i < this.lazyImports.size(); i++) {
            String longName = (String) this.lazyImports.get(i);
            if (Report.should_report(TOPICS, 2)) {
                Report.report(2, new StringBuffer().append(this).append(": import ").append(longName).toString());
            }
            try {
                StringTokenizer st = new StringTokenizer(longName, ".");
                StringBuffer name = new StringBuffer();
                Named t = null;
                while (st.hasMoreTokens()) {
                    String s = st.nextToken();
                    name.append(s);
                    try {
                        t = cachedFind(name.toString());
                    } catch (SemanticException e) {
                        if (!st.hasMoreTokens()) {
                            throw e;
                        }
                        name.append(".");
                    }
                    if (st.hasMoreTokens()) {
                        if (t instanceof ClassType) {
                            ClassType ct = (ClassType) t;
                            while (st.hasMoreTokens()) {
                                String n = st.nextToken();
                                Named findMemberClass = this.ts.findMemberClass(ct, n);
                                ct = findMemberClass;
                                t = findMemberClass;
                                this.map.put(n, ct);
                            }
                        } else {
                            throw new InternalCompilerError(new StringBuffer().append("Qualified type \"").append(t).append("\" is not a class type.").toString(), this.sourcePos);
                            break loop0;
                        }
                    } else {
                        break;
                    }
                }
                String shortName = StringUtil.getShortNameComponent(longName);
                if (Report.should_report(TOPICS, 2)) {
                    Report.report(2, new StringBuffer().append(this).append(": import ").append(shortName).append(" as ").append(t).toString());
                }
                if (this.map.containsKey(shortName)) {
                    Named s2 = (Named) this.map.get(shortName);
                    if (!this.ts.equals(s2, t)) {
                        throw new SemanticException(new StringBuffer().append("Class ").append(shortName).append(" already defined as ").append(this.map.get(shortName)).toString(), this.sourcePos);
                    }
                }
                this.map.put(shortName, t);
            } catch (SemanticException e2) {
                if (e2.position == null) {
                    e2.position = this.sourcePos;
                }
                throw e2;
            }
        }
        this.lazyImports = new ArrayList();
    }

    public String toString() {
        if (this.sourceName != null) {
            return new StringBuffer().append("(import ").append(this.sourceName).append(")").toString();
        }
        return "(import)";
    }
}
