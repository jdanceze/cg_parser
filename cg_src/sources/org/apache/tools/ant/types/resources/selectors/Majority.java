package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/selectors/Majority.class */
public class Majority extends ResourceSelectorContainer implements ResourceSelector {
    private boolean tie;

    public Majority() {
        this.tie = true;
    }

    public Majority(ResourceSelector... r) {
        super(r);
        this.tie = true;
    }

    public synchronized void setAllowtie(boolean b) {
        this.tie = b;
    }

    @Override // org.apache.tools.ant.types.resources.selectors.ResourceSelector
    public synchronized boolean isSelected(Resource r) {
        int passed = 0;
        int failed = 0;
        int count = selectorCount();
        boolean even = count % 2 == 0;
        int threshold = count / 2;
        for (ResourceSelector rs : getResourceSelectors()) {
            if (rs.isSelected(r)) {
                passed++;
                if (passed > threshold) {
                    return true;
                }
                if (even && this.tie && passed == threshold) {
                    return true;
                }
            } else {
                failed++;
                if (failed > threshold) {
                    return false;
                }
                if (even && !this.tie && failed == threshold) {
                    return false;
                }
            }
        }
        return false;
    }
}
