package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/ModuleCPInfo.class */
public class ModuleCPInfo extends ConstantCPInfo {
    private int moduleNameIndex;
    private String moduleName;

    public ModuleCPInfo() {
        super(19, 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        this.moduleNameIndex = cpStream.readUnsignedShort();
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void resolve(ConstantPool constantPool) {
        this.moduleName = ((Utf8CPInfo) constantPool.getEntry(this.moduleNameIndex)).getValue();
        super.resolve(constantPool);
    }

    public String toString() {
        return "Module info Constant Pool Entry for " + this.moduleName + "[" + this.moduleNameIndex + "]";
    }
}
