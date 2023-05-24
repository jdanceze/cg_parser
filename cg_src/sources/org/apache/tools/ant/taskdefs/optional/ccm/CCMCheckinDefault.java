package org.apache.tools.ant.taskdefs.optional.ccm;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ccm/CCMCheckinDefault.class */
public class CCMCheckinDefault extends CCMCheck {
    public static final String DEFAULT_TASK = "default";

    public CCMCheckinDefault() {
        setCcmAction(Continuus.COMMAND_CHECKIN);
        setTask("default");
    }
}
