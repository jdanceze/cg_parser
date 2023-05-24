package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/MethodHandleCPInfo.class */
public class MethodHandleCPInfo extends ConstantPoolEntry {
    private ConstantPoolEntry reference;
    private ReferenceKind referenceKind;
    private int referenceIndex;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/MethodHandleCPInfo$ReferenceKind.class */
    public enum ReferenceKind {
        REF_getField,
        REF_getStatic,
        REF_putField,
        REF_putStatic,
        REF_invokeVirtual,
        REF_invokeStatic,
        REF_invokeSpecial,
        REF_newInvokeSpecial,
        REF_invokeInterface;

        public int value() {
            return ordinal() + 1;
        }
    }

    public MethodHandleCPInfo() {
        super(15, 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        this.referenceKind = ReferenceKind.values()[cpStream.readUnsignedByte() - 1];
        this.referenceIndex = cpStream.readUnsignedShort();
    }

    public String toString() {
        if (isResolved()) {
            return "MethodHandle : " + this.reference.toString();
        }
        return "MethodHandle : Reference kind = " + this.referenceKind + "Reference index = " + this.referenceIndex;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void resolve(ConstantPool constantPool) {
        this.reference = constantPool.getEntry(this.referenceIndex);
        this.reference.resolve(constantPool);
        super.resolve(constantPool);
    }
}
