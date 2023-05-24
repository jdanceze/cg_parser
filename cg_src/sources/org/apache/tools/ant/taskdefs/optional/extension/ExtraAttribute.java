package org.apache.tools.ant.taskdefs.optional.extension;

import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/extension/ExtraAttribute.class */
public class ExtraAttribute {
    private String name;
    private String value;

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getName() {
        return this.name;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getValue() {
        return this.value;
    }

    public void validate() throws BuildException {
        if (null == this.name) {
            throw new BuildException("Missing name from parameter.");
        }
        if (null == this.value) {
            throw new BuildException("Missing value from parameter " + this.name + ".");
        }
    }
}
