package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Validation$$Lambda$3.class */
public final /* synthetic */ class Validation$$Lambda$3 implements F {
    private static final Validation$$Lambda$3 instance = new Validation$$Lambda$3();

    private Validation$$Lambda$3() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object fail;
        fail = ((Validation) obj).fail();
        return fail;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
