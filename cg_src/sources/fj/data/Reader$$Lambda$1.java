package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Reader$$Lambda$1.class */
final /* synthetic */ class Reader$$Lambda$1 implements F {
    private final Object arg$1;

    private Reader$$Lambda$1(Object obj) {
        this.arg$1 = obj;
    }

    private static F get$Lambda(Object obj) {
        return new Reader$$Lambda$1(obj);
    }

    @Override // fj.F
    public Object f(Object obj) {
        return Reader.access$lambda$0(this.arg$1, obj);
    }

    public static F lambdaFactory$(Object obj) {
        return new Reader$$Lambda$1(obj);
    }
}
