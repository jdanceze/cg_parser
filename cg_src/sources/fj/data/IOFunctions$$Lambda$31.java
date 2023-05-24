package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$$Lambda$31.class */
public final /* synthetic */ class IOFunctions$$Lambda$31 implements F {
    private final List arg$1;

    private IOFunctions$$Lambda$31(List list) {
        this.arg$1 = list;
    }

    private static F get$Lambda(List list) {
        return new IOFunctions$$Lambda$31(list);
    }

    @Override // fj.F
    public Object f(Object obj) {
        List cons;
        cons = List.cons(obj, this.arg$1);
        return cons;
    }

    public static F lambdaFactory$(List list) {
        return new IOFunctions$$Lambda$31(list);
    }
}
