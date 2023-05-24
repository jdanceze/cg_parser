package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$5.class */
final /* synthetic */ class IOFunctions$$Lambda$5 implements IO {
    private final F arg$1;

    private IOFunctions$$Lambda$5(F f) {
        this.arg$1 = f;
    }

    private static IO get$Lambda(F f) {
        return new IOFunctions$$Lambda$5(f);
    }

    @Override // fj.data.IO
    public Object run() {
        Object lambda$lazy$109;
        lambda$lazy$109 = IOFunctions.lambda$lazy$109(this.arg$1);
        return lambda$lazy$109;
    }

    public static IO lambdaFactory$(F f) {
        return new IOFunctions$$Lambda$5(f);
    }
}
