package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$15.class */
final /* synthetic */ class IOFunctions$$Lambda$15 implements F {
    private final IO arg$1;

    private IOFunctions$$Lambda$15(IO io) {
        this.arg$1 = io;
    }

    private static F get$Lambda(IO io) {
        return new IOFunctions$$Lambda$15(io);
    }

    @Override // fj.F
    public Object f(Object obj) {
        IO lambda$apply$128;
        lambda$apply$128 = IOFunctions.lambda$apply$128(this.arg$1, (F) obj);
        return lambda$apply$128;
    }

    public static F lambdaFactory$(IO io) {
        return new IOFunctions$$Lambda$15(io);
    }
}
