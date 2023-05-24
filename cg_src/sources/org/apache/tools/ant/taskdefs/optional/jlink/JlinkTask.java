package org.apache.tools.ant.taskdefs.optional.jlink;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/jlink/JlinkTask.class */
public class JlinkTask extends MatchingTask {
    private File outfile = null;
    private Path mergefiles = null;
    private Path addfiles = null;
    private boolean compress = false;

    public void setOutfile(File outfile) {
        this.outfile = outfile;
    }

    public Path createMergefiles() {
        if (this.mergefiles == null) {
            this.mergefiles = new Path(getProject());
        }
        return this.mergefiles.createPath();
    }

    public void setMergefiles(Path mergefiles) {
        if (this.mergefiles == null) {
            this.mergefiles = mergefiles;
        } else {
            this.mergefiles.append(mergefiles);
        }
    }

    public Path createAddfiles() {
        if (this.addfiles == null) {
            this.addfiles = new Path(getProject());
        }
        return this.addfiles.createPath();
    }

    public void setAddfiles(Path addfiles) {
        if (this.addfiles == null) {
            this.addfiles = addfiles;
        } else {
            this.addfiles.append(addfiles);
        }
    }

    public void setCompress(boolean compress) {
        this.compress = compress;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.outfile == null) {
            throw new BuildException("outfile attribute is required! Please set.");
        }
        if (!haveAddFiles() && !haveMergeFiles()) {
            throw new BuildException("addfiles or mergefiles required! Please set.");
        }
        log("linking:     " + this.outfile.getPath());
        log("compression: " + this.compress, 3);
        jlink linker = new jlink();
        linker.setOutfile(this.outfile.getPath());
        linker.setCompression(this.compress);
        if (haveMergeFiles()) {
            log("merge files: " + this.mergefiles.toString(), 3);
            linker.addMergeFiles(this.mergefiles.list());
        }
        if (haveAddFiles()) {
            log("add files: " + this.addfiles.toString(), 3);
            linker.addAddFiles(this.addfiles.list());
        }
        try {
            linker.link();
        } catch (Exception ex) {
            throw new BuildException(ex, getLocation());
        }
    }

    private boolean haveAddFiles() {
        return haveEntries(this.addfiles);
    }

    private boolean haveMergeFiles() {
        return haveEntries(this.mergefiles);
    }

    private boolean haveEntries(Path p) {
        return (p == null || p.isEmpty()) ? false : true;
    }
}
