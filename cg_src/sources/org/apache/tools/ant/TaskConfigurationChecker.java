package org.apache.tools.ant;

import java.util.ArrayList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/TaskConfigurationChecker.class */
public class TaskConfigurationChecker {
    private List<String> errors = new ArrayList();
    private final Task task;

    public TaskConfigurationChecker(Task task) {
        this.task = task;
    }

    public void assertConfig(boolean condition, String errormessage) {
        if (!condition) {
            this.errors.add(errormessage);
        }
    }

    public void fail(String errormessage) {
        this.errors.add(errormessage);
    }

    public void checkErrors() throws BuildException {
        if (!this.errors.isEmpty()) {
            StringBuilder sb = new StringBuilder(String.format("Configuration error on <%s>:%n", this.task.getTaskName()));
            for (String msg : this.errors) {
                sb.append(String.format("- %s%n", msg));
            }
            throw new BuildException(sb.toString(), this.task.getLocation());
        }
    }
}
