package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P8$9$$Lambda$7.class */
public final /* synthetic */ class P8$9$$Lambda$7 implements F {
    private final P8 arg$1;

    private P8$9$$Lambda$7(P8 p8) {
        this.arg$1 = p8;
    }

    private static F get$Lambda(P8 p8) {
        return new P8$9$$Lambda$7(p8);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object _7;
        Unit unit = (Unit) obj;
        _7 = this.arg$1._7();
        return _7;
    }

    public static F lambdaFactory$(P8 p8) {
        return new P8$9$$Lambda$7(p8);
    }
}
