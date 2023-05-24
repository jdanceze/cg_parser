package fj.data;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$12.class */
final /* synthetic */ class IOFunctions$$Lambda$12 implements SafeIO {
    private final IO arg$1;

    private IOFunctions$$Lambda$12(IO io) {
        this.arg$1 = io;
    }

    private static SafeIO get$Lambda(IO io) {
        return new IOFunctions$$Lambda$12(io);
    }

    @Override // fj.data.SafeIO, fj.data.IO
    public Object run() {
        Validation lambda$toSafeIO$124;
        lambda$toSafeIO$124 = IOFunctions.lambda$toSafeIO$124(this.arg$1);
        return lambda$toSafeIO$124;
    }

    public static SafeIO lambdaFactory$(IO io) {
        return new IOFunctions$$Lambda$12(io);
    }
}
