package org.apache.tools.ant.taskdefs.optional.javah;

import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.taskdefs.optional.Javah;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.JavaEnvUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/javah/ForkingJavah.class */
public class ForkingJavah implements JavahAdapter {
    public static final String IMPLEMENTATION_NAME = "forking";

    @Override // org.apache.tools.ant.taskdefs.optional.javah.JavahAdapter
    public boolean compile(Javah javah) throws BuildException {
        Commandline cmd = SunJavah.setupJavahCommand(javah);
        Project project = javah.getProject();
        String executable = JavaEnvUtils.getJdkExecutable("javah");
        javah.log("Running " + executable, 3);
        cmd.setExecutable(executable);
        String[] args = cmd.getCommandline();
        try {
            Execute exe = new Execute(new LogStreamHandler((Task) javah, 2, 1));
            exe.setAntRun(project);
            exe.setWorkingDirectory(project.getBaseDir());
            exe.setCommandline(args);
            exe.execute();
            return !exe.isFailure();
        } catch (IOException exception) {
            throw new BuildException("Error running " + executable + " -maybe it is not on the path", exception);
        }
    }
}
