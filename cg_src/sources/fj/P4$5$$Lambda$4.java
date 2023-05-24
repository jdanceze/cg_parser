package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P4$5$$Lambda$4.class */
public final /* synthetic */ class P4$5$$Lambda$4 implements F {
    private final P4 arg$1;

    private P4$5$$Lambda$4(P4 p4) {
        this.arg$1 = p4;
    }

    private static F get$Lambda(P4 p4) {
        return new P4$5$$Lambda$4(p4);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object _4;
        Unit unit = (Unit) obj;
        _4 = this.arg$1._4();
        return _4;
    }

    public static F lambdaFactory$(P4 p4) {
        return new P4$5$$Lambda$4(p4);
    }
}
