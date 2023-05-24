package fj.test;

import fj.F;
import java.util.HashMap;
import org.apache.http.HttpStatus;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Variant.class */
public final class Variant {
    private static final HashMap<LongGen, Gen<?>> variantMemo = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Variant$LongGen.class */
    public static final class LongGen {
        private final long n;
        private final Gen<?> gen;

        LongGen(long n, Gen<?> gen) {
            this.n = n;
            this.gen = gen;
        }

        public boolean equals(Object o) {
            return o != null && o.getClass() == LongGen.class && this.n == ((LongGen) o).n && this.gen == ((LongGen) o).gen;
        }

        public int hashCode() {
            int result = (HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * 239) + ((int) (this.n ^ (this.n >>> 32)));
            return (HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * result) + this.gen.hashCode();
        }
    }

    private Variant() {
        throw new UnsupportedOperationException();
    }

    public static <A> Gen<A> variant(final long n, final Gen<A> g) {
        LongGen p = new LongGen(n, g);
        final Gen<?> gx = variantMemo.get(p);
        if (gx == null) {
            Gen<A> t = Gen.gen(new F<Integer, F<Rand, A>>() { // from class: fj.test.Variant.1
                @Override // fj.F
                public F<Rand, A> f(final Integer i) {
                    return new F<Rand, A>() { // from class: fj.test.Variant.1.1
                        /* JADX WARN: Type inference failed for: r0v3, types: [A, java.lang.Object] */
                        @Override // fj.F
                        public A f(Rand r) {
                            return Gen.this.gen(i.intValue(), r.reseed(n));
                        }
                    };
                }
            });
            variantMemo.put(p, t);
            return t;
        }
        return Gen.gen(new F<Integer, F<Rand, A>>() { // from class: fj.test.Variant.2
            @Override // fj.F
            public F<Rand, A> f(final Integer i) {
                return new F<Rand, A>() { // from class: fj.test.Variant.2.1
                    /* JADX WARN: Type inference failed for: r0v3, types: [A, java.lang.Object] */
                    @Override // fj.F
                    public A f(Rand r) {
                        return Gen.this.gen(i.intValue(), r);
                    }
                };
            }
        });
    }

    public static <A> F<Gen<A>, Gen<A>> variant(final long n) {
        return new F<Gen<A>, Gen<A>>() { // from class: fj.test.Variant.3
            @Override // fj.F
            public Gen<A> f(Gen<A> g) {
                return Variant.variant(n, g);
            }
        };
    }
}
