package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$28.class */
public final /* synthetic */ class IOFunctions$$Lambda$28 implements F {
    private final Stream arg$1;

    private IOFunctions$$Lambda$28(Stream stream) {
        this.arg$1 = stream;
    }

    private static F get$Lambda(Stream stream) {
        return new IOFunctions$$Lambda$28(stream);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Stream lambda$null$116;
        lambda$null$116 = IOFunctions.lambda$null$116(this.arg$1, obj);
        return lambda$null$116;
    }

    public static F lambdaFactory$(Stream stream) {
        return new IOFunctions$$Lambda$28(stream);
    }
}
