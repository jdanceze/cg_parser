package org.apache.tools.ant.taskdefs.condition;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.ReflectWrapper;
import org.apache.tools.ant.util.StringUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/HasFreeSpace.class */
public class HasFreeSpace implements Condition {
    private String partition;
    private String needed;

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        validate();
        try {
            if (JavaEnvUtils.isAtLeastJavaVersion(JavaEnvUtils.JAVA_1_6)) {
                File fs = new File(this.partition);
                ReflectWrapper w = new ReflectWrapper(fs);
                long free = ((Long) w.invoke("getFreeSpace")).longValue();
                return free >= StringUtils.parseHumanSizes(this.needed);
            }
            throw new BuildException("HasFreeSpace condition not supported on Java5 or less.");
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    private void validate() throws BuildException {
        if (null == this.partition) {
            throw new BuildException("Please set the partition attribute.");
        }
        if (null == this.needed) {
            throw new BuildException("Please set the needed attribute.");
        }
    }

    public String getPartition() {
        return this.partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public String getNeeded() {
        return this.needed;
    }

    public void setNeeded(String needed) {
        this.needed = needed;
    }
}
