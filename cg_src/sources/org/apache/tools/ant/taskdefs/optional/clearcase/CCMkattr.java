package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/clearcase/CCMkattr.class */
public class CCMkattr extends ClearCase {
    public static final String FLAG_REPLACE = "-replace";
    public static final String FLAG_RECURSE = "-recurse";
    public static final String FLAG_VERSION = "-version";
    public static final String FLAG_COMMENT = "-c";
    public static final String FLAG_COMMENTFILE = "-cfile";
    public static final String FLAG_NOCOMMENT = "-nc";
    private boolean mReplace = false;
    private boolean mRecurse = false;
    private String mVersion = null;
    private String mTypeName = null;
    private String mTypeValue = null;
    private String mComment = null;
    private String mCfile = null;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        if (getTypeName() == null) {
            throw new BuildException("Required attribute TypeName not specified");
        }
        if (getTypeValue() == null) {
            throw new BuildException("Required attribute TypeValue not specified");
        }
        if (getViewPath() == null) {
            setViewPath(aProj.getBaseDir().getPath());
        }
        commandLine.setExecutable(getClearToolCommand());
        commandLine.createArgument().setValue(ClearCase.COMMAND_MKATTR);
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
        if (getReplace()) {
            cmd.createArgument().setValue("-replace");
        }
        if (getRecurse()) {
            cmd.createArgument().setValue("-recurse");
        }
        if (getVersion() != null) {
            getVersionCommand(cmd);
        }
        if (getComment() != null) {
            getCommentCommand(cmd);
        } else if (getCommentFile() != null) {
            getCommentFileCommand(cmd);
        } else {
            cmd.createArgument().setValue("-nc");
        }
        if (getTypeName() != null) {
            getTypeCommand(cmd);
        }
        if (getTypeValue() != null) {
            getTypeValueCommand(cmd);
        }
        cmd.createArgument().setValue(getViewPath());
    }

    public void setReplace(boolean replace) {
        this.mReplace = replace;
    }

    public boolean getReplace() {
        return this.mReplace;
    }

    public void setRecurse(boolean recurse) {
        this.mRecurse = recurse;
    }

    public boolean getRecurse() {
        return this.mRecurse;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public String getVersion() {
        return this.mVersion;
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

    public void setTypeName(String tn) {
        this.mTypeName = tn;
    }

    public String getTypeName() {
        return this.mTypeName;
    }

    public void setTypeValue(String tv) {
        this.mTypeValue = tv;
    }

    public String getTypeValue() {
        return this.mTypeValue;
    }

    private void getVersionCommand(Commandline cmd) {
        if (getVersion() != null) {
            cmd.createArgument().setValue("-version");
            cmd.createArgument().setValue(getVersion());
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

    private void getTypeCommand(Commandline cmd) {
        String typenm = getTypeName();
        if (typenm != null) {
            cmd.createArgument().setValue(typenm);
        }
    }

    private void getTypeValueCommand(Commandline cmd) {
        String typevl;
        String typevl2 = getTypeValue();
        if (typevl2 != null) {
            if (Os.isFamily(Os.FAMILY_WINDOWS)) {
                typevl = "\\\"" + typevl2 + "\\\"";
            } else {
                typevl = "\"" + typevl2 + "\"";
            }
            cmd.createArgument().setValue(typevl);
        }
    }
}
