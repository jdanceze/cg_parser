package fj;

import fj.data.Validation;
import fj.function.TryEffect4;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/TryEffect$$Lambda$5.class */
final /* synthetic */ class TryEffect$$Lambda$5 implements F4 {
    private final TryEffect4 arg$1;

    private TryEffect$$Lambda$5(TryEffect4 tryEffect4) {
        this.arg$1 = tryEffect4;
    }

    private static F4 get$Lambda(TryEffect4 tryEffect4) {
        return new TryEffect$$Lambda$5(tryEffect4);
    }

    @Override // fj.F4
    public Object f(Object obj, Object obj2, Object obj3, Object obj4) {
        Validation lambda$f$83;
        lambda$f$83 = TryEffect.lambda$f$83(this.arg$1, obj, obj2, obj3, obj4);
        return lambda$f$83;
    }

    public static F4 lambdaFactory$(TryEffect4 tryEffect4) {
        return new TryEffect$$Lambda$5(tryEffect4);
    }
}
