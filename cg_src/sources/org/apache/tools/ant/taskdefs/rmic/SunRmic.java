package org.apache.tools.ant.taskdefs.rmic;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.JavaEnvUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/rmic/SunRmic.class */
public class SunRmic extends DefaultRmicAdapter {
    public static final String RMIC_CLASSNAME = "sun.rmi.rmic.Main";
    public static final String COMPILER_NAME = "sun";
    public static final String RMIC_EXECUTABLE = "rmic";
    public static final String ERROR_NO_RMIC_ON_CLASSPATH = "Cannot use SUN rmic, as it is not available.  A common solution is to set the environment variable JAVA_HOME";
    public static final String ERROR_NO_RMIC_ON_CLASSPATH_JAVA_9 = "Cannot use SUN rmic, as it is not available.  The class we try to use is part of the jdk.rmic module which may not be. Please use the 'forking' compiler for JDK 9+";
    public static final String ERROR_RMIC_FAILED = "Error starting SUN rmic: ";

    @Override // org.apache.tools.ant.taskdefs.rmic.RmicAdapter
    public boolean execute() throws BuildException {
        getRmic().log("Using SUN rmic compiler", 3);
        Commandline cmd = setupRmicCommand();
        LogOutputStream logstr = new LogOutputStream((Task) getRmic(), 1);
        boolean success = false;
        try {
            try {
                try {
                    Class<?> c = Class.forName(RMIC_CLASSNAME);
                    Constructor<?> cons = c.getConstructor(OutputStream.class, String.class);
                    Object rmic = cons.newInstance(logstr, RMIC_EXECUTABLE);
                    Method doRmic = c.getMethod("compile", String[].class);
                    boolean ok = Boolean.TRUE.equals(doRmic.invoke(rmic, cmd.getArguments()));
                    success = true;
                    try {
                        logstr.close();
                    } catch (IOException e) {
                        if (1 != 0) {
                            throw new BuildException(e);
                        }
                    }
                    return ok;
                } catch (Exception ex) {
                    if (ex instanceof BuildException) {
                        throw ((BuildException) ex);
                    }
                    throw new BuildException(ERROR_RMIC_FAILED, ex, getRmic().getLocation());
                }
            } catch (ClassNotFoundException e2) {
                if (JavaEnvUtils.isAtLeastJavaVersion(JavaEnvUtils.JAVA_9)) {
                    throw new BuildException(ERROR_NO_RMIC_ON_CLASSPATH_JAVA_9, getRmic().getLocation());
                }
                throw new BuildException(ERROR_NO_RMIC_ON_CLASSPATH, getRmic().getLocation());
            }
        } catch (Throwable th) {
            try {
                logstr.close();
            } catch (IOException e3) {
                if (success) {
                    throw new BuildException(e3);
                }
            }
            throw th;
        }
    }

    @Override // org.apache.tools.ant.taskdefs.rmic.DefaultRmicAdapter
    protected String[] preprocessCompilerArgs(String[] compilerArgs) {
        return filterJvmCompilerArgs(compilerArgs);
    }
}
