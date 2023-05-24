package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/IsTrue.class */
public class IsTrue extends ProjectComponent implements Condition {
    private Boolean value = null;

    public void setValue(boolean value) {
        this.value = value ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        if (this.value == null) {
            throw new BuildException("Nothing to test for truth");
        }
        return this.value.booleanValue();
    }
}
