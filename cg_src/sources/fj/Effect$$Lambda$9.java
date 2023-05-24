package fj;

import fj.function.Effect8;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Effect$$Lambda$9.class */
final /* synthetic */ class Effect$$Lambda$9 implements F8 {
    private final Effect8 arg$1;

    private Effect$$Lambda$9(Effect8 effect8) {
        this.arg$1 = effect8;
    }

    private static F8 get$Lambda(Effect8 effect8) {
        return new Effect$$Lambda$9(effect8);
    }

    @Override // fj.F8
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8) {
        Unit lambda$f$60;
        lambda$f$60 = Effect.lambda$f$60(this.arg$1, obj, obj2, obj3, obj4, obj5, obj6, obj7, obj8);
        return lambda$f$60;
    }

    public static F8 lambdaFactory$(Effect8 effect8) {
        return new Effect$$Lambda$9(effect8);
    }
}
