package fj.data;

import fj.F;
import fj.Unit;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$29.class */
public final /* synthetic */ class IOFunctions$$Lambda$29 implements F {
    private final Stream arg$1;

    private IOFunctions$$Lambda$29(Stream stream) {
        this.arg$1 = stream;
    }

    private static F get$Lambda(Stream stream) {
        return new IOFunctions$$Lambda$29(stream);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Stream lambda$null$115;
        lambda$null$115 = IOFunctions.lambda$null$115(this.arg$1, (Unit) obj);
        return lambda$null$115;
    }

    public static F lambdaFactory$(Stream stream) {
        return new IOFunctions$$Lambda$29(stream);
    }
}
