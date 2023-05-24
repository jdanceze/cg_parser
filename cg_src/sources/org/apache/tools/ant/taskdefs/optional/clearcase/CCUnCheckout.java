package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/clearcase/CCUnCheckout.class */
public class CCUnCheckout extends ClearCase {
    public static final String FLAG_KEEPCOPY = "-keep";
    public static final String FLAG_RM = "-rm";
    private boolean mKeep = false;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        if (getViewPath() == null) {
            setViewPath(aProj.getBaseDir().getPath());
        }
        commandLine.setExecutable(getClearToolCommand());
        commandLine.createArgument().setValue(ClearCase.COMMAND_UNCHECKOUT);
        checkOptions(commandLine);
        if (!getFailOnErr()) {
            getProject().log("Ignoring any errors that occur for: " + getViewPathBasename(), 3);
        }
        int result = run(commandLine);
        if (Execute.isFailure(result) && getFailOnErr()) {
            throw new BuildException("Failed executing: " + commandLine, getLocation());
        }
    }

    private void checkOptions(Commandline cmd) {
        if (getKeepCopy()) {
            cmd.createArgument().setValue("-keep");
        } else {
            cmd.createArgument().setValue(FLAG_RM);
        }
        cmd.createArgument().setValue(getViewPath());
    }

    public void setKeepCopy(boolean keep) {
        this.mKeep = keep;
    }

    public boolean getKeepCopy() {
        return this.mKeep;
    }
}
