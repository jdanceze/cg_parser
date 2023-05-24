package org.apache.tools.ant.taskdefs.optional.sos;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/sos/SOS.class */
public abstract class SOS extends Task implements SOSCmd {
    private static final int ERROR_EXIT_STATUS = 255;
    private String sosCmdDir = null;
    private String sosUsername = null;
    private String sosPassword = null;
    private String projectPath = null;
    private String vssServerPath = null;
    private String sosServerPath = null;
    private String sosHome = null;
    private String localPath = null;
    private String version = null;
    private String label = null;
    private String comment = null;
    private String filename = null;
    private boolean noCompress = false;
    private boolean noCache = false;
    private boolean recursive = false;
    private boolean verbose = false;
    protected Commandline commandLine;

    abstract Commandline buildCmdLine();

    public final void setNoCache(boolean nocache) {
        this.noCache = nocache;
    }

    public final void setNoCompress(boolean nocompress) {
        this.noCompress = nocompress;
    }

    public final void setSosCmd(String dir) {
        this.sosCmdDir = FileUtils.translatePath(dir);
    }

    public final void setUsername(String username) {
        this.sosUsername = username;
    }

    public final void setPassword(String password) {
        this.sosPassword = password;
    }

    public final void setProjectPath(String projectpath) {
        if (projectpath.startsWith("$")) {
            this.projectPath = projectpath;
        } else {
            this.projectPath = "$" + projectpath;
        }
    }

    public final void setVssServerPath(String vssServerPath) {
        this.vssServerPath = vssServerPath;
    }

    public final void setSosHome(String sosHome) {
        this.sosHome = sosHome;
    }

    public final void setSosServerPath(String sosServerPath) {
        this.sosServerPath = sosServerPath;
    }

    public final void setLocalPath(Path path) {
        this.localPath = path.toString();
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalFilename(String file) {
        this.filename = file;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalRecursive(boolean recurse) {
        this.recursive = recurse;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalComment(String text) {
        this.comment = text;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalLabel(String text) {
        this.label = text;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInternalVersion(String text) {
        this.version = text;
    }

    protected String getSosCommand() {
        if (this.sosCmdDir == null) {
            return SOSCmd.COMMAND_SOS_EXE;
        }
        return this.sosCmdDir + File.separator + SOSCmd.COMMAND_SOS_EXE;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getComment() {
        return this.comment;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getVersion() {
        return this.version;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getLabel() {
        return this.label;
    }

    protected String getUsername() {
        return this.sosUsername;
    }

    protected String getPassword() {
        return this.sosPassword == null ? "" : this.sosPassword;
    }

    protected String getProjectPath() {
        return this.projectPath;
    }

    protected String getVssServerPath() {
        return this.vssServerPath;
    }

    protected String getSosHome() {
        return this.sosHome;
    }

    protected String getSosServerPath() {
        return this.sosServerPath;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getFilename() {
        return this.filename;
    }

    protected String getNoCompress() {
        return this.noCompress ? SOSCmd.FLAG_NO_COMPRESSION : "";
    }

    protected String getNoCache() {
        return this.noCache ? SOSCmd.FLAG_NO_CACHE : "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getVerbose() {
        return this.verbose ? SOSCmd.FLAG_VERBOSE : "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getRecursive() {
        return this.recursive ? SOSCmd.FLAG_RECURSION : "";
    }

    protected String getLocalPath() {
        if (this.localPath == null) {
            return getProject().getBaseDir().getAbsolutePath();
        }
        File dir = getProject().resolveFile(this.localPath);
        if (!dir.exists()) {
            boolean done = dir.mkdirs() || dir.isDirectory();
            if (!done) {
                String msg = "Directory " + this.localPath + " creation was not successful for an unknown reason";
                throw new BuildException(msg, getLocation());
            }
            getProject().log("Created dir: " + dir.getAbsolutePath());
        }
        return dir.getAbsolutePath();
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        buildCmdLine();
        int result = run(this.commandLine);
        if (result == 255) {
            String msg = "Failed executing: " + this.commandLine.toString();
            throw new BuildException(msg, getLocation());
        }
    }

    protected int run(Commandline cmd) {
        try {
            Execute exe = new Execute(new LogStreamHandler((Task) this, 2, 1));
            exe.setAntRun(getProject());
            exe.setWorkingDirectory(getProject().getBaseDir());
            exe.setCommandline(cmd.getCommandline());
            exe.setVMLauncher(false);
            return exe.execute();
        } catch (IOException e) {
            throw new BuildException(e, getLocation());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void getRequiredAttributes() {
        this.commandLine.setExecutable(getSosCommand());
        if (getSosServerPath() == null) {
            throw new BuildException("sosserverpath attribute must be set!", getLocation());
        }
        this.commandLine.createArgument().setValue(SOSCmd.FLAG_SOS_SERVER);
        this.commandLine.createArgument().setValue(getSosServerPath());
        if (getUsername() == null) {
            throw new BuildException("username attribute must be set!", getLocation());
        }
        this.commandLine.createArgument().setValue(SOSCmd.FLAG_USERNAME);
        this.commandLine.createArgument().setValue(getUsername());
        this.commandLine.createArgument().setValue(SOSCmd.FLAG_PASSWORD);
        this.commandLine.createArgument().setValue(getPassword());
        if (getVssServerPath() == null) {
            throw new BuildException("vssserverpath attribute must be set!", getLocation());
        }
        this.commandLine.createArgument().setValue(SOSCmd.FLAG_VSS_SERVER);
        this.commandLine.createArgument().setValue(getVssServerPath());
        if (getProjectPath() == null) {
            throw new BuildException("projectpath attribute must be set!", getLocation());
        }
        this.commandLine.createArgument().setValue(SOSCmd.FLAG_PROJECT);
        this.commandLine.createArgument().setValue(getProjectPath());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void getOptionalAttributes() {
        this.commandLine.createArgument().setValue(getVerbose());
        this.commandLine.createArgument().setValue(getNoCompress());
        if (getSosHome() == null) {
            this.commandLine.createArgument().setValue(getNoCache());
        } else {
            this.commandLine.createArgument().setValue(SOSCmd.FLAG_SOS_HOME);
            this.commandLine.createArgument().setValue(getSosHome());
        }
        if (getLocalPath() != null) {
            this.commandLine.createArgument().setValue(SOSCmd.FLAG_WORKING_DIR);
            this.commandLine.createArgument().setValue(getLocalPath());
        }
    }
}
