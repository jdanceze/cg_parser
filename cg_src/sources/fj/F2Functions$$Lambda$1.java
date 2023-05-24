package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/F2Functions$$Lambda$1.class */
final /* synthetic */ class F2Functions$$Lambda$1 implements F2 {
    private final F2 arg$1;
    private final F arg$2;

    private F2Functions$$Lambda$1(F2 f2, F f) {
        this.arg$1 = f2;
        this.arg$2 = f;
    }

    private static F2 get$Lambda(F2 f2, F f) {
        return new F2Functions$$Lambda$1(f2, f);
    }

    @Override // fj.F2
    public Object f(Object obj, Object obj2) {
        return F2Functions.access$lambda$0(this.arg$1, this.arg$2, obj, obj2);
    }

    public static F2 lambdaFactory$(F2 f2, F f) {
        return new F2Functions$$Lambda$1(f2, f);
    }
}
