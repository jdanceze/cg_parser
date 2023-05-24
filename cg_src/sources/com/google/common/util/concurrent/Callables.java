package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.concurrent.Callable;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtCompatible(emulated = true)
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/Callables.class */
public final class Callables {
    private Callables() {
    }

    public static <T> Callable<T> returning(@NullableDecl final T value) {
        return new Callable<T>() { // from class: com.google.common.util.concurrent.Callables.1
            /* JADX WARN: Type inference failed for: r0v1, types: [T, java.lang.Object] */
            @Override // java.util.concurrent.Callable
            public T call() {
                return value;
            }
        };
    }

    @Beta
    @GwtIncompatible
    public static <T> AsyncCallable<T> asAsyncCallable(final Callable<T> callable, final ListeningExecutorService listeningExecutorService) {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(listeningExecutorService);
        return new AsyncCallable<T>() { // from class: com.google.common.util.concurrent.Callables.2
            @Override // com.google.common.util.concurrent.AsyncCallable
            public ListenableFuture<T> call() throws Exception {
                return ListeningExecutorService.this.submit(callable);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GwtIncompatible
    public static <T> Callable<T> threadRenaming(final Callable<T> callable, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(nameSupplier);
        Preconditions.checkNotNull(callable);
        return new Callable<T>() { // from class: com.google.common.util.concurrent.Callables.3
            /* JADX WARN: Type inference failed for: r0v14, types: [T, java.lang.Object] */
            @Override // java.util.concurrent.Callable
            public T call() throws Exception {
                Thread currentThread = Thread.currentThread();
                String oldName = currentThread.getName();
                boolean restoreName = Callables.trySetName((String) Supplier.this.get(), currentThread);
                try {
                    ?? call = callable.call();
                    if (restoreName) {
                        Callables.trySetName(oldName, currentThread);
                    }
                    return call;
                } catch (Throwable th) {
                    if (restoreName) {
                        Callables.trySetName(oldName, currentThread);
                    }
                    throw th;
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GwtIncompatible
    public static Runnable threadRenaming(final Runnable task, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(nameSupplier);
        Preconditions.checkNotNull(task);
        return new Runnable() { // from class: com.google.common.util.concurrent.Callables.4
            @Override // java.lang.Runnable
            public void run() {
                Thread currentThread = Thread.currentThread();
                String oldName = currentThread.getName();
                boolean restoreName = Callables.trySetName((String) Supplier.this.get(), currentThread);
                try {
                    task.run();
                    if (restoreName) {
                        Callables.trySetName(oldName, currentThread);
                    }
                } catch (Throwable th) {
                    if (restoreName) {
                        Callables.trySetName(oldName, currentThread);
                    }
                    throw th;
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GwtIncompatible
    public static boolean trySetName(String threadName, Thread currentThread) {
        try {
            currentThread.setName(threadName);
            return true;
        } catch (SecurityException e) {
            return false;
        }
    }
}
