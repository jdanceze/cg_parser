package org.apache.tools.ant.types.resources;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/Intersect.class */
public class Intersect extends BaseResourceCollectionContainer {
    @Override // org.apache.tools.ant.types.resources.BaseResourceCollectionContainer
    protected Collection<Resource> getCollection() {
        List<ResourceCollection> rcs = getResourceCollections();
        int size = rcs.size();
        if (size < 2) {
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(size);
            objArr[1] = size == 1 ? "collection" : "collections";
            throw new BuildException("The intersection of %d resource %s is undefined.", objArr);
        }
        Function<ResourceCollection, Set<Resource>> toSet = c -> {
            return (Set) c.stream().collect(Collectors.toSet());
        };
        Iterator<ResourceCollection> rc = rcs.iterator();
        Set<Resource> s = new LinkedHashSet<>(toSet.apply(rc.next()));
        rc.forEachRemaining(c2 -> {
            s.retainAll((Collection) toSet.apply(c2));
        });
        return s;
    }
}
