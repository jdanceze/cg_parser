package org.apache.tools.ant.taskdefs.launcher;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/launcher/Java13CommandLauncher.class */
public class Java13CommandLauncher extends CommandLauncher {
    @Override // org.apache.tools.ant.taskdefs.launcher.CommandLauncher
    public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
        if (project != null) {
            try {
                project.log("Execute:Java13CommandLauncher: " + Commandline.describeCommand(cmd), 4);
            } catch (IOException ioex) {
                throw ioex;
            } catch (Exception exc) {
                throw new BuildException("Unable to execute command", exc);
            }
        }
        return Runtime.getRuntime().exec(cmd, env, workingDir);
    }
}
