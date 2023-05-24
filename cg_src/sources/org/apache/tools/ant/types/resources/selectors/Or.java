package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/selectors/Or.class */
public class Or extends ResourceSelectorContainer implements ResourceSelector {
    public Or() {
    }

    public Or(ResourceSelector... r) {
        super(r);
    }

    @Override // org.apache.tools.ant.types.resources.selectors.ResourceSelector
    public boolean isSelected(Resource r) {
        return getResourceSelectors().stream().anyMatch(s -> {
            return s.isSelected(r);
        });
    }
}
