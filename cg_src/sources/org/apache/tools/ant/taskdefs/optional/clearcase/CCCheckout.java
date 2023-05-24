package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/clearcase/CCCheckout.class */
public class CCCheckout extends ClearCase {
    public static final String FLAG_RESERVED = "-reserved";
    public static final String FLAG_UNRESERVED = "-unreserved";
    public static final String FLAG_OUT = "-out";
    public static final String FLAG_NODATA = "-ndata";
    public static final String FLAG_BRANCH = "-branch";
    public static final String FLAG_VERSION = "-version";
    public static final String FLAG_NOWARN = "-nwarn";
    public static final String FLAG_COMMENT = "-c";
    public static final String FLAG_COMMENTFILE = "-cfile";
    public static final String FLAG_NOCOMMENT = "-nc";
    private boolean mReserved = true;
    private String mOut = null;
    private boolean mNdata = false;
    private String mBranch = null;
    private boolean mVersion = false;
    private boolean mNwarn = false;
    private String mComment = null;
    private String mCfile = null;
    private boolean mNotco = true;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        if (getViewPath() == null) {
            setViewPath(aProj.getBaseDir().getPath());
        }
        commandLine.setExecutable(getClearToolCommand());
        commandLine.createArgument().setValue(ClearCase.COMMAND_CHECKOUT);
        checkOptions(commandLine);
        if (!getNotco() && lsCheckout()) {
            getProject().log("Already checked out in this view: " + getViewPathBasename(), 3);
            return;
        }
        if (!getFailOnErr()) {
            getProject().log("Ignoring any errors that occur for: " + getViewPathBasename(), 3);
        }
        int result = run(commandLine);
        if (Execute.isFailure(result) && getFailOnErr()) {
            throw new BuildException("Failed executing: " + commandLine, getLocation());
        }
    }

    private boolean lsCheckout() {
        Commandline cmdl = new Commandline();
        cmdl.setExecutable(getClearToolCommand());
        cmdl.createArgument().setValue(ClearCase.COMMAND_LSCO);
        cmdl.createArgument().setValue("-cview");
        cmdl.createArgument().setValue("-short");
        cmdl.createArgument().setValue("-d");
        cmdl.createArgument().setValue(getViewPath());
        String result = runS(cmdl, getFailOnErr());
        return (result == null || result.isEmpty()) ? false : true;
    }

    private void checkOptions(Commandline cmd) {
        if (getReserved()) {
            cmd.createArgument().setValue(FLAG_RESERVED);
        } else {
            cmd.createArgument().setValue(FLAG_UNRESERVED);
        }
        if (getOut() != null) {
            getOutCommand(cmd);
        } else if (getNoData()) {
            cmd.createArgument().setValue(FLAG_NODATA);
        }
        if (getBranch() != null) {
            getBranchCommand(cmd);
        } else if (getVersion()) {
            cmd.createArgument().setValue("-version");
        }
        if (getNoWarn()) {
            cmd.createArgument().setValue("-nwarn");
        }
        if (getComment() != null) {
            getCommentCommand(cmd);
        } else if (getCommentFile() != null) {
            getCommentFileCommand(cmd);
        } else {
            cmd.createArgument().setValue("-nc");
        }
        cmd.createArgument().setValue(getViewPath());
    }

    public void setReserved(boolean reserved) {
        this.mReserved = reserved;
    }

    public boolean getReserved() {
        return this.mReserved;
    }

    public void setNotco(boolean notco) {
        this.mNotco = notco;
    }

    public boolean getNotco() {
        return this.mNotco;
    }

    public void setOut(String outf) {
        this.mOut = outf;
    }

    public String getOut() {
        return this.mOut;
    }

    public void setNoData(boolean ndata) {
        this.mNdata = ndata;
    }

    public boolean getNoData() {
        return this.mNdata;
    }

    public void setBranch(String branch) {
        this.mBranch = branch;
    }

    public String getBranch() {
        return this.mBranch;
    }

    public void setVersion(boolean version) {
        this.mVersion = version;
    }

    public boolean getVersion() {
        return this.mVersion;
    }

    public void setNoWarn(boolean nwarn) {
        this.mNwarn = nwarn;
    }

    public boolean getNoWarn() {
        return this.mNwarn;
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

    private void getOutCommand(Commandline cmd) {
        if (getOut() != null) {
            cmd.createArgument().setValue(FLAG_OUT);
            cmd.createArgument().setValue(getOut());
        }
    }

    private void getBranchCommand(Commandline cmd) {
        if (getBranch() != null) {
            cmd.createArgument().setValue(FLAG_BRANCH);
            cmd.createArgument().setValue(getBranch());
        }
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
