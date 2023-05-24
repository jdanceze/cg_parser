package org.apache.tools.ant.types.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/Difference.class */
public class Difference extends BaseResourceCollectionContainer {
    @Override // org.apache.tools.ant.types.resources.BaseResourceCollectionContainer
    protected Collection<Resource> getCollection() {
        List<ResourceCollection> rcs = getResourceCollections();
        int size = rcs.size();
        if (size < 2) {
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(size);
            objArr[1] = size == 1 ? "collection" : "collections";
            throw new BuildException("The difference of %d resource %s is undefined.", objArr);
        }
        Set<Resource> hs = new HashSet<>();
        List<Resource> al = new ArrayList<>();
        for (ResourceCollection rc : rcs) {
            for (Resource r : rc) {
                if (hs.add(r)) {
                    al.add(r);
                } else {
                    al.remove(r);
                }
            }
        }
        return al;
    }
}
