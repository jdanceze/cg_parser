package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Validation$$Lambda$5.class */
final /* synthetic */ class Validation$$Lambda$5 implements F {
    private static final Validation$$Lambda$5 instance = new Validation$$Lambda$5();

    private Validation$$Lambda$5() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object success;
        success = ((Validation) obj).success();
        return success;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
