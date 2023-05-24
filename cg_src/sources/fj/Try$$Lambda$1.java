package fj;

import fj.data.Validation;
import fj.function.Try0;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Try$$Lambda$1.class */
public final /* synthetic */ class Try$$Lambda$1 implements F {
    private final Try0 arg$1;

    private Try$$Lambda$1(Try0 try0) {
        this.arg$1 = try0;
    }

    private static F get$Lambda(Try0 try0) {
        return new Try$$Lambda$1(try0);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Validation lambda$f$70;
        lambda$f$70 = Try.lambda$f$70(this.arg$1, (Unit) obj);
        return lambda$f$70;
    }

    public static F lambdaFactory$(Try0 try0) {
        return new Try$$Lambda$1(try0);
    }
}
