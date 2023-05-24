package org.apache.tools.ant.types.resources.comparators;

import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.ResourceUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/comparators/Content.class */
public class Content extends ResourceComparator {
    private boolean binary = true;

    public void setBinary(boolean b) {
        this.binary = b;
    }

    public boolean isBinary() {
        return this.binary;
    }

    @Override // org.apache.tools.ant.types.resources.comparators.ResourceComparator
    protected int resourceCompare(Resource foo, Resource bar) {
        try {
            return ResourceUtils.compareContent(foo, bar, !this.binary);
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }
}
