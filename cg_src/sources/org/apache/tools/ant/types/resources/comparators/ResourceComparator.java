package org.apache.tools.ant.types.resources.comparators;

import java.util.Comparator;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/comparators/ResourceComparator.class */
public abstract class ResourceComparator extends DataType implements Comparator<Resource> {
    protected abstract int resourceCompare(Resource resource, Resource resource2);

    @Override // java.util.Comparator
    public final int compare(Resource foo, Resource bar) {
        dieOnCircularReference();
        ResourceComparator c = isReference() ? getRef() : this;
        return c.resourceCompare(foo, bar);
    }

    public boolean equals(Object o) {
        if (isReference()) {
            return getRef().equals(o);
        }
        return o != null && (o == this || o.getClass().equals(getClass()));
    }

    public synchronized int hashCode() {
        if (isReference()) {
            return getRef().hashCode();
        }
        return getClass().hashCode();
    }

    private ResourceComparator getRef() {
        return (ResourceComparator) getCheckedRef(ResourceComparator.class);
    }
}
