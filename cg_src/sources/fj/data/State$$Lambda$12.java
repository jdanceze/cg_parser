package fj.data;

import fj.F;
import fj.F2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/State$$Lambda$12.class */
final /* synthetic */ class State$$Lambda$12 implements F2 {
    private final F arg$1;

    private State$$Lambda$12(F f) {
        this.arg$1 = f;
    }

    private static F2 get$Lambda(F f) {
        return new State$$Lambda$12(f);
    }

    @Override // fj.F2
    public Object f(Object obj, Object obj2) {
        State lambda$traverse$152;
        lambda$traverse$152 = State.lambda$traverse$152(this.arg$1, (State) obj, obj2);
        return lambda$traverse$152;
    }

    public static F2 lambdaFactory$(F f) {
        return new State$$Lambda$12(f);
    }
}
