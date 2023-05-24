package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Rng.class */
public abstract class Rng {
    public abstract P2<Rng, Integer> nextInt();

    public abstract P2<Rng, Long> nextLong();

    public P2<Rng, Integer> range(int low, int high) {
        return nextNatural().map2(Rng$$Lambda$1.lambdaFactory$(high, low));
    }

    public static /* synthetic */ Integer lambda$range$68(int i, int i2, Integer x) {
        return Integer.valueOf((x.intValue() % ((i - i2) + 1)) + i2);
    }

    public P2<Rng, Integer> nextNatural() {
        F<Integer, X> f;
        P2<Rng, Integer> nextInt = nextInt();
        f = Rng$$Lambda$2.instance;
        return nextInt.map2(f);
    }

    public static /* synthetic */ Integer lambda$nextNatural$69(Integer x) {
        return Integer.valueOf(x.intValue() < 0 ? -(x.intValue() + 1) : x.intValue());
    }
}
