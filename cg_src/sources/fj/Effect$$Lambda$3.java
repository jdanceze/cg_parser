package fj;

import fj.function.Effect2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Effect$$Lambda$3.class */
final /* synthetic */ class Effect$$Lambda$3 implements F2 {
    private final Effect2 arg$1;

    private Effect$$Lambda$3(Effect2 effect2) {
        this.arg$1 = effect2;
    }

    private static F2 get$Lambda(Effect2 effect2) {
        return new Effect$$Lambda$3(effect2);
    }

    @Override // fj.F2
    public Object f(Object obj, Object obj2) {
        Unit lambda$f$54;
        lambda$f$54 = Effect.lambda$f$54(this.arg$1, obj, obj2);
        return lambda$f$54;
    }

    public static F2 lambdaFactory$(Effect2 effect2) {
        return new Effect$$Lambda$3(effect2);
    }
}
