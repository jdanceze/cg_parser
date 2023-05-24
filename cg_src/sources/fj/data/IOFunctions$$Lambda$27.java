package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$27.class */
public final /* synthetic */ class IOFunctions$$Lambda$27 implements F {
    private final IO arg$1;

    private IOFunctions$$Lambda$27(IO io) {
        this.arg$1 = io;
    }

    private static F get$Lambda(IO io) {
        return new IOFunctions$$Lambda$27(io);
    }

    @Override // fj.F
    public Object f(Object obj) {
        IO lambda$null$117;
        lambda$null$117 = IOFunctions.lambda$null$117(this.arg$1, (Stream) obj);
        return lambda$null$117;
    }

    public static F lambdaFactory$(IO io) {
        return new IOFunctions$$Lambda$27(io);
    }
}
