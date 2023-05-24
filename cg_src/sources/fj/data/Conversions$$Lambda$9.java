package fj.data;

import fj.F;
import fj.Unit;
import fj.function.Try0;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$$Lambda$9.class */
public final /* synthetic */ class Conversions$$Lambda$9 implements F {
    private final Try0 arg$1;

    private Conversions$$Lambda$9(Try0 try0) {
        this.arg$1 = try0;
    }

    private static F get$Lambda(Try0 try0) {
        return new Conversions$$Lambda$9(try0);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Validation lambda$Try_SafeIO$100;
        lambda$Try_SafeIO$100 = Conversions.lambda$Try_SafeIO$100(this.arg$1, (Unit) obj);
        return lambda$Try_SafeIO$100;
    }

    public static F lambdaFactory$(Try0 try0) {
        return new Conversions$$Lambda$9(try0);
    }
}
