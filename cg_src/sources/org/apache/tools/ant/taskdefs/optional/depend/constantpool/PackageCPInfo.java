package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/PackageCPInfo.class */
public class PackageCPInfo extends ConstantCPInfo {
    private int packageNameIndex;
    private String packageName;

    public PackageCPInfo() {
        super(20, 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        this.packageNameIndex = cpStream.readUnsignedShort();
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void resolve(ConstantPool constantPool) {
        this.packageName = ((Utf8CPInfo) constantPool.getEntry(this.packageNameIndex)).getValue();
        super.resolve(constantPool);
    }

    public String toString() {
        return "Package info Constant Pool Entry for " + this.packageName + "[" + this.packageNameIndex + "]";
    }
}
