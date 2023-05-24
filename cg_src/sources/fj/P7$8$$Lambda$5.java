package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P7$8$$Lambda$5.class */
public final /* synthetic */ class P7$8$$Lambda$5 implements F {
    private final P7 arg$1;

    private P7$8$$Lambda$5(P7 p7) {
        this.arg$1 = p7;
    }

    private static F get$Lambda(P7 p7) {
        return new P7$8$$Lambda$5(p7);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object _5;
        Unit unit = (Unit) obj;
        _5 = this.arg$1._5();
        return _5;
    }

    public static F lambdaFactory$(P7 p7) {
        return new P7$8$$Lambda$5(p7);
    }
}
