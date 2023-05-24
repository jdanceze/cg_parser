package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/F5Functions$$Lambda$1.class */
final /* synthetic */ class F5Functions$$Lambda$1 implements F4 {
    private final F5 arg$1;
    private final Object arg$2;

    private F5Functions$$Lambda$1(F5 f5, Object obj) {
        this.arg$1 = f5;
        this.arg$2 = obj;
    }

    private static F4 get$Lambda(F5 f5, Object obj) {
        return new F5Functions$$Lambda$1(f5, obj);
    }

    @Override // fj.F4
    public Object f(Object obj, Object obj2, Object obj3, Object obj4) {
        Object f;
        f = this.arg$1.f(this.arg$2, obj, obj2, obj3, obj4);
        return f;
    }

    public static F4 lambdaFactory$(F5 f5, Object obj) {
        return new F5Functions$$Lambda$1(f5, obj);
    }
}
