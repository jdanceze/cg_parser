package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/selectors/And.class */
public class And extends ResourceSelectorContainer implements ResourceSelector {
    public And() {
    }

    public And(ResourceSelector... r) {
        super(r);
    }

    @Override // org.apache.tools.ant.types.resources.selectors.ResourceSelector
    public boolean isSelected(Resource r) {
        return getResourceSelectors().stream().allMatch(s -> {
            return s.isSelected(r);
        });
    }
}
