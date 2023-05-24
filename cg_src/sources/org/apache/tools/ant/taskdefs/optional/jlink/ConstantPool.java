package org.apache.tools.ant.taskdefs.optional.jlink;

import java.io.DataInput;
import java.io.IOException;
/* compiled from: ClassNameReader.java */
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/jlink/ConstantPool.class */
class ConstantPool {
    static final byte UTF8 = 1;
    static final byte UNUSED = 2;
    static final byte INTEGER = 3;
    static final byte FLOAT = 4;
    static final byte LONG = 5;
    static final byte DOUBLE = 6;
    static final byte CLASS = 7;
    static final byte STRING = 8;
    static final byte FIELDREF = 9;
    static final byte METHODREF = 10;
    static final byte INTERFACEMETHODREF = 11;
    static final byte NAMEANDTYPE = 12;
    byte[] types;
    Object[] values;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConstantPool(DataInput data) throws IOException {
        int count = data.readUnsignedShort();
        this.types = new byte[count];
        this.values = new Object[count];
        int i = 1;
        while (i < count) {
            byte type = data.readByte();
            this.types[i] = type;
            switch (type) {
                case 1:
                    this.values[i] = data.readUTF();
                    break;
                case 3:
                case 9:
                case 10:
                case 11:
                case 12:
                    this.values[i] = Integer.valueOf(data.readInt());
                    break;
                case 4:
                    this.values[i] = Float.valueOf(data.readFloat());
                    break;
                case 5:
                    this.values[i] = Long.valueOf(data.readLong());
                    i++;
                    break;
                case 6:
                    this.values[i] = Double.valueOf(data.readDouble());
                    i++;
                    break;
                case 7:
                case 8:
                    this.values[i] = Integer.valueOf(data.readUnsignedShort());
                    break;
            }
            i++;
        }
    }
}
