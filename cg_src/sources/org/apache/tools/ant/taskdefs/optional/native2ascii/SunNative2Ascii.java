package org.apache.tools.ant.taskdefs.optional.native2ascii;

import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.optional.Native2Ascii;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/native2ascii/SunNative2Ascii.class */
public final class SunNative2Ascii extends DefaultNative2Ascii {
    public static final String IMPLEMENTATION_NAME = "sun";
    private static final String SUN_TOOLS_NATIVE2ASCII_MAIN = "sun.tools.native2ascii.Main";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.optional.native2ascii.DefaultNative2Ascii
    public void setup(Commandline cmd, Native2Ascii args) throws BuildException {
        if (args.getReverse()) {
            cmd.createArgument().setValue("-reverse");
        }
        super.setup(cmd, args);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.native2ascii.DefaultNative2Ascii
    protected boolean run(Commandline cmd, ProjectComponent log) throws BuildException {
        try {
            Class<?> n2aMain = Class.forName(SUN_TOOLS_NATIVE2ASCII_MAIN);
            Method convert = n2aMain.getMethod("convert", String[].class);
            return Boolean.TRUE.equals(convert.invoke(n2aMain.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), cmd.getArguments()));
        } catch (NoSuchMethodException e) {
            throw new BuildException("Could not find convert() method in %s", SUN_TOOLS_NATIVE2ASCII_MAIN);
        } catch (BuildException ex) {
            throw ex;
        } catch (Exception ex2) {
            throw new BuildException("Error starting Sun's native2ascii: ", ex2);
        }
    }
}
