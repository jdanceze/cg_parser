package fj;

import fj.function.TryEffect1;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/TryEffect$$Lambda$2.class */
final /* synthetic */ class TryEffect$$Lambda$2 implements F {
    private final TryEffect1 arg$1;

    private TryEffect$$Lambda$2(TryEffect1 tryEffect1) {
        this.arg$1 = tryEffect1;
    }

    private static F get$Lambda(TryEffect1 tryEffect1) {
        return new TryEffect$$Lambda$2(tryEffect1);
    }

    @Override // fj.F
    public Object f(Object obj) {
        return TryEffect.access$lambda$1(this.arg$1, obj);
    }

    public static F lambdaFactory$(TryEffect1 tryEffect1) {
        return new TryEffect$$Lambda$2(tryEffect1);
    }
}
