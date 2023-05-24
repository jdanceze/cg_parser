package fj;

import fj.function.Effect5;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Effect$$Lambda$6.class */
final /* synthetic */ class Effect$$Lambda$6 implements F5 {
    private final Effect5 arg$1;

    private Effect$$Lambda$6(Effect5 effect5) {
        this.arg$1 = effect5;
    }

    private static F5 get$Lambda(Effect5 effect5) {
        return new Effect$$Lambda$6(effect5);
    }

    @Override // fj.F5
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        Unit lambda$f$57;
        lambda$f$57 = Effect.lambda$f$57(this.arg$1, obj, obj2, obj3, obj4, obj5);
        return lambda$f$57;
    }

    public static F5 lambdaFactory$(Effect5 effect5) {
        return new Effect$$Lambda$6(effect5);
    }
}
