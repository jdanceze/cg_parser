package gnu.trove.stack;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/stack/TIntStack.class */
public interface TIntStack {
    int getNoEntryValue();

    void push(int i);

    int pop();

    int peek();

    int size();

    void clear();

    int[] toArray();

    void toArray(int[] iArr);
}
