package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/F1Functions$$Lambda$1.class */
final /* synthetic */ class F1Functions$$Lambda$1 implements F {
    private final F arg$1;
    private final Object arg$2;

    private F1Functions$$Lambda$1(F f, Object obj) {
        this.arg$1 = f;
        this.arg$2 = obj;
    }

    private static F get$Lambda(F f, Object obj) {
        return new F1Functions$$Lambda$1(f, obj);
    }

    @Override // fj.F
    public Object f(Object obj) {
        return F1Functions.access$lambda$0(this.arg$1, this.arg$2, (Unit) obj);
    }

    public static F lambdaFactory$(F f, Object obj) {
        return new F1Functions$$Lambda$1(f, obj);
    }
}
