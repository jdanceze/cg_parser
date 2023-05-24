package fj.data;

import fj.F;
import fj.function.Effect1;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$$Lambda$2.class */
final /* synthetic */ class Conversions$$Lambda$2 implements F {
    private static final Conversions$$Lambda$2 instance = new Conversions$$Lambda$2();

    private Conversions$$Lambda$2() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        F Effect1_F;
        Effect1_F = Conversions.Effect1_F((Effect1) obj);
        return Effect1_F;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
