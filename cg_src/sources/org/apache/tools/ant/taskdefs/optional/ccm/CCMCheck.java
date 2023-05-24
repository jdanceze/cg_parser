package org.apache.tools.ant.taskdefs.optional.ccm;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ccm/CCMCheck.class */
public class CCMCheck extends Continuus {
    public static final String FLAG_COMMENT = "/comment";
    public static final String FLAG_TASK = "/task";
    private File file = null;
    private String comment = null;
    private String task = null;
    protected Vector<FileSet> filesets = new Vector<>();

    public File getFile() {
        return this.file;
    }

    public void setFile(File v) {
        log("working file " + v, 3);
        this.file = v;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String v) {
        this.comment = v;
    }

    public String getTask() {
        return this.task;
    }

    public void setTask(String v) {
        this.task = v;
    }

    public void addFileset(FileSet set) {
        this.filesets.addElement(set);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        String[] includedFiles;
        if (this.file == null && this.filesets.isEmpty()) {
            throw new BuildException("Specify at least one source - a file or a fileset.");
        }
        if (this.file != null && this.file.exists() && this.file.isDirectory()) {
            throw new BuildException("CCMCheck cannot be generated for directories");
        }
        if (this.file != null && !this.filesets.isEmpty()) {
            throw new BuildException("Choose between file and fileset !");
        }
        if (getFile() != null) {
            doit();
            return;
        }
        Iterator<FileSet> it = this.filesets.iterator();
        while (it.hasNext()) {
            FileSet fs = it.next();
            File basedir = fs.getDir(getProject());
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            for (String srcFile : ds.getIncludedFiles()) {
                setFile(new File(basedir, srcFile));
                doit();
            }
        }
    }

    private void doit() {
        Commandline commandLine = new Commandline();
        commandLine.setExecutable(getCcmCommand());
        commandLine.createArgument().setValue(getCcmAction());
        checkOptions(commandLine);
        int result = run(commandLine);
        if (Execute.isFailure(result)) {
            throw new BuildException("Failed executing: " + commandLine, getLocation());
        }
    }

    private void checkOptions(Commandline cmd) {
        if (getComment() != null) {
            cmd.createArgument().setValue(FLAG_COMMENT);
            cmd.createArgument().setValue(getComment());
        }
        if (getTask() != null) {
            cmd.createArgument().setValue("/task");
            cmd.createArgument().setValue(getTask());
        }
        if (getFile() != null) {
            cmd.createArgument().setValue(this.file.getAbsolutePath());
        }
    }
}
