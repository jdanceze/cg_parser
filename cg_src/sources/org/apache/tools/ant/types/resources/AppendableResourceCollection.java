package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/AppendableResourceCollection.class */
public interface AppendableResourceCollection extends ResourceCollection {
    void add(ResourceCollection resourceCollection) throws BuildException;
}
