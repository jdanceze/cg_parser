package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P5$6$$Lambda$4.class */
public final /* synthetic */ class P5$6$$Lambda$4 implements F {
    private final P5 arg$1;

    private P5$6$$Lambda$4(P5 p5) {
        this.arg$1 = p5;
    }

    private static F get$Lambda(P5 p5) {
        return new P5$6$$Lambda$4(p5);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object _4;
        Unit unit = (Unit) obj;
        _4 = this.arg$1._4();
        return _4;
    }

    public static F lambdaFactory$(P5 p5) {
        return new P5$6$$Lambda$4(p5);
    }
}
