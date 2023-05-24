package org.apache.tools.ant.taskdefs.optional.testing;

import org.apache.tools.ant.taskdefs.WaitFor;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/testing/BlockFor.class */
public class BlockFor extends WaitFor {
    private String text;

    public BlockFor() {
        super("blockfor");
        this.text = getTaskName() + " timed out";
    }

    public BlockFor(String taskName) {
        super(taskName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.WaitFor
    public void processTimeout() throws BuildTimeoutException {
        super.processTimeout();
        throw new BuildTimeoutException(this.text, getLocation());
    }

    public void addText(String message) {
        this.text = getProject().replaceProperties(message);
    }
}
