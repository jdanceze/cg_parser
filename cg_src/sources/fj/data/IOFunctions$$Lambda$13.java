package fj.data;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$13.class */
final /* synthetic */ class IOFunctions$$Lambda$13 implements IO {
    private final IO arg$1;
    private final IO arg$2;

    private IOFunctions$$Lambda$13(IO io, IO io2) {
        this.arg$1 = io;
        this.arg$2 = io2;
    }

    private static IO get$Lambda(IO io, IO io2) {
        return new IOFunctions$$Lambda$13(io, io2);
    }

    @Override // fj.data.IO
    public Object run() {
        return IOFunctions.access$lambda$12(this.arg$1, this.arg$2);
    }

    public static IO lambdaFactory$(IO io, IO io2) {
        return new IOFunctions$$Lambda$13(io, io2);
    }
}
