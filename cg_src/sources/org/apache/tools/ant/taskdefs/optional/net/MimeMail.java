package org.apache.tools.ant.taskdefs.optional.net;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.email.EmailTask;
@Deprecated
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/net/MimeMail.class */
public class MimeMail extends EmailTask {
    @Override // org.apache.tools.ant.taskdefs.email.EmailTask, org.apache.tools.ant.Task
    public void execute() throws BuildException {
        log("DEPRECATED - The " + getTaskName() + " task is deprecated. Use the mail task instead.");
        super.execute();
    }
}
