package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/selectors/Not.class */
public class Not implements ResourceSelector {
    private ResourceSelector sel;

    public Not() {
    }

    public Not(ResourceSelector s) {
        add(s);
    }

    public void add(ResourceSelector s) {
        if (this.sel != null) {
            throw new IllegalStateException("The Not ResourceSelector accepts a single nested ResourceSelector");
        }
        this.sel = s;
    }

    @Override // org.apache.tools.ant.types.resources.selectors.ResourceSelector
    public boolean isSelected(Resource r) {
        return !this.sel.isSelected(r);
    }
}
