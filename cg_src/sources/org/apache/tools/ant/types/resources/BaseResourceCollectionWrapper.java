package org.apache.tools.ant.types.resources;

import java.util.Collection;
import java.util.Iterator;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/BaseResourceCollectionWrapper.class */
public abstract class BaseResourceCollectionWrapper extends AbstractResourceCollectionWrapper {
    private Collection<Resource> coll = null;

    protected abstract Collection<Resource> getCollection();

    @Override // org.apache.tools.ant.types.resources.AbstractResourceCollectionWrapper
    protected Iterator<Resource> createIterator() {
        return cacheCollection().iterator();
    }

    @Override // org.apache.tools.ant.types.resources.AbstractResourceCollectionWrapper
    protected int getSize() {
        return cacheCollection().size();
    }

    private synchronized Collection<Resource> cacheCollection() {
        if (this.coll == null || !isCache()) {
            this.coll = getCollection();
        }
        return this.coll;
    }
}
