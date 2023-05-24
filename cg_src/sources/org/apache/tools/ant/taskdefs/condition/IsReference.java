package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Reference;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/IsReference.class */
public class IsReference extends ProjectComponent implements Condition {
    private Reference ref;
    private String type;

    public void setRefid(Reference r) {
        this.ref = r;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        if (this.ref == null) {
            throw new BuildException("No reference specified for isreference condition");
        }
        String key = this.ref.getRefId();
        if (!getProject().hasReference(key)) {
            return false;
        }
        if (this.type == null) {
            return true;
        }
        Class<?> typeClass = getProject().getDataTypeDefinitions().get(this.type);
        if (typeClass == null) {
            typeClass = getProject().getTaskDefinitions().get(this.type);
        }
        return typeClass != null && typeClass.isAssignableFrom(getProject().getReference(key).getClass());
    }
}
