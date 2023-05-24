package fj.data;

import fj.F;
import fj.F2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$10.class */
final /* synthetic */ class IOFunctions$$Lambda$10 implements F2 {
    private final F arg$1;

    private IOFunctions$$Lambda$10(F f) {
        this.arg$1 = f;
    }

    private static F2 get$Lambda(F f) {
        return new IOFunctions$$Lambda$10(f);
    }

    @Override // fj.F2
    public Object f(Object obj, Object obj2) {
        IO lambda$traverse$121;
        lambda$traverse$121 = IOFunctions.lambda$traverse$121(this.arg$1, obj, (IO) obj2);
        return lambda$traverse$121;
    }

    public static F2 lambdaFactory$(F f) {
        return new IOFunctions$$Lambda$10(f);
    }
}
