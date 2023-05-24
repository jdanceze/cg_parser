package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/InvokeDynamicCPInfo.class */
public class InvokeDynamicCPInfo extends ConstantCPInfo {
    private int bootstrapMethodAttrIndex;
    private int nameAndTypeIndex;
    private NameAndTypeCPInfo nameAndTypeCPInfo;

    public InvokeDynamicCPInfo() {
        super(18, 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        this.bootstrapMethodAttrIndex = cpStream.readUnsignedShort();
        this.nameAndTypeIndex = cpStream.readUnsignedShort();
    }

    public String toString() {
        if (isResolved()) {
            return "Name = " + this.nameAndTypeCPInfo.getName() + ", type = " + this.nameAndTypeCPInfo.getType();
        }
        return "BootstrapMethodAttrIndex inx = " + this.bootstrapMethodAttrIndex + "NameAndType index = " + this.nameAndTypeIndex;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void resolve(ConstantPool constantPool) {
        this.nameAndTypeCPInfo = (NameAndTypeCPInfo) constantPool.getEntry(this.nameAndTypeIndex);
        this.nameAndTypeCPInfo.resolve(constantPool);
        super.resolve(constantPool);
    }
}
