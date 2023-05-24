package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$26.class */
public final /* synthetic */ class IOFunctions$$Lambda$26 implements F {
    private final List arg$1;

    private IOFunctions$$Lambda$26(List list) {
        this.arg$1 = list;
    }

    private static F get$Lambda(List list) {
        return new IOFunctions$$Lambda$26(list);
    }

    @Override // fj.F
    public Object f(Object obj) {
        List lambda$null$119;
        lambda$null$119 = IOFunctions.lambda$null$119(this.arg$1, obj);
        return lambda$null$119;
    }

    public static F lambdaFactory$(List list) {
        return new IOFunctions$$Lambda$26(list);
    }
}
