package fj;

import fj.function.Try4;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Try$$Lambda$5.class */
final /* synthetic */ class Try$$Lambda$5 implements F4 {
    private final Try4 arg$1;

    private Try$$Lambda$5(Try4 try4) {
        this.arg$1 = try4;
    }

    private static F4 get$Lambda(Try4 try4) {
        return new Try$$Lambda$5(try4);
    }

    @Override // fj.F4
    public Object f(Object obj, Object obj2, Object obj3, Object obj4) {
        return Try.lambda$f$74(this.arg$1, obj, obj2, obj3, obj4);
    }

    public static F4 lambdaFactory$(Try4 try4) {
        return new Try$$Lambda$5(try4);
    }
}
