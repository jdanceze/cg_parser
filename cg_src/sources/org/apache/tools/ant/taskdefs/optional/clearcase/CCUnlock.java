package org.apache.tools.ant.taskdefs.optional.clearcase;

import java.util.Optional;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/clearcase/CCUnlock.class */
public class CCUnlock extends ClearCase {
    public static final String FLAG_COMMENT = "-comment";
    public static final String FLAG_PNAME = "-pname";
    private String mComment = null;
    private String mPname = null;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        if (getViewPath() == null) {
            setViewPath(aProj.getBaseDir().getPath());
        }
        commandLine.setExecutable(getClearToolCommand());
        commandLine.createArgument().setValue(ClearCase.COMMAND_UNLOCK);
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
        getCommentCommand(cmd);
        if (getObjSelect() == null && getPname() == null) {
            throw new BuildException("Should select either an element (pname) or an object (objselect)");
        }
        getPnameCommand(cmd);
        if (getObjSelect() != null) {
            cmd.createArgument().setValue(getObjSelect());
        }
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

    public void setObjselect(String objselect) {
        setObjSelect(objselect);
    }

    public void setObjSel(String objsel) {
        setObjSelect(objsel);
    }

    public String getObjselect() {
        return getObjSelect();
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
        return (String) Optional.ofNullable(getPname()).orElseGet(this::getObjSelect);
    }
}
