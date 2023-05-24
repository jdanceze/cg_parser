package gnu.trove.stack;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/stack/TDoubleStack.class */
public interface TDoubleStack {
    double getNoEntryValue();

    void push(double d);

    double pop();

    double peek();

    int size();

    void clear();

    double[] toArray();

    void toArray(double[] dArr);
}
