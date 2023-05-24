package fj;

import fj.function.Try2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Try$$Lambda$3.class */
final /* synthetic */ class Try$$Lambda$3 implements F2 {
    private final Try2 arg$1;

    private Try$$Lambda$3(Try2 try2) {
        this.arg$1 = try2;
    }

    private static F2 get$Lambda(Try2 try2) {
        return new Try$$Lambda$3(try2);
    }

    @Override // fj.F2
    public Object f(Object obj, Object obj2) {
        return Try.access$lambda$2(this.arg$1, obj, obj2);
    }

    public static F2 lambdaFactory$(Try2 try2) {
        return new Try$$Lambda$3(try2);
    }
}
