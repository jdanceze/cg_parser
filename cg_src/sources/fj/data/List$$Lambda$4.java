package fj.data;

import fj.F;
import fj.F2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/List$$Lambda$4.class */
public final /* synthetic */ class List$$Lambda$4 implements F {
    private final F arg$1;
    private final F arg$2;
    private final TreeMap arg$3;
    private final F2 arg$4;
    private final Object arg$5;

    private List$$Lambda$4(F f, F f2, TreeMap treeMap, F2 f22, Object obj) {
        this.arg$1 = f;
        this.arg$2 = f2;
        this.arg$3 = treeMap;
        this.arg$4 = f22;
        this.arg$5 = obj;
    }

    private static F get$Lambda(F f, F f2, TreeMap treeMap, F2 f22, Object obj) {
        return new List$$Lambda$4(f, f2, treeMap, f22, obj);
    }

    @Override // fj.F
    public Object f(Object obj) {
        TreeMap lambda$null$36;
        lambda$null$36 = List.lambda$null$36(this.arg$1, this.arg$2, this.arg$3, this.arg$4, this.arg$5, obj);
        return lambda$null$36;
    }

    public static F lambdaFactory$(F f, F f2, TreeMap treeMap, F2 f22, Object obj) {
        return new List$$Lambda$4(f, f2, treeMap, f22, obj);
    }
}
