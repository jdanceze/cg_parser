package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/clearcase/CCMkelem.class */
public class CCMkelem extends ClearCase {
    public static final String FLAG_COMMENT = "-c";
    public static final String FLAG_COMMENTFILE = "-cfile";
    public static final String FLAG_NOCOMMENT = "-nc";
    public static final String FLAG_NOWARN = "-nwarn";
    public static final String FLAG_PRESERVETIME = "-ptime";
    public static final String FLAG_NOCHECKOUT = "-nco";
    public static final String FLAG_CHECKIN = "-ci";
    public static final String FLAG_MASTER = "-master";
    public static final String FLAG_ELTYPE = "-eltype";
    private String mComment = null;
    private String mCfile = null;
    private boolean mNwarn = false;
    private boolean mPtime = false;
    private boolean mNoco = false;
    private boolean mCheckin = false;
    private boolean mMaster = false;
    private String mEltype = null;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        if (getViewPath() == null) {
            setViewPath(aProj.getBaseDir().getPath());
        }
        commandLine.setExecutable(getClearToolCommand());
        commandLine.createArgument().setValue(ClearCase.COMMAND_MKELEM);
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
        if (getNoCheckout() && getCheckin()) {
            throw new BuildException("Should choose either [nocheckout | checkin]");
        }
        if (getNoCheckout()) {
            cmd.createArgument().setValue("-nco");
        }
        if (getCheckin()) {
            cmd.createArgument().setValue(FLAG_CHECKIN);
            if (getPreserveTime()) {
                cmd.createArgument().setValue("-ptime");
            }
        }
        if (getMaster()) {
            cmd.createArgument().setValue(FLAG_MASTER);
        }
        if (getEltype() != null) {
            getEltypeCommand(cmd);
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

    public void setNoCheckout(boolean co) {
        this.mNoco = co;
    }

    public boolean getNoCheckout() {
        return this.mNoco;
    }

    public void setCheckin(boolean ci) {
        this.mCheckin = ci;
    }

    public boolean getCheckin() {
        return this.mCheckin;
    }

    public void setMaster(boolean master) {
        this.mMaster = master;
    }

    public boolean getMaster() {
        return this.mMaster;
    }

    public void setEltype(String eltype) {
        this.mEltype = eltype;
    }

    public String getEltype() {
        return this.mEltype;
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

    private void getEltypeCommand(Commandline cmd) {
        if (getEltype() != null) {
            cmd.createArgument().setValue(FLAG_ELTYPE);
            cmd.createArgument().setValue(getEltype());
        }
    }
}
