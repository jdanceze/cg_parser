package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/IntegerCPInfo.class */
public class IntegerCPInfo extends ConstantCPInfo {
    public IntegerCPInfo() {
        super(3, 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        setValue(Integer.valueOf(cpStream.readInt()));
    }

    public String toString() {
        return "Integer Constant Pool Entry: " + getValue();
    }
}
