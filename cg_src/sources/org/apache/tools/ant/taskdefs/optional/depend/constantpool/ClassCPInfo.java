package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/ClassCPInfo.class */
public class ClassCPInfo extends ConstantPoolEntry {
    private String className;
    private int index;

    public ClassCPInfo() {
        super(7, 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        this.index = cpStream.readUnsignedShort();
        this.className = "unresolved";
    }

    public String toString() {
        return "Class Constant Pool Entry for " + this.className + "[" + this.index + "]";
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void resolve(ConstantPool constantPool) {
        this.className = ((Utf8CPInfo) constantPool.getEntry(this.index)).getValue();
        super.resolve(constantPool);
    }

    public String getClassName() {
        return this.className;
    }
}
