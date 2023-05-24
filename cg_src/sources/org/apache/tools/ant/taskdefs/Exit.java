package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ExitStatusException;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.taskdefs.condition.ConditionBase;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Exit.class */
public class Exit extends Task {
    private String message;
    private Object ifCondition;
    private Object unlessCondition;
    private NestedCondition nestedCondition;
    private Integer status;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Exit$NestedCondition.class */
    public static class NestedCondition extends ConditionBase implements Condition {
        private NestedCondition() {
        }

        @Override // org.apache.tools.ant.taskdefs.condition.Condition
        public boolean eval() {
            if (countConditions() != 1) {
                throw new BuildException("A single nested condition is required.");
            }
            return getConditions().nextElement().eval();
        }
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public void setIf(Object c) {
        this.ifCondition = c;
    }

    public void setIf(String c) {
        setIf((Object) c);
    }

    public void setUnless(Object c) {
        this.unlessCondition = c;
    }

    public void setUnless(String c) {
        setUnless((Object) c);
    }

    public void setStatus(int i) {
        this.status = Integer.valueOf(i);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        boolean z;
        String text;
        if (nestedConditionPresent()) {
            z = testNestedCondition();
        } else {
            z = testIfCondition() && testUnlessCondition();
        }
        boolean fail = z;
        if (fail) {
            String text2 = null;
            if (this.message != null && !this.message.trim().isEmpty()) {
                text2 = this.message.trim();
            } else {
                if (!isNullOrEmpty(this.ifCondition) && testIfCondition()) {
                    text2 = "if=" + this.ifCondition;
                }
                if (!isNullOrEmpty(this.unlessCondition) && testUnlessCondition()) {
                    if (text2 == null) {
                        text = "";
                    } else {
                        text = text2 + " and ";
                    }
                    text2 = text + "unless=" + this.unlessCondition;
                }
                if (nestedConditionPresent()) {
                    text2 = "condition satisfied";
                } else if (text2 == null) {
                    text2 = "No message";
                }
            }
            log("failing due to " + text2, 4);
            if (this.status != null) {
                throw new ExitStatusException(text2, this.status.intValue());
            }
        }
    }

    private boolean isNullOrEmpty(Object value) {
        return value == null || "".equals(value);
    }

    public void addText(String msg) {
        if (this.message == null) {
            this.message = "";
        }
        this.message += getProject().replaceProperties(msg);
    }

    public ConditionBase createCondition() {
        if (this.nestedCondition != null) {
            throw new BuildException("Only one nested condition is allowed.");
        }
        this.nestedCondition = new NestedCondition();
        return this.nestedCondition;
    }

    private boolean testIfCondition() {
        return PropertyHelper.getPropertyHelper(getProject()).testIfCondition(this.ifCondition);
    }

    private boolean testUnlessCondition() {
        return PropertyHelper.getPropertyHelper(getProject()).testUnlessCondition(this.unlessCondition);
    }

    private boolean testNestedCondition() {
        boolean result = nestedConditionPresent();
        if ((!result || this.ifCondition == null) && this.unlessCondition == null) {
            return result && this.nestedCondition.eval();
        }
        throw new BuildException("Nested conditions not permitted in conjunction with if/unless attributes");
    }

    private boolean nestedConditionPresent() {
        return this.nestedCondition != null;
    }
}
