package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/clearcase/CCCheckin.class */
public class CCCheckin extends ClearCase {
    public static final String FLAG_COMMENT = "-c";
    public static final String FLAG_COMMENTFILE = "-cfile";
    public static final String FLAG_NOCOMMENT = "-nc";
    public static final String FLAG_NOWARN = "-nwarn";
    public static final String FLAG_PRESERVETIME = "-ptime";
    public static final String FLAG_KEEPCOPY = "-keep";
    public static final String FLAG_IDENTICAL = "-identical";
    private String mComment = null;
    private String mCfile = null;
    private boolean mNwarn = false;
    private boolean mPtime = false;
    private boolean mKeep = false;
    private boolean mIdentical = true;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        if (getViewPath() == null) {
            setViewPath(aProj.getBaseDir().getPath());
        }
        commandLine.setExecutable(getClearToolCommand());
        commandLine.createArgument().setValue(ClearCase.COMMAND_CHECKIN);
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
        if (getComment() != null) {
            getCommentCommand(cmd);
        } else if (getCommentFile() != null) {
            getCommentFileCommand(cmd);
        } else {
            cmd.createArgument().setValue("-nc");
        }
        if (getNoWarn()) {
            cmd.createArgument().setValue("-nwarn");
        }
        if (getPreserveTime()) {
            cmd.createArgument().setValue("-ptime");
        }
        if (getKeepCopy()) {
            cmd.createArgument().setValue("-keep");
        }
        if (getIdentical()) {
            cmd.createArgument().setValue("-identical");
        }
        cmd.createArgument().setValue(getViewPath());
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

    public void setNoWarn(boolean nwarn) {
        this.mNwarn = nwarn;
    }

    public boolean getNoWarn() {
        return this.mNwarn;
    }

    public void setPreserveTime(boolean ptime) {
        this.mPtime = ptime;
    }

    public boolean getPreserveTime() {
        return this.mPtime;
    }

    public void setKeepCopy(boolean keep) {
        this.mKeep = keep;
    }

    public boolean getKeepCopy() {
        return this.mKeep;
    }

    public void setIdentical(boolean identical) {
        this.mIdentical = identical;
    }

    public boolean getIdentical() {
        return this.mIdentical;
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
