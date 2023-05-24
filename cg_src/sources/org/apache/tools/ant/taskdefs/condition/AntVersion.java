package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.DeweyDecimal;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/AntVersion.class */
public class AntVersion extends Task implements Condition {
    private String atLeast = null;
    private String exactly = null;
    private String propertyname = null;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.propertyname == null) {
            throw new BuildException("'property' must be set.");
        }
        if (this.atLeast != null || this.exactly != null) {
            if (eval()) {
                getProject().setNewProperty(this.propertyname, getVersion().toString());
                return;
            }
            return;
        }
        getProject().setNewProperty(this.propertyname, getVersion().toString());
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        validate();
        DeweyDecimal actual = getVersion();
        if (null != this.atLeast) {
            return actual.isGreaterThanOrEqual(new DeweyDecimal(this.atLeast));
        }
        if (null != this.exactly) {
            return actual.isEqual(new DeweyDecimal(this.exactly));
        }
        return false;
    }

    private void validate() throws BuildException {
        if (this.atLeast != null && this.exactly != null) {
            throw new BuildException("Only one of atleast or exactly may be set.");
        }
        if (null == this.atLeast && null == this.exactly) {
            throw new BuildException("One of atleast or exactly must be set.");
        }
        if (this.atLeast != null) {
            try {
                new DeweyDecimal(this.atLeast);
                return;
            } catch (NumberFormatException e) {
                throw new BuildException("The 'atleast' attribute is not a Dewey Decimal eg 1.1.0 : %s", this.atLeast);
            }
        }
        try {
            new DeweyDecimal(this.exactly);
        } catch (NumberFormatException e2) {
            throw new BuildException("The 'exactly' attribute is not a Dewey Decimal eg 1.1.0 : %s", this.exactly);
        }
    }

    private DeweyDecimal getVersion() {
        char[] charArray;
        Project p = new Project();
        p.init();
        StringBuilder sb = new StringBuilder();
        boolean foundFirstDigit = false;
        for (char versionChar : p.getProperty(MagicNames.ANT_VERSION).toCharArray()) {
            if (Character.isDigit(versionChar)) {
                sb.append(versionChar);
                foundFirstDigit = true;
            }
            if (versionChar == '.' && foundFirstDigit) {
                sb.append(versionChar);
            }
            if (Character.isLetter(versionChar) && foundFirstDigit) {
                break;
            }
        }
        return new DeweyDecimal(sb.toString());
    }

    public String getAtLeast() {
        return this.atLeast;
    }

    public void setAtLeast(String atLeast) {
        this.atLeast = atLeast;
    }

    public String getExactly() {
        return this.exactly;
    }

    public void setExactly(String exactly) {
        this.exactly = exactly;
    }

    public String getProperty() {
        return this.propertyname;
    }

    public void setProperty(String propertyname) {
        this.propertyname = propertyname;
    }
}
