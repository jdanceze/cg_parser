package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/clearcase/CCUpdate.class */
public class CCUpdate extends ClearCase {
    public static final String FLAG_GRAPHICAL = "-graphical";
    public static final String FLAG_LOG = "-log";
    public static final String FLAG_OVERWRITE = "-overwrite";
    public static final String FLAG_NOVERWRITE = "-noverwrite";
    public static final String FLAG_RENAME = "-rename";
    public static final String FLAG_CURRENTTIME = "-ctime";
    public static final String FLAG_PRESERVETIME = "-ptime";
    private boolean mGraphical = false;
    private boolean mOverwrite = false;
    private boolean mRename = false;
    private boolean mCtime = false;
    private boolean mPtime = false;
    private String mLog = null;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        if (getViewPath() == null) {
            setViewPath(aProj.getBaseDir().getPath());
        }
        commandLine.setExecutable(getClearToolCommand());
        commandLine.createArgument().setValue("update");
        checkOptions(commandLine);
        getProject().log(commandLine.toString(), 4);
        if (!getFailOnErr()) {
            getProject().log("Ignoring any errors that occur for: " + getViewPathBasename(), 3);
        }
        int result = run(commandLine);
        if (Execute.isFailure(result) && getFailOnErr()) {
            throw new BuildException("Failed executing: " + commandLine, getLocation());
        }
    }

    private void checkOptions(Commandline cmd) {
        if (getGraphical()) {
            cmd.createArgument().setValue(FLAG_GRAPHICAL);
        } else {
            if (getOverwrite()) {
                cmd.createArgument().setValue(FLAG_OVERWRITE);
            } else if (getRename()) {
                cmd.createArgument().setValue(FLAG_RENAME);
            } else {
                cmd.createArgument().setValue(FLAG_NOVERWRITE);
            }
            if (getCurrentTime()) {
                cmd.createArgument().setValue(FLAG_CURRENTTIME);
            } else if (getPreserveTime()) {
                cmd.createArgument().setValue("-ptime");
            }
            getLogCommand(cmd);
        }
        cmd.createArgument().setValue(getViewPath());
    }

    public void setGraphical(boolean graphical) {
        this.mGraphical = graphical;
    }

    public boolean getGraphical() {
        return this.mGraphical;
    }

    public void setOverwrite(boolean ow) {
        this.mOverwrite = ow;
    }

    public boolean getOverwrite() {
        return this.mOverwrite;
    }

    public void setRename(boolean ren) {
        this.mRename = ren;
    }

    public boolean getRename() {
        return this.mRename;
    }

    public void setCurrentTime(boolean ct) {
        this.mCtime = ct;
    }

    public boolean getCurrentTime() {
        return this.mCtime;
    }

    public void setPreserveTime(boolean pt) {
        this.mPtime = pt;
    }

    public boolean getPreserveTime() {
        return this.mPtime;
    }

    public void setLog(String log) {
        this.mLog = log;
    }

    public String getLog() {
        return this.mLog;
    }

    private void getLogCommand(Commandline cmd) {
        if (getLog() == null) {
            return;
        }
        cmd.createArgument().setValue("-log");
        cmd.createArgument().setValue(getLog());
    }
}
