package fj;

import fj.function.Try3;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Try$$Lambda$4.class */
final /* synthetic */ class Try$$Lambda$4 implements F3 {
    private final Try3 arg$1;

    private Try$$Lambda$4(Try3 try3) {
        this.arg$1 = try3;
    }

    private static F3 get$Lambda(Try3 try3) {
        return new Try$$Lambda$4(try3);
    }

    @Override // fj.F3
    public Object f(Object obj, Object obj2, Object obj3) {
        return Try.access$lambda$3(this.arg$1, obj, obj2, obj3);
    }

    public static F3 lambdaFactory$(Try3 try3) {
        return new Try$$Lambda$4(try3);
    }
}
