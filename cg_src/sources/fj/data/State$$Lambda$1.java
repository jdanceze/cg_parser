package fj.data;

import fj.F;
import fj.P2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/State$$Lambda$1.class */
final /* synthetic */ class State$$Lambda$1 implements F {
    private final F arg$1;

    private State$$Lambda$1(F f) {
        this.arg$1 = f;
    }

    private static F get$Lambda(F f) {
        return new State$$Lambda$1(f);
    }

    @Override // fj.F
    public Object f(Object obj) {
        P2 lambda$units$136;
        lambda$units$136 = State.lambda$units$136(this.arg$1, obj);
        return lambda$units$136;
    }

    public static F lambdaFactory$(F f) {
        return new State$$Lambda$1(f);
    }
}
