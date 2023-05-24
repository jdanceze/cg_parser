package fj;

import fj.data.Validation;
import fj.function.TryEffect7;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/TryEffect$$Lambda$8.class */
final /* synthetic */ class TryEffect$$Lambda$8 implements F7 {
    private final TryEffect7 arg$1;

    private TryEffect$$Lambda$8(TryEffect7 tryEffect7) {
        this.arg$1 = tryEffect7;
    }

    private static F7 get$Lambda(TryEffect7 tryEffect7) {
        return new TryEffect$$Lambda$8(tryEffect7);
    }

    @Override // fj.F7
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7) {
        Validation lambda$f$86;
        lambda$f$86 = TryEffect.lambda$f$86(this.arg$1, obj, obj2, obj3, obj4, obj5, obj6, obj7);
        return lambda$f$86;
    }

    public static F7 lambdaFactory$(TryEffect7 tryEffect7) {
        return new TryEffect$$Lambda$8(tryEffect7);
    }
}
