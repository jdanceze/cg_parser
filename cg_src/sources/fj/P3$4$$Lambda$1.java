package fj;

import fj.P3;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P3$4$$Lambda$1.class */
final /* synthetic */ class P3$4$$Lambda$1 implements F {
    private final P3 arg$1;

    private P3$4$$Lambda$1(P3 p3) {
        this.arg$1 = p3;
    }

    private static F get$Lambda(P3 p3) {
        return new P3$4$$Lambda$1(p3);
    }

    @Override // fj.F
    public Object f(Object obj) {
        return P3.AnonymousClass4.access$lambda$0(this.arg$1, (Unit) obj);
    }

    public static F lambdaFactory$(P3 p3) {
        return new P3$4$$Lambda$1(p3);
    }
}
