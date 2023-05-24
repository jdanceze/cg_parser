package fj.data;

import fj.F;
import fj.P2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/State$$Lambda$8.class */
final /* synthetic */ class State$$Lambda$8 implements F {
    private final State arg$1;

    private State$$Lambda$8(State state) {
        this.arg$1 = state;
    }

    private static F get$Lambda(State state) {
        return new State$$Lambda$8(state);
    }

    @Override // fj.F
    public Object f(Object obj) {
        P2 lambda$gets$144;
        lambda$gets$144 = this.arg$1.lambda$gets$144(obj);
        return lambda$gets$144;
    }

    public static F lambdaFactory$(State state) {
        return new State$$Lambda$8(state);
    }
}
