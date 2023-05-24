package fj.test;

import fj.F;
import fj.data.Option;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Rand.class */
public final class Rand {
    private final F<Option<Long>, F<Integer, F<Integer, Integer>>> f;
    private final F<Option<Long>, F<Double, F<Double, Double>>> g;
    private static final F<Long, Random> fr = new F<Long, Random>() { // from class: fj.test.Rand.3
        @Override // fj.F
        public Random f(Long x) {
            return new Random(x.longValue());
        }
    };
    public static final Rand standard = new Rand(new F<Option<Long>, F<Integer, F<Integer, Integer>>>() { // from class: fj.test.Rand.4
        @Override // fj.F
        public F<Integer, F<Integer, Integer>> f(final Option<Long> seed) {
            return new F<Integer, F<Integer, Integer>>() { // from class: fj.test.Rand.4.1
                @Override // fj.F
                public F<Integer, Integer> f(final Integer from) {
                    return new F<Integer, Integer>() { // from class: fj.test.Rand.4.1.1
                        @Override // fj.F
                        public Integer f(Integer to) {
                            int f = Math.min(from.intValue(), to.intValue());
                            int t = Math.max(from.intValue(), to.intValue());
                            return Integer.valueOf(f + ((Random) seed.map(Rand.fr).orSome((Option) new Random())).nextInt((t - f) + 1));
                        }
                    };
                }
            };
        }
    }, new F<Option<Long>, F<Double, F<Double, Double>>>() { // from class: fj.test.Rand.5
        @Override // fj.F
        public F<Double, F<Double, Double>> f(final Option<Long> seed) {
            return new F<Double, F<Double, Double>>() { // from class: fj.test.Rand.5.1
                @Override // fj.F
                public F<Double, Double> f(final Double from) {
                    return new F<Double, Double>() { // from class: fj.test.Rand.5.1.1
                        @Override // fj.F
                        public Double f(Double to) {
                            double f = Math.min(from.doubleValue(), to.doubleValue());
                            double t = Math.max(from.doubleValue(), to.doubleValue());
                            return Double.valueOf((((Random) seed.map(Rand.fr).orSome((Option) new Random())).nextDouble() * (t - f)) + f);
                        }
                    };
                }
            };
        }
    });

    private Rand(F<Option<Long>, F<Integer, F<Integer, Integer>>> f, F<Option<Long>, F<Double, F<Double, Double>>> g) {
        this.f = f;
        this.g = g;
    }

    public int choose(long seed, int from, int to) {
        return this.f.f(Option.some(Long.valueOf(seed))).f(Integer.valueOf(from)).f(Integer.valueOf(to)).intValue();
    }

    public int choose(int from, int to) {
        return this.f.f(Option.none()).f(Integer.valueOf(from)).f(Integer.valueOf(to)).intValue();
    }

    public double choose(long seed, double from, double to) {
        return this.g.f(Option.some(Long.valueOf(seed))).f(Double.valueOf(from)).f(Double.valueOf(to)).doubleValue();
    }

    public double choose(double from, double to) {
        return this.g.f(Option.none()).f(Double.valueOf(from)).f(Double.valueOf(to)).doubleValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Rand$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Rand$1.class */
    public class AnonymousClass1 implements F<Option<Long>, F<Integer, F<Integer, Integer>>> {
        final /* synthetic */ long val$seed;

        AnonymousClass1(long j) {
            this.val$seed = j;
        }

        @Override // fj.F
        public F<Integer, F<Integer, Integer>> f(Option<Long> old) {
            return new F<Integer, F<Integer, Integer>>() { // from class: fj.test.Rand.1.1
                @Override // fj.F
                public F<Integer, Integer> f(final Integer from) {
                    return new F<Integer, Integer>() { // from class: fj.test.Rand.1.1.1
                        @Override // fj.F
                        public Integer f(Integer to) {
                            return (Integer) ((F) ((F) Rand.this.f.f(Option.some(Long.valueOf(AnonymousClass1.this.val$seed)))).f(from)).f(to);
                        }
                    };
                }
            };
        }
    }

    public Rand reseed(long seed) {
        return new Rand(new AnonymousClass1(seed), new AnonymousClass2(seed));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Rand$2  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Rand$2.class */
    public class AnonymousClass2 implements F<Option<Long>, F<Double, F<Double, Double>>> {
        final /* synthetic */ long val$seed;

        AnonymousClass2(long j) {
            this.val$seed = j;
        }

        @Override // fj.F
        public F<Double, F<Double, Double>> f(Option<Long> old) {
            return new F<Double, F<Double, Double>>() { // from class: fj.test.Rand.2.1
                @Override // fj.F
                public F<Double, Double> f(final Double from) {
                    return new F<Double, Double>() { // from class: fj.test.Rand.2.1.1
                        @Override // fj.F
                        public Double f(Double to) {
                            return (Double) ((F) ((F) Rand.this.g.f(Option.some(Long.valueOf(AnonymousClass2.this.val$seed)))).f(from)).f(to);
                        }
                    };
                }
            };
        }
    }

    public static Rand rand(F<Option<Long>, F<Integer, F<Integer, Integer>>> f, F<Option<Long>, F<Double, F<Double, Double>>> g) {
        return new Rand(f, g);
    }
}
