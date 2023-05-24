package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
@Deprecated
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Deltree.class */
public class Deltree extends Task {
    private File dir;

    public void setDir(File dir) {
        this.dir = dir;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        log("DEPRECATED - The deltree task is deprecated.  Use delete instead.");
        if (this.dir == null) {
            throw new BuildException("dir attribute must be set!", getLocation());
        }
        if (this.dir.exists()) {
            if (!this.dir.isDirectory()) {
                if (!this.dir.delete()) {
                    throw new BuildException("Unable to delete directory " + this.dir.getAbsolutePath(), getLocation());
                }
                return;
            }
            log("Deleting: " + this.dir.getAbsolutePath());
            removeDir(this.dir);
        }
    }

    private void removeDir(File dir) {
        String[] list;
        for (String s : dir.list()) {
            File f = new File(dir, s);
            if (f.isDirectory()) {
                removeDir(f);
            } else if (!f.delete()) {
                throw new BuildException("Unable to delete file " + f.getAbsolutePath());
            }
        }
        if (!dir.delete()) {
            throw new BuildException("Unable to delete directory " + dir.getAbsolutePath());
        }
    }
}
