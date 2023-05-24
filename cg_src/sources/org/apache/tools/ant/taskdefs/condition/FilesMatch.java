package org.apache.tools.ant.taskdefs.condition;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/FilesMatch.class */
public class FilesMatch implements Condition {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private File file1;
    private File file2;
    private boolean textfile = false;

    public void setFile1(File file1) {
        this.file1 = file1;
    }

    public void setFile2(File file2) {
        this.file2 = file2;
    }

    public void setTextfile(boolean textfile) {
        this.textfile = textfile;
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        if (this.file1 == null || this.file2 == null) {
            throw new BuildException("both file1 and file2 are required in filesmatch");
        }
        try {
            boolean matches = FILE_UTILS.contentEquals(this.file1, this.file2, this.textfile);
            return matches;
        } catch (IOException ioe) {
            throw new BuildException("when comparing files: " + ioe.getMessage(), ioe);
        }
    }
}
