package org.apache.tools.ant.types.resources.comparators;

import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/comparators/Name.class */
public class Name extends ResourceComparator {
    @Override // org.apache.tools.ant.types.resources.comparators.ResourceComparator
    protected int resourceCompare(Resource foo, Resource bar) {
        return foo.getName().compareTo(bar.getName());
    }
}
