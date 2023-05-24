package fj.data;

import fj.F;
import fj.function.Try1;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$$Lambda$13.class */
final /* synthetic */ class Conversions$$Lambda$13 implements F {
    private static final Conversions$$Lambda$13 instance = new Conversions$$Lambda$13();

    private Conversions$$Lambda$13() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        F Try_F;
        Try_F = Conversions.Try_F((Try1) obj);
        return Try_F;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
