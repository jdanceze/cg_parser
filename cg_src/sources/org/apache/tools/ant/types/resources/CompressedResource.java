package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/CompressedResource.class */
public abstract class CompressedResource extends ContentTransformingResource {
    protected abstract String getCompressionName();

    /* JADX INFO: Access modifiers changed from: protected */
    public CompressedResource() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CompressedResource(ResourceCollection other) {
        addConfigured(other);
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public String toString() {
        return getCompressionName() + " compressed " + super.toString();
    }
}
