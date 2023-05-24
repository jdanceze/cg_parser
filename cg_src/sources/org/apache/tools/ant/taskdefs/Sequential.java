package org.apache.tools.ant.taskdefs;

import java.util.List;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.property.LocalProperties;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Sequential.class */
public class Sequential extends Task implements TaskContainer {
    private List<Task> nestedTasks = new Vector();

    @Override // org.apache.tools.ant.TaskContainer
    public void addTask(Task nestedTask) {
        this.nestedTasks.add(nestedTask);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        LocalProperties localProperties = LocalProperties.get(getProject());
        localProperties.enterScope();
        try {
            this.nestedTasks.forEach((v0) -> {
                v0.perform();
            });
        } finally {
            localProperties.exitScope();
        }
    }
}
