package fj.data;

import fj.F;
import fj.F2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$16.class */
final /* synthetic */ class IOFunctions$$Lambda$16 implements F {
    private final IO arg$1;
    private final F2 arg$2;

    private IOFunctions$$Lambda$16(IO io, F2 f2) {
        this.arg$1 = io;
        this.arg$2 = f2;
    }

    private static F get$Lambda(IO io, F2 f2) {
        return new IOFunctions$$Lambda$16(io, f2);
    }

    @Override // fj.F
    public Object f(Object obj) {
        IO lambda$liftM2$130;
        lambda$liftM2$130 = IOFunctions.lambda$liftM2$130(this.arg$1, this.arg$2, obj);
        return lambda$liftM2$130;
    }

    public static F lambdaFactory$(IO io, F2 f2) {
        return new IOFunctions$$Lambda$16(io, f2);
    }
}
