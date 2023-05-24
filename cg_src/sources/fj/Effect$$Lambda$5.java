package fj;

import fj.function.Effect4;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Effect$$Lambda$5.class */
final /* synthetic */ class Effect$$Lambda$5 implements F4 {
    private final Effect4 arg$1;

    private Effect$$Lambda$5(Effect4 effect4) {
        this.arg$1 = effect4;
    }

    private static F4 get$Lambda(Effect4 effect4) {
        return new Effect$$Lambda$5(effect4);
    }

    @Override // fj.F4
    public Object f(Object obj, Object obj2, Object obj3, Object obj4) {
        Unit lambda$f$56;
        lambda$f$56 = Effect.lambda$f$56(this.arg$1, obj, obj2, obj3, obj4);
        return lambda$f$56;
    }

    public static F4 lambdaFactory$(Effect4 effect4) {
        return new Effect$$Lambda$5(effect4);
    }
}
