package fj.data;

import fj.F;
import fj.P2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/State$$Lambda$9.class */
final /* synthetic */ class State$$Lambda$9 implements F {
    private final Object arg$1;

    private State$$Lambda$9(Object obj) {
        this.arg$1 = obj;
    }

    private static F get$Lambda(Object obj) {
        return new State$$Lambda$9(obj);
    }

    @Override // fj.F
    public Object f(Object obj) {
        P2 lambda$put$145;
        lambda$put$145 = State.lambda$put$145(this.arg$1, obj);
        return lambda$put$145;
    }

    public static F lambdaFactory$(Object obj) {
        return new State$$Lambda$9(obj);
    }
}
