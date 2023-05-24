package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.Comparison;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/selectors/Size.class */
public class Size implements ResourceSelector {
    private long size = -1;
    private Comparison when = Comparison.EQUAL;

    public void setSize(long l) {
        this.size = l;
    }

    public long getSize() {
        return this.size;
    }

    public void setWhen(Comparison c) {
        this.when = c;
    }

    public Comparison getWhen() {
        return this.when;
    }

    @Override // org.apache.tools.ant.types.resources.selectors.ResourceSelector
    public boolean isSelected(Resource r) {
        long diff = r.getSize() - this.size;
        return this.when.evaluate(diff == 0 ? 0 : (int) (diff / Math.abs(diff)));
    }
}
