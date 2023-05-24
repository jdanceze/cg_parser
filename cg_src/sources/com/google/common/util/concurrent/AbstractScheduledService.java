package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.Service;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.GuardedBy;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tools.ant.taskdefs.optional.clearcase.ClearCase;
import org.checkerframework.checker.nullness.compatqual.MonotonicNonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import soot.coffi.Instruction;
@Beta
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractScheduledService.class */
public abstract class AbstractScheduledService implements Service {
    private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
    private final AbstractService delegate = new ServiceDelegate();

    protected abstract void runOneIteration() throws Exception;

    protected abstract Scheduler scheduler();

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractScheduledService$Scheduler.class */
    public static abstract class Scheduler {
        abstract Future<?> schedule(AbstractService abstractService, ScheduledExecutorService scheduledExecutorService, Runnable runnable);

        public static Scheduler newFixedDelaySchedule(final long initialDelay, final long delay, final TimeUnit unit) {
            Preconditions.checkNotNull(unit);
            Preconditions.checkArgument(delay > 0, "delay must be > 0, found %s", delay);
            return new Scheduler() { // from class: com.google.common.util.concurrent.AbstractScheduledService.Scheduler.1
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // com.google.common.util.concurrent.AbstractScheduledService.Scheduler
                public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task) {
                    return executor.scheduleWithFixedDelay(task, initialDelay, delay, unit);
                }
            };
        }

        public static Scheduler newFixedRateSchedule(final long initialDelay, final long period, final TimeUnit unit) {
            Preconditions.checkNotNull(unit);
            Preconditions.checkArgument(period > 0, "period must be > 0, found %s", period);
            return new Scheduler() { // from class: com.google.common.util.concurrent.AbstractScheduledService.Scheduler.2
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // com.google.common.util.concurrent.AbstractScheduledService.Scheduler
                public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task) {
                    return executor.scheduleAtFixedRate(task, initialDelay, period, unit);
                }
            };
        }

        private Scheduler() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractScheduledService$ServiceDelegate.class */
    public final class ServiceDelegate extends AbstractService {
        @MonotonicNonNullDecl
        private volatile Future<?> runningTask;
        @MonotonicNonNullDecl
        private volatile ScheduledExecutorService executorService;
        private final ReentrantLock lock;
        private final Runnable task;

        private ServiceDelegate() {
            this.lock = new ReentrantLock();
            this.task = new Task();
        }

        /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractScheduledService$ServiceDelegate$Task.class */
        class Task implements Runnable {
            Task() {
            }

            @Override // java.lang.Runnable
            public void run() {
                ServiceDelegate.this.lock.lock();
                try {
                    if (ServiceDelegate.this.runningTask.isCancelled()) {
                        return;
                    }
                    AbstractScheduledService.this.runOneIteration();
                } catch (Throwable t) {
                    try {
                        try {
                            AbstractScheduledService.this.shutDown();
                        } catch (Exception ignored) {
                            AbstractScheduledService.logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", (Throwable) ignored);
                        }
                        ServiceDelegate.this.notifyFailed(t);
                        ServiceDelegate.this.runningTask.cancel(false);
                    } finally {
                        ServiceDelegate.this.lock.unlock();
                    }
                }
            }
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected final void doStart() {
            this.executorService = MoreExecutors.renamingDecorator(AbstractScheduledService.this.executor(), new Supplier<String>() { // from class: com.google.common.util.concurrent.AbstractScheduledService.ServiceDelegate.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.google.common.base.Supplier
                public String get() {
                    return AbstractScheduledService.this.serviceName() + Instruction.argsep + ServiceDelegate.this.state();
                }
            });
            this.executorService.execute(new Runnable() { // from class: com.google.common.util.concurrent.AbstractScheduledService.ServiceDelegate.2
                @Override // java.lang.Runnable
                public void run() {
                    ServiceDelegate.this.lock.lock();
                    try {
                        AbstractScheduledService.this.startUp();
                        ServiceDelegate.this.runningTask = AbstractScheduledService.this.scheduler().schedule(AbstractScheduledService.this.delegate, ServiceDelegate.this.executorService, ServiceDelegate.this.task);
                        ServiceDelegate.this.notifyStarted();
                    } catch (Throwable t) {
                        try {
                            ServiceDelegate.this.notifyFailed(t);
                            if (ServiceDelegate.this.runningTask != null) {
                                ServiceDelegate.this.runningTask.cancel(false);
                            }
                        } finally {
                            ServiceDelegate.this.lock.unlock();
                        }
                    }
                }
            });
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected final void doStop() {
            this.runningTask.cancel(false);
            this.executorService.execute(new Runnable() { // from class: com.google.common.util.concurrent.AbstractScheduledService.ServiceDelegate.3
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ServiceDelegate.this.lock.lock();
                        if (ServiceDelegate.this.state() != Service.State.STOPPING) {
                            ServiceDelegate.this.lock.unlock();
                            return;
                        }
                        AbstractScheduledService.this.shutDown();
                        ServiceDelegate.this.lock.unlock();
                        ServiceDelegate.this.notifyStopped();
                    } catch (Throwable t) {
                        ServiceDelegate.this.notifyFailed(t);
                    }
                }
            });
        }

        @Override // com.google.common.util.concurrent.AbstractService
        public String toString() {
            return AbstractScheduledService.this.toString();
        }
    }

    protected AbstractScheduledService() {
    }

    protected void startUp() throws Exception {
    }

    protected void shutDown() throws Exception {
    }

    protected ScheduledExecutorService executor() {
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() { // from class: com.google.common.util.concurrent.AbstractScheduledService.1ThreadFactoryImpl
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return MoreExecutors.newThread(AbstractScheduledService.this.serviceName(), runnable);
            }
        });
        addListener(new Service.Listener() { // from class: com.google.common.util.concurrent.AbstractScheduledService.1
            @Override // com.google.common.util.concurrent.Service.Listener
            public void terminated(Service.State from) {
                executor.shutdown();
            }

            @Override // com.google.common.util.concurrent.Service.Listener
            public void failed(Service.State from, Throwable failure) {
                executor.shutdown();
            }
        }, MoreExecutors.directExecutor());
        return executor;
    }

    protected String serviceName() {
        return getClass().getSimpleName();
    }

    public String toString() {
        return serviceName() + " [" + state() + "]";
    }

    @Override // com.google.common.util.concurrent.Service
    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service.State state() {
        return this.delegate.state();
    }

    @Override // com.google.common.util.concurrent.Service
    public final void addListener(Service.Listener listener, Executor executor) {
        this.delegate.addListener(listener, executor);
    }

    @Override // com.google.common.util.concurrent.Service
    public final Throwable failureCause() {
        return this.delegate.failureCause();
    }

    @Override // com.google.common.util.concurrent.Service
    @CanIgnoreReturnValue
    public final Service startAsync() {
        this.delegate.startAsync();
        return this;
    }

    @Override // com.google.common.util.concurrent.Service
    @CanIgnoreReturnValue
    public final Service stopAsync() {
        this.delegate.stopAsync();
        return this;
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitRunning() {
        this.delegate.awaitRunning();
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
        this.delegate.awaitRunning(timeout, unit);
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitTerminated() {
        this.delegate.awaitTerminated();
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
        this.delegate.awaitTerminated(timeout, unit);
    }

    @Beta
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractScheduledService$CustomScheduler.class */
    public static abstract class CustomScheduler extends Scheduler {
        protected abstract Schedule getNextSchedule() throws Exception;

        public CustomScheduler() {
            super();
        }

        /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractScheduledService$CustomScheduler$ReschedulableCallable.class */
        private class ReschedulableCallable extends ForwardingFuture<Void> implements Callable<Void> {
            private final Runnable wrappedRunnable;
            private final ScheduledExecutorService executor;
            private final AbstractService service;
            private final ReentrantLock lock = new ReentrantLock();
            @NullableDecl
            @GuardedBy(ClearCase.COMMAND_LOCK)
            private Future<Void> currentFuture;

            ReschedulableCallable(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
                this.wrappedRunnable = runnable;
                this.executor = executor;
                this.service = service;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                this.wrappedRunnable.run();
                reschedule();
                return null;
            }

            /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
                jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:17:0x005e
                	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
                	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
                	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
                */
            public void reschedule() {
                /*
                    r7 = this;
                    r0 = r7
                    com.google.common.util.concurrent.AbstractScheduledService$CustomScheduler r0 = com.google.common.util.concurrent.AbstractScheduledService.CustomScheduler.this     // Catch: java.lang.Throwable -> Lb
                    com.google.common.util.concurrent.AbstractScheduledService$CustomScheduler$Schedule r0 = r0.getNextSchedule()     // Catch: java.lang.Throwable -> Lb
                    r8 = r0
                    goto L15
                Lb:
                    r9 = move-exception
                    r0 = r7
                    com.google.common.util.concurrent.AbstractService r0 = r0.service
                    r1 = r9
                    r0.notifyFailed(r1)
                    return
                L15:
                    r0 = 0
                    r9 = r0
                    r0 = r7
                    java.util.concurrent.locks.ReentrantLock r0 = r0.lock
                    r0.lock()
                    r0 = r7
                    java.util.concurrent.Future<java.lang.Void> r0 = r0.currentFuture     // Catch: java.lang.Throwable -> L51
                    if (r0 == 0) goto L31
                    r0 = r7
                    java.util.concurrent.Future<java.lang.Void> r0 = r0.currentFuture     // Catch: java.lang.Throwable -> L51
                    boolean r0 = r0.isCancelled()     // Catch: java.lang.Throwable -> L51
                    if (r0 != 0) goto L47
                L31:
                    r0 = r7
                    r1 = r7
                    java.util.concurrent.ScheduledExecutorService r1 = r1.executor     // Catch: java.lang.Throwable -> L51
                    r2 = r7
                    r3 = r8
                    long r3 = com.google.common.util.concurrent.AbstractScheduledService.CustomScheduler.Schedule.access$800(r3)     // Catch: java.lang.Throwable -> L51
                    r4 = r8
                    java.util.concurrent.TimeUnit r4 = com.google.common.util.concurrent.AbstractScheduledService.CustomScheduler.Schedule.access$900(r4)     // Catch: java.lang.Throwable -> L51
                    java.util.concurrent.ScheduledFuture r1 = r1.schedule(r2, r3, r4)     // Catch: java.lang.Throwable -> L51
                    r0.currentFuture = r1     // Catch: java.lang.Throwable -> L51
                L47:
                    r0 = r7
                    java.util.concurrent.locks.ReentrantLock r0 = r0.lock
                    r0.unlock()
                    goto L6a
                L51:
                    r10 = move-exception
                    r0 = r10
                    r9 = r0
                    r0 = r7
                    java.util.concurrent.locks.ReentrantLock r0 = r0.lock
                    r0.unlock()
                    goto L6a
                L5e:
                    r11 = move-exception
                    r0 = r7
                    java.util.concurrent.locks.ReentrantLock r0 = r0.lock
                    r0.unlock()
                    r0 = r11
                    throw r0
                L6a:
                    r0 = r9
                    if (r0 == 0) goto L76
                    r0 = r7
                    com.google.common.util.concurrent.AbstractService r0 = r0.service
                    r1 = r9
                    r0.notifyFailed(r1)
                L76:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.AbstractScheduledService.CustomScheduler.ReschedulableCallable.reschedule():void");
            }

            @Override // com.google.common.util.concurrent.ForwardingFuture, java.util.concurrent.Future
            public boolean cancel(boolean mayInterruptIfRunning) {
                this.lock.lock();
                try {
                    return this.currentFuture.cancel(mayInterruptIfRunning);
                } finally {
                    this.lock.unlock();
                }
            }

            @Override // com.google.common.util.concurrent.ForwardingFuture, java.util.concurrent.Future
            public boolean isCancelled() {
                this.lock.lock();
                try {
                    return this.currentFuture.isCancelled();
                } finally {
                    this.lock.unlock();
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
            public Future<Void> delegate() {
                throw new UnsupportedOperationException("Only cancel and isCancelled is supported by this future");
            }
        }

        @Override // com.google.common.util.concurrent.AbstractScheduledService.Scheduler
        final Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
            ReschedulableCallable task = new ReschedulableCallable(service, executor, runnable);
            task.reschedule();
            return task;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Beta
        /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractScheduledService$CustomScheduler$Schedule.class */
        public static final class Schedule {
            private final long delay;
            private final TimeUnit unit;

            static /* synthetic */ long access$800(Schedule x0) {
                return x0.delay;
            }

            static /* synthetic */ TimeUnit access$900(Schedule x0) {
                return x0.unit;
            }

            public Schedule(long delay, TimeUnit unit) {
                this.delay = delay;
                this.unit = (TimeUnit) Preconditions.checkNotNull(unit);
            }
        }
    }
}
