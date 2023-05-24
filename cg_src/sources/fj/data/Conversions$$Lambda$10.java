package fj.data;

import fj.F;
import fj.function.Try0;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$$Lambda$10.class */
final /* synthetic */ class Conversions$$Lambda$10 implements F {
    private static final Conversions$$Lambda$10 instance = new Conversions$$Lambda$10();

    private Conversions$$Lambda$10() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        SafeIO Try_SafeIO;
        Try_SafeIO = Conversions.Try_SafeIO((Try0) obj);
        return Try_SafeIO;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
