package org.apache.tools.ant.taskdefs.optional.unix;

import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/unix/Chown.class */
public class Chown extends AbstractAccessTask {
    private boolean haveOwner = false;

    public Chown() {
        super.setExecutable("chown");
    }

    public void setOwner(String owner) {
        createArg().setValue(owner);
        this.haveOwner = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.ExecuteOn, org.apache.tools.ant.taskdefs.ExecTask
    public void checkConfiguration() {
        if (!this.haveOwner) {
            throw new BuildException("Required attribute owner not set in chown", getLocation());
        }
        super.checkConfiguration();
    }

    @Override // org.apache.tools.ant.taskdefs.ExecTask
    public void setExecutable(String e) {
        throw new BuildException(getTaskType() + " doesn't support the executable attribute", getLocation());
    }
}
