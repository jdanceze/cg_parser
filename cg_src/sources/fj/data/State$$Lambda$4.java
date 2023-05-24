package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/State$$Lambda$4.class */
final /* synthetic */ class State$$Lambda$4 implements F {
    private final F arg$1;

    private State$$Lambda$4(F f) {
        this.arg$1 = f;
    }

    private static F get$Lambda(F f) {
        return new State$$Lambda$4(f);
    }

    @Override // fj.F
    public Object f(Object obj) {
        State lambda$modify$140;
        lambda$modify$140 = State.lambda$modify$140(this.arg$1, obj);
        return lambda$modify$140;
    }

    public static F lambdaFactory$(F f) {
        return new State$$Lambda$4(f);
    }
}
