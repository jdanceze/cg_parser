package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/FloatCPInfo.class */
public class FloatCPInfo extends ConstantCPInfo {
    public FloatCPInfo() {
        super(4, 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        setValue(Float.valueOf(cpStream.readFloat()));
    }

    public String toString() {
        return "Float Constant Pool Entry: " + getValue();
    }
}
