package org.apache.tools.ant.util;

import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/TaskLogger.class */
public final class TaskLogger {
    private Task task;

    public TaskLogger(Task task) {
        this.task = task;
    }

    public void info(String message) {
        this.task.log(message, 2);
    }

    public void error(String message) {
        this.task.log(message, 0);
    }

    public void warning(String message) {
        this.task.log(message, 1);
    }

    public void verbose(String message) {
        this.task.log(message, 3);
    }

    public void debug(String message) {
        this.task.log(message, 4);
    }
}
