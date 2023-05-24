package org.apache.tools.ant.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/Watchdog.class */
public class Watchdog implements Runnable {
    public static final String ERROR_INVALID_TIMEOUT = "timeout less than 1.";
    private long timeout;
    private List<TimeoutObserver> observers = Collections.synchronizedList(new ArrayList(1));
    private volatile boolean stopped = false;

    public Watchdog(long timeout) {
        this.timeout = -1L;
        if (timeout < 1) {
            throw new IllegalArgumentException(ERROR_INVALID_TIMEOUT);
        }
        this.timeout = timeout;
    }

    public void addTimeoutObserver(TimeoutObserver to) {
        this.observers.add(to);
    }

    public void removeTimeoutObserver(TimeoutObserver to) {
        this.observers.remove(to);
    }

    protected final void fireTimeoutOccured() {
        this.observers.forEach(o -> {
            o.timeoutOccured(this);
        });
    }

    public synchronized void start() {
        this.stopped = false;
        Thread t = new Thread(this, "WATCHDOG");
        t.setDaemon(true);
        t.start();
    }

    public synchronized void stop() {
        this.stopped = true;
        notifyAll();
    }

    @Override // java.lang.Runnable
    public synchronized void run() {
        long now = System.currentTimeMillis();
        long until = now + this.timeout;
        while (!this.stopped && until > now) {
            try {
                wait(until - now);
                now = System.currentTimeMillis();
            } catch (InterruptedException e) {
            }
        }
        if (!this.stopped) {
            fireTimeoutOccured();
        }
    }
}
