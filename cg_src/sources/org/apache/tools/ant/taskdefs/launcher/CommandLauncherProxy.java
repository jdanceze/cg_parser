package org.apache.tools.ant.taskdefs.launcher;

import java.io.IOException;
import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/launcher/CommandLauncherProxy.class */
public class CommandLauncherProxy extends CommandLauncher {
    private final CommandLauncher myLauncher;

    /* JADX INFO: Access modifiers changed from: protected */
    public CommandLauncherProxy(CommandLauncher launcher) {
        this.myLauncher = launcher;
    }

    @Override // org.apache.tools.ant.taskdefs.launcher.CommandLauncher
    public Process exec(Project project, String[] cmd, String[] env) throws IOException {
        return this.myLauncher.exec(project, cmd, env);
    }
}
