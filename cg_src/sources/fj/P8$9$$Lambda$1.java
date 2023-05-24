package fj;

import fj.P8;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P8$9$$Lambda$1.class */
final /* synthetic */ class P8$9$$Lambda$1 implements F {
    private final P8 arg$1;

    private P8$9$$Lambda$1(P8 p8) {
        this.arg$1 = p8;
    }

    private static F get$Lambda(P8 p8) {
        return new P8$9$$Lambda$1(p8);
    }

    @Override // fj.F
    public Object f(Object obj) {
        return P8.AnonymousClass9.access$lambda$0(this.arg$1, (Unit) obj);
    }

    public static F lambdaFactory$(P8 p8) {
        return new P8$9$$Lambda$1(p8);
    }
}
