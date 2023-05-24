package fj.data;

import fj.data.Conversions;
import fj.function.Effect1;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$20$$Lambda$1.class */
final /* synthetic */ class Conversions$20$$Lambda$1 implements Effect1 {
    private final StringBuffer arg$1;

    private Conversions$20$$Lambda$1(StringBuffer stringBuffer) {
        this.arg$1 = stringBuffer;
    }

    private static Effect1 get$Lambda(StringBuffer stringBuffer) {
        return new Conversions$20$$Lambda$1(stringBuffer);
    }

    @Override // fj.function.Effect1
    public void f(Object obj) {
        Conversions.AnonymousClass20.access$lambda$0(this.arg$1, (Character) obj);
    }

    public static Effect1 lambdaFactory$(StringBuffer stringBuffer) {
        return new Conversions$20$$Lambda$1(stringBuffer);
    }
}
