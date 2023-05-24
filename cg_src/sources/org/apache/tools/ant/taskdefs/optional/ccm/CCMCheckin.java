package org.apache.tools.ant.taskdefs.optional.ccm;

import java.util.Date;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ccm/CCMCheckin.class */
public class CCMCheckin extends CCMCheck {
    public CCMCheckin() {
        setCcmAction(Continuus.COMMAND_CHECKIN);
        setComment("Checkin " + new Date());
    }
}
