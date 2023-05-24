package fj;

import fj.data.Validation;
import fj.function.Try7;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Try$$Lambda$8.class */
final /* synthetic */ class Try$$Lambda$8 implements F7 {
    private final Try7 arg$1;

    private Try$$Lambda$8(Try7 try7) {
        this.arg$1 = try7;
    }

    private static F7 get$Lambda(Try7 try7) {
        return new Try$$Lambda$8(try7);
    }

    @Override // fj.F7
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7) {
        Validation lambda$f$77;
        lambda$f$77 = Try.lambda$f$77(this.arg$1, obj, obj2, obj3, obj4, obj5, obj6, obj7);
        return lambda$f$77;
    }

    public static F7 lambdaFactory$(Try7 try7) {
        return new Try$$Lambda$8(try7);
    }
}
