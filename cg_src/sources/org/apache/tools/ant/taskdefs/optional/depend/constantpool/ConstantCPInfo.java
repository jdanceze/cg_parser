package org.apache.tools.ant.taskdefs.optional.depend.constantpool;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/constantpool/ConstantCPInfo.class */
public abstract class ConstantCPInfo extends ConstantPoolEntry {
    private Object value;

    /* JADX INFO: Access modifiers changed from: protected */
    public ConstantCPInfo(int tagValue, int entries) {
        super(tagValue, entries);
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object newValue) {
        this.value = newValue;
    }
}
