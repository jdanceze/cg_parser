package org.mockito.internal.matchers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.exceptions.Reporter;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/CapturingMatcher.class */
public class CapturingMatcher<T> implements ArgumentMatcher<T>, CapturesArguments, VarargMatcher, Serializable {
    private final List<Object> arguments = new ArrayList();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = this.lock.readLock();
    private final Lock writeLock = this.lock.writeLock();

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(Object argument) {
        return true;
    }

    public String toString() {
        return "<Capturing argument>";
    }

    public T getLastValue() {
        this.readLock.lock();
        try {
            if (this.arguments.isEmpty()) {
                throw Reporter.noArgumentValueWasCaptured();
            }
            return (T) this.arguments.get(this.arguments.size() - 1);
        } finally {
            this.readLock.unlock();
        }
    }

    public List<T> getAllValues() {
        this.readLock.lock();
        try {
            return new ArrayList(this.arguments);
        } finally {
            this.readLock.unlock();
        }
    }

    @Override // org.mockito.internal.matchers.CapturesArguments
    public void captureFrom(Object argument) {
        this.writeLock.lock();
        try {
            this.arguments.add(argument);
        } finally {
            this.writeLock.unlock();
        }
    }
}
