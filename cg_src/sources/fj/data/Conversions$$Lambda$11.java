package fj.data;

import fj.function.Try0;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$$Lambda$11.class */
public final /* synthetic */ class Conversions$$Lambda$11 implements IO {
    private final Try0 arg$1;

    private Conversions$$Lambda$11(Try0 try0) {
        this.arg$1 = try0;
    }

    private static IO get$Lambda(Try0 try0) {
        return new Conversions$$Lambda$11(try0);
    }

    @Override // fj.data.IO
    public Object run() {
        Object f;
        f = this.arg$1.f();
        return f;
    }

    public static IO lambdaFactory$(Try0 try0) {
        return new Conversions$$Lambda$11(try0);
    }
}
