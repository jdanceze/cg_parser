package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.StreamUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/Xor.class */
public class Xor extends ConditionBase implements Condition {
    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        return ((Boolean) StreamUtils.enumerationAsStream(getConditions()).map((v0) -> {
            return v0.eval();
        }).reduce(a, b -> {
            return Boolean.valueOf(a.booleanValue() ^ b.booleanValue());
        }).orElse(Boolean.FALSE)).booleanValue();
    }
}
