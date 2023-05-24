package org.apache.http.impl.conn.tsccm;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
@Deprecated
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/conn/tsccm/RefQueueWorker.class */
public class RefQueueWorker implements Runnable {
    protected final ReferenceQueue<?> refQueue;
    protected final RefQueueHandler refHandler;
    protected volatile Thread workerThread;

    public RefQueueWorker(ReferenceQueue<?> queue, RefQueueHandler handler) {
        if (queue == null) {
            throw new IllegalArgumentException("Queue must not be null.");
        }
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null.");
        }
        this.refQueue = queue;
        this.refHandler = handler;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.workerThread == null) {
            this.workerThread = Thread.currentThread();
        }
        while (this.workerThread == Thread.currentThread()) {
            try {
                Reference<?> ref = this.refQueue.remove();
                this.refHandler.handleReference(ref);
            } catch (InterruptedException e) {
            }
        }
    }

    public void shutdown() {
        Thread wt = this.workerThread;
        if (wt != null) {
            this.workerThread = null;
            wt.interrupt();
        }
    }

    public String toString() {
        return "RefQueueWorker::" + this.workerThread;
    }
}
