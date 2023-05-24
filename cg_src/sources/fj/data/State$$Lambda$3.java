package fj.data;

import fj.F;
import fj.P2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/State$$Lambda$3.class */
public final /* synthetic */ class State$$Lambda$3 implements F {
    private final State arg$1;
    private final F arg$2;

    private State$$Lambda$3(State state, F f) {
        this.arg$1 = state;
        this.arg$2 = f;
    }

    private static F get$Lambda(State state, F f) {
        return new State$$Lambda$3(state, f);
    }

    @Override // fj.F
    public Object f(Object obj) {
        P2 lambda$map$138;
        lambda$map$138 = this.arg$1.lambda$map$138(this.arg$2, obj);
        return lambda$map$138;
    }

    public static F lambdaFactory$(State state, F f) {
        return new State$$Lambda$3(state, f);
    }
}
