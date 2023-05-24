package gnu.trove.stack;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/stack/TCharStack.class */
public interface TCharStack {
    char getNoEntryValue();

    void push(char c);

    char pop();

    char peek();

    int size();

    void clear();

    char[] toArray();

    void toArray(char[] cArr);
}
