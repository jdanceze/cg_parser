package org.apache.tools.ant.property;

import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/property/NullReturn.class */
public final class NullReturn {
    public static final NullReturn NULL = new NullReturn();

    private NullReturn() {
    }

    public String toString() {
        return Jimple.NULL;
    }
}
