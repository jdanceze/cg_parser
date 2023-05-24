package gnu.trove.stack;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/stack/TLongStack.class */
public interface TLongStack {
    long getNoEntryValue();

    void push(long j);

    long pop();

    long peek();

    int size();

    void clear();

    long[] toArray();

    void toArray(long[] jArr);
}
