package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Reader$$Lambda$2.class */
public final /* synthetic */ class Reader$$Lambda$2 implements F {
    private final Reader arg$1;
    private final F arg$2;

    private Reader$$Lambda$2(Reader reader, F f) {
        this.arg$1 = reader;
        this.arg$2 = f;
    }

    private static F get$Lambda(Reader reader, F f) {
        return new Reader$$Lambda$2(reader, f);
    }

    @Override // fj.F
    public Object f(Object obj) {
        Object lambda$flatMap$155;
        lambda$flatMap$155 = this.arg$1.lambda$flatMap$155(this.arg$2, obj);
        return lambda$flatMap$155;
    }

    public static F lambdaFactory$(Reader reader, F f) {
        return new Reader$$Lambda$2(reader, f);
    }
}
