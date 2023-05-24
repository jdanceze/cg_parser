package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/NameAndTypeCPInfo.class */
public class NameAndTypeCPInfo extends ConstantPoolEntry {
    private String name;
    private String type;
    private int nameIndex;
    private int descriptorIndex;

    public NameAndTypeCPInfo() {
        super(12, 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        this.nameIndex = cpStream.readUnsignedShort();
        this.descriptorIndex = cpStream.readUnsignedShort();
    }

    public String toString() {
        if (isResolved()) {
            return "Name = " + this.name + ", type = " + this.type;
        }
        return "Name index = " + this.nameIndex + ", descriptor index = " + this.descriptorIndex;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void resolve(ConstantPool constantPool) {
        this.name = ((Utf8CPInfo) constantPool.getEntry(this.nameIndex)).getValue();
        this.type = ((Utf8CPInfo) constantPool.getEntry(this.descriptorIndex)).getValue();
        super.resolve(constantPool);
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }
}
