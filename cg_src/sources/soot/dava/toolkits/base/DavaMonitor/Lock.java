package soot.dava.toolkits.base.DavaMonitor;

import java.util.LinkedList;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/DavaMonitor/Lock.class */
class Lock {
    public int level = 0;
    public Thread owner = null;
    private final LinkedList<Thread> q = new LinkedList<>();

    public Thread nextThread() {
        return this.q.getFirst();
    }

    public Thread deQ(Thread t) throws IllegalMonitorStateException {
        if (t != this.q.getFirst()) {
            throw new IllegalMonitorStateException();
        }
        return this.q.removeFirst();
    }

    public void enQ(Thread t) {
        this.q.add(t);
    }
}
