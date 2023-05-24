package fj;

import fj.data.Validation;
import fj.function.Try5;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Try$$Lambda$6.class */
final /* synthetic */ class Try$$Lambda$6 implements F5 {
    private final Try5 arg$1;

    private Try$$Lambda$6(Try5 try5) {
        this.arg$1 = try5;
    }

    private static F5 get$Lambda(Try5 try5) {
        return new Try$$Lambda$6(try5);
    }

    @Override // fj.F5
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        Validation lambda$f$75;
        lambda$f$75 = Try.lambda$f$75(this.arg$1, obj, obj2, obj3, obj4, obj5);
        return lambda$f$75;
    }

    public static F5 lambdaFactory$(Try5 try5) {
        return new Try$$Lambda$6(try5);
    }
}
