package fj.data;

import fj.F;
import fj.function.Effect0;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$$Lambda$6.class */
final /* synthetic */ class Conversions$$Lambda$6 implements F {
    private static final Conversions$$Lambda$6 instance = new Conversions$$Lambda$6();

    private Conversions$$Lambda$6() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        SafeIO Effect_SafeIO;
        Effect_SafeIO = Conversions.Effect_SafeIO((Effect0) obj);
        return Effect_SafeIO;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
