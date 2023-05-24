package gnu.trove.stack;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/stack/TFloatStack.class */
public interface TFloatStack {
    float getNoEntryValue();

    void push(float f);

    float pop();

    float peek();

    int size();

    void clear();

    float[] toArray();

    void toArray(float[] fArr);
}
