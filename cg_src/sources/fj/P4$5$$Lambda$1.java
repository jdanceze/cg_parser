package fj;

import fj.P4;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P4$5$$Lambda$1.class */
final /* synthetic */ class P4$5$$Lambda$1 implements F {
    private final P4 arg$1;

    private P4$5$$Lambda$1(P4 p4) {
        this.arg$1 = p4;
    }

    private static F get$Lambda(P4 p4) {
        return new P4$5$$Lambda$1(p4);
    }

    @Override // fj.F
    public Object f(Object obj) {
        return P4.AnonymousClass5.access$lambda$0(this.arg$1, (Unit) obj);
    }

    public static F lambdaFactory$(P4 p4) {
        return new P4$5$$Lambda$1(p4);
    }
}
