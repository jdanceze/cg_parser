package gnu.trove.stack;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/stack/TByteStack.class */
public interface TByteStack {
    byte getNoEntryValue();

    void push(byte b);

    byte pop();

    byte peek();

    int size();

    void clear();

    byte[] toArray();

    void toArray(byte[] bArr);
}
