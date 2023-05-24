package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Mkdir.class */
public class Mkdir extends Task {
    private static final int MKDIR_RETRY_SLEEP_MILLIS = 10;
    private File dir;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.dir == null) {
            throw new BuildException("dir attribute is required", getLocation());
        }
        if (this.dir.isFile()) {
            throw new BuildException("Unable to create directory as a file already exists with that name: %s", this.dir.getAbsolutePath());
        }
        if (!this.dir.exists()) {
            boolean result = mkdirs(this.dir);
            if (!result) {
                if (this.dir.exists()) {
                    log("A different process or task has already created dir " + this.dir.getAbsolutePath(), 3);
                    return;
                }
                throw new BuildException("Directory " + this.dir.getAbsolutePath() + " creation was not successful for an unknown reason", getLocation());
            }
            log("Created dir: " + this.dir.getAbsolutePath());
            return;
        }
        log("Skipping " + this.dir.getAbsolutePath() + " because it already exists.", 3);
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public File getDir() {
        return this.dir;
    }

    private boolean mkdirs(File f) {
        if (!f.mkdirs()) {
            try {
                Thread.sleep(10L);
                return f.mkdirs();
            } catch (InterruptedException e) {
                return f.mkdirs();
            }
        }
        return true;
    }
}
