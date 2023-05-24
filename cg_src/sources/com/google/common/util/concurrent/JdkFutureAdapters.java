package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
@Beta
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/JdkFutureAdapters.class */
public final class JdkFutureAdapters {
    public static <V> ListenableFuture<V> listenInPoolThread(Future<V> future) {
        if (future instanceof ListenableFuture) {
            return (ListenableFuture) future;
        }
        return new ListenableFutureAdapter(future);
    }

    public static <V> ListenableFuture<V> listenInPoolThread(Future<V> future, Executor executor) {
        Preconditions.checkNotNull(executor);
        if (future instanceof ListenableFuture) {
            return (ListenableFuture) future;
        }
        return new ListenableFutureAdapter(future, executor);
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/JdkFutureAdapters$ListenableFutureAdapter.class */
    private static class ListenableFutureAdapter<V> extends ForwardingFuture<V> implements ListenableFuture<V> {
        private static final ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ListenableFutureAdapter-thread-%d").build();
        private static final Executor defaultAdapterExecutor = Executors.newCachedThreadPool(threadFactory);
        private final Executor adapterExecutor;
        private final ExecutionList executionList;
        private final AtomicBoolean hasListeners;
        private final Future<V> delegate;

        ListenableFutureAdapter(Future<V> delegate) {
            this(delegate, defaultAdapterExecutor);
        }

        ListenableFutureAdapter(Future<V> delegate, Executor adapterExecutor) {
            this.executionList = new ExecutionList();
            this.hasListeners = new AtomicBoolean(false);
            this.delegate = (Future) Preconditions.checkNotNull(delegate);
            this.adapterExecutor = (Executor) Preconditions.checkNotNull(adapterExecutor);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
        public Future<V> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.util.concurrent.ListenableFuture
        public void addListener(Runnable listener, Executor exec) {
            this.executionList.add(listener, exec);
            if (this.hasListeners.compareAndSet(false, true)) {
                if (this.delegate.isDone()) {
                    this.executionList.execute();
                } else {
                    this.adapterExecutor.execute(new Runnable() { // from class: com.google.common.util.concurrent.JdkFutureAdapters.ListenableFutureAdapter.1
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                Uninterruptibles.getUninterruptibly(ListenableFutureAdapter.this.delegate);
                            } catch (Throwable th) {
                            }
                            ListenableFutureAdapter.this.executionList.execute();
                        }
                    });
                }
            }
        }
    }

    private JdkFutureAdapters() {
    }
}
