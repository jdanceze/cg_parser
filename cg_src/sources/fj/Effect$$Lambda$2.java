package fj;

import fj.function.Effect1;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Effect$$Lambda$2.class */
public final /* synthetic */ class Effect$$Lambda$2 implements F {
    private final Effect1 arg$1;

    private Effect$$Lambda$2(Effect1 effect1) {
        this.arg$1 = effect1;
    }

    private static F get$Lambda(Effect1 effect1) {
        return new Effect$$Lambda$2(effect1);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Unit lambda$f$53;
        lambda$f$53 = Effect.lambda$f$53(this.arg$1, obj);
        return lambda$f$53;
    }

    public static F lambdaFactory$(Effect1 effect1) {
        return new Effect$$Lambda$2(effect1);
    }
}
