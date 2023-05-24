package polyglot.types.reflect;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import polyglot.main.Report;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/reflect/ClassPathLoader.class */
public class ClassPathLoader {
    List classpath;
    ClassFileLoader loader;
    static Collection verbose = new HashSet();

    public ClassPathLoader(List classpath, ClassFileLoader loader) {
        this.classpath = new ArrayList(classpath);
        this.loader = loader;
    }

    public ClassPathLoader(String classpath, ClassFileLoader loader) {
        this.classpath = new ArrayList();
        StringTokenizer st = new StringTokenizer(classpath, File.pathSeparator);
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            this.classpath.add(new File(s));
        }
        this.loader = loader;
    }

    public String classpath() {
        return this.classpath.toString();
    }

    public boolean packageExists(String name) {
        for (File dir : this.classpath) {
            if (this.loader.packageExists(dir, name)) {
                return true;
            }
        }
        return false;
    }

    public ClassFile loadClass(String name) {
        if (Report.should_report(verbose, 2)) {
            Report.report(2, new StringBuffer().append("attempting to load class ").append(name).toString());
            Report.report(2, new StringBuffer().append("classpath = ").append(this.classpath).toString());
        }
        for (File dir : this.classpath) {
            ClassFile cf = this.loader.loadClass(dir, name);
            if (cf != null) {
                return cf;
            }
        }
        return null;
    }

    static {
        verbose.add(Report.loader);
    }
}
