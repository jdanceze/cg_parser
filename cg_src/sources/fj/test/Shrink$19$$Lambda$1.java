package fj.test;

import fj.P2;
import fj.function.Effect1;
import fj.test.Shrink;
import java.util.Hashtable;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Shrink$19$$Lambda$1.class */
final /* synthetic */ class Shrink$19$$Lambda$1 implements Effect1 {
    private final Hashtable arg$1;

    private Shrink$19$$Lambda$1(Hashtable hashtable) {
        this.arg$1 = hashtable;
    }

    private static Effect1 get$Lambda(Hashtable hashtable) {
        return new Shrink$19$$Lambda$1(hashtable);
    }

    @Override // fj.function.Effect1
    public void f(Object obj) {
        Shrink.AnonymousClass19.access$lambda$0(this.arg$1, (P2) obj);
    }

    public static Effect1 lambdaFactory$(Hashtable hashtable) {
        return new Shrink$19$$Lambda$1(hashtable);
    }
}
