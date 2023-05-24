package soot.jimple.infoflow.solver.executors;

import heros.solver.CountingThreadPoolExecutor;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.infoflow.collect.BlackHoleCollection;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/executors/InterruptableExecutor.class */
public class InterruptableExecutor extends CountingThreadPoolExecutor {
    protected static final Logger logger = LoggerFactory.getLogger(InterruptableExecutor.class);
    private boolean interrupted;
    private boolean terminated;

    public InterruptableExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.interrupted = false;
        this.terminated = false;
    }

    public void interrupt() {
        this.interrupted = true;
        getQueue().clear();
        getQueue().drainTo(new BlackHoleCollection());
        purge();
        this.numRunningTasks.resetAndInterrupt();
    }

    public void reset() {
        this.terminated = false;
        this.interrupted = false;
    }

    @Override // heros.solver.CountingThreadPoolExecutor, java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.Executor
    public void execute(Runnable command) {
        if (this.terminated) {
            logger.warn("Executor has terminated. Call reset() before submitting new tasks.");
            return;
        }
        try {
            if (!this.interrupted) {
                super.execute(command);
            }
        } catch (RejectedExecutionException e) {
            this.interrupted = true;
        }
    }

    @Override // heros.solver.CountingThreadPoolExecutor
    public void awaitCompletion() throws InterruptedException {
        if (this.terminated) {
            return;
        }
        super.awaitCompletion();
        this.terminated = true;
    }

    @Override // heros.solver.CountingThreadPoolExecutor
    public void awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException {
        if (this.terminated) {
            return;
        }
        super.awaitCompletion(timeout, unit);
        this.terminated = true;
    }

    public boolean isFinished() {
        return this.terminated || this.numRunningTasks.isAtZero();
    }

    @Override // java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return this.terminated || super.isTerminated();
    }
}
