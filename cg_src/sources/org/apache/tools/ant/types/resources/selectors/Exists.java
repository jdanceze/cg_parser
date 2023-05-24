package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/selectors/Exists.class */
public class Exists implements ResourceSelector {
    @Override // org.apache.tools.ant.types.resources.selectors.ResourceSelector
    public boolean isSelected(Resource r) {
        return r.isExists();
    }
}
