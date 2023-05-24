package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/Contains.class */
public class Contains implements Condition {
    private String string;
    private String subString;
    private boolean caseSensitive = true;

    public void setString(String string) {
        this.string = string;
    }

    public void setSubstring(String subString) {
        this.subString = subString;
    }

    public void setCasesensitive(boolean b) {
        this.caseSensitive = b;
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        if (this.string == null || this.subString == null) {
            throw new BuildException("both string and substring are required in contains");
        }
        if (this.caseSensitive) {
            return this.string.contains(this.subString);
        }
        return this.string.toLowerCase().contains(this.subString.toLowerCase());
    }
}
