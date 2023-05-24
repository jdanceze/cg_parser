package fj;

import fj.function.Effect6;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Effect$$Lambda$7.class */
final /* synthetic */ class Effect$$Lambda$7 implements F6 {
    private final Effect6 arg$1;

    private Effect$$Lambda$7(Effect6 effect6) {
        this.arg$1 = effect6;
    }

    private static F6 get$Lambda(Effect6 effect6) {
        return new Effect$$Lambda$7(effect6);
    }

    @Override // fj.F6
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        Unit lambda$f$58;
        lambda$f$58 = Effect.lambda$f$58(this.arg$1, obj, obj2, obj3, obj4, obj5, obj6);
        return lambda$f$58;
    }

    public static F6 lambdaFactory$(Effect6 effect6) {
        return new Effect$$Lambda$7(effect6);
    }
}
