package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/BaseSelector.class */
public abstract class BaseSelector extends DataType implements FileSelector {
    private String errmsg = null;
    private Throwable cause;

    public abstract boolean isSelected(File file, String str, File file2);

    public void setError(String msg) {
        if (this.errmsg == null) {
            this.errmsg = msg;
        }
    }

    public void setError(String msg, Throwable cause) {
        if (this.errmsg == null) {
            this.errmsg = msg;
            this.cause = cause;
        }
    }

    public String getError() {
        return this.errmsg;
    }

    public void verifySettings() {
        if (isReference()) {
            getRef().verifySettings();
        }
    }

    public void validate() {
        if (getError() == null) {
            verifySettings();
        }
        if (getError() != null) {
            throw new BuildException(this.errmsg, this.cause);
        }
        if (!isReference()) {
            dieOnCircularReference();
        }
    }

    private BaseSelector getRef() {
        return (BaseSelector) getCheckedRef(BaseSelector.class);
    }
}
