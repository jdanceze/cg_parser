package fj;

import fj.P7;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P7$8$$Lambda$1.class */
final /* synthetic */ class P7$8$$Lambda$1 implements F {
    private final P7 arg$1;

    private P7$8$$Lambda$1(P7 p7) {
        this.arg$1 = p7;
    }

    private static F get$Lambda(P7 p7) {
        return new P7$8$$Lambda$1(p7);
    }

    @Override // fj.F
    public Object f(Object obj) {
        return P7.AnonymousClass8.access$lambda$0(this.arg$1, (Unit) obj);
    }

    public static F lambdaFactory$(P7 p7) {
        return new P7$8$$Lambda$1(p7);
    }
}
