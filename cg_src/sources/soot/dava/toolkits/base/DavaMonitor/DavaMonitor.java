package soot.dava.toolkits.base.DavaMonitor;

import java.util.HashMap;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/DavaMonitor/DavaMonitor.class */
public class DavaMonitor {
    private static final DavaMonitor instance = new DavaMonitor();
    private final HashMap<Object, Lock> ref = new HashMap<>(1, 0.7f);
    private final HashMap<Object, Lock> lockTable = new HashMap<>(1, 0.7f);

    private DavaMonitor() {
    }

    public static DavaMonitor v() {
        return instance;
    }

    public synchronized void enter(Object o) throws NullPointerException {
        Thread currentThread = Thread.currentThread();
        if (o == null) {
            throw new NullPointerException();
        }
        Lock lock = this.ref.get(o);
        if (lock == null) {
            lock = new Lock();
            this.ref.put(o, lock);
        }
        if (lock.level == 0) {
            lock.level = 1;
            lock.owner = currentThread;
        } else if (lock.owner == currentThread) {
            lock.level++;
        } else {
            this.lockTable.put(currentThread, lock);
            lock.enQ(currentThread);
            while (true) {
                if (lock.level > 0 || lock.nextThread() != currentThread) {
                    try {
                        wait();
                        currentThread = Thread.currentThread();
                        lock = this.lockTable.get(currentThread);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    lock.deQ(currentThread);
                    lock.level = 1;
                    lock.owner = currentThread;
                    return;
                }
            }
        }
    }

    public synchronized void exit(Object o) throws NullPointerException, IllegalMonitorStateException {
        if (o == null) {
            throw new NullPointerException();
        }
        Lock lock = this.ref.get(o);
        if (lock == null || lock.level == 0 || lock.owner != Thread.currentThread()) {
            throw new IllegalMonitorStateException();
        }
        lock.level--;
        if (lock.level == 0) {
            notifyAll();
        }
    }
}
