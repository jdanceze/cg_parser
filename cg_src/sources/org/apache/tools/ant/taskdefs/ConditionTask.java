package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.taskdefs.condition.ConditionBase;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/ConditionTask.class */
public class ConditionTask extends ConditionBase {
    private String property;
    private Object value;
    private Object alternative;

    public ConditionTask() {
        super("condition");
        this.property = null;
        this.value = "true";
        this.alternative = null;
    }

    public void setProperty(String p) {
        this.property = p;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setValue(String v) {
        setValue((Object) v);
    }

    public void setElse(Object alt) {
        this.alternative = alt;
    }

    public void setElse(String e) {
        setElse((Object) e);
    }

    public void execute() throws BuildException {
        if (countConditions() > 1) {
            throw new BuildException("You must not nest more than one condition into <%s>", getTaskName());
        }
        if (countConditions() < 1) {
            throw new BuildException("You must nest a condition into <%s>", getTaskName());
        }
        if (this.property == null) {
            throw new BuildException("The property attribute is required.");
        }
        Condition c = getConditions().nextElement();
        if (c.eval()) {
            log("Condition true; setting " + this.property + " to " + this.value, 4);
            PropertyHelper.getPropertyHelper(getProject()).setNewProperty(this.property, this.value);
        } else if (this.alternative != null) {
            log("Condition false; setting " + this.property + " to " + this.alternative, 4);
            PropertyHelper.getPropertyHelper(getProject()).setNewProperty(this.property, this.alternative);
        } else {
            log("Condition false; not setting " + this.property, 4);
        }
    }
}
