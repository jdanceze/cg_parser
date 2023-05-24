package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$30.class */
public final /* synthetic */ class IOFunctions$$Lambda$30 implements F {
    private final IO arg$1;

    private IOFunctions$$Lambda$30(IO io) {
        this.arg$1 = io;
    }

    private static F get$Lambda(IO io) {
        return new IOFunctions$$Lambda$30(io);
    }

    @Override // fj.F
    public Object f(Object obj) {
        IO lambda$null$113;
        lambda$null$113 = IOFunctions.lambda$null$113(this.arg$1, (List) obj);
        return lambda$null$113;
    }

    public static F lambdaFactory$(IO io) {
        return new IOFunctions$$Lambda$30(io);
    }
}
