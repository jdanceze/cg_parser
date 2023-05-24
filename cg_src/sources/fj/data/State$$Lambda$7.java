package fj.data;

import fj.F;
import fj.P;
import fj.P2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/State$$Lambda$7.class */
public final /* synthetic */ class State$$Lambda$7 implements F {
    private static final State$$Lambda$7 instance = new State$$Lambda$7();

    private State$$Lambda$7() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        P2 p;
        p = P.p(obj, obj);
        return p;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
