package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.LineOrientedOutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/LogOutputStream.class */
public class LogOutputStream extends LineOrientedOutputStream {
    private ProjectComponent pc;
    private int level;

    public LogOutputStream(ProjectComponent pc) {
        this.level = 2;
        this.pc = pc;
    }

    public LogOutputStream(Task task, int level) {
        this((ProjectComponent) task, level);
    }

    public LogOutputStream(ProjectComponent pc, int level) {
        this(pc);
        this.level = level;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.util.LineOrientedOutputStream
    public void processBuffer() {
        try {
            super.processBuffer();
        } catch (IOException e) {
            throw new RuntimeException("Impossible IOException caught: " + e);
        }
    }

    @Override // org.apache.tools.ant.util.LineOrientedOutputStream
    protected void processLine(String line) {
        processLine(line, this.level);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void processLine(String line, int level) {
        this.pc.log(line, level);
    }

    public int getMessageLevel() {
        return this.level;
    }
}
