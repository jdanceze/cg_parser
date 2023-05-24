package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/ResourceExists.class */
public class ResourceExists extends ProjectComponent implements Condition {
    private Resource resource;

    public void add(Resource r) {
        if (this.resource != null) {
            throw new BuildException("only one resource can be tested");
        }
        this.resource = r;
    }

    protected void validate() throws BuildException {
        if (this.resource == null) {
            throw new BuildException("resource is required");
        }
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        validate();
        return this.resource.isExists();
    }
}
