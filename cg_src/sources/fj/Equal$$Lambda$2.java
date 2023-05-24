package fj;

import fj.data.Writer;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Equal$$Lambda$2.class */
public final /* synthetic */ class Equal$$Lambda$2 implements F {
    private final Equal arg$1;
    private final Equal arg$2;
    private final Writer arg$3;

    private Equal$$Lambda$2(Equal equal, Equal equal2, Writer writer) {
        this.arg$1 = equal;
        this.arg$2 = equal2;
        this.arg$3 = writer;
    }

    private static F get$Lambda(Equal equal, Equal equal2, Writer writer) {
        return new Equal$$Lambda$2(equal, equal2, writer);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Boolean lambda$null$46;
        lambda$null$46 = Equal.lambda$null$46(this.arg$1, this.arg$2, this.arg$3, (Writer) obj);
        return lambda$null$46;
    }

    public static F lambdaFactory$(Equal equal, Equal equal2, Writer writer) {
        return new Equal$$Lambda$2(equal, equal2, writer);
    }
}
