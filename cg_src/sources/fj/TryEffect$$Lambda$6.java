package fj;

import fj.data.Validation;
import fj.function.TryEffect5;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/TryEffect$$Lambda$6.class */
final /* synthetic */ class TryEffect$$Lambda$6 implements F5 {
    private final TryEffect5 arg$1;

    private TryEffect$$Lambda$6(TryEffect5 tryEffect5) {
        this.arg$1 = tryEffect5;
    }

    private static F5 get$Lambda(TryEffect5 tryEffect5) {
        return new TryEffect$$Lambda$6(tryEffect5);
    }

    @Override // fj.F5
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        Validation lambda$f$84;
        lambda$f$84 = TryEffect.lambda$f$84(this.arg$1, obj, obj2, obj3, obj4, obj5);
        return lambda$f$84;
    }

    public static F5 lambdaFactory$(TryEffect5 tryEffect5) {
        return new TryEffect$$Lambda$6(tryEffect5);
    }
}
