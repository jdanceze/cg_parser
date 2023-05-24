package fj;

import fj.data.Validation;
import fj.function.TryEffect0;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/TryEffect$$Lambda$1.class */
public final /* synthetic */ class TryEffect$$Lambda$1 implements F {
    private final TryEffect0 arg$1;

    private TryEffect$$Lambda$1(TryEffect0 tryEffect0) {
        this.arg$1 = tryEffect0;
    }

    private static F get$Lambda(TryEffect0 tryEffect0) {
        return new TryEffect$$Lambda$1(tryEffect0);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Validation lambda$f$79;
        lambda$f$79 = TryEffect.lambda$f$79(this.arg$1, (Unit) obj);
        return lambda$f$79;
    }

    public static F lambdaFactory$(TryEffect0 tryEffect0) {
        return new TryEffect$$Lambda$1(tryEffect0);
    }
}
