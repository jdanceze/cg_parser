package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P7$8$$Lambda$4.class */
public final /* synthetic */ class P7$8$$Lambda$4 implements F {
    private final P7 arg$1;

    private P7$8$$Lambda$4(P7 p7) {
        this.arg$1 = p7;
    }

    private static F get$Lambda(P7 p7) {
        return new P7$8$$Lambda$4(p7);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object _4;
        Unit unit = (Unit) obj;
        _4 = this.arg$1._4();
        return _4;
    }

    public static F lambdaFactory$(P7 p7) {
        return new P7$8$$Lambda$4(p7);
    }
}
