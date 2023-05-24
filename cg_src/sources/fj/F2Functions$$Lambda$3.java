package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/F2Functions$$Lambda$3.class */
final /* synthetic */ class F2Functions$$Lambda$3 implements F2 {
    private final F arg$1;
    private final F2 arg$2;

    private F2Functions$$Lambda$3(F f, F2 f2) {
        this.arg$1 = f;
        this.arg$2 = f2;
    }

    private static F2 get$Lambda(F f, F2 f2) {
        return new F2Functions$$Lambda$3(f, f2);
    }

    @Override // fj.F2
    public Object f(Object obj, Object obj2) {
        Object lambda$map$50;
        lambda$map$50 = F2Functions.lambda$map$50(this.arg$1, this.arg$2, obj, obj2);
        return lambda$map$50;
    }

    public static F2 lambdaFactory$(F f, F2 f2) {
        return new F2Functions$$Lambda$3(f, f2);
    }
}
