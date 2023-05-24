package org.apache.tools.ant.taskdefs.optional.ccm;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ccm/CCMReconfigure.class */
public class CCMReconfigure extends Continuus {
    public static final String FLAG_RECURSE = "/recurse";
    public static final String FLAG_VERBOSE = "/verbose";
    public static final String FLAG_PROJECT = "/project";
    private String ccmProject = null;
    private boolean recurse = false;
    private boolean verbose = false;

    public CCMReconfigure() {
        setCcmAction(Continuus.COMMAND_RECONFIGURE);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        commandLine.setExecutable(getCcmCommand());
        commandLine.createArgument().setValue(getCcmAction());
        checkOptions(commandLine);
        int result = run(commandLine);
        if (Execute.isFailure(result)) {
            throw new BuildException("Failed executing: " + commandLine, getLocation());
        }
    }

    private void checkOptions(Commandline cmd) {
        if (isRecurse()) {
            cmd.createArgument().setValue(FLAG_RECURSE);
        }
        if (isVerbose()) {
            cmd.createArgument().setValue(FLAG_VERBOSE);
        }
        if (getCcmProject() != null) {
            cmd.createArgument().setValue(FLAG_PROJECT);
            cmd.createArgument().setValue(getCcmProject());
        }
    }

    public String getCcmProject() {
        return this.ccmProject;
    }

    public void setCcmProject(String v) {
        this.ccmProject = v;
    }

    public boolean isRecurse() {
        return this.recurse;
    }

    public void setRecurse(boolean v) {
        this.recurse = v;
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public void setVerbose(boolean v) {
        this.verbose = v;
    }
}
