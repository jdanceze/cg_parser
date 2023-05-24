package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/F7Functions$$Lambda$1.class */
final /* synthetic */ class F7Functions$$Lambda$1 implements F6 {
    private final F7 arg$1;
    private final Object arg$2;

    private F7Functions$$Lambda$1(F7 f7, Object obj) {
        this.arg$1 = f7;
        this.arg$2 = obj;
    }

    private static F6 get$Lambda(F7 f7, Object obj) {
        return new F7Functions$$Lambda$1(f7, obj);
    }

    @Override // fj.F6
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        return F7Functions.lambda$f$66(this.arg$1, this.arg$2, obj, obj2, obj3, obj4, obj5, obj6);
    }

    public static F6 lambdaFactory$(F7 f7, Object obj) {
        return new F7Functions$$Lambda$1(f7, obj);
    }
}
