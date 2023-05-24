package org.apache.tools.ant.taskdefs.optional.ccm;

import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ccm/Continuus.class */
public abstract class Continuus extends Task {
    private static final String CCM_EXE = "ccm";
    public static final String COMMAND_CREATE_TASK = "create_task";
    public static final String COMMAND_CHECKOUT = "co";
    public static final String COMMAND_CHECKIN = "ci";
    public static final String COMMAND_RECONFIGURE = "reconfigure";
    public static final String COMMAND_DEFAULT_TASK = "default_task";
    private String ccmDir = "";
    private String ccmAction = "";

    public String getCcmAction() {
        return this.ccmAction;
    }

    public void setCcmAction(String v) {
        this.ccmAction = v;
    }

    public final void setCcmDir(String dir) {
        this.ccmDir = FileUtils.translatePath(dir);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String getCcmCommand() {
        String toReturn = this.ccmDir;
        if (!toReturn.isEmpty() && !toReturn.endsWith("/")) {
            toReturn = toReturn + "/";
        }
        return toReturn + CCM_EXE;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int run(Commandline cmd, ExecuteStreamHandler handler) {
        try {
            Execute exe = new Execute(handler);
            exe.setAntRun(getProject());
            exe.setWorkingDirectory(getProject().getBaseDir());
            exe.setCommandline(cmd.getCommandline());
            return exe.execute();
        } catch (IOException e) {
            throw new BuildException(e, getLocation());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int run(Commandline cmd) {
        return run(cmd, new LogStreamHandler((Task) this, 3, 1));
    }
}
