package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$25.class */
public final /* synthetic */ class IOFunctions$$Lambda$25 implements F {
    private final F arg$1;
    private final Object arg$2;

    private IOFunctions$$Lambda$25(F f, Object obj) {
        this.arg$1 = f;
        this.arg$2 = obj;
    }

    private static F get$Lambda(F f, Object obj) {
        return new IOFunctions$$Lambda$25(f, obj);
    }

    @Override // fj.F
    public Object f(Object obj) {
        IO lambda$null$120;
        lambda$null$120 = IOFunctions.lambda$null$120(this.arg$1, this.arg$2, (List) obj);
        return lambda$null$120;
    }

    public static F lambdaFactory$(F f, Object obj) {
        return new IOFunctions$$Lambda$25(f, obj);
    }
}
