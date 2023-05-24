package org.apache.tools.ant.types.resources;

import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/First.class */
public class First extends SizeLimitCollection {
    @Override // org.apache.tools.ant.types.resources.BaseResourceCollectionWrapper
    protected Collection<Resource> getCollection() {
        return (Collection) getResourceCollection().stream().limit(getValidCount()).collect(Collectors.toList());
    }
}
