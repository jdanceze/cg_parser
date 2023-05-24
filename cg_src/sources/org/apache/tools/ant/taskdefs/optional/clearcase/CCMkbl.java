package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/clearcase/CCMkbl.class */
public class CCMkbl extends ClearCase {
    public static final String FLAG_COMMENT = "-c";
    public static final String FLAG_COMMENTFILE = "-cfile";
    public static final String FLAG_NOCOMMENT = "-nc";
    public static final String FLAG_IDENTICAL = "-identical";
    public static final String FLAG_INCREMENTAL = "-incremental";
    public static final String FLAG_FULL = "-full";
    public static final String FLAG_NLABEL = "-nlabel";
    private String mComment = null;
    private String mCfile = null;
    private String mBaselineRootName = null;
    private boolean mNwarn = false;
    private boolean mIdentical = true;
    private boolean mFull = false;
    private boolean mNlabel = false;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        if (getViewPath() == null) {
            setViewPath(aProj.getBaseDir().getPath());
        }
        commandLine.setExecutable(getClearToolCommand());
        commandLine.createArgument().setValue(ClearCase.COMMAND_MKBL);
        checkOptions(commandLine);
        if (!getFailOnErr()) {
            getProject().log("Ignoring any errors that occur for: " + getBaselineRootName(), 3);
        }
        int result = run(commandLine);
        if (Execute.isFailure(result) && getFailOnErr()) {
            throw new BuildException("Failed executing: " + commandLine, getLocation());
        }
    }

    private void checkOptions(Commandline cmd) {
        if (getComment() != null) {
            getCommentCommand(cmd);
        } else if (getCommentFile() != null) {
            getCommentFileCommand(cmd);
        } else {
            cmd.createArgument().setValue("-nc");
        }
        if (getIdentical()) {
            cmd.createArgument().setValue("-identical");
        }
        if (getFull()) {
            cmd.createArgument().setValue(FLAG_FULL);
        } else {
            cmd.createArgument().setValue(FLAG_INCREMENTAL);
        }
        if (getNlabel()) {
            cmd.createArgument().setValue(FLAG_NLABEL);
        }
        cmd.createArgument().setValue(getBaselineRootName());
    }

    public void setComment(String comment) {
        this.mComment = comment;
    }

    public String getComment() {
        return this.mComment;
    }

    public void setCommentFile(String cfile) {
        this.mCfile = cfile;
    }

    public String getCommentFile() {
        return this.mCfile;
    }

    public void setBaselineRootName(String baselineRootName) {
        this.mBaselineRootName = baselineRootName;
    }

    public String getBaselineRootName() {
        return this.mBaselineRootName;
    }

    public void setNoWarn(boolean nwarn) {
        this.mNwarn = nwarn;
    }

    public boolean getNoWarn() {
        return this.mNwarn;
    }

    public void setIdentical(boolean identical) {
        this.mIdentical = identical;
    }

    public boolean getIdentical() {
        return this.mIdentical;
    }

    public void setFull(boolean full) {
        this.mFull = full;
    }

    public boolean getFull() {
        return this.mFull;
    }

    public void setNlabel(boolean nlabel) {
        this.mNlabel = nlabel;
    }

    public boolean getNlabel() {
        return this.mNlabel;
    }

    private void getCommentCommand(Commandline cmd) {
        if (getComment() != null) {
            cmd.createArgument().setValue("-c");
            cmd.createArgument().setValue(getComment());
        }
    }

    private void getCommentFileCommand(Commandline cmd) {
        if (getCommentFile() != null) {
            cmd.createArgument().setValue("-cfile");
            cmd.createArgument().setValue(getCommentFile());
        }
    }
}
