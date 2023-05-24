package org.apache.tools.ant.taskdefs.optional.extension.resolvers;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.optional.extension.Extension;
import org.apache.tools.ant.taskdefs.optional.extension.ExtensionResolver;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/extension/resolvers/LocationResolver.class */
public class LocationResolver implements ExtensionResolver {
    private String location;

    public void setLocation(String location) {
        this.location = location;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.extension.ExtensionResolver
    public File resolve(Extension extension, Project project) throws BuildException {
        if (null == this.location) {
            throw new BuildException("No location specified for resolver");
        }
        return project.resolveFile(this.location);
    }

    public String toString() {
        return "Location[" + this.location + "]";
    }
}
