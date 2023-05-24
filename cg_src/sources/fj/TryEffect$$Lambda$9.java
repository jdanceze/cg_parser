package fj;

import fj.data.Validation;
import fj.function.TryEffect8;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/TryEffect$$Lambda$9.class */
final /* synthetic */ class TryEffect$$Lambda$9 implements F8 {
    private final TryEffect8 arg$1;

    private TryEffect$$Lambda$9(TryEffect8 tryEffect8) {
        this.arg$1 = tryEffect8;
    }

    private static F8 get$Lambda(TryEffect8 tryEffect8) {
        return new TryEffect$$Lambda$9(tryEffect8);
    }

    @Override // fj.F8
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8) {
        Validation lambda$f$87;
        lambda$f$87 = TryEffect.lambda$f$87(this.arg$1, obj, obj2, obj3, obj4, obj5, obj6, obj7, obj8);
        return lambda$f$87;
    }

    public static F8 lambdaFactory$(TryEffect8 tryEffect8) {
        return new TryEffect$$Lambda$9(tryEffect8);
    }
}
