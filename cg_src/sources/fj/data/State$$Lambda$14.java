package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/State$$Lambda$14.class */
public final /* synthetic */ class State$$Lambda$14 implements F {
    private final List arg$1;

    private State$$Lambda$14(List list) {
        this.arg$1 = list;
    }

    private static F get$Lambda(List list) {
        return new State$$Lambda$14(list);
    }

    @Override // fj.F
    public Object f(Object obj) {
        List snoc;
        snoc = this.arg$1.snoc(obj);
        return snoc;
    }

    public static F lambdaFactory$(List list) {
        return new State$$Lambda$14(list);
    }
}
