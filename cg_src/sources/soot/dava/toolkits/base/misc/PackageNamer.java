package soot.dava.toolkits.base.misc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.dava.Dava;
import soot.jimple.Jimple;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/misc/PackageNamer.class */
public class PackageNamer {
    private static final Logger logger = LoggerFactory.getLogger(PackageNamer.class);
    private boolean fixed = false;
    private final ArrayList<NameHolder> appRoots = new ArrayList<>();
    private final ArrayList<NameHolder> otherRoots = new ArrayList<>();
    private final HashSet<String> keywords = new HashSet<>();
    private char fileSep;
    private String classPath;
    private String pathSep;

    public PackageNamer(Singletons.Global g) {
    }

    public static PackageNamer v() {
        return G.v().soot_dava_toolkits_base_misc_PackageNamer();
    }

    public boolean has_FixedNames() {
        return this.fixed;
    }

    public boolean use_ShortName(String fixedPackageName, String fixedShortClassName) {
        IterableSet packageContext;
        if (!this.fixed) {
            return false;
        }
        if (fixedPackageName.equals(Dava.v().get_CurrentPackage()) || (packageContext = Dava.v().get_CurrentPackageContext()) == null) {
            return true;
        }
        IterableSet packageContext2 = patch_PackageContext(packageContext);
        int count = 0;
        StringTokenizer st = new StringTokenizer(this.classPath, this.pathSep);
        while (st.hasMoreTokens()) {
            String classpathDir = st.nextToken();
            Iterator packIt = packageContext2.iterator();
            while (packIt.hasNext()) {
                if (package_ContainsClass(classpathDir, (String) packIt.next(), fixedShortClassName)) {
                    count++;
                    if (count > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String get_FixedClassName(String originalFullClassName) {
        if (!this.fixed) {
            return originalFullClassName;
        }
        Iterator<NameHolder> it = this.appRoots.iterator();
        while (it.hasNext()) {
            NameHolder h = it.next();
            if (h.contains_OriginalName(new StringTokenizer(originalFullClassName, "."), true)) {
                return h.get_FixedName(new StringTokenizer(originalFullClassName, "."), true);
            }
        }
        return originalFullClassName.substring(originalFullClassName.lastIndexOf(".") + 1);
    }

    public String get_FixedPackageName(String originalPackageName) {
        if (!this.fixed) {
            return originalPackageName;
        }
        if (originalPackageName.equals("")) {
            return "";
        }
        Iterator<NameHolder> it = this.appRoots.iterator();
        while (it.hasNext()) {
            NameHolder h = it.next();
            if (h.contains_OriginalName(new StringTokenizer(originalPackageName, "."), false)) {
                return h.get_FixedName(new StringTokenizer(originalPackageName, "."), false);
            }
        }
        return originalPackageName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/misc/PackageNamer$NameHolder.class */
    public class NameHolder {
        private final String originalName;
        private String packageName;
        private String className;
        private final ArrayList<NameHolder> children = new ArrayList<>();
        private NameHolder parent;
        private boolean isClass;

        public NameHolder(String name, NameHolder parent, boolean isClass) {
            this.originalName = name;
            this.className = name;
            this.packageName = name;
            this.parent = parent;
            this.isClass = isClass;
        }

        public NameHolder get_Parent() {
            return this.parent;
        }

        public void set_ClassAttr() {
            this.isClass = true;
        }

        public boolean is_Class() {
            if (this.children.isEmpty()) {
                return true;
            }
            return this.isClass;
        }

        public boolean is_Package() {
            return !this.children.isEmpty();
        }

        public String get_PackageName() {
            return this.packageName;
        }

        public String get_ClassName() {
            return this.className;
        }

        public void set_PackageName(String packageName) {
            this.packageName = packageName;
        }

        public void set_ClassName(String className) {
            this.className = className;
        }

        public String get_OriginalName() {
            return this.originalName;
        }

        public ArrayList<NameHolder> get_Children() {
            return this.children;
        }

        public String get_FixedPackageName() {
            if (this.parent == null) {
                return "";
            }
            return this.parent.retrieve_FixedPackageName();
        }

        public String retrieve_FixedPackageName() {
            if (this.parent == null) {
                return this.packageName;
            }
            return String.valueOf(this.parent.get_FixedPackageName()) + "." + this.packageName;
        }

        public String get_FixedName(StringTokenizer st, boolean forClass) {
            if (!st.nextToken().equals(this.originalName)) {
                throw new RuntimeException("Unable to resolve naming.");
            }
            return retrieve_FixedName(st, forClass);
        }

        private String retrieve_FixedName(StringTokenizer st, boolean forClass) {
            if (!st.hasMoreTokens()) {
                if (forClass) {
                    return this.className;
                }
                return this.packageName;
            }
            String subName = st.nextToken();
            Iterator<NameHolder> cit = this.children.iterator();
            while (cit.hasNext()) {
                NameHolder h = cit.next();
                if (h.get_OriginalName().equals(subName)) {
                    if (forClass) {
                        return h.retrieve_FixedName(st, forClass);
                    }
                    return String.valueOf(this.packageName) + "." + h.retrieve_FixedName(st, forClass);
                }
            }
            throw new RuntimeException("Unable to resolve naming.");
        }

        public String get_OriginalPackageName(StringTokenizer st) {
            if (!st.hasMoreTokens()) {
                return get_OriginalName();
            }
            String subName = st.nextToken();
            Iterator<NameHolder> cit = this.children.iterator();
            while (cit.hasNext()) {
                NameHolder h = cit.next();
                if (h.get_PackageName().equals(subName)) {
                    String originalSubPackageName = h.get_OriginalPackageName(st);
                    if (originalSubPackageName == null) {
                        return null;
                    }
                    return String.valueOf(get_OriginalName()) + "." + originalSubPackageName;
                }
            }
            return null;
        }

        public boolean contains_OriginalName(StringTokenizer st, boolean forClass) {
            if (!get_OriginalName().equals(st.nextToken())) {
                return false;
            }
            return finds_OriginalName(st, forClass);
        }

        private boolean finds_OriginalName(StringTokenizer st, boolean forClass) {
            if (!st.hasMoreTokens()) {
                if (forClass && is_Class()) {
                    return true;
                }
                return !forClass && is_Package();
            }
            String subName = st.nextToken();
            Iterator<NameHolder> cit = this.children.iterator();
            while (cit.hasNext()) {
                NameHolder h = cit.next();
                if (h.get_OriginalName().equals(subName)) {
                    return h.finds_OriginalName(st, forClass);
                }
            }
            return false;
        }

        public void fix_ClassNames(String curPackName) {
            if (is_Class() && PackageNamer.this.keywords.contains(this.className)) {
                String tClassName = this.className;
                if (Character.isLowerCase(this.className.charAt(0))) {
                    tClassName = String.valueOf(tClassName.substring(0, 1).toUpperCase()) + tClassName.substring(1);
                    this.className = tClassName;
                }
                int i = 0;
                while (PackageNamer.this.keywords.contains(this.className)) {
                    this.className = String.valueOf(tClassName) + "_c" + i;
                    i++;
                }
            }
            Iterator<NameHolder> it = this.children.iterator();
            while (it.hasNext()) {
                it.next().fix_ClassNames(String.valueOf(curPackName) + "." + this.packageName);
            }
        }

        public void fix_PackageNames() {
            if (is_Package() && !verify_PackageName()) {
                String tPackageName = this.packageName;
                if (Character.isUpperCase(this.packageName.charAt(0))) {
                    tPackageName = String.valueOf(tPackageName.substring(0, 1).toLowerCase()) + tPackageName.substring(1);
                    this.packageName = tPackageName;
                }
                int i = 0;
                while (!verify_PackageName()) {
                    this.packageName = String.valueOf(tPackageName) + "_p" + i;
                    i++;
                }
            }
            Iterator<NameHolder> it = this.children.iterator();
            while (it.hasNext()) {
                it.next().fix_PackageNames();
            }
        }

        public boolean verify_PackageName() {
            if (PackageNamer.this.keywords.contains(this.packageName) || siblingClashes(this.packageName)) {
                return false;
            }
            return (is_Class() && this.className.equals(this.packageName)) ? false : true;
        }

        public boolean siblingClashes(String name) {
            Iterator<NameHolder> it;
            if (this.parent != null) {
                it = this.parent.get_Children().iterator();
            } else if (!PackageNamer.this.appRoots.contains(this)) {
                throw new RuntimeException("Unable to find package siblings.");
            } else {
                it = PackageNamer.this.appRoots.iterator();
            }
            while (it.hasNext()) {
                NameHolder sibling = it.next();
                if (sibling != this) {
                    if (!sibling.is_Package() || !sibling.get_PackageName().equals(name)) {
                        if (sibling.is_Class() && sibling.get_ClassName().equals(name)) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
            return false;
        }

        public void dump(String indentation) {
            PackageNamer.logger.debug(indentation + "\"" + this.originalName + "\", \"" + this.packageName + "\", \"" + this.className + "\" (");
            if (is_Class()) {
                PackageNamer.logger.debug("c");
            }
            if (is_Package()) {
                PackageNamer.logger.debug("p");
            }
            PackageNamer.logger.debug(")");
            Iterator<NameHolder> it = this.children.iterator();
            while (it.hasNext()) {
                it.next().dump(String.valueOf(indentation) + "  ");
            }
        }
    }

    public void fixNames() {
        if (this.fixed) {
            return;
        }
        String[] keywordArray = {Jimple.ABSTRACT, "default", Jimple.IF, Jimple.PRIVATE, "this", "boolean", "do", Jimple.IMPLEMENTS, Jimple.PROTECTED, Jimple.THROW, Jimple.BREAK, "double", "import", Jimple.PUBLIC, Jimple.THROWS, "byte", "else", Jimple.INSTANCEOF, "return", Jimple.TRANSIENT, Jimple.CASE, Jimple.EXTENDS, "int", "short", "try", Jimple.CATCH, Jimple.FINAL, "interface", Jimple.STATIC, Jimple.VOID, "char", "finally", "long", Jimple.STRICTFP, Jimple.VOLATILE, "class", Jimple.FLOAT, Jimple.NATIVE, "super", "while", "const", "for", "new", "switch", "continue", Jimple.GOTO, "package", Jimple.SYNCHRONIZED, "true", "false", Jimple.NULL};
        for (String element : keywordArray) {
            this.keywords.add(element);
        }
        for (SootClass sootClass : Scene.v().getLibraryClasses()) {
            add_ClassName(sootClass.getName(), this.otherRoots);
        }
        for (SootClass sootClass2 : Scene.v().getApplicationClasses()) {
            add_ClassName(sootClass2.getName(), this.appRoots);
        }
        Iterator<NameHolder> arit = this.appRoots.iterator();
        while (arit.hasNext()) {
            arit.next().fix_ClassNames("");
        }
        Iterator<NameHolder> arit2 = this.appRoots.iterator();
        while (arit2.hasNext()) {
            arit2.next().fix_PackageNames();
        }
        this.fileSep = System.getProperty("file.separator").charAt(0);
        this.pathSep = System.getProperty("path.separator");
        this.classPath = System.getProperty("java.class.path");
        this.fixed = true;
    }

    private void add_ClassName(String className, ArrayList<NameHolder> roots) {
        ArrayList<NameHolder> children = roots;
        NameHolder curNode = null;
        StringTokenizer st = new StringTokenizer(className, ".");
        while (st.hasMoreTokens()) {
            String curName = st.nextToken();
            NameHolder child = null;
            boolean found = false;
            Iterator<NameHolder> lit = children.iterator();
            while (true) {
                if (!lit.hasNext()) {
                    break;
                }
                child = lit.next();
                if (child.get_OriginalName().equals(curName)) {
                    if (!st.hasMoreTokens()) {
                        child.set_ClassAttr();
                    }
                    found = true;
                }
            }
            if (!found) {
                child = new NameHolder(curName, curNode, !st.hasMoreTokens());
                children.add(child);
            }
            curNode = child;
            children = child.get_Children();
        }
    }

    public boolean package_ContainsClass(String classpathDir, String packageName, String className) {
        File p = new File(classpathDir);
        if (!p.exists()) {
            return false;
        }
        String packageName2 = packageName.replace('.', this.fileSep);
        if (packageName2.length() > 0 && packageName2.charAt(packageName2.length() - 1) != this.fileSep) {
            packageName2 = String.valueOf(packageName2) + this.fileSep;
        }
        String name = String.valueOf(packageName2) + className + ".class";
        if (p.isDirectory()) {
            if (classpathDir.length() > 0 && classpathDir.charAt(classpathDir.length() - 1) != this.fileSep) {
                classpathDir = String.valueOf(classpathDir) + this.fileSep;
            }
            return new File(String.valueOf(classpathDir) + name).exists();
        }
        try {
            JarFile jf = new JarFile(p);
            return jf.getJarEntry(name) != null;
        } catch (IOException e) {
            return false;
        }
    }

    IterableSet patch_PackageContext(IterableSet currentContext) {
        IterableSet newContext = new IterableSet();
        Iterator it = currentContext.iterator();
        while (it.hasNext()) {
            String currentPackage = (String) it.next();
            String newPackage = null;
            StringTokenizer st = new StringTokenizer(currentPackage, ".");
            if (!st.hasMoreTokens()) {
                newContext.add(currentPackage);
            } else {
                String firstToken = st.nextToken();
                Iterator<NameHolder> arit = this.appRoots.iterator();
                while (true) {
                    if (!arit.hasNext()) {
                        break;
                    }
                    NameHolder h = arit.next();
                    if (h.get_PackageName().equals(firstToken)) {
                        newPackage = h.get_OriginalPackageName(st);
                        break;
                    }
                }
                if (newPackage != null) {
                    newContext.add(newPackage);
                } else {
                    newContext.add(currentPackage);
                }
            }
        }
        return newContext;
    }
}
