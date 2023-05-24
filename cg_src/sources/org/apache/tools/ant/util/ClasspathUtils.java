package org.apache.tools.ant.util;

import java.lang.reflect.InvocationTargetException;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.rmic.RmicAdapterFactory;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/ClasspathUtils.class */
public class ClasspathUtils {
    public static final String REUSE_LOADER_REF = "ant.reuse.loader";

    public static ClassLoader getClassLoaderForPath(Project p, Reference ref) {
        return getClassLoaderForPath(p, ref, false);
    }

    public static ClassLoader getClassLoaderForPath(Project p, Reference ref, boolean reverseLoader) {
        String pathId = ref.getRefId();
        Object path = p.getReference(pathId);
        if (!(path instanceof Path)) {
            throw new BuildException("The specified classpathref %s does not reference a Path.", pathId);
        }
        String loaderId = MagicNames.REFID_CLASSPATH_LOADER_PREFIX + pathId;
        return getClassLoaderForPath(p, (Path) path, loaderId, reverseLoader);
    }

    public static ClassLoader getClassLoaderForPath(Project p, Path path, String loaderId) {
        return getClassLoaderForPath(p, path, loaderId, false);
    }

    public static ClassLoader getClassLoaderForPath(Project p, Path path, String loaderId, boolean reverseLoader) {
        return getClassLoaderForPath(p, path, loaderId, reverseLoader, isMagicPropertySet(p));
    }

    public static ClassLoader getClassLoaderForPath(Project p, Path path, String loaderId, boolean reverseLoader, boolean reuseLoader) {
        ClassLoader cl = null;
        if (loaderId != null && reuseLoader) {
            Object reusedLoader = p.getReference(loaderId);
            if (reusedLoader != null && !(reusedLoader instanceof ClassLoader)) {
                throw new BuildException("The specified loader id %s does not reference a class loader", loaderId);
            }
            cl = (ClassLoader) reusedLoader;
        }
        if (cl == null) {
            cl = getUniqueClassLoaderForPath(p, path, reverseLoader);
            if (loaderId != null && reuseLoader) {
                p.addReference(loaderId, cl);
            }
        }
        return cl;
    }

    public static ClassLoader getUniqueClassLoaderForPath(Project p, Path path, boolean reverseLoader) {
        AntClassLoader acl = p.createClassLoader(path);
        if (reverseLoader) {
            acl.setParentFirst(false);
            acl.addJavaLibraries();
        }
        return acl;
    }

    public static Object newInstance(String className, ClassLoader userDefinedLoader) {
        return newInstance(className, userDefinedLoader, Object.class);
    }

    public static <T> T newInstance(String className, ClassLoader userDefinedLoader, Class<T> expectedType) {
        try {
            T o = (T) Class.forName(className, true, userDefinedLoader).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            if (!expectedType.isInstance(o)) {
                throw new BuildException("Class of unexpected Type: %s expected : %s", className, expectedType);
            }
            return o;
        } catch (ClassNotFoundException e) {
            throw new BuildException(RmicAdapterFactory.ERROR_UNKNOWN_COMPILER + className, e);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e2) {
            throw new BuildException("Could not instantiate " + className + ". Specified class should have a public constructor.", e2);
        } catch (InstantiationException e3) {
            throw new BuildException("Could not instantiate " + className + ". Specified class should have a no argument constructor.", e3);
        } catch (LinkageError e4) {
            throw new BuildException("Class " + className + " could not be loaded because of an invalid dependency.", e4);
        }
    }

    public static Delegate getDelegate(ProjectComponent component) {
        return new Delegate(component);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isMagicPropertySet(Project p) {
        return p.getProperty("ant.reuse.loader") != null;
    }

    private ClasspathUtils() {
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/ClasspathUtils$Delegate.class */
    public static class Delegate {
        private final ProjectComponent component;
        private Path classpath;
        private String classpathId;
        private String className;
        private String loaderId;
        private boolean reverseLoader = false;

        Delegate(ProjectComponent component) {
            this.component = component;
        }

        public void setClasspath(Path classpath) {
            if (this.classpath == null) {
                this.classpath = classpath;
            } else {
                this.classpath.append(classpath);
            }
        }

        public Path createClasspath() {
            if (this.classpath == null) {
                this.classpath = new Path(this.component.getProject());
            }
            return this.classpath.createPath();
        }

        public void setClassname(String fcqn) {
            this.className = fcqn;
        }

        public void setClasspathref(Reference r) {
            this.classpathId = r.getRefId();
            createClasspath().setRefid(r);
        }

        public void setReverseLoader(boolean reverseLoader) {
            this.reverseLoader = reverseLoader;
        }

        public void setLoaderRef(Reference r) {
            this.loaderId = r.getRefId();
        }

        public ClassLoader getClassLoader() {
            return ClasspathUtils.getClassLoaderForPath(getContextProject(), this.classpath, getClassLoadId(), this.reverseLoader, this.loaderId != null || ClasspathUtils.isMagicPropertySet(getContextProject()));
        }

        private Project getContextProject() {
            return this.component.getProject();
        }

        public String getClassLoadId() {
            if (this.loaderId == null && this.classpathId != null) {
                return MagicNames.REFID_CLASSPATH_LOADER_PREFIX + this.classpathId;
            }
            return this.loaderId;
        }

        public Object newInstance() {
            return ClasspathUtils.newInstance(this.className, getClassLoader());
        }

        public Path getClasspath() {
            return this.classpath;
        }

        public boolean isReverseLoader() {
            return this.reverseLoader;
        }
    }
}
