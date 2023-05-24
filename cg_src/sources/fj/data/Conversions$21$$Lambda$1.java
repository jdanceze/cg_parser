package fj.data;

import fj.data.Conversions;
import fj.function.Effect1;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$21$$Lambda$1.class */
final /* synthetic */ class Conversions$21$$Lambda$1 implements Effect1 {
    private final StringBuilder arg$1;

    private Conversions$21$$Lambda$1(StringBuilder sb) {
        this.arg$1 = sb;
    }

    private static Effect1 get$Lambda(StringBuilder sb) {
        return new Conversions$21$$Lambda$1(sb);
    }

    @Override // fj.function.Effect1
    public void f(Object obj) {
        Conversions.AnonymousClass21.lambda$f$91(this.arg$1, (Character) obj);
    }

    public static Effect1 lambdaFactory$(StringBuilder sb) {
        return new Conversions$21$$Lambda$1(sb);
    }
}
