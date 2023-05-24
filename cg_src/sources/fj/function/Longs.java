package fj.function;

import fj.F;
import fj.F2;
import fj.Function;
import fj.Semigroup;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/function/Longs.class */
public final class Longs {
    public static final F<Long, F<Long, Long>> add = Semigroup.longAdditionSemigroup.sum();
    public static final F<Long, F<Long, Long>> multiply = Semigroup.longMultiplicationSemigroup.sum();
    public static final F<Long, F<Long, Long>> subtract = Function.curry(new F2<Long, Long, Long>() { // from class: fj.function.Longs.1
        @Override // fj.F2
        public Long f(Long x, Long y) {
            return Long.valueOf(x.longValue() - y.longValue());
        }
    });
    public static final F<Long, Long> negate = new F<Long, Long>() { // from class: fj.function.Longs.2
        @Override // fj.F
        public Long f(Long x) {
            return Long.valueOf(x.longValue() * (-1));
        }
    };
    public static final F<Long, Long> abs = new F<Long, Long>() { // from class: fj.function.Longs.3
        @Override // fj.F
        public Long f(Long x) {
            return Long.valueOf(Math.abs(x.longValue()));
        }
    };
    public static final F<Long, F<Long, Long>> remainder = Function.curry(new F2<Long, Long, Long>() { // from class: fj.function.Longs.4
        @Override // fj.F2
        public Long f(Long a, Long b) {
            return Long.valueOf(a.longValue() % b.longValue());
        }
    });

    private Longs() {
        throw new UnsupportedOperationException();
    }
}
