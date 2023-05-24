package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.launcher.CommandLauncher;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/CommandLauncherTask.class */
public class CommandLauncherTask extends Task {
    private boolean vmLauncher;
    private CommandLauncher commandLauncher;

    public synchronized void addConfigured(CommandLauncher commandLauncher) {
        if (this.commandLauncher != null) {
            throw new BuildException("Only one CommandLauncher can be installed");
        }
        this.commandLauncher = commandLauncher;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        if (this.commandLauncher != null) {
            if (this.vmLauncher) {
                CommandLauncher.setVMLauncher(getProject(), this.commandLauncher);
            } else {
                CommandLauncher.setShellLauncher(getProject(), this.commandLauncher);
            }
        }
    }

    public void setVmLauncher(boolean vmLauncher) {
        this.vmLauncher = vmLauncher;
    }
}
