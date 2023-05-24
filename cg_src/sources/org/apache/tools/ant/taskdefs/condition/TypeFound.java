package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.ProjectHelper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/TypeFound.class */
public class TypeFound extends ProjectComponent implements Condition {
    private String name;
    private String uri;

    public void setName(String name) {
        this.name = name;
    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    protected boolean doesTypeExist(String typename) {
        ComponentHelper helper = ComponentHelper.getComponentHelper(getProject());
        String componentName = ProjectHelper.genComponentName(this.uri, typename);
        AntTypeDefinition def = helper.getDefinition(componentName);
        if (def == null) {
            return false;
        }
        boolean found = def.getExposedClass(getProject()) != null;
        if (!found) {
            String text = helper.diagnoseCreationFailure(componentName, "type");
            log(text, 3);
        }
        return found;
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        if (this.name == null) {
            throw new BuildException("No type specified");
        }
        return doesTypeExist(this.name);
    }
}
