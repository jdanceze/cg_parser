package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/F3Functions$$Lambda$1.class */
final /* synthetic */ class F3Functions$$Lambda$1 implements F2 {
    private final F3 arg$1;
    private final Object arg$2;

    private F3Functions$$Lambda$1(F3 f3, Object obj) {
        this.arg$1 = f3;
        this.arg$2 = obj;
    }

    private static F2 get$Lambda(F3 f3, Object obj) {
        return new F3Functions$$Lambda$1(f3, obj);
    }

    @Override // fj.F2
    public Object f(Object obj, Object obj2) {
        return F3Functions.lambda$f$62(this.arg$1, this.arg$2, obj, obj2);
    }

    public static F2 lambdaFactory$(F3 f3, Object obj) {
        return new F3Functions$$Lambda$1(f3, obj);
    }
}
