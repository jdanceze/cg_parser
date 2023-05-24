package org.apache.tools.ant.taskdefs.optional;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/Rpm.class */
public class Rpm extends Task {
    private static final String PATH1 = "PATH";
    private static final String PATH2 = "Path";
    private static final String PATH3 = "path";
    private String specFile;
    private File topDir;
    private File output;
    private File error;
    private String command = "-bb";
    private String rpmBuildCommand = null;
    private boolean cleanBuildDir = false;
    private boolean removeSpec = false;
    private boolean removeSource = false;
    private boolean failOnError = false;
    private boolean quiet = false;

    /* JADX WARN: Finally extract failed */
    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        ExecuteStreamHandler streamhandler;
        Commandline toExecute = new Commandline();
        toExecute.setExecutable(this.rpmBuildCommand == null ? guessRpmBuildCommand() : this.rpmBuildCommand);
        if (this.topDir != null) {
            toExecute.createArgument().setValue("--define");
            toExecute.createArgument().setValue("_topdir " + this.topDir);
        }
        toExecute.createArgument().setLine(this.command);
        if (this.cleanBuildDir) {
            toExecute.createArgument().setValue("--clean");
        }
        if (this.removeSpec) {
            toExecute.createArgument().setValue("--rmspec");
        }
        if (this.removeSource) {
            toExecute.createArgument().setValue("--rmsource");
        }
        toExecute.createArgument().setValue("SPECS/" + this.specFile);
        OutputStream outputstream = null;
        OutputStream errorstream = null;
        if (this.error == null && this.output == null) {
            if (!this.quiet) {
                streamhandler = new LogStreamHandler((Task) this, 2, 1);
            } else {
                streamhandler = new LogStreamHandler((Task) this, 4, 4);
            }
        } else {
            if (this.output != null) {
                OutputStream fos = null;
                try {
                    fos = Files.newOutputStream(this.output.toPath(), new OpenOption[0]);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    outputstream = new PrintStream(bos);
                } catch (IOException e) {
                    FileUtils.close(fos);
                    throw new BuildException(e, getLocation());
                }
            } else if (!this.quiet) {
                outputstream = new LogOutputStream((Task) this, 2);
            } else {
                outputstream = new LogOutputStream((Task) this, 4);
            }
            if (this.error != null) {
                OutputStream fos2 = null;
                try {
                    fos2 = Files.newOutputStream(this.error.toPath(), new OpenOption[0]);
                    BufferedOutputStream bos2 = new BufferedOutputStream(fos2);
                    errorstream = new PrintStream(bos2);
                } catch (IOException e2) {
                    FileUtils.close(fos2);
                    throw new BuildException(e2, getLocation());
                }
            } else if (!this.quiet) {
                errorstream = new LogOutputStream((Task) this, 1);
            } else {
                errorstream = new LogOutputStream((Task) this, 4);
            }
            streamhandler = new PumpStreamHandler(outputstream, errorstream);
        }
        Execute exe = getExecute(toExecute, streamhandler);
        try {
            try {
                log("Building the RPM based on the " + this.specFile + " file");
                int returncode = exe.execute();
                if (Execute.isFailure(returncode)) {
                    String msg = "'" + toExecute.getExecutable() + "' failed with exit code " + returncode;
                    if (this.failOnError) {
                        throw new BuildException(msg);
                    }
                    log(msg, 0);
                }
                FileUtils.close(outputstream);
                FileUtils.close(errorstream);
            } catch (IOException e3) {
                throw new BuildException(e3, getLocation());
            }
        } catch (Throwable th) {
            FileUtils.close(outputstream);
            FileUtils.close(errorstream);
            throw th;
        }
    }

    public void setTopDir(File td) {
        this.topDir = td;
    }

    public void setCommand(String c) {
        this.command = c;
    }

    public void setSpecFile(String sf) {
        if (sf == null || sf.trim().isEmpty()) {
            throw new BuildException("You must specify a spec file", getLocation());
        }
        this.specFile = sf;
    }

    public void setCleanBuildDir(boolean cbd) {
        this.cleanBuildDir = cbd;
    }

    public void setRemoveSpec(boolean rs) {
        this.removeSpec = rs;
    }

    public void setRemoveSource(boolean rs) {
        this.removeSource = rs;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public void setError(File error) {
        this.error = error;
    }

    public void setRpmBuildCommand(String c) {
        this.rpmBuildCommand = c;
    }

    public void setFailOnError(boolean value) {
        this.failOnError = value;
    }

    public void setQuiet(boolean value) {
        this.quiet = value;
    }

    protected String guessRpmBuildCommand() {
        Map<String, String> env = Execute.getEnvironmentVariables();
        String path = env.get(PATH1);
        if (path == null) {
            path = env.get(PATH2);
            if (path == null) {
                path = env.get("path");
            }
        }
        if (path != null) {
            Path p = new Path(getProject(), path);
            String[] pElements = p.list();
            for (String pElement : pElements) {
                File f = new File(pElement, "rpmbuild" + (Os.isFamily(Os.FAMILY_DOS) ? ".exe" : ""));
                if (f.canRead()) {
                    return f.getAbsolutePath();
                }
            }
            return "rpm";
        }
        return "rpm";
    }

    protected Execute getExecute(Commandline toExecute, ExecuteStreamHandler streamhandler) {
        Execute exe = new Execute(streamhandler, null);
        exe.setAntRun(getProject());
        if (this.topDir == null) {
            this.topDir = getProject().getBaseDir();
        }
        exe.setWorkingDirectory(this.topDir);
        exe.setCommandline(toExecute.getCommandline());
        return exe;
    }
}
