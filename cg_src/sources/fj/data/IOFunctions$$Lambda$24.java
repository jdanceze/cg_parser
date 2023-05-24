package fj.data;

import fj.function.Try0;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$24.class */
public final /* synthetic */ class IOFunctions$$Lambda$24 implements Try0 {
    private final IO arg$1;

    private IOFunctions$$Lambda$24(IO io) {
        this.arg$1 = io;
    }

    private static Try0 get$Lambda(IO io) {
        return new IOFunctions$$Lambda$24(io);
    }

    @Override // fj.function.Try0
    public Object f() {
        Object run;
        run = this.arg$1.run();
        return run;
    }

    public static Try0 lambdaFactory$(IO io) {
        return new IOFunctions$$Lambda$24(io);
    }
}
