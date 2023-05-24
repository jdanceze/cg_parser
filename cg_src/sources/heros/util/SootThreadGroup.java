package heros.util;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/util/SootThreadGroup.class */
public class SootThreadGroup extends ThreadGroup {
    private final Thread startThread;

    public SootThreadGroup() {
        super("Soot Threadgroup");
        if (Thread.currentThread().getThreadGroup() instanceof SootThreadGroup) {
            SootThreadGroup group = (SootThreadGroup) Thread.currentThread().getThreadGroup();
            this.startThread = group.getStarterThread();
            return;
        }
        this.startThread = Thread.currentThread();
    }

    public Thread getStarterThread() {
        return this.startThread;
    }
}
