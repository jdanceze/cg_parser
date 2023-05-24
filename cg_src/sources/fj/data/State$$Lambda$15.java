package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/State$$Lambda$15.class */
public final /* synthetic */ class State$$Lambda$15 implements F {
    private final State arg$1;

    private State$$Lambda$15(State state) {
        this.arg$1 = state;
    }

    private static F get$Lambda(State state) {
        return new State$$Lambda$15(state);
    }

    @Override // fj.F
    public Object f(Object obj) {
        State lambda$null$148;
        lambda$null$148 = State.lambda$null$148(this.arg$1, (List) obj);
        return lambda$null$148;
    }

    public static F lambdaFactory$(State state) {
        return new State$$Lambda$15(state);
    }
}
