package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Nice.class */
public class Nice extends Task {
    private Integer newPriority;
    private String currentPriority;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Thread self = Thread.currentThread();
        int priority = self.getPriority();
        if (this.currentPriority != null) {
            String current = Integer.toString(priority);
            getProject().setNewProperty(this.currentPriority, current);
        }
        if (this.newPriority != null && priority != this.newPriority.intValue()) {
            try {
                self.setPriority(this.newPriority.intValue());
            } catch (IllegalArgumentException iae) {
                throw new BuildException("Priority out of range", iae);
            } catch (SecurityException e) {
                log("Unable to set new priority -a security manager is in the way", 1);
            }
        }
    }

    public void setCurrentPriority(String currentPriority) {
        this.currentPriority = currentPriority;
    }

    public void setNewPriority(int newPriority) {
        if (newPriority < 1 || newPriority > 10) {
            throw new BuildException("The thread priority is out of the range 1-10");
        }
        this.newPriority = Integer.valueOf(newPriority);
    }
}
