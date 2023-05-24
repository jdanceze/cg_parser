package fj;

import fj.data.Writer;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Equal$$Lambda$1.class */
final /* synthetic */ class Equal$$Lambda$1 implements F {
    private final Equal arg$1;
    private final Equal arg$2;

    private Equal$$Lambda$1(Equal equal, Equal equal2) {
        this.arg$1 = equal;
        this.arg$2 = equal2;
    }

    private static F get$Lambda(Equal equal, Equal equal2) {
        return new Equal$$Lambda$1(equal, equal2);
    }

    @Override // fj.F
    public Object f(Object obj) {
        return Equal.access$lambda$0(this.arg$1, this.arg$2, (Writer) obj);
    }

    public static F lambdaFactory$(Equal equal, Equal equal2) {
        return new Equal$$Lambda$1(equal, equal2);
    }
}
