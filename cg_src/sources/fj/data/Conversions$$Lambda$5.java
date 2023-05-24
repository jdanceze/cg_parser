package fj.data;

import fj.Unit;
import fj.function.Effect0;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$$Lambda$5.class */
public final /* synthetic */ class Conversions$$Lambda$5 implements SafeIO {
    private final Effect0 arg$1;

    private Conversions$$Lambda$5(Effect0 effect0) {
        this.arg$1 = effect0;
    }

    private static SafeIO get$Lambda(Effect0 effect0) {
        return new Conversions$$Lambda$5(effect0);
    }

    @Override // fj.data.SafeIO, fj.data.IO
    public Object run() {
        Unit lambda$Effect_SafeIO$96;
        lambda$Effect_SafeIO$96 = Conversions.lambda$Effect_SafeIO$96(this.arg$1);
        return lambda$Effect_SafeIO$96;
    }

    public static SafeIO lambdaFactory$(Effect0 effect0) {
        return new Conversions$$Lambda$5(effect0);
    }
}
