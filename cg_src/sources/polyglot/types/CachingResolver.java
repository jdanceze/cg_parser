package polyglot.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import polyglot.frontend.ExtensionInfo;
import polyglot.main.Report;
import polyglot.util.CollectionUtil;
import polyglot.util.StringUtil;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/CachingResolver.class */
public class CachingResolver implements TopLevelResolver {
    TopLevelResolver inner;
    Map cache = new HashMap();
    Map packageCache = new HashMap();
    ExtensionInfo extInfo;
    static Object NOT_FOUND = new Object();
    private static final Collection TOPICS = CollectionUtil.list(Report.types, Report.resolver);

    public CachingResolver(TopLevelResolver inner, ExtensionInfo extInfo) {
        this.inner = inner;
        this.extInfo = extInfo;
    }

    public TopLevelResolver inner() {
        return this.inner;
    }

    public String toString() {
        return new StringBuffer().append("(cache ").append(this.inner.toString()).append(")").toString();
    }

    @Override // polyglot.types.TopLevelResolver
    public boolean packageExists(String name) {
        Boolean b = (Boolean) this.packageCache.get(name);
        if (b != null) {
            return b.booleanValue();
        }
        String prefix = StringUtil.getPackageComponent(name);
        if (this.packageCache.get(prefix) == Boolean.FALSE) {
            this.packageCache.put(name, Boolean.FALSE);
            return false;
        }
        boolean exists = this.inner.packageExists(name);
        if (exists) {
            this.packageCache.put(name, Boolean.TRUE);
            do {
                this.packageCache.put(prefix, Boolean.TRUE);
                prefix = StringUtil.getPackageComponent(prefix);
            } while (!prefix.equals(""));
        } else {
            this.packageCache.put(name, Boolean.FALSE);
        }
        return exists;
    }

    protected void cachePackage(Package p) {
        if (p != null) {
            this.packageCache.put(p.fullName(), Boolean.TRUE);
            cachePackage(p.prefix());
        }
    }

    @Override // polyglot.types.Resolver
    public Named find(String name) throws SemanticException {
        if (Report.should_report(TOPICS, 2)) {
            Report.report(2, new StringBuffer().append("CachingResolver: find: ").append(name).toString());
        }
        Object o = this.cache.get(name);
        if (o == NOT_FOUND) {
            throw new NoClassException(name);
        }
        Named q = (Named) o;
        if (q == null) {
            if (Report.should_report(TOPICS, 3)) {
                Report.report(3, new StringBuffer().append("CachingResolver: not cached: ").append(name).toString());
            }
            try {
                q = this.inner.find(name);
                if (q instanceof ClassType) {
                    Package p = ((ClassType) q).package_();
                    cachePackage(p);
                }
                addNamed(name, q);
                if (Report.should_report(TOPICS, 3)) {
                    Report.report(3, new StringBuffer().append("CachingResolver: loaded: ").append(name).toString());
                }
            } catch (NoClassException e) {
                this.cache.put(name, NOT_FOUND);
                throw e;
            }
        } else if (Report.should_report(TOPICS, 3)) {
            Report.report(3, new StringBuffer().append("CachingResolver: cached: ").append(name).toString());
        }
        if (q instanceof ParsedClassType) {
            this.extInfo.addDependencyToCurrentJob(((ParsedClassType) q).fromSource());
        }
        return q;
    }

    public Type checkType(String name) {
        return (Type) check(name);
    }

    public Named check(String name) {
        Object o = this.cache.get(name);
        if (o == NOT_FOUND) {
            return null;
        }
        return (Named) this.cache.get(name);
    }

    public void install(String name, Named q) {
        this.cache.put(name, q);
    }

    public void addNamed(String name, Named q) throws SemanticException {
        install(name, q);
        if ((q instanceof Type) && packageExists(name)) {
            throw new SemanticException(new StringBuffer().append("Type \"").append(name).append("\" clashes with package of the same name.").toString(), q.position());
        }
    }
}
