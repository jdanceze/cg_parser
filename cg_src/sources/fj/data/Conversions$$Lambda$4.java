package fj.data;

import fj.F;
import fj.function.Effect0;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$$Lambda$4.class */
final /* synthetic */ class Conversions$$Lambda$4 implements F {
    private static final Conversions$$Lambda$4 instance = new Conversions$$Lambda$4();

    private Conversions$$Lambda$4() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        IO Effect_IO;
        Effect_IO = Conversions.Effect_IO((Effect0) obj);
        return Effect_IO;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
