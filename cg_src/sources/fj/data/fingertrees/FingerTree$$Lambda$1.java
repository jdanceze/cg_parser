package fj.data.fingertrees;

import fj.F;
import fj.F2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/fingertrees/FingerTree$$Lambda$1.class */
final /* synthetic */ class FingerTree$$Lambda$1 implements F2 {
    private final F arg$1;

    private FingerTree$$Lambda$1(F f) {
        this.arg$1 = f;
    }

    private static F2 get$Lambda(F f) {
        return new FingerTree$$Lambda$1(f);
    }

    @Override // fj.F2
    public Object f(Object obj, Object obj2) {
        return FingerTree.access$lambda$0(this.arg$1, (FingerTree) obj, obj2);
    }

    public static F2 lambdaFactory$(F f) {
        return new FingerTree$$Lambda$1(f);
    }
}
