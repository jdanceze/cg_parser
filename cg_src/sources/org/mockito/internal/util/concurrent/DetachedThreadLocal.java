package org.mockito.internal.util.concurrent;

import org.mockito.internal.util.concurrent.WeakConcurrentMap;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/concurrent/DetachedThreadLocal.class */
public class DetachedThreadLocal<T> implements Runnable {
    final WeakConcurrentMap<Thread, T> map;

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/concurrent/DetachedThreadLocal$Cleaner.class */
    public enum Cleaner {
        THREAD,
        INLINE,
        MANUAL
    }

    public DetachedThreadLocal(Cleaner cleaner) {
        switch (cleaner) {
            case THREAD:
            case MANUAL:
                this.map = new WeakConcurrentMap<Thread, T>(cleaner == Cleaner.THREAD) { // from class: org.mockito.internal.util.concurrent.DetachedThreadLocal.1
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // org.mockito.internal.util.concurrent.WeakConcurrentMap
                    public T defaultValue(Thread key) {
                        return (T) DetachedThreadLocal.this.initialValue(key);
                    }
                };
                return;
            case INLINE:
                this.map = new WeakConcurrentMap.WithInlinedExpunction<Thread, T>() { // from class: org.mockito.internal.util.concurrent.DetachedThreadLocal.2
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // org.mockito.internal.util.concurrent.WeakConcurrentMap
                    public T defaultValue(Thread key) {
                        return (T) DetachedThreadLocal.this.initialValue(key);
                    }
                };
                return;
            default:
                throw new AssertionError();
        }
    }

    public T get() {
        return this.map.get(Thread.currentThread());
    }

    public void set(T value) {
        this.map.put(Thread.currentThread(), value);
    }

    public void clear() {
        this.map.remove((WeakConcurrentMap<Thread, T>) Thread.currentThread());
    }

    public void clearAll() {
        this.map.clear();
    }

    public T pushTo(Thread thread) {
        T value = get();
        if (value != null) {
            this.map.put(thread, inheritValue(value));
        }
        return value;
    }

    public T fetchFrom(Thread thread) {
        T value = this.map.get(thread);
        if (value != null) {
            set(inheritValue(value));
        }
        return value;
    }

    public T get(Thread thread) {
        return this.map.get(thread);
    }

    public void define(Thread thread, T value) {
        this.map.put(thread, value);
    }

    protected T initialValue(Thread thread) {
        return null;
    }

    protected T inheritValue(T value) {
        return value;
    }

    public WeakConcurrentMap<Thread, T> getBackingMap() {
        return this.map;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.map.run();
    }
}
