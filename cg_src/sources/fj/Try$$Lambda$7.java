package fj;

import fj.data.Validation;
import fj.function.Try6;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Try$$Lambda$7.class */
final /* synthetic */ class Try$$Lambda$7 implements F6 {
    private final Try6 arg$1;

    private Try$$Lambda$7(Try6 try6) {
        this.arg$1 = try6;
    }

    private static F6 get$Lambda(Try6 try6) {
        return new Try$$Lambda$7(try6);
    }

    @Override // fj.F6
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        Validation lambda$f$76;
        lambda$f$76 = Try.lambda$f$76(this.arg$1, obj, obj2, obj3, obj4, obj5, obj6);
        return lambda$f$76;
    }

    public static F6 lambdaFactory$(Try6 try6) {
        return new Try$$Lambda$7(try6);
    }
}
