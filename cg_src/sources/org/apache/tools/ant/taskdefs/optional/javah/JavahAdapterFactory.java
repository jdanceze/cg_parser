package org.apache.tools.ant.taskdefs.optional.javah;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.JavaEnvUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/javah/JavahAdapterFactory.class */
public class JavahAdapterFactory {
    public static String getDefault() {
        if (JavaEnvUtils.isKaffe()) {
            return Kaffeh.IMPLEMENTATION_NAME;
        }
        if (JavaEnvUtils.isGij()) {
            return Gcjh.IMPLEMENTATION_NAME;
        }
        return "forking";
    }

    public static JavahAdapter getAdapter(String choice, ProjectComponent log) throws BuildException {
        return getAdapter(choice, log, null);
    }

    public static JavahAdapter getAdapter(String choice, ProjectComponent log, Path classpath) throws BuildException {
        if ((JavaEnvUtils.isKaffe() && choice == null) || Kaffeh.IMPLEMENTATION_NAME.equals(choice)) {
            return new Kaffeh();
        }
        if ((JavaEnvUtils.isGij() && choice == null) || Gcjh.IMPLEMENTATION_NAME.equals(choice)) {
            return new Gcjh();
        }
        if (JavaEnvUtils.isAtLeastJavaVersion(JavaEnvUtils.JAVA_10) && (choice == null || "forking".equals(choice))) {
            throw new BuildException("javah does not exist under Java 10 and higher, use the javac task with nativeHeaderDir instead");
        }
        if ("forking".equals(choice)) {
            return new ForkingJavah();
        }
        if ("sun".equals(choice)) {
            return new SunJavah();
        }
        if (choice != null) {
            return resolveClassName(choice, log.getProject().createClassLoader(classpath));
        }
        return new ForkingJavah();
    }

    private static JavahAdapter resolveClassName(String className, ClassLoader loader) throws BuildException {
        return (JavahAdapter) ClasspathUtils.newInstance(className, loader != null ? loader : JavahAdapterFactory.class.getClassLoader(), JavahAdapter.class);
    }
}
