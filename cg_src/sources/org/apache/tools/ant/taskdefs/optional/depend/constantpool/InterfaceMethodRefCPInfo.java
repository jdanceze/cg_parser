package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/InterfaceMethodRefCPInfo.class */
public class InterfaceMethodRefCPInfo extends ConstantPoolEntry {
    private String interfaceMethodClassName;
    private String interfaceMethodName;
    private String interfaceMethodType;
    private int classIndex;
    private int nameAndTypeIndex;

    public InterfaceMethodRefCPInfo() {
        super(11, 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        this.classIndex = cpStream.readUnsignedShort();
        this.nameAndTypeIndex = cpStream.readUnsignedShort();
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void resolve(ConstantPool constantPool) {
        ClassCPInfo interfaceMethodClass = (ClassCPInfo) constantPool.getEntry(this.classIndex);
        interfaceMethodClass.resolve(constantPool);
        this.interfaceMethodClassName = interfaceMethodClass.getClassName();
        NameAndTypeCPInfo nt = (NameAndTypeCPInfo) constantPool.getEntry(this.nameAndTypeIndex);
        nt.resolve(constantPool);
        this.interfaceMethodName = nt.getName();
        this.interfaceMethodType = nt.getType();
        super.resolve(constantPool);
    }

    public String toString() {
        if (isResolved()) {
            return "InterfaceMethod : Class = " + this.interfaceMethodClassName + ", name = " + this.interfaceMethodName + ", type = " + this.interfaceMethodType;
        }
        return "InterfaceMethod : Class index = " + this.classIndex + ", name and type index = " + this.nameAndTypeIndex;
    }

    public String getInterfaceMethodClassName() {
        return this.interfaceMethodClassName;
    }

    public String getInterfaceMethodName() {
        return this.interfaceMethodName;
    }

    public String getInterfaceMethodType() {
        return this.interfaceMethodType;
    }
}
