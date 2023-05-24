package org.apache.tools.ant.taskdefs.rmic;

import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.taskdefs.Rmic;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.JavaEnvUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/rmic/ForkingSunRmic.class */
public class ForkingSunRmic extends DefaultRmicAdapter {
    public static final String COMPILER_NAME = "forking";

    @Override // org.apache.tools.ant.taskdefs.rmic.DefaultRmicAdapter
    protected boolean areIiopAndIdlSupported() {
        boolean supported = !JavaEnvUtils.isAtLeastJavaVersion(JavaEnvUtils.JAVA_11);
        if (!supported && getRmic().getExecutable() != null) {
            getRmic().getProject().log("Allowing -iiop and -idl for forked rmic even though this version of Java doesn't support it.", 2);
            return true;
        }
        return supported;
    }

    @Override // org.apache.tools.ant.taskdefs.rmic.RmicAdapter
    public boolean execute() throws BuildException {
        Rmic owner = getRmic();
        Commandline cmd = setupRmicCommand();
        Project project = owner.getProject();
        String executable = owner.getExecutable();
        if (executable == null) {
            if (JavaEnvUtils.isAtLeastJavaVersion("15")) {
                throw new BuildException("rmic does not exist under Java 15 and higher, use rmic of an older JDK and explicitly set the executable attribute");
            }
            executable = JavaEnvUtils.getJdkExecutable(getExecutableName());
        }
        cmd.setExecutable(executable);
        String[] args = cmd.getCommandline();
        try {
            Execute exe = new Execute(new LogStreamHandler((Task) owner, 2, 1));
            exe.setAntRun(project);
            exe.setWorkingDirectory(project.getBaseDir());
            exe.setCommandline(args);
            exe.execute();
            return !exe.isFailure();
        } catch (IOException exception) {
            throw new BuildException("Error running " + getExecutableName() + " -maybe it is not on the path", exception);
        }
    }

    protected String getExecutableName() {
        return SunRmic.RMIC_EXECUTABLE;
    }
}
