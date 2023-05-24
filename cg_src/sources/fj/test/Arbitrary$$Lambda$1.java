package fj.test;

import fj.F;
import fj.data.Reader;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$$Lambda$1.class */
final /* synthetic */ class Arbitrary$$Lambda$1 implements F {
    private static final Arbitrary$$Lambda$1 instance = new Arbitrary$$Lambda$1();

    private Arbitrary$$Lambda$1() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        Reader unit;
        unit = Reader.unit((F) obj);
        return unit;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
