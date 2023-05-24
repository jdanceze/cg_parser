package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/clearcase/CCMklbtype.class */
public class CCMklbtype extends ClearCase {
    public static final String FLAG_REPLACE = "-replace";
    public static final String FLAG_GLOBAL = "-global";
    public static final String FLAG_ORDINARY = "-ordinary";
    public static final String FLAG_PBRANCH = "-pbranch";
    public static final String FLAG_SHARED = "-shared";
    public static final String FLAG_COMMENT = "-c";
    public static final String FLAG_COMMENTFILE = "-cfile";
    public static final String FLAG_NOCOMMENT = "-nc";
    private String mTypeName = null;
    private String mVOB = null;
    private String mComment = null;
    private String mCfile = null;
    private boolean mReplace = false;
    private boolean mGlobal = false;
    private boolean mOrdinary = true;
    private boolean mPbranch = false;
    private boolean mShared = false;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        if (getTypeName() == null) {
            throw new BuildException("Required attribute TypeName not specified");
        }
        commandLine.setExecutable(getClearToolCommand());
        commandLine.createArgument().setValue(ClearCase.COMMAND_MKLBTYPE);
        checkOptions(commandLine);
        if (!getFailOnErr()) {
            getProject().log("Ignoring any errors that occur for: " + getTypeSpecifier(), 3);
        }
        int result = run(commandLine);
        if (Execute.isFailure(result) && getFailOnErr()) {
            throw new BuildException("Failed executing: " + commandLine, getLocation());
        }
    }

    private void checkOptions(Commandline cmd) {
        if (getReplace()) {
            cmd.createArgument().setValue("-replace");
        }
        if (getOrdinary()) {
            cmd.createArgument().setValue(FLAG_ORDINARY);
        } else if (getGlobal()) {
            cmd.createArgument().setValue(FLAG_GLOBAL);
        }
        if (getPbranch()) {
            cmd.createArgument().setValue(FLAG_PBRANCH);
        }
        if (getShared()) {
            cmd.createArgument().setValue(FLAG_SHARED);
        }
        if (getComment() != null) {
            getCommentCommand(cmd);
        } else if (getCommentFile() != null) {
            getCommentFileCommand(cmd);
        } else {
            cmd.createArgument().setValue("-nc");
        }
        cmd.createArgument().setValue(getTypeSpecifier());
    }

    public void setTypeName(String tn) {
        this.mTypeName = tn;
    }

    public String getTypeName() {
        return this.mTypeName;
    }

    public void setVOB(String vob) {
        this.mVOB = vob;
    }

    public String getVOB() {
        return this.mVOB;
    }

    public void setReplace(boolean repl) {
        this.mReplace = repl;
    }

    public boolean getReplace() {
        return this.mReplace;
    }

    public void setGlobal(boolean glob) {
        this.mGlobal = glob;
    }

    public boolean getGlobal() {
        return this.mGlobal;
    }

    public void setOrdinary(boolean ordinary) {
        this.mOrdinary = ordinary;
    }

    public boolean getOrdinary() {
        return this.mOrdinary;
    }

    public void setPbranch(boolean pbranch) {
        this.mPbranch = pbranch;
    }

    public boolean getPbranch() {
        return this.mPbranch;
    }

    public void setShared(boolean shared) {
        this.mShared = shared;
    }

    public boolean getShared() {
        return this.mShared;
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

    private String getTypeSpecifier() {
        String typenm = getTypeName();
        if (getVOB() != null) {
            typenm = typenm + "@" + getVOB();
        }
        return typenm;
    }
}
