package fj;

import fj.function.TryEffect2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/TryEffect$$Lambda$3.class */
final /* synthetic */ class TryEffect$$Lambda$3 implements F2 {
    private final TryEffect2 arg$1;

    private TryEffect$$Lambda$3(TryEffect2 tryEffect2) {
        this.arg$1 = tryEffect2;
    }

    private static F2 get$Lambda(TryEffect2 tryEffect2) {
        return new TryEffect$$Lambda$3(tryEffect2);
    }

    @Override // fj.F2
    public Object f(Object obj, Object obj2) {
        return TryEffect.access$lambda$2(this.arg$1, obj, obj2);
    }

    public static F2 lambdaFactory$(TryEffect2 tryEffect2) {
        return new TryEffect$$Lambda$3(tryEffect2);
    }
}
