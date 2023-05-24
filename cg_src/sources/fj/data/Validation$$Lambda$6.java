package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Validation$$Lambda$6.class */
public final /* synthetic */ class Validation$$Lambda$6 implements F {
    private final Validation arg$1;

    private Validation$$Lambda$6(Validation validation) {
        this.arg$1 = validation;
    }

    private static F get$Lambda(Validation validation) {
        return new Validation$$Lambda$6(validation);
    }

    @Override // fj.F
    public Object f(Object obj) {
        List lambda$null$39;
        lambda$null$39 = Validation.lambda$null$39(this.arg$1, (List) obj);
        return lambda$null$39;
    }

    public static F lambdaFactory$(Validation validation) {
        return new Validation$$Lambda$6(validation);
    }
}
