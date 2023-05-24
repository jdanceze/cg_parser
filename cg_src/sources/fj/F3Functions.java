package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/F3Functions.class */
public class F3Functions {
    public static <A, B, C, D> F2<B, C, D> f(F3<A, B, C, D> f, A a) {
        return F3Functions$$Lambda$1.lambdaFactory$(f, a);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object lambda$f$62(F3 f3, Object obj, Object b, Object c) {
        return f3.f(obj, b, c);
    }
}
