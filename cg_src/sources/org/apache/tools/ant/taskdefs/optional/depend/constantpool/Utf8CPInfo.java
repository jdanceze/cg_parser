package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/Utf8CPInfo.class */
public class Utf8CPInfo extends ConstantPoolEntry {
    private String value;

    public Utf8CPInfo() {
        super(1, 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        this.value = cpStream.readUTF();
    }

    public String toString() {
        return "UTF8 Value = " + this.value;
    }

    public String getValue() {
        return this.value;
    }
}
