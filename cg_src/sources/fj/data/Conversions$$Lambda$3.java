package fj.data;

import fj.Unit;
import fj.function.Effect0;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$$Lambda$3.class */
public final /* synthetic */ class Conversions$$Lambda$3 implements IO {
    private final Effect0 arg$1;

    private Conversions$$Lambda$3(Effect0 effect0) {
        this.arg$1 = effect0;
    }

    private static IO get$Lambda(Effect0 effect0) {
        return new Conversions$$Lambda$3(effect0);
    }

    @Override // fj.data.IO
    public Object run() {
        Unit lambda$Effect_IO$94;
        lambda$Effect_IO$94 = Conversions.lambda$Effect_IO$94(this.arg$1);
        return lambda$Effect_IO$94;
    }

    public static IO lambdaFactory$(Effect0 effect0) {
        return new Conversions$$Lambda$3(effect0);
    }
}
