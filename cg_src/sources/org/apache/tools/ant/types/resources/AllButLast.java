package org.apache.tools.ant.types.resources;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/AllButLast.class */
public class AllButLast extends SizeLimitCollection {
    @Override // org.apache.tools.ant.types.resources.BaseResourceCollectionWrapper
    protected Collection<Resource> getCollection() {
        int ct = getValidCount();
        ResourceCollection nested = getResourceCollection();
        if (ct > nested.size()) {
            return Collections.emptyList();
        }
        return (Collection) nested.stream().limit(nested.size() - ct).collect(Collectors.toList());
    }

    @Override // org.apache.tools.ant.types.resources.SizeLimitCollection, org.apache.tools.ant.types.resources.AbstractResourceCollectionWrapper, org.apache.tools.ant.types.ResourceCollection
    public synchronized int size() {
        return Math.max(getResourceCollection().size() - getValidCount(), 0);
    }
}
