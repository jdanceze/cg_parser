package fj.data;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$$Lambda$7.class */
public final /* synthetic */ class Conversions$$Lambda$7 implements SafeIO {
    private final F arg$1;

    private Conversions$$Lambda$7(F f) {
        this.arg$1 = f;
    }

    private static SafeIO get$Lambda(F f) {
        return new Conversions$$Lambda$7(f);
    }

    @Override // fj.data.SafeIO, fj.data.IO
    public Object run() {
        Object lambda$F_SafeIO$98;
        lambda$F_SafeIO$98 = Conversions.lambda$F_SafeIO$98(this.arg$1);
        return lambda$F_SafeIO$98;
    }

    public static SafeIO lambdaFactory$(F f) {
        return new Conversions$$Lambda$7(f);
    }
}
