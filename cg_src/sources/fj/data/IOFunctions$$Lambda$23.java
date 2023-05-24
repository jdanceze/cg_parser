package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$23.class */
public final /* synthetic */ class IOFunctions$$Lambda$23 implements F {
    private final F arg$1;

    private IOFunctions$$Lambda$23(F f) {
        this.arg$1 = f;
    }

    private static F get$Lambda(F f) {
        return new IOFunctions$$Lambda$23(f);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object f;
        f = this.arg$1.f(obj);
        return f;
    }

    public static F lambdaFactory$(F f) {
        return new IOFunctions$$Lambda$23(f);
    }
}
