package fj.test;

import fj.F;
import fj.data.State;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$$Lambda$2.class */
final /* synthetic */ class Arbitrary$$Lambda$2 implements F {
    private static final Arbitrary$$Lambda$2 instance = new Arbitrary$$Lambda$2();

    private Arbitrary$$Lambda$2() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        State unit;
        unit = State.unit((F) obj);
        return unit;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
