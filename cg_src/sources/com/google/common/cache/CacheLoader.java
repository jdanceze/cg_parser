package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
@GwtCompatible(emulated = true)
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheLoader.class */
public abstract class CacheLoader<K, V> {
    public abstract V load(K k) throws Exception;

    @GwtIncompatible
    public ListenableFuture<V> reload(K key, V oldValue) throws Exception {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(oldValue);
        return Futures.immediateFuture(load(key));
    }

    public Map<K, V> loadAll(Iterable<? extends K> keys) throws Exception {
        throw new UnsupportedLoadingOperationException();
    }

    public static <K, V> CacheLoader<K, V> from(Function<K, V> function) {
        return new FunctionToCacheLoader(function);
    }

    public static <V> CacheLoader<Object, V> from(Supplier<V> supplier) {
        return new SupplierToCacheLoader(supplier);
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheLoader$FunctionToCacheLoader.class */
    private static final class FunctionToCacheLoader<K, V> extends CacheLoader<K, V> implements Serializable {
        private final Function<K, V> computingFunction;
        private static final long serialVersionUID = 0;

        public FunctionToCacheLoader(Function<K, V> computingFunction) {
            this.computingFunction = (Function) Preconditions.checkNotNull(computingFunction);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.cache.CacheLoader
        public V load(K key) {
            return (V) this.computingFunction.apply(Preconditions.checkNotNull(key));
        }
    }

    @GwtIncompatible
    public static <K, V> CacheLoader<K, V> asyncReloading(CacheLoader<K, V> loader, final Executor executor) {
        Preconditions.checkNotNull(loader);
        Preconditions.checkNotNull(executor);
        return new CacheLoader<K, V>() { // from class: com.google.common.cache.CacheLoader.1
            @Override // com.google.common.cache.CacheLoader
            public V load(K key) throws Exception {
                return (V) CacheLoader.this.load(key);
            }

            @Override // com.google.common.cache.CacheLoader
            public ListenableFuture<V> reload(final K key, final V oldValue) throws Exception {
                ListenableFutureTask<V> task = ListenableFutureTask.create(new Callable<V>() { // from class: com.google.common.cache.CacheLoader.1.1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.concurrent.Callable
                    public V call() throws Exception {
                        return CacheLoader.this.reload(key, oldValue).get();
                    }
                });
                executor.execute(task);
                return task;
            }

            @Override // com.google.common.cache.CacheLoader
            public Map<K, V> loadAll(Iterable<? extends K> keys) throws Exception {
                return CacheLoader.this.loadAll(keys);
            }
        };
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheLoader$SupplierToCacheLoader.class */
    private static final class SupplierToCacheLoader<V> extends CacheLoader<Object, V> implements Serializable {
        private final Supplier<V> computingSupplier;
        private static final long serialVersionUID = 0;

        public SupplierToCacheLoader(Supplier<V> computingSupplier) {
            this.computingSupplier = (Supplier) Preconditions.checkNotNull(computingSupplier);
        }

        @Override // com.google.common.cache.CacheLoader
        public V load(Object key) {
            Preconditions.checkNotNull(key);
            return this.computingSupplier.get();
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheLoader$UnsupportedLoadingOperationException.class */
    public static final class UnsupportedLoadingOperationException extends UnsupportedOperationException {
        UnsupportedLoadingOperationException() {
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheLoader$InvalidCacheLoadException.class */
    public static final class InvalidCacheLoadException extends RuntimeException {
        public InvalidCacheLoadException(String message) {
            super(message);
        }
    }
}
