package soot.jimple.infoflow.solver.executors;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/executors/SetPoolExecutor.class */
public class SetPoolExecutor extends InterruptableExecutor {
    protected Set<Runnable> waiting;

    public SetPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.waiting = Collections.newSetFromMap(new ConcurrentHashMap());
    }

    @Override // soot.jimple.infoflow.solver.executors.InterruptableExecutor, heros.solver.CountingThreadPoolExecutor, java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.Executor
    public void execute(Runnable command) {
        if (this.waiting.add(command)) {
            super.execute(command);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // heros.solver.CountingThreadPoolExecutor, java.util.concurrent.ThreadPoolExecutor
    public void afterExecute(Runnable r, Throwable t) {
        this.waiting.remove(r);
        super.afterExecute(r, t);
    }

    @Override // soot.jimple.infoflow.solver.executors.InterruptableExecutor
    public void interrupt() {
        super.interrupt();
        this.waiting.clear();
    }

    @Override // java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.ExecutorService
    public void shutdown() {
        super.shutdown();
        this.waiting.clear();
    }

    @Override // java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.ExecutorService
    public List<Runnable> shutdownNow() {
        List<Runnable> tasks = super.shutdownNow();
        this.waiting.clear();
        return tasks;
    }
}
