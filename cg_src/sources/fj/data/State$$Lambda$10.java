package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/State$$Lambda$10.class */
final /* synthetic */ class State$$Lambda$10 implements F {
    private final F arg$1;

    private State$$Lambda$10(F f) {
        this.arg$1 = f;
    }

    private static F get$Lambda(F f) {
        return new State$$Lambda$10(f);
    }

    @Override // fj.F
    public Object f(Object obj) {
        return State.access$lambda$9(this.arg$1, obj);
    }

    public static F lambdaFactory$(F f) {
        return new State$$Lambda$10(f);
    }
}
