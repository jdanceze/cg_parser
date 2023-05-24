package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskAdapter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Taskdef.class */
public class Taskdef extends Typedef {
    public Taskdef() {
        setAdapterClass(TaskAdapter.class);
        setAdaptToClass(Task.class);
    }
}
