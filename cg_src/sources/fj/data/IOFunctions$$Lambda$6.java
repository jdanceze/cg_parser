package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$6.class */
final /* synthetic */ class IOFunctions$$Lambda$6 implements SafeIO {
    private final F arg$1;

    private IOFunctions$$Lambda$6(F f) {
        this.arg$1 = f;
    }

    private static SafeIO get$Lambda(F f) {
        return new IOFunctions$$Lambda$6(f);
    }

    @Override // fj.data.SafeIO, fj.data.IO
    public Object run() {
        Object lambda$lazySafe$110;
        lambda$lazySafe$110 = IOFunctions.lambda$lazySafe$110(this.arg$1);
        return lambda$lazySafe$110;
    }

    public static SafeIO lambdaFactory$(F f) {
        return new IOFunctions$$Lambda$6(f);
    }
}
