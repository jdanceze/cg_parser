package soot.jimple.spark.ondemand.genericutil;

import java.util.Arrays;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/ImmutableStack.class */
public class ImmutableStack<T> {
    private static final ImmutableStack<Object> EMPTY;
    private static final int MAX_SIZE = Integer.MAX_VALUE;
    private final T[] entries;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ImmutableStack.class.desiredAssertionStatus();
        EMPTY = new ImmutableStack<>(new Object[0]);
    }

    public static int getMaxSize() {
        return Integer.MAX_VALUE;
    }

    public static final <T> ImmutableStack<T> emptyStack() {
        return (ImmutableStack<T>) EMPTY;
    }

    private ImmutableStack(T[] tArr) {
        this.entries = tArr;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && (o instanceof ImmutableStack)) {
            ImmutableStack other = (ImmutableStack) o;
            return Arrays.equals(this.entries, other.entries);
        }
        return false;
    }

    public int hashCode() {
        return Util.hashArray(this.entries);
    }

    public ImmutableStack<T> push(T entry) {
        Object[] tmpEntries;
        if ($assertionsDisabled || entry != null) {
            int size = this.entries.length + 1;
            if (size <= Integer.MAX_VALUE) {
                tmpEntries = new Object[size];
                System.arraycopy(this.entries, 0, tmpEntries, 0, this.entries.length);
                tmpEntries[size - 1] = entry;
            } else {
                tmpEntries = new Object[Integer.MAX_VALUE];
                System.arraycopy(this.entries, 1, tmpEntries, 0, this.entries.length - 1);
                tmpEntries[2147483646] = entry;
            }
            return new ImmutableStack<>(tmpEntries);
        }
        throw new AssertionError();
    }

    public T peek() {
        if ($assertionsDisabled || this.entries.length != 0) {
            return this.entries[this.entries.length - 1];
        }
        throw new AssertionError();
    }

    public ImmutableStack<T> pop() {
        if ($assertionsDisabled || this.entries.length != 0) {
            int size = this.entries.length - 1;
            Object[] tmpEntries = new Object[size];
            System.arraycopy(this.entries, 0, tmpEntries, 0, size);
            return new ImmutableStack<>(tmpEntries);
        }
        throw new AssertionError();
    }

    public boolean isEmpty() {
        return this.entries.length == 0;
    }

    public int size() {
        return this.entries.length;
    }

    public T get(int i) {
        return this.entries[i];
    }

    public String toString() {
        String objArrayToString = Util.objArrayToString(this.entries);
        if ($assertionsDisabled || this.entries.length <= Integer.MAX_VALUE) {
            return objArrayToString;
        }
        throw new AssertionError(objArrayToString);
    }

    public boolean contains(T entry) {
        return Util.arrayContains(this.entries, entry, this.entries.length);
    }

    public boolean topMatches(ImmutableStack<T> other) {
        if (other.size() > size()) {
            return false;
        }
        int i = other.size() - 1;
        int j = size() - 1;
        while (i >= 0) {
            if (other.get(i).equals(get(j))) {
                i--;
                j--;
            } else {
                return false;
            }
        }
        return true;
    }

    public ImmutableStack<T> reverse() {
        Object[] tmpEntries = new Object[this.entries.length];
        int i = this.entries.length - 1;
        int j = 0;
        while (i >= 0) {
            tmpEntries[j] = this.entries[i];
            i--;
            j++;
        }
        return new ImmutableStack<>(tmpEntries);
    }

    public ImmutableStack<T> popAll(ImmutableStack<T> other) {
        if ($assertionsDisabled || topMatches(other)) {
            int size = this.entries.length - other.entries.length;
            Object[] tmpEntries = new Object[size];
            System.arraycopy(this.entries, 0, tmpEntries, 0, size);
            return new ImmutableStack<>(tmpEntries);
        }
        throw new AssertionError();
    }

    public ImmutableStack<T> pushAll(ImmutableStack<T> other) {
        Object[] tmpEntries;
        int size = this.entries.length + other.entries.length;
        if (size <= Integer.MAX_VALUE) {
            tmpEntries = new Object[size];
            System.arraycopy(this.entries, 0, tmpEntries, 0, this.entries.length);
            System.arraycopy(other.entries, 0, tmpEntries, this.entries.length, other.entries.length);
        } else {
            tmpEntries = new Object[Integer.MAX_VALUE];
            int numFromThis = Integer.MAX_VALUE - other.entries.length;
            System.arraycopy(this.entries, this.entries.length - numFromThis, tmpEntries, 0, numFromThis);
            System.arraycopy(other.entries, 0, tmpEntries, numFromThis, other.entries.length);
        }
        return new ImmutableStack<>(tmpEntries);
    }
}
