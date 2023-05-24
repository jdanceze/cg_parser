package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/IsSet.class */
public class IsSet extends ProjectComponent implements Condition {
    private String property;

    public void setProperty(String p) {
        this.property = p;
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        if (this.property == null) {
            throw new BuildException("No property specified for isset condition");
        }
        return getProject().getProperty(this.property) != null;
    }
}
