package org.apache.tools.ant.types.resources.comparators;

import java.util.Comparator;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/comparators/Size.class */
public class Size extends ResourceComparator {
    @Override // org.apache.tools.ant.types.resources.comparators.ResourceComparator
    protected int resourceCompare(Resource foo, Resource bar) {
        return Comparator.comparingLong((v0) -> {
            return v0.getSize();
        }).compare(foo, bar);
    }
}
