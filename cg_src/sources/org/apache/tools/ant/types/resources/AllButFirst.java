package org.apache.tools.ant.types.resources;

import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/AllButFirst.class */
public class AllButFirst extends SizeLimitCollection {
    @Override // org.apache.tools.ant.types.resources.BaseResourceCollectionWrapper
    protected Collection<Resource> getCollection() {
        return (Collection) getResourceCollection().stream().skip(getValidCount()).collect(Collectors.toList());
    }

    @Override // org.apache.tools.ant.types.resources.SizeLimitCollection, org.apache.tools.ant.types.resources.AbstractResourceCollectionWrapper, org.apache.tools.ant.types.ResourceCollection
    public synchronized int size() {
        return Math.max(getResourceCollection().size() - getValidCount(), 0);
    }
}
