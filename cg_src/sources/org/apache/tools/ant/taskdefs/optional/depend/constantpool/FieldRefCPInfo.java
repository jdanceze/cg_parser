package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/FieldRefCPInfo.class */
public class FieldRefCPInfo extends ConstantPoolEntry {
    private String fieldClassName;
    private String fieldName;
    private String fieldType;
    private int classIndex;
    private int nameAndTypeIndex;

    public FieldRefCPInfo() {
        super(9, 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        this.classIndex = cpStream.readUnsignedShort();
        this.nameAndTypeIndex = cpStream.readUnsignedShort();
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void resolve(ConstantPool constantPool) {
        ClassCPInfo fieldClass = (ClassCPInfo) constantPool.getEntry(this.classIndex);
        fieldClass.resolve(constantPool);
        this.fieldClassName = fieldClass.getClassName();
        NameAndTypeCPInfo nt = (NameAndTypeCPInfo) constantPool.getEntry(this.nameAndTypeIndex);
        nt.resolve(constantPool);
        this.fieldName = nt.getName();
        this.fieldType = nt.getType();
        super.resolve(constantPool);
    }

    public String toString() {
        if (isResolved()) {
            return "Field : Class = " + this.fieldClassName + ", name = " + this.fieldName + ", type = " + this.fieldType;
        }
        return "Field : Class index = " + this.classIndex + ", name and type index = " + this.nameAndTypeIndex;
    }

    public String getFieldClassName() {
        return this.fieldClassName;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getFieldType() {
        return this.fieldType;
    }
}
