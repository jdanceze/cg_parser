package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/F4Functions$$Lambda$1.class */
final /* synthetic */ class F4Functions$$Lambda$1 implements F3 {
    private final F4 arg$1;
    private final Object arg$2;

    private F4Functions$$Lambda$1(F4 f4, Object obj) {
        this.arg$1 = f4;
        this.arg$2 = obj;
    }

    private static F3 get$Lambda(F4 f4, Object obj) {
        return new F4Functions$$Lambda$1(f4, obj);
    }

    @Override // fj.F3
    public Object f(Object obj, Object obj2, Object obj3) {
        Object f;
        f = this.arg$1.f(this.arg$2, obj, obj2, obj3);
        return f;
    }

    public static F3 lambdaFactory$(F4 f4, Object obj) {
        return new F4Functions$$Lambda$1(f4, obj);
    }
}
