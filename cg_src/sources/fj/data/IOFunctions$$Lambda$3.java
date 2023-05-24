package fj.data;

import fj.function.Try0;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$3.class */
public final /* synthetic */ class IOFunctions$$Lambda$3 implements IO {
    private final Try0 arg$1;

    private IOFunctions$$Lambda$3(Try0 try0) {
        this.arg$1 = try0;
    }

    private static IO get$Lambda(Try0 try0) {
        return new IOFunctions$$Lambda$3(try0);
    }

    @Override // fj.data.IO
    public Object run() {
        Object f;
        f = this.arg$1.f();
        return f;
    }

    public static IO lambdaFactory$(Try0 try0) {
        return new IOFunctions$$Lambda$3(try0);
    }
}
