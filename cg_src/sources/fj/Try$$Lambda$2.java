package fj;

import fj.data.Validation;
import fj.function.Try1;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Try$$Lambda$2.class */
public final /* synthetic */ class Try$$Lambda$2 implements F {
    private final Try1 arg$1;

    private Try$$Lambda$2(Try1 try1) {
        this.arg$1 = try1;
    }

    private static F get$Lambda(Try1 try1) {
        return new Try$$Lambda$2(try1);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Validation lambda$f$71;
        lambda$f$71 = Try.lambda$f$71(this.arg$1, obj);
        return lambda$f$71;
    }

    public static F lambdaFactory$(Try1 try1) {
        return new Try$$Lambda$2(try1);
    }
}
