package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/F8Functions$$Lambda$1.class */
final /* synthetic */ class F8Functions$$Lambda$1 implements F7 {
    private final F8 arg$1;
    private final Object arg$2;

    private F8Functions$$Lambda$1(F8 f8, Object obj) {
        this.arg$1 = f8;
        this.arg$2 = obj;
    }

    private static F7 get$Lambda(F8 f8, Object obj) {
        return new F8Functions$$Lambda$1(f8, obj);
    }

    @Override // fj.F7
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7) {
        Object f;
        f = this.arg$1.f(this.arg$2, obj, obj2, obj3, obj4, obj5, obj6, obj7);
        return f;
    }

    public static F7 lambdaFactory$(F8 f8, Object obj) {
        return new F8Functions$$Lambda$1(f8, obj);
    }
}
