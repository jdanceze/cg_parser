package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.taskdefs.email.EmailTask;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/SendEmail.class */
public class SendEmail extends EmailTask {
    @Deprecated
    public void setMailport(Integer value) {
        setMailport(value.intValue());
    }
}
