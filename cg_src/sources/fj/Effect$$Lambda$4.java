package fj;

import fj.function.Effect3;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Effect$$Lambda$4.class */
final /* synthetic */ class Effect$$Lambda$4 implements F3 {
    private final Effect3 arg$1;

    private Effect$$Lambda$4(Effect3 effect3) {
        this.arg$1 = effect3;
    }

    private static F3 get$Lambda(Effect3 effect3) {
        return new Effect$$Lambda$4(effect3);
    }

    @Override // fj.F3
    public Object f(Object obj, Object obj2, Object obj3) {
        Unit lambda$f$55;
        lambda$f$55 = Effect.lambda$f$55(this.arg$1, obj, obj2, obj3);
        return lambda$f$55;
    }

    public static F3 lambdaFactory$(Effect3 effect3) {
        return new Effect$$Lambda$4(effect3);
    }
}
