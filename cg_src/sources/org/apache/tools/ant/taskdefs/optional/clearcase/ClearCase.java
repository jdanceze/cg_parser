package org.apache.tools.ant.taskdefs.optional.clearcase;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/clearcase/ClearCase.class */
public abstract class ClearCase extends Task {
    private static final String CLEARTOOL_EXE = "cleartool";
    public static final String COMMAND_UPDATE = "update";
    public static final String COMMAND_CHECKOUT = "checkout";
    public static final String COMMAND_CHECKIN = "checkin";
    public static final String COMMAND_UNCHECKOUT = "uncheckout";
    public static final String COMMAND_LOCK = "lock";
    public static final String COMMAND_UNLOCK = "unlock";
    public static final String COMMAND_MKBL = "mkbl";
    public static final String COMMAND_MKLABEL = "mklabel";
    public static final String COMMAND_MKLBTYPE = "mklbtype";
    public static final String COMMAND_RMTYPE = "rmtype";
    public static final String COMMAND_LSCO = "lsco";
    public static final String COMMAND_MKELEM = "mkelem";
    public static final String COMMAND_MKATTR = "mkattr";
    public static final String COMMAND_MKDIR = "mkdir";
    private String mClearToolDir = "";
    private String mviewPath = null;
    private String mobjSelect = null;
    private int pcnt = 0;
    private boolean mFailonerr = true;

    public final void setClearToolDir(String dir) {
        this.mClearToolDir = FileUtils.translatePath(dir);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String getClearToolCommand() {
        String toReturn = this.mClearToolDir;
        if (!toReturn.isEmpty() && !toReturn.endsWith("/")) {
            toReturn = toReturn + "/";
        }
        return toReturn + CLEARTOOL_EXE;
    }

    public final void setViewPath(String viewPath) {
        this.mviewPath = viewPath;
    }

    public String getViewPath() {
        return this.mviewPath;
    }

    public String getViewPathBasename() {
        return new File(this.mviewPath).getName();
    }

    public final void setObjSelect(String objSelect) {
        this.mobjSelect = objSelect;
    }

    public String getObjSelect() {
        return this.mobjSelect;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int run(Commandline cmd) {
        try {
            Project aProj = getProject();
            Execute exe = new Execute(new LogStreamHandler((Task) this, 2, 1));
            exe.setAntRun(aProj);
            exe.setWorkingDirectory(aProj.getBaseDir());
            exe.setCommandline(cmd.getCommandline());
            return exe.execute();
        } catch (IOException e) {
            throw new BuildException(e, getLocation());
        }
    }

    @Deprecated
    protected String runS(Commandline cmdline) {
        return runS(cmdline, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String runS(Commandline cmdline, boolean failOnError) {
        StringBuilder append = new StringBuilder().append("opts.cc.runS.output");
        int i = this.pcnt;
        this.pcnt = i + 1;
        String outV = append.append(i).toString();
        ExecTask exe = new ExecTask(this);
        Commandline.Argument arg = exe.createArg();
        exe.setExecutable(cmdline.getExecutable());
        arg.setLine(Commandline.toString(cmdline.getArguments()));
        exe.setOutputproperty(outV);
        exe.setFailonerror(failOnError);
        exe.execute();
        return getProject().getProperty(outV);
    }

    public void setFailOnErr(boolean failonerr) {
        this.mFailonerr = failonerr;
    }

    public boolean getFailOnErr() {
        return this.mFailonerr;
    }
}
