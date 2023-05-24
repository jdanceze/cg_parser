package org.apache.tools.ant.taskdefs.optional.clearcase;

import java.util.Optional;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/clearcase/CCLock.class */
public class CCLock extends ClearCase {
    public static final String FLAG_REPLACE = "-replace";
    public static final String FLAG_NUSERS = "-nusers";
    public static final String FLAG_OBSOLETE = "-obsolete";
    public static final String FLAG_COMMENT = "-comment";
    public static final String FLAG_PNAME = "-pname";
    private boolean mReplace = false;
    private boolean mObsolete = false;
    private String mComment = null;
    private String mNusers = null;
    private String mPname = null;
    private String mObjselect = null;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        if (getViewPath() == null) {
            setViewPath(aProj.getBaseDir().getPath());
        }
        commandLine.setExecutable(getClearToolCommand());
        commandLine.createArgument().setValue(ClearCase.COMMAND_LOCK);
        checkOptions(commandLine);
        if (!getFailOnErr()) {
            getProject().log("Ignoring any errors that occur for: " + getOpType(), 3);
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
        if (getObsolete()) {
            cmd.createArgument().setValue(FLAG_OBSOLETE);
        } else {
            getNusersCommand(cmd);
        }
        getCommentCommand(cmd);
        if (getObjselect() == null && getPname() == null) {
            throw new BuildException("Should select either an element (pname) or an object (objselect)");
        }
        getPnameCommand(cmd);
        if (getObjselect() != null) {
            cmd.createArgument().setValue(getObjselect());
        }
    }

    public void setReplace(boolean replace) {
        this.mReplace = replace;
    }

    public boolean getReplace() {
        return this.mReplace;
    }

    public void setObsolete(boolean obsolete) {
        this.mObsolete = obsolete;
    }

    public boolean getObsolete() {
        return this.mObsolete;
    }

    public void setNusers(String nusers) {
        this.mNusers = nusers;
    }

    public String getNusers() {
        return this.mNusers;
    }

    public void setComment(String comment) {
        this.mComment = comment;
    }

    public String getComment() {
        return this.mComment;
    }

    public void setPname(String pname) {
        this.mPname = pname;
    }

    public String getPname() {
        return this.mPname;
    }

    public void setObjSel(String objsel) {
        this.mObjselect = objsel;
    }

    public void setObjselect(String objselect) {
        this.mObjselect = objselect;
    }

    public String getObjselect() {
        return this.mObjselect;
    }

    private void getNusersCommand(Commandline cmd) {
        if (getNusers() == null) {
            return;
        }
        cmd.createArgument().setValue(FLAG_NUSERS);
        cmd.createArgument().setValue(getNusers());
    }

    private void getCommentCommand(Commandline cmd) {
        if (getComment() == null) {
            return;
        }
        cmd.createArgument().setValue("-comment");
        cmd.createArgument().setValue(getComment());
    }

    private void getPnameCommand(Commandline cmd) {
        if (getPname() == null) {
            return;
        }
        cmd.createArgument().setValue("-pname");
        cmd.createArgument().setValue(getPname());
    }

    private String getOpType() {
        return (String) Optional.ofNullable(getPname()).orElseGet(this::getObjselect);
    }
}
