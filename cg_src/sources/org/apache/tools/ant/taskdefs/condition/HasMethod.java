package org.apache.tools.ant.taskdefs.condition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/HasMethod.class */
public class HasMethod extends ProjectComponent implements Condition {
    private String classname;
    private String method;
    private String field;
    private Path classpath;
    private AntClassLoader loader;
    private boolean ignoreSystemClasses = false;

    public void setClasspath(Path classpath) {
        createClasspath().append(classpath);
    }

    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        return this.classpath.createPath();
    }

    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setIgnoreSystemClasses(boolean ignoreSystemClasses) {
        this.ignoreSystemClasses = ignoreSystemClasses;
    }

    private Class<?> loadClass(String classname) {
        try {
            if (this.ignoreSystemClasses) {
                this.loader = getProject().createClassLoader(this.classpath);
                this.loader.setParentFirst(false);
                this.loader.addJavaLibraries();
                try {
                    return this.loader.findClass(classname);
                } catch (SecurityException se) {
                    throw new BuildException("class \"" + classname + "\" was found but a SecurityException has been raised while loading it", se);
                }
            } else if (this.loader != null) {
                return this.loader.loadClass(classname);
            } else {
                ClassLoader l = getClass().getClassLoader();
                if (l != null) {
                    return Class.forName(classname, true, l);
                }
                return Class.forName(classname);
            }
        } catch (ClassNotFoundException e) {
            throw new BuildException("class \"" + classname + "\" was not found");
        } catch (NoClassDefFoundError e2) {
            throw new BuildException("Could not load dependent class \"" + e2.getMessage() + "\" for class \"" + classname + "\"");
        }
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        if (this.classname == null) {
            throw new BuildException("No classname defined");
        }
        ClassLoader preLoadClass = this.loader;
        try {
            Class<?> clazz = loadClass(this.classname);
            if (this.method != null) {
                boolean isMethodFound = isMethodFound(clazz);
                if (preLoadClass != this.loader && this.loader != null) {
                    this.loader.cleanup();
                    this.loader = null;
                }
                return isMethodFound;
            } else if (this.field != null) {
                boolean isFieldFound = isFieldFound(clazz);
                if (preLoadClass != this.loader && this.loader != null) {
                    this.loader.cleanup();
                    this.loader = null;
                }
                return isFieldFound;
            } else {
                throw new BuildException("Neither method nor field defined");
            }
        } catch (Throwable th) {
            if (preLoadClass != this.loader && this.loader != null) {
                this.loader.cleanup();
                this.loader = null;
            }
            throw th;
        }
    }

    private boolean isFieldFound(Class<?> clazz) {
        Field[] declaredFields;
        for (Field fieldEntry : clazz.getDeclaredFields()) {
            if (fieldEntry.getName().equals(this.field)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMethodFound(Class<?> clazz) {
        Method[] declaredMethods;
        for (Method methodEntry : clazz.getDeclaredMethods()) {
            if (methodEntry.getName().equals(this.method)) {
                return true;
            }
        }
        return false;
    }
}
