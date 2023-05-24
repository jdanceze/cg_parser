package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Validation$$Lambda$4.class */
final /* synthetic */ class Validation$$Lambda$4 implements F {
    private static final Validation$$Lambda$4 instance = new Validation$$Lambda$4();

    private Validation$$Lambda$4() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        Boolean lambda$successes$44;
        lambda$successes$44 = Validation.lambda$successes$44((Validation) obj);
        return lambda$successes$44;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
