package soot.jimple.infoflow.solver.fastSolver;

import java.util.ArrayDeque;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/fastSolver/LocalWorklistTask.class */
public abstract class LocalWorklistTask implements Runnable {
    private ArrayDeque<Runnable> localTaskList = new ArrayDeque<>();
    private static final ThreadLocal<LocalWorklistTask> TASKS = new ThreadLocal<>();

    public abstract void runInternal();

    @Override // java.lang.Runnable
    public final void run() {
        try {
            ArrayDeque<Runnable> list = this.localTaskList;
            list.add(this);
            TASKS.set(this);
            while (true) {
                Runnable d = list.poll();
                if (d != null) {
                    if (d instanceof LocalWorklistTask) {
                        LocalWorklistTask l = (LocalWorklistTask) d;
                        l.runInternal();
                    } else {
                        d.run();
                    }
                } else {
                    return;
                }
            }
        } finally {
            TASKS.remove();
        }
    }

    public static void scheduleLocal(Runnable task) {
        LocalWorklistTask t = TASKS.get();
        if (t != null) {
            t.localTaskList.add(task);
        }
    }
}
