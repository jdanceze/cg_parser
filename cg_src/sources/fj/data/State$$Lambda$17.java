package fj.data;

import fj.F;
import fj.P2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/State$$Lambda$17.class */
public final /* synthetic */ class State$$Lambda$17 implements F {
    private final F arg$1;
    private final Object arg$2;

    private State$$Lambda$17(F f, Object obj) {
        this.arg$1 = f;
        this.arg$2 = obj;
    }

    private static F get$Lambda(F f, Object obj) {
        return new State$$Lambda$17(f, obj);
    }

    @Override // fj.F
    public Object f(Object obj) {
        P2 lambda$null$139;
        lambda$null$139 = State.lambda$null$139(this.arg$1, this.arg$2, obj);
        return lambda$null$139;
    }

    public static F lambdaFactory$(F f, Object obj) {
        return new State$$Lambda$17(f, obj);
    }
}
