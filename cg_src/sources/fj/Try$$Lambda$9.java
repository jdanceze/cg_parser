package fj;

import fj.data.Validation;
import fj.function.Try8;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Try$$Lambda$9.class */
final /* synthetic */ class Try$$Lambda$9 implements F8 {
    private final Try8 arg$1;

    private Try$$Lambda$9(Try8 try8) {
        this.arg$1 = try8;
    }

    private static F8 get$Lambda(Try8 try8) {
        return new Try$$Lambda$9(try8);
    }

    @Override // fj.F8
    public Object f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8) {
        Validation lambda$f$78;
        lambda$f$78 = Try.lambda$f$78(this.arg$1, obj, obj2, obj3, obj4, obj5, obj6, obj7, obj8);
        return lambda$f$78;
    }

    public static F8 lambdaFactory$(Try8 try8) {
        return new Try$$Lambda$9(try8);
    }
}
