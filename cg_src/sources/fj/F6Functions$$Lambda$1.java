package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/F6Functions$$Lambda$1.class */
final /* synthetic */ class F6Functions$$Lambda$1 implements F5 {
    private final F6 arg$1;
    private final Object arg$2;

    private F6Functions$$Lambda$1(F6 f6, Object obj) {
        this.arg$1 = f6;
        this.arg$2 = obj;
    }

    private static F5 get$Lambda(F6 f6, Object obj) {
        return new F6Functions$$Lambda$1(f6, obj);
    }

    @Override // fj.F5
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        Object f;
        f = this.arg$1.f(this.arg$2, obj, obj2, obj3, obj4, obj5);
        return f;
    }

    public static F5 lambdaFactory$(F6 f6, Object obj) {
        return new F6Functions$$Lambda$1(f6, obj);
    }
}
