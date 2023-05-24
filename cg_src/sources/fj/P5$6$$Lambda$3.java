package fj;

import fj.P5;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P5$6$$Lambda$3.class */
final /* synthetic */ class P5$6$$Lambda$3 implements F {
    private final P5 arg$1;

    private P5$6$$Lambda$3(P5 p5) {
        this.arg$1 = p5;
    }

    private static F get$Lambda(P5 p5) {
        return new P5$6$$Lambda$3(p5);
    }

    @Override // fj.F
    public Object f(Object obj) {
        return P5.AnonymousClass6.access$lambda$2(this.arg$1, (Unit) obj);
    }

    public static F lambdaFactory$(P5 p5) {
        return new P5$6$$Lambda$3(p5);
    }
}
