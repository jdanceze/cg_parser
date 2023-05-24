package heros.solver;

import heros.util.SootThreadGroup;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/CountingThreadPoolExecutor.class */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
    protected static final Logger logger = LoggerFactory.getLogger(CountingThreadPoolExecutor.class);
    protected final CountLatch numRunningTasks;
    protected volatile Throwable exception;

    public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadFactory() { // from class: heros.solver.CountingThreadPoolExecutor.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable r) {
                return new Thread(new SootThreadGroup(), r);
            }
        });
        this.numRunningTasks = new CountLatch(0);
        this.exception = null;
    }

    @Override // java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.Executor
    public void execute(Runnable command) {
        try {
            this.numRunningTasks.increment();
            super.execute(command);
        } catch (RejectedExecutionException ex) {
            this.numRunningTasks.decrement();
            throw ex;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.util.concurrent.ThreadPoolExecutor
    public void afterExecute(Runnable r, Throwable t) {
        if (t != null) {
            this.exception = t;
            logger.error("Worker thread execution failed: " + t.getMessage(), t);
            shutdownNow();
            this.numRunningTasks.resetAndInterrupt();
        } else {
            this.numRunningTasks.decrement();
        }
        super.afterExecute(r, t);
    }

    public void awaitCompletion() throws InterruptedException {
        this.numRunningTasks.awaitZero();
    }

    public void awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException {
        this.numRunningTasks.awaitZero(timeout, unit);
    }

    public Throwable getException() {
        return this.exception;
    }
}
