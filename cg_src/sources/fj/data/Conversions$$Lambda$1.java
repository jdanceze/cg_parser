package fj.data;

import fj.F;
import fj.P1;
import fj.function.Effect0;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$$Lambda$1.class */
final /* synthetic */ class Conversions$$Lambda$1 implements F {
    private static final Conversions$$Lambda$1 instance = new Conversions$$Lambda$1();

    private Conversions$$Lambda$1() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        P1 Effect0_P1;
        Effect0_P1 = Conversions.Effect0_P1((Effect0) obj);
        return Effect0_P1;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
