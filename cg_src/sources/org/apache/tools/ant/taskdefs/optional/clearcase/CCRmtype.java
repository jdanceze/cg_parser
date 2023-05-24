package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/clearcase/CCRmtype.class */
public class CCRmtype extends ClearCase {
    public static final String FLAG_IGNORE = "-ignore";
    public static final String FLAG_RMALL = "-rmall";
    public static final String FLAG_FORCE = "-force";
    public static final String FLAG_COMMENT = "-c";
    public static final String FLAG_COMMENTFILE = "-cfile";
    public static final String FLAG_NOCOMMENT = "-nc";
    private String mTypeKind = null;
    private String mTypeName = null;
    private String mVOB = null;
    private String mComment = null;
    private String mCfile = null;
    private boolean mRmall = false;
    private boolean mIgnore = false;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        if (getTypeKind() == null) {
            throw new BuildException("Required attribute TypeKind not specified");
        }
        if (getTypeName() == null) {
            throw new BuildException("Required attribute TypeName not specified");
        }
        commandLine.setExecutable(getClearToolCommand());
        commandLine.createArgument().setValue(ClearCase.COMMAND_RMTYPE);
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
        if (getIgnore()) {
            cmd.createArgument().setValue(FLAG_IGNORE);
        }
        if (getRmAll()) {
            cmd.createArgument().setValue(FLAG_RMALL);
            cmd.createArgument().setValue(FLAG_FORCE);
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

    public void setIgnore(boolean ignore) {
        this.mIgnore = ignore;
    }

    public boolean getIgnore() {
        return this.mIgnore;
    }

    public void setRmAll(boolean rmall) {
        this.mRmall = rmall;
    }

    public boolean getRmAll() {
        return this.mRmall;
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

    public void setTypeKind(String tk) {
        this.mTypeKind = tk;
    }

    public String getTypeKind() {
        return this.mTypeKind;
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

    private String getTypeSpecifier() {
        String tkind = getTypeKind();
        String tname = getTypeName();
        String typeSpec = tkind + ":" + tname;
        if (getVOB() != null) {
            typeSpec = typeSpec + "@" + getVOB();
        }
        return typeSpec;
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
