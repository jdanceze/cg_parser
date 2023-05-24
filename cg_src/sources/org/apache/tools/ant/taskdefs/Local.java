package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.property.LocalProperties;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Local.class */
public class Local extends Task {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        if (this.name == null) {
            throw new BuildException("Missing attribute name");
        }
        LocalProperties.get(getProject()).addLocal(this.name);
    }
}
