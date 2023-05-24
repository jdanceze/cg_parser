package org.apache.tools.ant.util;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/SplitClassLoader.class */
public final class SplitClassLoader extends AntClassLoader {
    private final String[] splitClasses;

    public SplitClassLoader(ClassLoader parent, Path path, Project project, String[] splitClasses) {
        super(parent, project, path, true);
        this.splitClasses = splitClasses;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.AntClassLoader, java.lang.ClassLoader
    public synchronized Class<?> loadClass(String classname, boolean resolve) throws ClassNotFoundException {
        Class<?> theClass = findLoadedClass(classname);
        if (theClass != null) {
            return theClass;
        }
        if (isSplit(classname)) {
            Class<?> theClass2 = findClass(classname);
            if (resolve) {
                resolveClass(theClass2);
            }
            return theClass2;
        }
        return super.loadClass(classname, resolve);
    }

    private boolean isSplit(String classname) {
        String[] strArr;
        String simplename = classname.substring(classname.lastIndexOf(46) + 1);
        for (String splitClass : this.splitClasses) {
            if (simplename.equals(splitClass) || simplename.startsWith(splitClass + '$')) {
                return true;
            }
        }
        return false;
    }
}
