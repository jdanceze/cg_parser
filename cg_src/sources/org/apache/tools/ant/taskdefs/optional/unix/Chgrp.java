package org.apache.tools.ant.taskdefs.optional.unix;

import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/unix/Chgrp.class */
public class Chgrp extends AbstractAccessTask {
    private boolean haveGroup = false;

    public Chgrp() {
        super.setExecutable("chgrp");
    }

    public void setGroup(String group) {
        createArg().setValue(group);
        this.haveGroup = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.ExecuteOn, org.apache.tools.ant.taskdefs.ExecTask
    public void checkConfiguration() {
        if (!this.haveGroup) {
            throw new BuildException("Required attribute group not set in chgrp", getLocation());
        }
        super.checkConfiguration();
    }

    @Override // org.apache.tools.ant.taskdefs.ExecTask
    public void setExecutable(String e) {
        throw new BuildException(getTaskType() + " doesn't support the executable attribute", getLocation());
    }
}
