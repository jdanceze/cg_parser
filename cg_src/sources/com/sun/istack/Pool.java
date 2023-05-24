package com.sun.istack;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedQueue;
/* loaded from: gencallgraphv3.jar:istack-commons-runtime-3.0.7.jar:com/sun/istack/Pool.class */
public interface Pool<T> {
    @NotNull
    T take();

    void recycle(@NotNull T t);

    /* loaded from: gencallgraphv3.jar:istack-commons-runtime-3.0.7.jar:com/sun/istack/Pool$Impl.class */
    public static abstract class Impl<T> implements Pool<T> {
        private volatile WeakReference<ConcurrentLinkedQueue<T>> queue;

        @NotNull
        protected abstract T create();

        @Override // com.sun.istack.Pool
        @NotNull
        public final T take() {
            T t = getQueue().poll();
            if (t == null) {
                return create();
            }
            return t;
        }

        @Override // com.sun.istack.Pool
        public final void recycle(T t) {
            getQueue().offer(t);
        }

        private ConcurrentLinkedQueue<T> getQueue() {
            ConcurrentLinkedQueue<T> d;
            WeakReference<ConcurrentLinkedQueue<T>> q = this.queue;
            if (q != null && (d = q.get()) != null) {
                return d;
            }
            ConcurrentLinkedQueue<T> d2 = new ConcurrentLinkedQueue<>();
            this.queue = new WeakReference<>(d2);
            return d2;
        }
    }
}
