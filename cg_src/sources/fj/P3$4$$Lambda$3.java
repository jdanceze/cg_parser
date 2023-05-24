package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P3$4$$Lambda$3.class */
public final /* synthetic */ class P3$4$$Lambda$3 implements F {
    private final P3 arg$1;

    private P3$4$$Lambda$3(P3 p3) {
        this.arg$1 = p3;
    }

    private static F get$Lambda(P3 p3) {
        return new P3$4$$Lambda$3(p3);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object _3;
        Unit unit = (Unit) obj;
        _3 = this.arg$1._3();
        return _3;
    }

    public static F lambdaFactory$(P3 p3) {
        return new P3$4$$Lambda$3(p3);
    }
}
