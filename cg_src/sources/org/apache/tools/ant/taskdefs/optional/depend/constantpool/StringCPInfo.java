package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/StringCPInfo.class */
public class StringCPInfo extends ConstantCPInfo {
    private int index;

    public StringCPInfo() {
        super(8, 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        this.index = cpStream.readUnsignedShort();
        setValue("unresolved");
    }

    public String toString() {
        return "String Constant Pool Entry for " + getValue() + "[" + this.index + "]";
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void resolve(ConstantPool constantPool) {
        setValue(((Utf8CPInfo) constantPool.getEntry(this.index)).getValue());
        super.resolve(constantPool);
    }
}
