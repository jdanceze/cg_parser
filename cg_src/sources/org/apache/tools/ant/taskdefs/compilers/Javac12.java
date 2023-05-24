package org.apache.tools.ant.taskdefs.compilers;

import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.JavaEnvUtils;
@Deprecated
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/compilers/Javac12.class */
public class Javac12 extends DefaultCompilerAdapter {
    protected static final String CLASSIC_COMPILER_CLASSNAME = "sun.tools.javac.Main";

    @Override // org.apache.tools.ant.taskdefs.compilers.CompilerAdapter
    public boolean execute() throws BuildException {
        this.attributes.log("Using classic compiler", 3);
        Commandline cmd = setupJavacCommand(true);
        try {
            OutputStream logstr = new LogOutputStream((Task) this.attributes, 1);
            Class<?> c = Class.forName(CLASSIC_COMPILER_CLASSNAME);
            Constructor<?> cons = c.getConstructor(OutputStream.class, String.class);
            Object compiler = cons.newInstance(logstr, "javac");
            Method compile = c.getMethod("compile", String[].class);
            boolean booleanValue = ((Boolean) compile.invoke(compiler, cmd.getArguments())).booleanValue();
            logstr.close();
            return booleanValue;
        } catch (ClassNotFoundException e) {
            throw new BuildException("Cannot use classic compiler, as it is not available. \n A common solution is to set the environment variable JAVA_HOME to your jdk directory.\nIt is currently set to \"" + JavaEnvUtils.getJavaHome() + "\"", this.location);
        } catch (Exception ex) {
            if (ex instanceof BuildException) {
                throw ((BuildException) ex);
            }
            throw new BuildException("Error starting classic compiler: ", ex, this.location);
        }
    }
}
