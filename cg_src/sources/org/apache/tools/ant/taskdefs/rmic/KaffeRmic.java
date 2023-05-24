package org.apache.tools.ant.taskdefs.rmic;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.ExecuteJava;
import org.apache.tools.ant.taskdefs.optional.sos.SOSCmd;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/rmic/KaffeRmic.class */
public class KaffeRmic extends DefaultRmicAdapter {
    private static final String[] RMIC_CLASSNAMES = {"gnu.classpath.tools.rmi.rmic.RMIC", "gnu.java.rmi.rmic.RMIC", "kaffe.rmi.rmic.RMIC"};
    public static final String COMPILER_NAME = "kaffe";

    @Override // org.apache.tools.ant.taskdefs.rmic.DefaultRmicAdapter
    protected boolean areIiopAndIdlSupported() {
        return true;
    }

    @Override // org.apache.tools.ant.taskdefs.rmic.RmicAdapter
    public boolean execute() throws BuildException {
        String[] strArr;
        getRmic().log("Using Kaffe rmic", 3);
        Commandline cmd = setupRmicCommand();
        Class<?> c = getRmicClass();
        if (c == null) {
            StringBuilder buf = new StringBuilder("Cannot use Kaffe rmic, as it is not available.  None of ");
            for (String className : RMIC_CLASSNAMES) {
                if (buf.length() > 0) {
                    buf.append(", ");
                }
                buf.append(className);
            }
            buf.append(" have been found. A common solution is to set the environment variable JAVA_HOME or CLASSPATH.");
            throw new BuildException(buf.toString(), getRmic().getLocation());
        }
        cmd.setExecutable(c.getName());
        if (!c.getName().equals(RMIC_CLASSNAMES[RMIC_CLASSNAMES.length - 1])) {
            cmd.createArgument().setValue(SOSCmd.FLAG_VERBOSE);
            getRmic().log(Commandline.describeCommand(cmd));
        }
        ExecuteJava ej = new ExecuteJava();
        ej.setJavaCommand(cmd);
        return ej.fork(getRmic()) == 0;
    }

    public static boolean isAvailable() {
        return getRmicClass() != null;
    }

    private static Class<?> getRmicClass() {
        String[] strArr;
        for (String className : RMIC_CLASSNAMES) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
            }
        }
        return null;
    }
}
