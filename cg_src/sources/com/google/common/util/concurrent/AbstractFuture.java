package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import com.google.common.util.concurrent.internal.InternalFutures;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.ForOverride;
import com.google.j2objc.annotations.ReflectionSupport;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import soot.coffi.Instruction;
import sun.misc.Unsafe;
@ReflectionSupport(ReflectionSupport.Level.FULL)
@GwtCompatible(emulated = true)
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractFuture.class */
public abstract class AbstractFuture<V> extends InternalFutureFailureAccess implements ListenableFuture<V> {
    private static final boolean GENERATE_CANCELLATION_CAUSES = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
    private static final Logger log = Logger.getLogger(AbstractFuture.class.getName());
    private static final long SPIN_THRESHOLD_NANOS = 1000;
    private static final AtomicHelper ATOMIC_HELPER;
    private static final Object NULL;
    @NullableDecl
    private volatile Object value;
    @NullableDecl
    private volatile Listener listeners;
    @NullableDecl
    private volatile Waiter waiters;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractFuture$Trusted.class */
    public interface Trusted<V> extends ListenableFuture<V> {
    }

    static {
        AtomicHelper helper;
        Throwable thrownUnsafeFailure = null;
        Throwable thrownAtomicReferenceFieldUpdaterFailure = null;
        try {
            helper = new UnsafeAtomicHelper();
        } catch (Throwable unsafeFailure) {
            thrownUnsafeFailure = unsafeFailure;
            try {
                helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
            } catch (Throwable atomicReferenceFieldUpdaterFailure) {
                thrownAtomicReferenceFieldUpdaterFailure = atomicReferenceFieldUpdaterFailure;
                helper = new SynchronizedHelper();
            }
        }
        ATOMIC_HELPER = helper;
        if (thrownAtomicReferenceFieldUpdaterFailure != null) {
            log.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", thrownUnsafeFailure);
            log.log(Level.SEVERE, "SafeAtomicHelper is broken!", thrownAtomicReferenceFieldUpdaterFailure);
        }
        NULL = new Object();
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractFuture$TrustedFuture.class */
    static abstract class TrustedFuture<V> extends AbstractFuture<V> implements Trusted<V> {
        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        @CanIgnoreReturnValue
        public final V get() throws InterruptedException, ExecutionException {
            return (V) super.get();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        @CanIgnoreReturnValue
        public final V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return (V) super.get(timeout, unit);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isDone() {
            return super.isDone();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isCancelled() {
            return super.isCancelled();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, com.google.common.util.concurrent.ListenableFuture
        public final void addListener(Runnable listener, Executor executor) {
            super.addListener(listener, executor);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        @CanIgnoreReturnValue
        public final boolean cancel(boolean mayInterruptIfRunning) {
            return super.cancel(mayInterruptIfRunning);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractFuture$Waiter.class */
    public static final class Waiter {
        static final Waiter TOMBSTONE = new Waiter(false);
        @NullableDecl
        volatile Thread thread;
        @NullableDecl
        volatile Waiter next;

        Waiter(boolean unused) {
        }

        Waiter() {
            AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
        }

        void setNext(Waiter next) {
            AbstractFuture.ATOMIC_HELPER.putNext(this, next);
        }

        void unpark() {
            Thread w = this.thread;
            if (w != null) {
                this.thread = null;
                LockSupport.unpark(w);
            }
        }
    }

    private void removeWaiter(Waiter node) {
        node.thread = null;
        while (true) {
            Waiter pred = null;
            Waiter curr = this.waiters;
            if (curr == Waiter.TOMBSTONE) {
                return;
            }
            while (curr != null) {
                Waiter succ = curr.next;
                if (curr.thread != null) {
                    pred = curr;
                } else if (pred != null) {
                    pred.next = succ;
                    if (pred.thread == null) {
                        break;
                    }
                } else if (!ATOMIC_HELPER.casWaiters(this, curr, succ)) {
                    break;
                }
                curr = succ;
            }
            return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractFuture$Listener.class */
    public static final class Listener {
        static final Listener TOMBSTONE = new Listener(null, null);
        final Runnable task;
        final Executor executor;
        @NullableDecl
        Listener next;

        Listener(Runnable task, Executor executor) {
            this.task = task;
            this.executor = executor;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractFuture$Failure.class */
    public static final class Failure {
        static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.") { // from class: com.google.common.util.concurrent.AbstractFuture.Failure.1
            @Override // java.lang.Throwable
            public synchronized Throwable fillInStackTrace() {
                return this;
            }
        });
        final Throwable exception;

        Failure(Throwable exception) {
            this.exception = (Throwable) Preconditions.checkNotNull(exception);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractFuture$Cancellation.class */
    public static final class Cancellation {
        static final Cancellation CAUSELESS_INTERRUPTED;
        static final Cancellation CAUSELESS_CANCELLED;
        final boolean wasInterrupted;
        @NullableDecl
        final Throwable cause;

        static {
            if (AbstractFuture.GENERATE_CANCELLATION_CAUSES) {
                CAUSELESS_CANCELLED = null;
                CAUSELESS_INTERRUPTED = null;
                return;
            }
            CAUSELESS_CANCELLED = new Cancellation(false, null);
            CAUSELESS_INTERRUPTED = new Cancellation(true, null);
        }

        Cancellation(boolean wasInterrupted, @NullableDecl Throwable cause) {
            this.wasInterrupted = wasInterrupted;
            this.cause = cause;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractFuture$SetFuture.class */
    public static final class SetFuture<V> implements Runnable {
        final AbstractFuture<V> owner;
        final ListenableFuture<? extends V> future;

        SetFuture(AbstractFuture<V> owner, ListenableFuture<? extends V> future) {
            this.owner = owner;
            this.future = future;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (((AbstractFuture) this.owner).value == this) {
                Object valueToSet = AbstractFuture.getFutureValue(this.future);
                if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, valueToSet)) {
                    AbstractFuture.complete(this.owner);
                }
            }
        }
    }

    @Override // java.util.concurrent.Future
    @CanIgnoreReturnValue
    public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
        long timeoutNanos = unit.toNanos(timeout);
        long remainingNanos = timeoutNanos;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object localValue = this.value;
        if ((localValue != null) & (!(localValue instanceof SetFuture))) {
            return getDoneValue(localValue);
        }
        long endNanos = remainingNanos > 0 ? System.nanoTime() + remainingNanos : 0L;
        if (remainingNanos >= 1000) {
            Waiter oldHead = this.waiters;
            if (oldHead != Waiter.TOMBSTONE) {
                Waiter node = new Waiter();
                do {
                    node.setNext(oldHead);
                    if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                        do {
                            LockSupport.parkNanos(this, remainingNanos);
                            if (Thread.interrupted()) {
                                removeWaiter(node);
                                throw new InterruptedException();
                            }
                            Object localValue2 = this.value;
                            if ((localValue2 != null) & (!(localValue2 instanceof SetFuture))) {
                                return getDoneValue(localValue2);
                            }
                            remainingNanos = endNanos - System.nanoTime();
                        } while (remainingNanos >= 1000);
                        removeWaiter(node);
                    } else {
                        oldHead = this.waiters;
                    }
                } while (oldHead != Waiter.TOMBSTONE);
                return getDoneValue(this.value);
            }
            return getDoneValue(this.value);
        }
        while (remainingNanos > 0) {
            Object localValue3 = this.value;
            if ((localValue3 != null) & (!(localValue3 instanceof SetFuture))) {
                return getDoneValue(localValue3);
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            remainingNanos = endNanos - System.nanoTime();
        }
        String futureToString = toString();
        String unitString = unit.toString().toLowerCase(Locale.ROOT);
        String message = "Waited " + timeout + Instruction.argsep + unit.toString().toLowerCase(Locale.ROOT);
        if (remainingNanos + 1000 < 0) {
            String message2 = message + " (plus ";
            long overWaitNanos = -remainingNanos;
            long overWaitUnits = unit.convert(overWaitNanos, TimeUnit.NANOSECONDS);
            long overWaitLeftoverNanos = overWaitNanos - unit.toNanos(overWaitUnits);
            boolean shouldShowExtraNanos = overWaitUnits == 0 || overWaitLeftoverNanos > 1000;
            if (overWaitUnits > 0) {
                String message3 = message2 + overWaitUnits + Instruction.argsep + unitString;
                if (shouldShowExtraNanos) {
                    message3 = message3 + ",";
                }
                message2 = message3 + Instruction.argsep;
            }
            if (shouldShowExtraNanos) {
                message2 = message2 + overWaitLeftoverNanos + " nanoseconds ";
            }
            message = message2 + "delay)";
        }
        if (isDone()) {
            throw new TimeoutException(message + " but future completed as timeout expired");
        }
        throw new TimeoutException(message + " for " + futureToString);
    }

    @Override // java.util.concurrent.Future
    @CanIgnoreReturnValue
    public V get() throws InterruptedException, ExecutionException {
        Object localValue;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object localValue2 = this.value;
        if ((localValue2 != null) & (!(localValue2 instanceof SetFuture))) {
            return getDoneValue(localValue2);
        }
        Waiter oldHead = this.waiters;
        if (oldHead != Waiter.TOMBSTONE) {
            Waiter node = new Waiter();
            do {
                node.setNext(oldHead);
                if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                    do {
                        LockSupport.park(this);
                        if (Thread.interrupted()) {
                            removeWaiter(node);
                            throw new InterruptedException();
                        }
                        localValue = this.value;
                    } while (!((localValue != null) & (!(localValue instanceof SetFuture))));
                    return getDoneValue(localValue);
                }
                oldHead = this.waiters;
            } while (oldHead != Waiter.TOMBSTONE);
            return getDoneValue(this.value);
        }
        return getDoneValue(this.value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private V getDoneValue(Object obj) throws ExecutionException {
        if (obj instanceof Cancellation) {
            throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation) obj).cause);
        }
        if (obj instanceof Failure) {
            throw new ExecutionException(((Failure) obj).exception);
        }
        if (obj == NULL) {
            return null;
        }
        return obj;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        Object localValue = this.value;
        return (localValue != null) & (!(localValue instanceof SetFuture));
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        Object localValue = this.value;
        return localValue instanceof Cancellation;
    }

    @Override // java.util.concurrent.Future
    @CanIgnoreReturnValue
    public boolean cancel(boolean mayInterruptIfRunning) {
        Object localValue = this.value;
        boolean rValue = false;
        if ((localValue == null) | (localValue instanceof SetFuture)) {
            Object valueToSet = GENERATE_CANCELLATION_CAUSES ? new Cancellation(mayInterruptIfRunning, new CancellationException("Future.cancel() was called.")) : mayInterruptIfRunning ? Cancellation.CAUSELESS_INTERRUPTED : Cancellation.CAUSELESS_CANCELLED;
            AbstractFuture<V> abstractFuture = this;
            while (true) {
                if (ATOMIC_HELPER.casValue(abstractFuture, localValue, valueToSet)) {
                    rValue = true;
                    if (mayInterruptIfRunning) {
                        abstractFuture.interruptTask();
                    }
                    complete(abstractFuture);
                    if (!(localValue instanceof SetFuture)) {
                        break;
                    }
                    ListenableFuture<?> futureToPropagateTo = ((SetFuture) localValue).future;
                    if (futureToPropagateTo instanceof Trusted) {
                        AbstractFuture<V> abstractFuture2 = (AbstractFuture) futureToPropagateTo;
                        localValue = abstractFuture2.value;
                        if (!(localValue == null) && !(localValue instanceof SetFuture)) {
                            break;
                        }
                        abstractFuture = abstractFuture2;
                    } else {
                        futureToPropagateTo.cancel(mayInterruptIfRunning);
                        break;
                    }
                } else {
                    localValue = abstractFuture.value;
                    if (!(localValue instanceof SetFuture)) {
                        break;
                    }
                }
            }
        }
        return rValue;
    }

    protected void interruptTask() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean wasInterrupted() {
        Object localValue = this.value;
        return (localValue instanceof Cancellation) && ((Cancellation) localValue).wasInterrupted;
    }

    @Override // com.google.common.util.concurrent.ListenableFuture
    public void addListener(Runnable listener, Executor executor) {
        Preconditions.checkNotNull(listener, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");
        if (!isDone()) {
            Listener oldHead = this.listeners;
            if (oldHead != Listener.TOMBSTONE) {
                Listener newNode = new Listener(listener, executor);
                do {
                    newNode.next = oldHead;
                    if (ATOMIC_HELPER.casListeners(this, oldHead, newNode)) {
                        return;
                    }
                    oldHead = this.listeners;
                } while (oldHead != Listener.TOMBSTONE);
            }
        }
        executeListener(listener, executor);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @CanIgnoreReturnValue
    public boolean set(@NullableDecl V value) {
        Object valueToSet = value == null ? NULL : value;
        if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
            complete(this);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @CanIgnoreReturnValue
    public boolean setException(Throwable throwable) {
        Object valueToSet = new Failure((Throwable) Preconditions.checkNotNull(throwable));
        if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
            complete(this);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @CanIgnoreReturnValue
    @Beta
    public boolean setFuture(ListenableFuture<? extends V> future) {
        Failure failure;
        Preconditions.checkNotNull(future);
        Object localValue = this.value;
        if (localValue == null) {
            if (future.isDone()) {
                Object value = getFutureValue(future);
                if (ATOMIC_HELPER.casValue(this, null, value)) {
                    complete(this);
                    return true;
                }
                return false;
            }
            SetFuture valueToSet = new SetFuture(this, future);
            if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
                try {
                    future.addListener(valueToSet, DirectExecutor.INSTANCE);
                    return true;
                } catch (Throwable t) {
                    try {
                        failure = new Failure(t);
                    } catch (Throwable th) {
                        failure = Failure.FALLBACK_INSTANCE;
                    }
                    ATOMIC_HELPER.casValue(this, valueToSet, failure);
                    return true;
                }
            }
            localValue = this.value;
        }
        if (localValue instanceof Cancellation) {
            future.cancel(((Cancellation) localValue).wasInterrupted);
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object getFutureValue(ListenableFuture<?> future) {
        Throwable throwable;
        if (future instanceof Trusted) {
            Object v = ((AbstractFuture) future).value;
            if (v instanceof Cancellation) {
                Cancellation c = (Cancellation) v;
                if (c.wasInterrupted) {
                    v = c.cause != null ? new Cancellation(false, c.cause) : Cancellation.CAUSELESS_CANCELLED;
                }
            }
            return v;
        } else if ((future instanceof InternalFutureFailureAccess) && (throwable = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess) future)) != null) {
            return new Failure(throwable);
        } else {
            boolean wasCancelled = future.isCancelled();
            if ((!GENERATE_CANCELLATION_CAUSES) & wasCancelled) {
                return Cancellation.CAUSELESS_CANCELLED;
            }
            try {
                Object v2 = getUninterruptibly(future);
                if (wasCancelled) {
                    return new Cancellation(false, new IllegalArgumentException("get() did not throw CancellationException, despite reporting isCancelled() == true: " + future));
                }
                return v2 == null ? NULL : v2;
            } catch (CancellationException cancellation) {
                if (!wasCancelled) {
                    return new Failure(new IllegalArgumentException("get() threw CancellationException, despite reporting isCancelled() == false: " + future, cancellation));
                }
                return new Cancellation(false, cancellation);
            } catch (ExecutionException exception) {
                if (wasCancelled) {
                    return new Cancellation(false, new IllegalArgumentException("get() did not throw CancellationException, despite reporting isCancelled() == true: " + future, exception));
                }
                return new Failure(exception.getCause());
            } catch (Throwable t) {
                return new Failure(t);
            }
        }
    }

    private static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
        V v;
        boolean interrupted = false;
        while (true) {
            try {
                v = future.get();
                break;
            } catch (InterruptedException e) {
                interrupted = true;
            } catch (Throwable th) {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v17, types: [com.google.common.util.concurrent.AbstractFuture<V>] */
    public static void complete(AbstractFuture<?> future) {
        Listener next = null;
        while (true) {
            future.releaseWaiters();
            future.afterDone();
            next = future.clearListeners(next);
            while (next != null) {
                Listener curr = next;
                next = next.next;
                Runnable task = curr.task;
                if (task instanceof SetFuture) {
                    SetFuture<?> setFuture = (SetFuture) task;
                    future = setFuture.owner;
                    if (((AbstractFuture) future).value == setFuture) {
                        Object valueToSet = getFutureValue(setFuture.future);
                        if (ATOMIC_HELPER.casValue(future, setFuture, valueToSet)) {
                            break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    executeListener(task, curr.executor);
                }
            }
            return;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Beta
    @ForOverride
    public void afterDone() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.util.concurrent.internal.InternalFutureFailureAccess
    @NullableDecl
    public final Throwable tryInternalFastPathGetFailure() {
        if (this instanceof Trusted) {
            Object obj = this.value;
            if (obj instanceof Failure) {
                return ((Failure) obj).exception;
            }
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void maybePropagateCancellationTo(@NullableDecl Future<?> related) {
        if ((related != null) & isCancelled()) {
            related.cancel(wasInterrupted());
        }
    }

    private void releaseWaiters() {
        Waiter head;
        do {
            head = this.waiters;
        } while (!ATOMIC_HELPER.casWaiters(this, head, Waiter.TOMBSTONE));
        Waiter waiter = head;
        while (true) {
            Waiter currentWaiter = waiter;
            if (currentWaiter != null) {
                currentWaiter.unpark();
                waiter = currentWaiter.next;
            } else {
                return;
            }
        }
    }

    private Listener clearListeners(Listener onto) {
        Listener head;
        do {
            head = this.listeners;
        } while (!ATOMIC_HELPER.casListeners(this, head, Listener.TOMBSTONE));
        Listener listener = onto;
        while (true) {
            Listener reversedList = listener;
            if (head != null) {
                Listener tmp = head;
                head = head.next;
                tmp.next = reversedList;
                listener = tmp;
            } else {
                return reversedList;
            }
        }
    }

    public String toString() {
        String pendingDescription;
        StringBuilder builder = new StringBuilder().append(super.toString()).append("[status=");
        if (isCancelled()) {
            builder.append("CANCELLED");
        } else if (isDone()) {
            addDoneString(builder);
        } else {
            try {
                pendingDescription = pendingToString();
            } catch (RuntimeException e) {
                pendingDescription = "Exception thrown from implementation: " + e.getClass();
            }
            if (pendingDescription != null && !pendingDescription.isEmpty()) {
                builder.append("PENDING, info=[").append(pendingDescription).append("]");
            } else if (isDone()) {
                addDoneString(builder);
            } else {
                builder.append("PENDING");
            }
        }
        return builder.append("]").toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @NullableDecl
    public String pendingToString() {
        Object localValue = this.value;
        if (localValue instanceof SetFuture) {
            return "setFuture=[" + userObjectToString(((SetFuture) localValue).future) + "]";
        }
        if (this instanceof ScheduledFuture) {
            return "remaining delay=[" + ((ScheduledFuture) this).getDelay(TimeUnit.MILLISECONDS) + " ms]";
        }
        return null;
    }

    private void addDoneString(StringBuilder builder) {
        try {
            builder.append("SUCCESS, result=[").append(userObjectToString(getUninterruptibly(this))).append("]");
        } catch (CancellationException e) {
            builder.append("CANCELLED");
        } catch (RuntimeException e2) {
            builder.append("UNKNOWN, cause=[").append(e2.getClass()).append(" thrown from get()]");
        } catch (ExecutionException e3) {
            builder.append("FAILURE, cause=[").append(e3.getCause()).append("]");
        }
    }

    private String userObjectToString(Object o) {
        if (o == this) {
            return "this future";
        }
        return String.valueOf(o);
    }

    private static void executeListener(Runnable runnable, Executor executor) {
        try {
            executor.execute(runnable);
        } catch (RuntimeException e) {
            log.log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, (Throwable) e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractFuture$AtomicHelper.class */
    public static abstract class AtomicHelper {
        abstract void putThread(Waiter waiter, Thread thread);

        abstract void putNext(Waiter waiter, Waiter waiter2);

        abstract boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2);

        abstract boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2);

        abstract boolean casValue(AbstractFuture<?> abstractFuture, Object obj, Object obj2);

        private AtomicHelper() {
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractFuture$UnsafeAtomicHelper.class */
    private static final class UnsafeAtomicHelper extends AtomicHelper {
        static final Unsafe UNSAFE;
        static final long LISTENERS_OFFSET;
        static final long WAITERS_OFFSET;
        static final long VALUE_OFFSET;
        static final long WAITER_THREAD_OFFSET;
        static final long WAITER_NEXT_OFFSET;

        private UnsafeAtomicHelper() {
            super();
        }

        static {
            Unsafe unsafe;
            try {
                unsafe = Unsafe.getUnsafe();
            } catch (SecurityException e) {
                try {
                    unsafe = (Unsafe) AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>() { // from class: com.google.common.util.concurrent.AbstractFuture.UnsafeAtomicHelper.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public Unsafe run() throws Exception {
                            Field[] declaredFields;
                            for (Field f : Unsafe.class.getDeclaredFields()) {
                                f.setAccessible(true);
                                Object x = f.get(null);
                                if (Unsafe.class.isInstance(x)) {
                                    return (Unsafe) Unsafe.class.cast(x);
                                }
                            }
                            throw new NoSuchFieldError("the Unsafe");
                        }
                    });
                } catch (PrivilegedActionException e2) {
                    throw new RuntimeException("Could not initialize intrinsics", e2.getCause());
                }
            }
            try {
                WAITERS_OFFSET = unsafe.objectFieldOffset(AbstractFuture.class.getDeclaredField("waiters"));
                LISTENERS_OFFSET = unsafe.objectFieldOffset(AbstractFuture.class.getDeclaredField("listeners"));
                VALUE_OFFSET = unsafe.objectFieldOffset(AbstractFuture.class.getDeclaredField("value"));
                WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("thread"));
                WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("next"));
                UNSAFE = unsafe;
            } catch (Exception e3) {
                Throwables.throwIfUnchecked(e3);
                throw new RuntimeException(e3);
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putThread(Waiter waiter, Thread newValue) {
            UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putNext(Waiter waiter, Waiter newValue) {
            UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casWaiters(AbstractFuture<?> future, Waiter expect, Waiter update) {
            return UNSAFE.compareAndSwapObject(future, WAITERS_OFFSET, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casListeners(AbstractFuture<?> future, Listener expect, Listener update) {
            return UNSAFE.compareAndSwapObject(future, LISTENERS_OFFSET, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
            return UNSAFE.compareAndSwapObject(future, VALUE_OFFSET, expect, update);
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractFuture$SafeAtomicHelper.class */
    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater, AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater) {
            super();
            this.waiterThreadUpdater = waiterThreadUpdater;
            this.waiterNextUpdater = waiterNextUpdater;
            this.waitersUpdater = waitersUpdater;
            this.listenersUpdater = listenersUpdater;
            this.valueUpdater = valueUpdater;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putThread(Waiter waiter, Thread newValue) {
            this.waiterThreadUpdater.lazySet(waiter, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putNext(Waiter waiter, Waiter newValue) {
            this.waiterNextUpdater.lazySet(waiter, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casWaiters(AbstractFuture<?> future, Waiter expect, Waiter update) {
            return this.waitersUpdater.compareAndSet(future, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casListeners(AbstractFuture<?> future, Listener expect, Listener update) {
            return this.listenersUpdater.compareAndSet(future, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
            return this.valueUpdater.compareAndSet(future, expect, update);
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AbstractFuture$SynchronizedHelper.class */
    private static final class SynchronizedHelper extends AtomicHelper {
        private SynchronizedHelper() {
            super();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putThread(Waiter waiter, Thread newValue) {
            waiter.thread = newValue;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putNext(Waiter waiter, Waiter newValue) {
            waiter.next = newValue;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casWaiters(AbstractFuture<?> future, Waiter expect, Waiter update) {
            synchronized (future) {
                if (((AbstractFuture) future).waiters == expect) {
                    ((AbstractFuture) future).waiters = update;
                    return true;
                }
                return false;
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casListeners(AbstractFuture<?> future, Listener expect, Listener update) {
            synchronized (future) {
                if (((AbstractFuture) future).listeners == expect) {
                    ((AbstractFuture) future).listeners = update;
                    return true;
                }
                return false;
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
            synchronized (future) {
                if (((AbstractFuture) future).value == expect) {
                    ((AbstractFuture) future).value = update;
                    return true;
                }
                return false;
            }
        }
    }

    private static CancellationException cancellationExceptionWithCause(@NullableDecl String message, @NullableDecl Throwable cause) {
        CancellationException exception = new CancellationException(message);
        exception.initCause(cause);
        return exception;
    }
}
