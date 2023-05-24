package soot;

import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/Timer.class */
public class Timer {
    private long duration;
    private long startTime;
    private boolean hasStarted;
    private String name;

    public Timer(String name) {
        this.name = name;
        this.duration = 0L;
    }

    public Timer() {
        this("unnamed");
    }

    static void doGarbageCollecting() {
        G g = G.v();
        if (g.Timer_isGarbageCollecting || !Options.v().subtract_gc()) {
            return;
        }
        int i = g.Timer_count;
        g.Timer_count = i + 1;
        if (i % 4 != 0) {
            return;
        }
        g.Timer_isGarbageCollecting = true;
        g.Timer_forcedGarbageCollectionTimer.start();
        for (Timer t : g.Timer_outstandingTimers) {
            t.end();
        }
        System.gc();
        for (Timer t2 : g.Timer_outstandingTimers) {
            t2.start();
        }
        g.Timer_forcedGarbageCollectionTimer.end();
        g.Timer_isGarbageCollecting = false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v7, types: [java.util.List<soot.Timer>] */
    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.Throwable] */
    public void start() {
        doGarbageCollecting();
        this.startTime = System.nanoTime();
        if (this.hasStarted) {
            throw new RuntimeException("timer " + this.name + " has already been started!");
        }
        this.hasStarted = true;
        if (!G.v().Timer_isGarbageCollecting) {
            ?? r0 = G.v().Timer_outstandingTimers;
            synchronized (r0) {
                G.v().Timer_outstandingTimers.add(this);
                r0 = r0;
            }
        }
    }

    public String toString() {
        return this.name;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v7, types: [java.util.List<soot.Timer>] */
    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.Throwable] */
    public void end() {
        if (!this.hasStarted) {
            throw new RuntimeException("timer " + this.name + " has not been started!");
        }
        this.hasStarted = false;
        this.duration += System.nanoTime() - this.startTime;
        if (!G.v().Timer_isGarbageCollecting) {
            ?? r0 = G.v().Timer_outstandingTimers;
            synchronized (r0) {
                G.v().Timer_outstandingTimers.remove(this);
                r0 = r0;
            }
        }
    }

    public long getTime() {
        return this.duration / 1000000;
    }
}
