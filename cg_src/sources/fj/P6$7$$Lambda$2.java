package fj;

import fj.P6;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P6$7$$Lambda$2.class */
final /* synthetic */ class P6$7$$Lambda$2 implements F {
    private final P6 arg$1;

    private P6$7$$Lambda$2(P6 p6) {
        this.arg$1 = p6;
    }

    private static F get$Lambda(P6 p6) {
        return new P6$7$$Lambda$2(p6);
    }

    @Override // fj.F
    public Object f(Object obj) {
        return P6.AnonymousClass7.access$lambda$1(this.arg$1, (Unit) obj);
    }

    public static F lambdaFactory$(P6 p6) {
        return new P6$7$$Lambda$2(p6);
    }
}
