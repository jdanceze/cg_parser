package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/MethodRefCPInfo.class */
public class MethodRefCPInfo extends ConstantPoolEntry {
    private String methodClassName;
    private String methodName;
    private String methodType;
    private int classIndex;
    private int nameAndTypeIndex;

    public MethodRefCPInfo() {
        super(10, 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        this.classIndex = cpStream.readUnsignedShort();
        this.nameAndTypeIndex = cpStream.readUnsignedShort();
    }

    public String toString() {
        if (isResolved()) {
            return "Method : Class = " + this.methodClassName + ", name = " + this.methodName + ", type = " + this.methodType;
        }
        return "Method : Class index = " + this.classIndex + ", name and type index = " + this.nameAndTypeIndex;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void resolve(ConstantPool constantPool) {
        ClassCPInfo methodClass = (ClassCPInfo) constantPool.getEntry(this.classIndex);
        methodClass.resolve(constantPool);
        this.methodClassName = methodClass.getClassName();
        NameAndTypeCPInfo nt = (NameAndTypeCPInfo) constantPool.getEntry(this.nameAndTypeIndex);
        nt.resolve(constantPool);
        this.methodName = nt.getName();
        this.methodType = nt.getType();
        super.resolve(constantPool);
    }

    public String getMethodClassName() {
        return this.methodClassName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getMethodType() {
        return this.methodType;
    }
}
