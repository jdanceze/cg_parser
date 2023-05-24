package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Rng$$Lambda$1.class */
final /* synthetic */ class Rng$$Lambda$1 implements F {
    private final int arg$1;
    private final int arg$2;

    private Rng$$Lambda$1(int i, int i2) {
        this.arg$1 = i;
        this.arg$2 = i2;
    }

    private static F get$Lambda(int i, int i2) {
        return new Rng$$Lambda$1(i, i2);
    }

    @Override // fj.F
    public Object f(Object obj) {
        return Rng.access$lambda$0(this.arg$1, this.arg$2, (Integer) obj);
    }

    public static F lambdaFactory$(int i, int i2) {
        return new Rng$$Lambda$1(i, i2);
    }
}
