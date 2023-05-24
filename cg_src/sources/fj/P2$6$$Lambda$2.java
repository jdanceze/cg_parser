package fj;

import fj.P2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P2$6$$Lambda$2.class */
final /* synthetic */ class P2$6$$Lambda$2 implements F {
    private final P2 arg$1;

    private P2$6$$Lambda$2(P2 p2) {
        this.arg$1 = p2;
    }

    private static F get$Lambda(P2 p2) {
        return new P2$6$$Lambda$2(p2);
    }

    @Override // fj.F
    public Object f(Object obj) {
        return P2.AnonymousClass6.access$lambda$1(this.arg$1, (Unit) obj);
    }

    public static F lambdaFactory$(P2 p2) {
        return new P2$6$$Lambda$2(p2);
    }
}
