package fj.data;

import fj.F;
import fj.F2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/List$$Lambda$5.class */
public final /* synthetic */ class List$$Lambda$5 implements F {
    private final F2 arg$1;
    private final Object arg$2;

    private List$$Lambda$5(F2 f2, Object obj) {
        this.arg$1 = f2;
        this.arg$2 = obj;
    }

    private static F get$Lambda(F2 f2, Object obj) {
        return new List$$Lambda$5(f2, obj);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object f;
        f = this.arg$1.f(this.arg$2, obj);
        return f;
    }

    public static F lambdaFactory$(F2 f2, Object obj) {
        return new List$$Lambda$5(f2, obj);
    }
}
