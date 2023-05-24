package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$$Lambda$8.class */
final /* synthetic */ class Conversions$$Lambda$8 implements F {
    private static final Conversions$$Lambda$8 instance = new Conversions$$Lambda$8();

    private Conversions$$Lambda$8() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        SafeIO F_SafeIO;
        F_SafeIO = Conversions.F_SafeIO((F) obj);
        return F_SafeIO;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
