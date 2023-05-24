package fj.data;

import fj.F;
import fj.function.Try0;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$$Lambda$12.class */
final /* synthetic */ class Conversions$$Lambda$12 implements F {
    private static final Conversions$$Lambda$12 instance = new Conversions$$Lambda$12();

    private Conversions$$Lambda$12() {
    }

    @Override // fj.F
    public Object f(Object obj) {
        IO Try_IO;
        Try_IO = Conversions.Try_IO((Try0) obj);
        return Try_IO;
    }

    public static F lambdaFactory$() {
        return instance;
    }
}
