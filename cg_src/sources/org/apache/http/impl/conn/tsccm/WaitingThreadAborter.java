package org.apache.http.impl.conn.tsccm;

import org.apache.http.annotation.NotThreadSafe;
@NotThreadSafe
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/conn/tsccm/WaitingThreadAborter.class */
public class WaitingThreadAborter {
    private WaitingThread waitingThread;
    private boolean aborted;

    public void abort() {
        this.aborted = true;
        if (this.waitingThread != null) {
            this.waitingThread.interrupt();
        }
    }

    public void setWaitingThread(WaitingThread waitingThread) {
        this.waitingThread = waitingThread;
        if (this.aborted) {
            waitingThread.interrupt();
        }
    }
}
