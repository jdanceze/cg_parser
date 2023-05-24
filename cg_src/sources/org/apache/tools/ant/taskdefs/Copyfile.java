package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
@Deprecated
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Copyfile.class */
public class Copyfile extends Task {
    private File srcFile;
    private File destFile;
    private boolean filtering = false;
    private boolean forceOverwrite = false;

    public void setSrc(File src) {
        this.srcFile = src;
    }

    public void setForceoverwrite(boolean force) {
        this.forceOverwrite = force;
    }

    public void setDest(File dest) {
        this.destFile = dest;
    }

    public void setFiltering(String filter) {
        this.filtering = Project.toBoolean(filter);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        log("DEPRECATED - The copyfile task is deprecated.  Use copy instead.");
        if (this.srcFile == null) {
            throw new BuildException("The src attribute must be present.", getLocation());
        }
        if (!this.srcFile.exists()) {
            throw new BuildException("src " + this.srcFile.toString() + DirectoryScanner.DOES_NOT_EXIST_POSTFIX, getLocation());
        }
        if (this.destFile == null) {
            throw new BuildException("The dest attribute must be present.", getLocation());
        }
        if (this.srcFile.equals(this.destFile)) {
            log("Warning: src == dest", 1);
        }
        if (this.forceOverwrite || this.srcFile.lastModified() > this.destFile.lastModified()) {
            try {
                getProject().copyFile(this.srcFile, this.destFile, this.filtering, this.forceOverwrite);
            } catch (IOException ioe) {
                throw new BuildException("Error copying file: " + this.srcFile.getAbsolutePath() + " due to " + ioe.getMessage());
            }
        }
    }
}
