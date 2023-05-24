package org.apache.tools.ant.util;

import java.io.IOException;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/RetryHandler.class */
public class RetryHandler {
    private int retriesAllowed;
    private Task task;

    public RetryHandler(int retriesAllowed, Task task) {
        this.retriesAllowed = 0;
        this.retriesAllowed = retriesAllowed;
        this.task = task;
    }

    public void execute(Retryable exe, String desc) throws IOException {
        int retries = 0;
        while (true) {
            try {
                exe.execute();
                return;
            } catch (IOException e) {
                retries++;
                if (retries > this.retriesAllowed && this.retriesAllowed > -1) {
                    this.task.log("try #" + retries + ": IO error (" + desc + "), number of maximum retries reached (" + this.retriesAllowed + "), giving up", 1);
                    throw e;
                }
                this.task.log("try #" + retries + ": IO error (" + desc + "), retrying", 1);
            }
        }
    }
}
