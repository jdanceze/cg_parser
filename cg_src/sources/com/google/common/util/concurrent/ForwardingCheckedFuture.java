package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.Exception;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
@Beta
@GwtIncompatible
@Deprecated
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/ForwardingCheckedFuture.class */
public abstract class ForwardingCheckedFuture<V, X extends Exception> extends ForwardingListenableFuture<V> implements CheckedFuture<V, X> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.util.concurrent.ForwardingListenableFuture, com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
    public abstract CheckedFuture<V, X> delegate();

    @Override // com.google.common.util.concurrent.CheckedFuture
    @CanIgnoreReturnValue
    public V checkedGet() throws Exception {
        return delegate().checkedGet();
    }

    @Override // com.google.common.util.concurrent.CheckedFuture
    @CanIgnoreReturnValue
    public V checkedGet(long timeout, TimeUnit unit) throws TimeoutException, Exception {
        return delegate().checkedGet(timeout, unit);
    }

    @Beta
    @Deprecated
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/ForwardingCheckedFuture$SimpleForwardingCheckedFuture.class */
    public static abstract class SimpleForwardingCheckedFuture<V, X extends Exception> extends ForwardingCheckedFuture<V, X> {
        private final CheckedFuture<V, X> delegate;

        protected SimpleForwardingCheckedFuture(CheckedFuture<V, X> delegate) {
            this.delegate = (CheckedFuture) Preconditions.checkNotNull(delegate);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.util.concurrent.ForwardingCheckedFuture, com.google.common.util.concurrent.ForwardingListenableFuture, com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
        public final CheckedFuture<V, X> delegate() {
            return this.delegate;
        }
    }
}
