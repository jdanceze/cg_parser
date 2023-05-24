package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/TempFile.class */
public class TempFile extends Task {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private String property;
    private String prefix;
    private boolean deleteOnExit;
    private boolean createFile;
    private File destDir = null;
    private String suffix = "";

    public void setProperty(String property) {
        this.property = property;
    }

    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setDeleteOnExit(boolean deleteOnExit) {
        this.deleteOnExit = deleteOnExit;
    }

    public boolean isDeleteOnExit() {
        return this.deleteOnExit;
    }

    public void setCreateFile(boolean createFile) {
        this.createFile = createFile;
    }

    public boolean isCreateFile() {
        return this.createFile;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.property == null || this.property.isEmpty()) {
            throw new BuildException("no property specified");
        }
        if (this.destDir == null) {
            this.destDir = getProject().resolveFile(".");
        }
        File tfile = FILE_UTILS.createTempFile(getProject(), this.prefix, this.suffix, this.destDir, this.deleteOnExit, this.createFile);
        getProject().setNewProperty(this.property, tfile.toString());
    }
}
