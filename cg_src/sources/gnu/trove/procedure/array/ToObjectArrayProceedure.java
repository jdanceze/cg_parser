package gnu.trove.procedure.array;

import gnu.trove.procedure.TObjectProcedure;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/procedure/array/ToObjectArrayProceedure.class */
public final class ToObjectArrayProceedure<T> implements TObjectProcedure<T> {
    private final T[] target;
    private int pos = 0;

    public ToObjectArrayProceedure(T[] target) {
        this.target = target;
    }

    @Override // gnu.trove.procedure.TObjectProcedure
    public final boolean execute(T value) {
        T[] tArr = this.target;
        int i = this.pos;
        this.pos = i + 1;
        tArr[i] = value;
        return true;
    }
}
