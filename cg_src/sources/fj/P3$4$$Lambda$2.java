package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P3$4$$Lambda$2.class */
public final /* synthetic */ class P3$4$$Lambda$2 implements F {
    private final P3 arg$1;

    private P3$4$$Lambda$2(P3 p3) {
        this.arg$1 = p3;
    }

    private static F get$Lambda(P3 p3) {
        return new P3$4$$Lambda$2(p3);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object _2;
        Unit unit = (Unit) obj;
        _2 = this.arg$1._2();
        return _2;
    }

    public static F lambdaFactory$(P3 p3) {
        return new P3$4$$Lambda$2(p3);
    }
}
