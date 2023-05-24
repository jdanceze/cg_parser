package gnu.trove.stack;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/stack/TShortStack.class */
public interface TShortStack {
    short getNoEntryValue();

    void push(short s);

    short pop();

    short peek();

    int size();

    void clear();

    short[] toArray();

    void toArray(short[] sArr);
}
