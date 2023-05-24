package org.apache.tools.ant.taskdefs.optional.depend.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/DoubleCPInfo.class */
public class DoubleCPInfo extends ConstantCPInfo {
    public DoubleCPInfo() {
        super(6, 2);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry
    public void read(DataInputStream cpStream) throws IOException {
        setValue(Double.valueOf(cpStream.readDouble()));
    }

    public String toString() {
        return "Double Constant Pool Entry: " + getValue();
    }
}
