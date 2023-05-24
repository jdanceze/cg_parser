package fj.data;

import fj.P1;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$7.class */
final /* synthetic */ class IOFunctions$$Lambda$7 implements SafeIO {
    private final P1 arg$1;

    private IOFunctions$$Lambda$7(P1 p1) {
        this.arg$1 = p1;
    }

    private static SafeIO get$Lambda(P1 p1) {
        return new IOFunctions$$Lambda$7(p1);
    }

    @Override // fj.data.SafeIO, fj.data.IO
    public Object run() {
        Object _1;
        _1 = this.arg$1._1();
        return _1;
    }

    public static SafeIO lambdaFactory$(P1 p1) {
        return new IOFunctions$$Lambda$7(p1);
    }
}
