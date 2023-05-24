package org.apache.tools.ant.taskdefs.optional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.taskdefs.StreamPumper;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.FileUtils;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/Cab.class */
public class Cab extends MatchingTask {
    private static final int DEFAULT_RESULT = -99;
    private File cabFile;
    private File baseDir;
    private String cmdOptions;
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private boolean doCompress = true;
    private boolean doVerbose = false;
    private boolean filesetAdded = false;
    protected String archiveType = "cab";

    public void setCabfile(File cabFile) {
        this.cabFile = cabFile;
    }

    public void setBasedir(File baseDir) {
        this.baseDir = baseDir;
    }

    public void setCompress(boolean compress) {
        this.doCompress = compress;
    }

    public void setVerbose(boolean verbose) {
        this.doVerbose = verbose;
    }

    public void setOptions(String options) {
        this.cmdOptions = options;
    }

    public void addFileset(FileSet fileset) {
        if (this.filesetAdded) {
            throw new BuildException("Only one nested fileset allowed");
        }
        this.filesetAdded = true;
        this.fileset = fileset;
    }

    protected void checkConfiguration() throws BuildException {
        if (this.baseDir == null && !this.filesetAdded) {
            throw new BuildException("basedir attribute or one nested fileset is required!", getLocation());
        }
        if (this.baseDir != null && !this.baseDir.exists()) {
            throw new BuildException("basedir does not exist!", getLocation());
        }
        if (this.baseDir != null && this.filesetAdded) {
            throw new BuildException("Both basedir attribute and a nested fileset is not allowed");
        }
        if (this.cabFile == null) {
            throw new BuildException("cabfile attribute must be set!", getLocation());
        }
    }

    protected ExecTask createExec() throws BuildException {
        return new ExecTask(this);
    }

    protected boolean isUpToDate(Vector<String> files) {
        long cabModified = this.cabFile.lastModified();
        return files.stream().map(f -> {
            return FILE_UTILS.resolveFile(this.baseDir, f);
        }).mapToLong((v0) -> {
            return v0.lastModified();
        }).allMatch(t -> {
            return t < cabModified;
        });
    }

    protected File createListFile(Vector<String> files) throws IOException {
        File listFile = FILE_UTILS.createTempFile(getProject(), "ant", "", null, true, true);
        BufferedWriter writer = new BufferedWriter(new FileWriter(listFile));
        try {
            Iterator<String> it = files.iterator();
            while (it.hasNext()) {
                String f = it.next();
                String s = String.format("\"%s\"", f);
                writer.write(s);
                writer.newLine();
            }
            writer.close();
            return listFile;
        } catch (Throwable th) {
            try {
                writer.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    protected void appendFiles(Vector<String> files, DirectoryScanner ds) {
        Collections.addAll(files, ds.getIncludedFiles());
    }

    protected Vector<String> getFileList() throws BuildException {
        Vector<String> files = new Vector<>();
        if (this.baseDir != null) {
            appendFiles(files, super.getDirectoryScanner(this.baseDir));
        } else {
            this.baseDir = this.fileset.getDir();
            appendFiles(files, this.fileset.getDirectoryScanner(getProject()));
        }
        return files;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        checkConfiguration();
        Vector<String> files = getFileList();
        if (isUpToDate(files)) {
            return;
        }
        log("Building " + this.archiveType + ": " + this.cabFile.getAbsolutePath());
        if (!Os.isFamily(Os.FAMILY_WINDOWS)) {
            log("Using listcab/libcabinet", 3);
            StringBuilder sb = new StringBuilder();
            files.forEach(f -> {
                sb.append(f).append("\n");
            });
            sb.append("\n").append(this.cabFile.getAbsolutePath()).append("\n");
            try {
                Process p = Execute.launch(getProject(), new String[]{"listcab"}, null, this.baseDir != null ? this.baseDir : getProject().getBaseDir(), true);
                OutputStream out = p.getOutputStream();
                LogOutputStream outLog = new LogOutputStream((Task) this, 3);
                LogOutputStream errLog = new LogOutputStream((Task) this, 0);
                StreamPumper outPump = new StreamPumper(p.getInputStream(), outLog);
                StreamPumper errPump = new StreamPumper(p.getErrorStream(), errLog);
                new Thread(outPump).start();
                new Thread(errPump).start();
                out.write(sb.toString().getBytes());
                out.flush();
                out.close();
                int result = DEFAULT_RESULT;
                try {
                    result = p.waitFor();
                    outPump.waitFor();
                    outLog.close();
                    errPump.waitFor();
                    errLog.close();
                } catch (InterruptedException ie) {
                    log("Thread interrupted: " + ie);
                }
                if (Execute.isFailure(result)) {
                    log("Error executing listcab; error code: " + result);
                }
                return;
            } catch (IOException ex) {
                throw new BuildException("Problem creating " + this.cabFile + Instruction.argsep + ex.getMessage(), getLocation());
            }
        }
        try {
            File listFile = createListFile(files);
            ExecTask exec = createExec();
            File outFile = null;
            exec.setFailonerror(true);
            exec.setDir(this.baseDir);
            if (!this.doVerbose) {
                outFile = FILE_UTILS.createTempFile(getProject(), "ant", "", null, true, true);
                exec.setOutput(outFile);
            }
            exec.setExecutable("cabarc");
            exec.createArg().setValue("-r");
            exec.createArg().setValue("-p");
            if (!this.doCompress) {
                exec.createArg().setValue("-m");
                exec.createArg().setValue("none");
            }
            if (this.cmdOptions != null) {
                exec.createArg().setLine(this.cmdOptions);
            }
            exec.createArg().setValue("n");
            exec.createArg().setFile(this.cabFile);
            exec.createArg().setValue("@" + listFile.getAbsolutePath());
            exec.execute();
            if (outFile != null) {
                outFile.delete();
            }
            listFile.delete();
        } catch (IOException ioe) {
            throw new BuildException("Problem creating " + this.cabFile + Instruction.argsep + ioe.getMessage(), getLocation());
        }
    }
}
