package fj;

import fj.function.TryEffect3;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/TryEffect$$Lambda$4.class */
final /* synthetic */ class TryEffect$$Lambda$4 implements F3 {
    private final TryEffect3 arg$1;

    private TryEffect$$Lambda$4(TryEffect3 tryEffect3) {
        this.arg$1 = tryEffect3;
    }

    private static F3 get$Lambda(TryEffect3 tryEffect3) {
        return new TryEffect$$Lambda$4(tryEffect3);
    }

    @Override // fj.F3
    public Object f(Object obj, Object obj2, Object obj3) {
        return TryEffect.lambda$f$82(this.arg$1, obj, obj2, obj3);
    }

    public static F3 lambdaFactory$(TryEffect3 tryEffect3) {
        return new TryEffect$$Lambda$4(tryEffect3);
    }
}
