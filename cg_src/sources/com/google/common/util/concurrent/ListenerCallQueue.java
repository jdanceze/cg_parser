package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import com.google.errorprone.annotations.concurrent.GuardedBy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
/* JADX INFO: Access modifiers changed from: package-private */
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/ListenerCallQueue.class */
public final class ListenerCallQueue<L> {
    private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
    private final List<PerListenerQueue<L>> listeners = Collections.synchronizedList(new ArrayList());

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/ListenerCallQueue$Event.class */
    public interface Event<L> {
        void call(L l);
    }

    public void addListener(L listener, Executor executor) {
        Preconditions.checkNotNull(listener, "listener");
        Preconditions.checkNotNull(executor, "executor");
        this.listeners.add(new PerListenerQueue<>(listener, executor));
    }

    public void enqueue(Event<L> event) {
        enqueueHelper(event, event);
    }

    public void enqueue(Event<L> event, String label) {
        enqueueHelper(event, label);
    }

    private void enqueueHelper(Event<L> event, Object label) {
        Preconditions.checkNotNull(event, "event");
        Preconditions.checkNotNull(label, "label");
        synchronized (this.listeners) {
            for (PerListenerQueue<L> queue : this.listeners) {
                queue.add(event, label);
            }
        }
    }

    public void dispatch() {
        for (int i = 0; i < this.listeners.size(); i++) {
            this.listeners.get(i).dispatch();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/ListenerCallQueue$PerListenerQueue.class */
    public static final class PerListenerQueue<L> implements Runnable {
        final L listener;
        final Executor executor;
        @GuardedBy("this")
        final Queue<Event<L>> waitQueue = Queues.newArrayDeque();
        @GuardedBy("this")
        final Queue<Object> labelQueue = Queues.newArrayDeque();
        @GuardedBy("this")
        boolean isThreadScheduled;

        PerListenerQueue(L listener, Executor executor) {
            this.listener = (L) Preconditions.checkNotNull(listener);
            this.executor = (Executor) Preconditions.checkNotNull(executor);
        }

        synchronized void add(Event<L> event, Object label) {
            this.waitQueue.add(event);
            this.labelQueue.add(label);
        }

        void dispatch() {
            boolean scheduleEventRunner = false;
            synchronized (this) {
                if (!this.isThreadScheduled) {
                    this.isThreadScheduled = true;
                    scheduleEventRunner = true;
                }
            }
            if (scheduleEventRunner) {
                try {
                    this.executor.execute(this);
                } catch (RuntimeException e) {
                    synchronized (this) {
                        this.isThreadScheduled = false;
                        ListenerCallQueue.logger.log(Level.SEVERE, "Exception while running callbacks for " + this.listener + " on " + this.executor, (Throwable) e);
                        throw e;
                    }
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:18:0x0044, code lost:
            r0.call(r5.listener);
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x0051, code lost:
            r9 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x0053, code lost:
            com.google.common.util.concurrent.ListenerCallQueue.logger.log(java.util.logging.Level.SEVERE, "Exception while executing callback: " + r5.listener + soot.coffi.Instruction.argsep + r0, (java.lang.Throwable) r9);
         */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0084  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00a2  */
        /* JADX WARN: Removed duplicated region for block: B:50:0x00bd A[ORIG_RETURN, RETURN] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                r5 = this;
                r0 = 1
                r6 = r0
            L2:
                r0 = r5
                r1 = r0
                r9 = r1
                monitor-enter(r0)     // Catch: java.lang.Throwable -> L9c
                r0 = r5
                boolean r0 = r0.isThreadScheduled     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L9c
                com.google.common.base.Preconditions.checkState(r0)     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L9c
                r0 = r5
                java.util.Queue<com.google.common.util.concurrent.ListenerCallQueue$Event<L>> r0 = r0.waitQueue     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L9c
                java.lang.Object r0 = r0.poll()     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L9c
                com.google.common.util.concurrent.ListenerCallQueue$Event r0 = (com.google.common.util.concurrent.ListenerCallQueue.Event) r0     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L9c
                r7 = r0
                r0 = r5
                java.util.Queue<java.lang.Object> r0 = r0.labelQueue     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L9c
                java.lang.Object r0 = r0.poll()     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L9c
                r8 = r0
                r0 = r7
                if (r0 != 0) goto L36
                r0 = r5
                r1 = 0
                r0.isThreadScheduled = r1     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L9c
                r0 = 0
                r6 = r0
                r0 = r9
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L9c
                goto L80
            L36:
                r0 = r9
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L9c
                goto L44
            L3c:
                r10 = move-exception
                r0 = r9
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L9c
                r0 = r10
                throw r0     // Catch: java.lang.Throwable -> L9c
            L44:
                r0 = r7
                r1 = r5
                L r1 = r1.listener     // Catch: java.lang.RuntimeException -> L51 java.lang.Throwable -> L9c
                r0.call(r1)     // Catch: java.lang.RuntimeException -> L51 java.lang.Throwable -> L9c
                goto L7d
            L51:
                r9 = move-exception
                java.util.logging.Logger r0 = com.google.common.util.concurrent.ListenerCallQueue.access$000()     // Catch: java.lang.Throwable -> L9c
                java.util.logging.Level r1 = java.util.logging.Level.SEVERE     // Catch: java.lang.Throwable -> L9c
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L9c
                r3 = r2
                r3.<init>()     // Catch: java.lang.Throwable -> L9c
                java.lang.String r3 = "Exception while executing callback: "
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> L9c
                r3 = r5
                L r3 = r3.listener     // Catch: java.lang.Throwable -> L9c
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> L9c
                java.lang.String r3 = " "
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> L9c
                r3 = r8
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> L9c
                java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L9c
                r3 = r9
                r0.log(r1, r2, r3)     // Catch: java.lang.Throwable -> L9c
            L7d:
                goto L2
            L80:
                r0 = r6
                if (r0 == 0) goto Lbd
                r0 = r5
                r1 = r0
                r7 = r1
                monitor-enter(r0)
                r0 = r5
                r1 = 0
                r0.isThreadScheduled = r1     // Catch: java.lang.Throwable -> L92
                r0 = r7
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L92
                goto L99
            L92:
                r11 = move-exception
                r0 = r7
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L92
                r0 = r11
                throw r0
            L99:
                goto Lbd
            L9c:
                r12 = move-exception
                r0 = r6
                if (r0 == 0) goto Lba
                r0 = r5
                r1 = r0
                r13 = r1
                monitor-enter(r0)
                r0 = r5
                r1 = 0
                r0.isThreadScheduled = r1     // Catch: java.lang.Throwable -> Lb2
                r0 = r13
                monitor-exit(r0)     // Catch: java.lang.Throwable -> Lb2
                goto Lba
            Lb2:
                r14 = move-exception
                r0 = r13
                monitor-exit(r0)     // Catch: java.lang.Throwable -> Lb2
                r0 = r14
                throw r0
            Lba:
                r0 = r12
                throw r0
            Lbd:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.ListenerCallQueue.PerListenerQueue.run():void");
        }
    }
}
