package fj;

import fj.function.Effect7;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Effect$$Lambda$8.class */
final /* synthetic */ class Effect$$Lambda$8 implements F7 {
    private final Effect7 arg$1;

    private Effect$$Lambda$8(Effect7 effect7) {
        this.arg$1 = effect7;
    }

    private static F7 get$Lambda(Effect7 effect7) {
        return new Effect$$Lambda$8(effect7);
    }

    @Override // fj.F7
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7) {
        Unit lambda$f$59;
        lambda$f$59 = Effect.lambda$f$59(this.arg$1, obj, obj2, obj3, obj4, obj5, obj6, obj7);
        return lambda$f$59;
    }

    public static F7 lambdaFactory$(Effect7 effect7) {
        return new Effect$$Lambda$8(effect7);
    }
}
