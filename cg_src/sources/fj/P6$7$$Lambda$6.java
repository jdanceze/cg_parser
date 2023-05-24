package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P6$7$$Lambda$6.class */
public final /* synthetic */ class P6$7$$Lambda$6 implements F {
    private final P6 arg$1;

    private P6$7$$Lambda$6(P6 p6) {
        this.arg$1 = p6;
    }

    private static F get$Lambda(P6 p6) {
        return new P6$7$$Lambda$6(p6);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object _6;
        Unit unit = (Unit) obj;
        _6 = this.arg$1._6();
        return _6;
    }

    public static F lambdaFactory$(P6 p6) {
        return new P6$7$$Lambda$6(p6);
    }
}
