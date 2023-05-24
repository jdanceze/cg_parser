package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Dirname.class */
public class Dirname extends Task {
    private File file;
    private String property;

    public void setFile(File file) {
        this.file = file;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.property == null) {
            throw new BuildException("property attribute required", getLocation());
        }
        if (this.file == null) {
            throw new BuildException("file attribute required", getLocation());
        }
        getProject().setNewProperty(this.property, this.file.getParent());
    }
}
