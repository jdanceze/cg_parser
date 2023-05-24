package org.apache.tools.ant.taskdefs.optional.javah;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.optional.Javah;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.JavaEnvUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/javah/Kaffeh.class */
public class Kaffeh implements JavahAdapter {
    public static final String IMPLEMENTATION_NAME = "kaffeh";

    @Override // org.apache.tools.ant.taskdefs.optional.javah.JavahAdapter
    public boolean compile(Javah javah) throws BuildException {
        Commandline cmd = setupKaffehCommand(javah);
        try {
            Execute.runCommand(javah, cmd.getCommandline());
            return true;
        } catch (BuildException e) {
            if (!e.getMessage().contains("failed with return code")) {
                throw e;
            }
            return false;
        }
    }

    private Commandline setupKaffehCommand(Javah javah) {
        Commandline cmd = new Commandline();
        cmd.setExecutable(JavaEnvUtils.getJdkExecutable(IMPLEMENTATION_NAME));
        if (javah.getDestdir() != null) {
            cmd.createArgument().setValue("-d");
            cmd.createArgument().setFile(javah.getDestdir());
        }
        if (javah.getOutputfile() != null) {
            cmd.createArgument().setValue("-o");
            cmd.createArgument().setFile(javah.getOutputfile());
        }
        Path cp = new Path(javah.getProject());
        if (javah.getBootclasspath() != null) {
            cp.append(javah.getBootclasspath());
        }
        Path cp2 = cp.concatSystemBootClasspath(Definer.OnError.POLICY_IGNORE);
        if (javah.getClasspath() != null) {
            cp2.append(javah.getClasspath());
        }
        if (cp2.size() > 0) {
            cmd.createArgument().setValue("-classpath");
            cmd.createArgument().setPath(cp2);
        }
        if (!javah.getOld()) {
            cmd.createArgument().setValue("-jni");
        }
        cmd.addArguments(javah.getCurrentArgs());
        javah.logAndAddFiles(cmd);
        return cmd;
    }
}
