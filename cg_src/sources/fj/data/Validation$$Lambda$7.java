package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Validation$$Lambda$7.class */
public final /* synthetic */ class Validation$$Lambda$7 implements F {
    private final Validation arg$1;

    private Validation$$Lambda$7(Validation validation) {
        this.arg$1 = validation;
    }

    private static F get$Lambda(Validation validation) {
        return new Validation$$Lambda$7(validation);
    }

    @Override // fj.F
    public Object f(Object obj) {
        List lambda$null$40;
        lambda$null$40 = Validation.lambda$null$40(this.arg$1, (List) obj);
        return lambda$null$40;
    }

    public static F lambdaFactory$(Validation validation) {
        return new Validation$$Lambda$7(validation);
    }
}
