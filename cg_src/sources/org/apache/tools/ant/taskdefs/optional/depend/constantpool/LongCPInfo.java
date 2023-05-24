package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/LongCPInfo.class */
public class LongCPInfo extends ConstantCPInfo {
    public LongCPInfo() {
        super(5, 2);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        setValue(Long.valueOf(cpStream.readLong()));
    }

    public String toString() {
        return "Long Constant Pool Entry: " + getValue();
    }
}
