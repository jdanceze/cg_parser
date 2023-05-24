package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Retry.class */
public class Retry extends Task implements TaskContainer {
    private Task nestedTask;
    private int retryCount = 1;
    private int retryDelay = 0;

    @Override // org.apache.tools.ant.TaskContainer
    public synchronized void addTask(Task t) {
        if (this.nestedTask != null) {
            throw new BuildException("The retry task container accepts a single nested task (which may be a sequential task container)");
        }
        this.nestedTask = t;
    }

    public void setRetryCount(int n) {
        this.retryCount = n;
    }

    public void setRetryDelay(int retryDelay) {
        if (retryDelay < 0) {
            throw new BuildException("retryDelay must be a non-negative number");
        }
        this.retryDelay = retryDelay;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        String msg;
        StringBuilder errorMessages = new StringBuilder();
        for (int i = 0; i <= this.retryCount; i++) {
            try {
                this.nestedTask.perform();
                return;
            } catch (Exception e) {
                errorMessages.append(e.getMessage());
                if (i >= this.retryCount) {
                    throw new BuildException(String.format("Task [%s] failed after [%d] attempts; giving up.%nError messages:%n%s", this.nestedTask.getTaskName(), Integer.valueOf(this.retryCount), errorMessages), getLocation());
                }
                if (this.retryDelay > 0) {
                    msg = "Attempt [" + i + "]: error occurred; retrying after " + this.retryDelay + " ms...";
                } else {
                    msg = "Attempt [" + i + "]: error occurred; retrying...";
                }
                log(msg, e, 2);
                errorMessages.append(System.lineSeparator());
                if (this.retryDelay > 0) {
                    try {
                        Thread.sleep(this.retryDelay);
                    } catch (InterruptedException e2) {
                    }
                }
            }
        }
    }
}
