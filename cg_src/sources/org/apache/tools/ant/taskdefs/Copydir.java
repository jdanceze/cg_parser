package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
@Deprecated
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Copydir.class */
public class Copydir extends MatchingTask {
    private File srcDir;
    private File destDir;
    private boolean filtering = false;
    private boolean flatten = false;
    private boolean forceOverwrite = false;
    private Map<String, String> filecopyList = new Hashtable();

    public void setSrc(File src) {
        this.srcDir = src;
    }

    public void setDest(File dest) {
        this.destDir = dest;
    }

    public void setFiltering(boolean filter) {
        this.filtering = filter;
    }

    public void setFlatten(boolean flatten) {
        this.flatten = flatten;
    }

    public void setForceoverwrite(boolean force) {
        this.forceOverwrite = force;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        log("DEPRECATED - The copydir task is deprecated.  Use copy instead.");
        if (this.srcDir == null) {
            throw new BuildException("src attribute must be set!", getLocation());
        }
        if (!this.srcDir.exists()) {
            throw new BuildException("srcdir " + this.srcDir.toString() + " does not exist!", getLocation());
        }
        if (this.destDir == null) {
            throw new BuildException("The dest attribute must be set.", getLocation());
        }
        if (this.srcDir.equals(this.destDir)) {
            log("Warning: src == dest", 1);
        }
        DirectoryScanner ds = super.getDirectoryScanner(this.srcDir);
        try {
            scanDir(this.srcDir, this.destDir, ds.getIncludedFiles());
            if (this.filecopyList.size() > 0) {
                log("Copying " + this.filecopyList.size() + " file" + (this.filecopyList.size() == 1 ? "" : "s") + " to " + this.destDir.getAbsolutePath());
                for (Map.Entry<String, String> e : this.filecopyList.entrySet()) {
                    String fromFile = e.getKey();
                    String toFile = e.getValue();
                    try {
                        getProject().copyFile(fromFile, toFile, this.filtering, this.forceOverwrite);
                    } catch (IOException ioe) {
                        String msg = "Failed to copy " + fromFile + " to " + toFile + " due to " + ioe.getMessage();
                        throw new BuildException(msg, ioe, getLocation());
                    }
                }
            }
        } finally {
            this.filecopyList.clear();
        }
    }

    private void scanDir(File from, File to, String[] files) {
        File destFile;
        for (String filename : files) {
            File srcFile = new File(from, filename);
            if (this.flatten) {
                destFile = new File(to, new File(filename).getName());
            } else {
                destFile = new File(to, filename);
            }
            if (this.forceOverwrite || srcFile.lastModified() > destFile.lastModified()) {
                this.filecopyList.put(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            }
        }
    }
}
