package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.taskdefs.Execute;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/IsFailure.class */
public class IsFailure implements Condition {
    private int code;

    public void setCode(int c) {
        this.code = c;
    }

    public int getCode() {
        return this.code;
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() {
        return Execute.isFailure(this.code);
    }
}
