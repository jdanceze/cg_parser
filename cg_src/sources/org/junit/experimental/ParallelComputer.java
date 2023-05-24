package org.junit.experimental;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.resource.spi.work.WorkManager;
import org.junit.runner.Computer;
import org.junit.runner.Runner;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.RunnerScheduler;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/ParallelComputer.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/ParallelComputer.class */
public class ParallelComputer extends Computer {
    private final boolean classes;
    private final boolean methods;

    public ParallelComputer(boolean classes, boolean methods) {
        this.classes = classes;
        this.methods = methods;
    }

    public static Computer classes() {
        return new ParallelComputer(true, false);
    }

    public static Computer methods() {
        return new ParallelComputer(false, true);
    }

    private static Runner parallelize(Runner runner) {
        if (runner instanceof ParentRunner) {
            ((ParentRunner) runner).setScheduler(new RunnerScheduler() { // from class: org.junit.experimental.ParallelComputer.1
                private final ExecutorService fService = Executors.newCachedThreadPool();

                @Override // org.junit.runners.model.RunnerScheduler
                public void schedule(Runnable childStatement) {
                    this.fService.submit(childStatement);
                }

                @Override // org.junit.runners.model.RunnerScheduler
                public void finished() {
                    try {
                        this.fService.shutdown();
                        this.fService.awaitTermination(WorkManager.INDEFINITE, TimeUnit.NANOSECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                    }
                }
            });
        }
        return runner;
    }

    @Override // org.junit.runner.Computer
    public Runner getSuite(RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
        Runner suite = super.getSuite(builder, classes);
        return this.classes ? parallelize(suite) : suite;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.junit.runner.Computer
    public Runner getRunner(RunnerBuilder builder, Class<?> testClass) throws Throwable {
        Runner runner = super.getRunner(builder, testClass);
        return this.methods ? parallelize(runner) : runner;
    }
}
