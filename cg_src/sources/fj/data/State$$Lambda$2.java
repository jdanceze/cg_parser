package fj.data;

import fj.F;
import fj.P;
import fj.P2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/State$$Lambda$2.class */
public final /* synthetic */ class State$$Lambda$2 implements F {
    private final Object arg$1;

    private State$$Lambda$2(Object obj) {
        this.arg$1 = obj;
    }

    private static F get$Lambda(Object obj) {
        return new State$$Lambda$2(obj);
    }

    @Override // fj.F
    public Object f(Object obj) {
        P2 p;
        p = P.p(obj, this.arg$1);
        return p;
    }

    public static F lambdaFactory$(Object obj) {
        return new State$$Lambda$2(obj);
    }
}
