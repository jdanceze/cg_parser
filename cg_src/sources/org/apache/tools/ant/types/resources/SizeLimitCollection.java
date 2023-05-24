package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/SizeLimitCollection.class */
public abstract class SizeLimitCollection extends BaseResourceCollectionWrapper {
    private static final String BAD_COUNT = "size-limited collection count should be set to an int >= 0";
    private int count = 1;

    public synchronized void setCount(int i) {
        checkAttributesAllowed();
        this.count = i;
    }

    public synchronized int getCount() {
        return this.count;
    }

    @Override // org.apache.tools.ant.types.resources.AbstractResourceCollectionWrapper, org.apache.tools.ant.types.ResourceCollection
    public synchronized int size() {
        return Math.min(getResourceCollection().size(), getValidCount());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getValidCount() {
        int ct = getCount();
        if (ct < 0) {
            throw new BuildException(BAD_COUNT);
        }
        return ct;
    }
}
