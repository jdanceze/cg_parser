package org.apache.tools.ant.taskdefs.launcher;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/launcher/WinNTCommandLauncher.class */
public class WinNTCommandLauncher extends CommandLauncherProxy {
    public WinNTCommandLauncher(CommandLauncher launcher) {
        super(launcher);
    }

    @Override // org.apache.tools.ant.taskdefs.launcher.CommandLauncher
    public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
        File commandDir = workingDir;
        if (workingDir == null) {
            if (project != null) {
                commandDir = project.getBaseDir();
            } else {
                return exec(project, cmd, env);
            }
        }
        String[] newcmd = new String[cmd.length + 6];
        newcmd[0] = "cmd";
        newcmd[1] = "/c";
        newcmd[2] = "cd";
        newcmd[3] = "/d";
        newcmd[4] = commandDir.getAbsolutePath();
        newcmd[5] = "&&";
        System.arraycopy(cmd, 0, newcmd, 6, cmd.length);
        return exec(project, newcmd, env);
    }
}
