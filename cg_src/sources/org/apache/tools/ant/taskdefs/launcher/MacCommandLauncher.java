package org.apache.tools.ant.taskdefs.launcher;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/launcher/MacCommandLauncher.class */
public class MacCommandLauncher extends CommandLauncherProxy {
    public MacCommandLauncher(CommandLauncher launcher) {
        super(launcher);
    }

    @Override // org.apache.tools.ant.taskdefs.launcher.CommandLauncher
    public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
        if (workingDir == null) {
            return exec(project, cmd, env);
        }
        System.getProperties().put("user.dir", workingDir.getAbsolutePath());
        try {
            Process exec = exec(project, cmd, env);
            System.getProperties().put("user.dir", System.getProperty("user.dir"));
            return exec;
        } catch (Throwable th) {
            System.getProperties().put("user.dir", System.getProperty("user.dir"));
            throw th;
        }
    }
}
