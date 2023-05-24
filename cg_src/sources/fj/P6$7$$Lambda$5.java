package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P6$7$$Lambda$5.class */
public final /* synthetic */ class P6$7$$Lambda$5 implements F {
    private final P6 arg$1;

    private P6$7$$Lambda$5(P6 p6) {
        this.arg$1 = p6;
    }

    private static F get$Lambda(P6 p6) {
        return new P6$7$$Lambda$5(p6);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object _5;
        Unit unit = (Unit) obj;
        _5 = this.arg$1._5();
        return _5;
    }

    public static F lambdaFactory$(P6 p6) {
        return new P6$7$$Lambda$5(p6);
    }
}
