package org.apache.tools.ant.taskdefs.condition;

import java.io.IOException;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.ResourceUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/ResourcesMatch.class */
public class ResourcesMatch implements Condition {
    private Union resources = null;
    private boolean asText = false;

    public void setAsText(boolean asText) {
        this.asText = asText;
    }

    public void add(ResourceCollection rc) {
        if (rc == null) {
            return;
        }
        this.resources = this.resources == null ? new Union() : this.resources;
        this.resources.add(rc);
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        if (this.resources == null) {
            throw new BuildException("You must specify one or more nested resource collections");
        }
        if (this.resources.size() > 1) {
            Iterator<Resource> i = this.resources.iterator();
            Resource next = i.next();
            while (true) {
                Resource r1 = next;
                if (i.hasNext()) {
                    Resource r2 = i.next();
                    try {
                        if (!ResourceUtils.contentEquals(r1, r2, this.asText)) {
                            return false;
                        }
                        next = r2;
                    } catch (IOException ioe) {
                        throw new BuildException("when comparing resources " + r1.toString() + " and " + r2.toString(), ioe);
                    }
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }
}
