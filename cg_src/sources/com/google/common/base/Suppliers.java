package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/Suppliers.class */
public final class Suppliers {

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/Suppliers$SupplierFunction.class */
    private interface SupplierFunction<T> extends Function<Supplier<T>, T> {
    }

    private Suppliers() {
    }

    public static <F, T> Supplier<T> compose(Function<? super F, T> function, Supplier<F> supplier) {
        return new SupplierComposition(function, supplier);
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/Suppliers$SupplierComposition.class */
    private static class SupplierComposition<F, T> implements Supplier<T>, Serializable {
        final Function<? super F, T> function;
        final Supplier<F> supplier;
        private static final long serialVersionUID = 0;

        SupplierComposition(Function<? super F, T> function, Supplier<F> supplier) {
            this.function = (Function) Preconditions.checkNotNull(function);
            this.supplier = (Supplier) Preconditions.checkNotNull(supplier);
        }

        @Override // com.google.common.base.Supplier
        public T get() {
            return this.function.apply((F) this.supplier.get());
        }

        public boolean equals(@NullableDecl Object obj) {
            if (obj instanceof SupplierComposition) {
                SupplierComposition<?, ?> that = (SupplierComposition) obj;
                return this.function.equals(that.function) && this.supplier.equals(that.supplier);
            }
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(this.function, this.supplier);
        }

        public String toString() {
            return "Suppliers.compose(" + this.function + ", " + this.supplier + ")";
        }
    }

    public static <T> Supplier<T> memoize(Supplier<T> delegate) {
        if ((delegate instanceof NonSerializableMemoizingSupplier) || (delegate instanceof MemoizingSupplier)) {
            return delegate;
        }
        return delegate instanceof Serializable ? new MemoizingSupplier(delegate) : new NonSerializableMemoizingSupplier(delegate);
    }

    @VisibleForTesting
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/Suppliers$MemoizingSupplier.class */
    static class MemoizingSupplier<T> implements Supplier<T>, Serializable {
        final Supplier<T> delegate;
        volatile transient boolean initialized;
        @NullableDecl
        transient T value;
        private static final long serialVersionUID = 0;

        MemoizingSupplier(Supplier<T> delegate) {
            this.delegate = (Supplier) Preconditions.checkNotNull(delegate);
        }

        @Override // com.google.common.base.Supplier
        public T get() {
            if (!this.initialized) {
                synchronized (this) {
                    if (!this.initialized) {
                        T t = this.delegate.get();
                        this.value = t;
                        this.initialized = true;
                        return t;
                    }
                }
            }
            return this.value;
        }

        public String toString() {
            return "Suppliers.memoize(" + (this.initialized ? "<supplier that returned " + this.value + ">" : this.delegate) + ")";
        }
    }

    @VisibleForTesting
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/Suppliers$NonSerializableMemoizingSupplier.class */
    static class NonSerializableMemoizingSupplier<T> implements Supplier<T> {
        volatile Supplier<T> delegate;
        volatile boolean initialized;
        @NullableDecl
        T value;

        NonSerializableMemoizingSupplier(Supplier<T> delegate) {
            this.delegate = (Supplier) Preconditions.checkNotNull(delegate);
        }

        @Override // com.google.common.base.Supplier
        public T get() {
            if (!this.initialized) {
                synchronized (this) {
                    if (!this.initialized) {
                        T t = this.delegate.get();
                        this.value = t;
                        this.initialized = true;
                        this.delegate = null;
                        return t;
                    }
                }
            }
            return this.value;
        }

        public String toString() {
            Supplier<T> delegate = this.delegate;
            return "Suppliers.memoize(" + (delegate == null ? "<supplier that returned " + this.value + ">" : delegate) + ")";
        }
    }

    public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit) {
        return new ExpiringMemoizingSupplier(delegate, duration, unit);
    }

    @VisibleForTesting
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/Suppliers$ExpiringMemoizingSupplier.class */
    static class ExpiringMemoizingSupplier<T> implements Supplier<T>, Serializable {
        final Supplier<T> delegate;
        final long durationNanos;
        @NullableDecl
        volatile transient T value;
        volatile transient long expirationNanos;
        private static final long serialVersionUID = 0;

        ExpiringMemoizingSupplier(Supplier<T> delegate, long duration, TimeUnit unit) {
            this.delegate = (Supplier) Preconditions.checkNotNull(delegate);
            this.durationNanos = unit.toNanos(duration);
            Preconditions.checkArgument(duration > 0, "duration (%s %s) must be > 0", duration, unit);
        }

        @Override // com.google.common.base.Supplier
        public T get() {
            long nanos = this.expirationNanos;
            long now = Platform.systemNanoTime();
            if (nanos == 0 || now - nanos >= 0) {
                synchronized (this) {
                    if (nanos == this.expirationNanos) {
                        T t = this.delegate.get();
                        this.value = t;
                        long nanos2 = now + this.durationNanos;
                        this.expirationNanos = nanos2 == 0 ? 1L : nanos2;
                        return t;
                    }
                }
            }
            return this.value;
        }

        public String toString() {
            return "Suppliers.memoizeWithExpiration(" + this.delegate + ", " + this.durationNanos + ", NANOS)";
        }
    }

    public static <T> Supplier<T> ofInstance(@NullableDecl T instance) {
        return new SupplierOfInstance(instance);
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/Suppliers$SupplierOfInstance.class */
    private static class SupplierOfInstance<T> implements Supplier<T>, Serializable {
        @NullableDecl
        final T instance;
        private static final long serialVersionUID = 0;

        SupplierOfInstance(@NullableDecl T instance) {
            this.instance = instance;
        }

        @Override // com.google.common.base.Supplier
        public T get() {
            return this.instance;
        }

        public boolean equals(@NullableDecl Object obj) {
            if (obj instanceof SupplierOfInstance) {
                SupplierOfInstance<?> that = (SupplierOfInstance) obj;
                return Objects.equal(this.instance, that.instance);
            }
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(this.instance);
        }

        public String toString() {
            return "Suppliers.ofInstance(" + this.instance + ")";
        }
    }

    public static <T> Supplier<T> synchronizedSupplier(Supplier<T> delegate) {
        return new ThreadSafeSupplier(delegate);
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/Suppliers$ThreadSafeSupplier.class */
    private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable {
        final Supplier<T> delegate;
        private static final long serialVersionUID = 0;

        ThreadSafeSupplier(Supplier<T> delegate) {
            this.delegate = (Supplier) Preconditions.checkNotNull(delegate);
        }

        @Override // com.google.common.base.Supplier
        public T get() {
            T t;
            synchronized (this.delegate) {
                t = this.delegate.get();
            }
            return t;
        }

        public String toString() {
            return "Suppliers.synchronizedSupplier(" + this.delegate + ")";
        }
    }

    public static <T> Function<Supplier<T>, T> supplierFunction() {
        SupplierFunction<T> sf = SupplierFunctionImpl.INSTANCE;
        return sf;
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/Suppliers$SupplierFunctionImpl.class */
    private enum SupplierFunctionImpl implements SupplierFunction<Object> {
        INSTANCE;

        @Override // com.google.common.base.Function
        public Object apply(Supplier<Object> input) {
            return input.get();
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Suppliers.supplierFunction()";
        }
    }
}
