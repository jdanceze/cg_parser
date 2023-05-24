package org.apache.tools.ant.taskdefs.compilers;

import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/compilers/Javac13.class */
public class Javac13 extends DefaultCompilerAdapter {
    private static final int MODERN_COMPILER_SUCCESS = 0;

    @Override // org.apache.tools.ant.taskdefs.compilers.CompilerAdapter
    public boolean execute() throws BuildException {
        this.attributes.log("Using modern compiler", 3);
        Commandline cmd = setupModernJavacCommand();
        try {
            Class<?> c = Class.forName("com.sun.tools.javac.Main");
            Object compiler = c.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            Method compile = c.getMethod("compile", String[].class);
            int result = ((Integer) compile.invoke(compiler, cmd.getArguments())).intValue();
            return result == 0;
        } catch (Exception ex) {
            if (ex instanceof BuildException) {
                throw ((BuildException) ex);
            }
            throw new BuildException("Error starting modern compiler", ex, this.location);
        }
    }
}
