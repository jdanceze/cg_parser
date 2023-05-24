package fj;

import fj.data.Validation;
import fj.function.TryEffect6;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/TryEffect$$Lambda$7.class */
final /* synthetic */ class TryEffect$$Lambda$7 implements F6 {
    private final TryEffect6 arg$1;

    private TryEffect$$Lambda$7(TryEffect6 tryEffect6) {
        this.arg$1 = tryEffect6;
    }

    private static F6 get$Lambda(TryEffect6 tryEffect6) {
        return new TryEffect$$Lambda$7(tryEffect6);
    }

    @Override // fj.F6
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        Validation lambda$f$85;
        lambda$f$85 = TryEffect.lambda$f$85(this.arg$1, obj, obj2, obj3, obj4, obj5, obj6);
        return lambda$f$85;
    }

    public static F6 lambdaFactory$(TryEffect6 tryEffect6) {
        return new TryEffect$$Lambda$7(tryEffect6);
    }
}
