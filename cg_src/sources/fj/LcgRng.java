package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/LcgRng.class */
public class LcgRng extends Rng {
    private final Long seed;

    public LcgRng() {
        this(System.currentTimeMillis());
    }

    public LcgRng(long s) {
        this.seed = Long.valueOf(s);
    }

    public long getSeed() {
        return this.seed.longValue();
    }

    @Override // fj.Rng
    public P2<Rng, Integer> nextInt() {
        P2<Rng, Long> p = nextLong();
        int i = (int) p._2().longValue();
        return P.p(p._1(), Integer.valueOf(i));
    }

    @Override // fj.Rng
    public P2<Rng, Long> nextLong() {
        P2<Long, Long> p = nextLong(this.seed.longValue());
        return P.p(new LcgRng(p._1().longValue()), p._2());
    }

    static P2<Long, Long> nextLong(long seed) {
        long newSeed = ((seed * 25214903917L) + 11) & 281474976710655L;
        long n = Long.valueOf(newSeed >>> 16).longValue();
        return P.p(Long.valueOf(newSeed), Long.valueOf(n));
    }
}
