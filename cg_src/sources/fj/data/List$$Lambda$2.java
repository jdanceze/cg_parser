package fj.data;

import fj.F;
import fj.F2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/List$$Lambda$2.class */
public final /* synthetic */ class List$$Lambda$2 implements F {
    private final F arg$1;
    private final F arg$2;
    private final F2 arg$3;
    private final Object arg$4;

    private List$$Lambda$2(F f, F f2, F2 f22, Object obj) {
        this.arg$1 = f;
        this.arg$2 = f2;
        this.arg$3 = f22;
        this.arg$4 = obj;
    }

    private static F get$Lambda(F f, F f2, F2 f22, Object obj) {
        return new List$$Lambda$2(f, f2, f22, obj);
    }

    @Override // fj.F
    public Object f(Object obj) {
        F lambdaFactory$;
        lambdaFactory$ = List$$Lambda$4.lambdaFactory$(this.arg$1, this.arg$2, (TreeMap) obj, this.arg$3, this.arg$4);
        return lambdaFactory$;
    }

    public static F lambdaFactory$(F f, F f2, F2 f22, Object obj) {
        return new List$$Lambda$2(f, f2, f22, obj);
    }
}
