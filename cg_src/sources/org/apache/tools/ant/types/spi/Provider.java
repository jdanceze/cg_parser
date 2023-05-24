package org.apache.tools.ant.types.spi;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/spi/Provider.class */
public class Provider extends ProjectComponent {
    private String type;

    public String getClassName() {
        return this.type;
    }

    public void setClassName(String type) {
        this.type = type;
    }

    public void check() {
        if (this.type == null) {
            throw new BuildException("classname attribute must be set for provider element", getLocation());
        }
        if (this.type.isEmpty()) {
            throw new BuildException("Invalid empty classname", getLocation());
        }
    }
}
