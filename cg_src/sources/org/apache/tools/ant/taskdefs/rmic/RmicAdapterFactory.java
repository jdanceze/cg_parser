package org.apache.tools.ant.taskdefs.rmic;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.JavaEnvUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/rmic/RmicAdapterFactory.class */
public final class RmicAdapterFactory {
    public static final String ERROR_UNKNOWN_COMPILER = "Class not found: ";
    public static final String ERROR_NOT_RMIC_ADAPTER = "Class of unexpected Type: ";
    public static final String DEFAULT_COMPILER = "default";

    private RmicAdapterFactory() {
    }

    public static RmicAdapter getRmic(String rmicType, Task task) throws BuildException {
        return getRmic(rmicType, task, null);
    }

    public static RmicAdapter getRmic(String rmicType, Task task, Path classpath) throws BuildException {
        if ("default".equalsIgnoreCase(rmicType) || rmicType.isEmpty()) {
            if (KaffeRmic.isAvailable()) {
                rmicType = "kaffe";
            } else if (JavaEnvUtils.isAtLeastJavaVersion(JavaEnvUtils.JAVA_9)) {
                rmicType = "forking";
            } else {
                rmicType = "sun";
            }
        }
        if ("sun".equalsIgnoreCase(rmicType)) {
            return new SunRmic();
        }
        if ("kaffe".equalsIgnoreCase(rmicType)) {
            return new KaffeRmic();
        }
        if (WLRmic.COMPILER_NAME.equalsIgnoreCase(rmicType)) {
            return new WLRmic();
        }
        if ("forking".equalsIgnoreCase(rmicType)) {
            return new ForkingSunRmic();
        }
        if (XNewRmic.COMPILER_NAME.equalsIgnoreCase(rmicType)) {
            return new XNewRmic();
        }
        return resolveClassName(rmicType, task.getProject().createClassLoader(classpath));
    }

    private static RmicAdapter resolveClassName(String className, ClassLoader loader) throws BuildException {
        return (RmicAdapter) ClasspathUtils.newInstance(className, loader != null ? loader : RmicAdapterFactory.class.getClassLoader(), RmicAdapter.class);
    }
}
