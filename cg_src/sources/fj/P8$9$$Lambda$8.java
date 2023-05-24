package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P8$9$$Lambda$8.class */
public final /* synthetic */ class P8$9$$Lambda$8 implements F {
    private final P8 arg$1;

    private P8$9$$Lambda$8(P8 p8) {
        this.arg$1 = p8;
    }

    private static F get$Lambda(P8 p8) {
        return new P8$9$$Lambda$8(p8);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object _8;
        Unit unit = (Unit) obj;
        _8 = this.arg$1._8();
        return _8;
    }

    public static F lambdaFactory$(P8 p8) {
        return new P8$9$$Lambda$8(p8);
    }
}
