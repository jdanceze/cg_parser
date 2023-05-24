package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.tools.ant.Task;
@Deprecated
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/TaskOutputStream.class */
public class TaskOutputStream extends OutputStream {
    private Task task;
    private StringBuffer line;
    private int msgOutputLevel;

    TaskOutputStream(Task task, int msgOutputLevel) {
        System.err.println("As of Ant 1.2 released in October 2000, the TaskOutputStream class");
        System.err.println("is considered to be dead code by the Ant developers and is unmaintained.");
        System.err.println("Don't use it!");
        this.task = task;
        this.msgOutputLevel = msgOutputLevel;
        this.line = new StringBuffer();
    }

    @Override // java.io.OutputStream
    public void write(int c) throws IOException {
        char cc = (char) c;
        if (cc == '\r' || cc == '\n') {
            if (this.line.length() > 0) {
                processLine();
                return;
            }
            return;
        }
        this.line.append(cc);
    }

    private void processLine() {
        String s = this.line.toString();
        this.task.log(s, this.msgOutputLevel);
        this.line = new StringBuffer();
    }
}
