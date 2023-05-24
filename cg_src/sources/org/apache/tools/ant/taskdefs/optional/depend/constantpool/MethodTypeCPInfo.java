package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/MethodTypeCPInfo.class */
public class MethodTypeCPInfo extends ConstantCPInfo {
    private int methodDescriptorIndex;
    private String methodDescriptor;

    public MethodTypeCPInfo() {
        super(16, 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        this.methodDescriptorIndex = cpStream.readUnsignedShort();
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void resolve(ConstantPool constantPool) {
        Utf8CPInfo methodClass = (Utf8CPInfo) constantPool.getEntry(this.methodDescriptorIndex);
        methodClass.resolve(constantPool);
        this.methodDescriptor = methodClass.getValue();
        super.resolve(constantPool);
    }

    public String toString() {
        if (isResolved()) {
            return "MethodDescriptor: " + this.methodDescriptor;
        }
        return "MethodDescriptorIndex: " + this.methodDescriptorIndex;
    }
}
