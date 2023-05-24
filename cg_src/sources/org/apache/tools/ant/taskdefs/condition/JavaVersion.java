package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.DeweyDecimal;
import org.apache.tools.ant.util.JavaEnvUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/JavaVersion.class */
public class JavaVersion implements Condition {
    private String atMost = null;
    private String atLeast = null;
    private String exactly = null;

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        validate();
        DeweyDecimal actual = JavaEnvUtils.getParsedJavaVersion();
        if (null != this.atLeast) {
            return actual.isGreaterThanOrEqual(new DeweyDecimal(this.atLeast));
        }
        if (null != this.exactly) {
            return actual.isEqual(new DeweyDecimal(this.exactly));
        }
        if (this.atMost != null) {
            return actual.isLessThanOrEqual(new DeweyDecimal(this.atMost));
        }
        return false;
    }

    private void validate() throws BuildException {
        if (this.atLeast != null && this.exactly != null && this.atMost != null) {
            throw new BuildException("Only one of atleast or atmost or exactly may be set.");
        }
        if (null == this.atLeast && null == this.exactly && this.atMost == null) {
            throw new BuildException("One of atleast or atmost or exactly must be set.");
        }
        if (this.atLeast != null) {
            try {
                new DeweyDecimal(this.atLeast);
            } catch (NumberFormatException e) {
                throw new BuildException("The 'atleast' attribute is not a Dewey Decimal eg 1.1.0 : " + this.atLeast);
            }
        } else if (this.atMost != null) {
            try {
                new DeweyDecimal(this.atMost);
            } catch (NumberFormatException e2) {
                throw new BuildException("The 'atmost' attribute is not a Dewey Decimal eg 1.1.0 : " + this.atMost);
            }
        } else {
            try {
                new DeweyDecimal(this.exactly);
            } catch (NumberFormatException e3) {
                throw new BuildException("The 'exactly' attribute is not a Dewey Decimal eg 1.1.0 : " + this.exactly);
            }
        }
    }

    public String getAtLeast() {
        return this.atLeast;
    }

    public void setAtLeast(String atLeast) {
        this.atLeast = atLeast;
    }

    public String getAtMost() {
        return this.atMost;
    }

    public void setAtMost(String atMost) {
        this.atMost = atMost;
    }

    public String getExactly() {
        return this.exactly;
    }

    public void setExactly(String exactly) {
        this.exactly = exactly;
    }
}
