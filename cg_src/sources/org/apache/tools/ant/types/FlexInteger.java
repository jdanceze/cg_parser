package org.apache.tools.ant.types;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/FlexInteger.class */
public class FlexInteger {
    private Integer value;

    public FlexInteger(String value) {
        this.value = Integer.decode(value);
    }

    public int intValue() {
        return this.value.intValue();
    }

    public String toString() {
        return this.value.toString();
    }
}
