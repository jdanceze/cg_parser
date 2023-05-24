package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/State$$Lambda$13.class */
public final /* synthetic */ class State$$Lambda$13 implements F {
    private final F arg$1;
    private final Object arg$2;

    private State$$Lambda$13(F f, Object obj) {
        this.arg$1 = f;
        this.arg$2 = obj;
    }

    private static F get$Lambda(F f, Object obj) {
        return new State$$Lambda$13(f, obj);
    }

    @Override // fj.F
    public Object f(Object obj) {
        State lambda$null$151;
        lambda$null$151 = State.lambda$null$151(this.arg$1, this.arg$2, (List) obj);
        return lambda$null$151;
    }

    public static F lambdaFactory$(F f, Object obj) {
        return new State$$Lambda$13(f, obj);
    }
}
