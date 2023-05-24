package fj.function;

import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.data.List;
import java.math.BigInteger;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/function/BigIntegers.class */
public final class BigIntegers {
    public static final F<BigInteger, F<BigInteger, BigInteger>> add = Function.curry(new F2<BigInteger, BigInteger, BigInteger>() { // from class: fj.function.BigIntegers.1
        @Override // fj.F2
        public BigInteger f(BigInteger a1, BigInteger a2) {
            return a1.add(a2);
        }
    });
    public static final F<BigInteger, F<BigInteger, BigInteger>> multiply = Function.curry(new F2<BigInteger, BigInteger, BigInteger>() { // from class: fj.function.BigIntegers.2
        @Override // fj.F2
        public BigInteger f(BigInteger a1, BigInteger a2) {
            return a1.multiply(a2);
        }
    });
    public static final F<BigInteger, F<BigInteger, BigInteger>> subtract = Function.curry(new F2<BigInteger, BigInteger, BigInteger>() { // from class: fj.function.BigIntegers.3
        @Override // fj.F2
        public BigInteger f(BigInteger a1, BigInteger a2) {
            return a1.subtract(a2);
        }
    });
    public static final F<BigInteger, BigInteger> negate = new F<BigInteger, BigInteger>() { // from class: fj.function.BigIntegers.4
        @Override // fj.F
        public BigInteger f(BigInteger i) {
            return i.negate();
        }
    };
    public static final F<BigInteger, BigInteger> abs = new F<BigInteger, BigInteger>() { // from class: fj.function.BigIntegers.5
        @Override // fj.F
        public BigInteger f(BigInteger i) {
            return i.abs();
        }
    };
    public static final F<BigInteger, F<BigInteger, BigInteger>> remainder = Function.curry(new F2<BigInteger, BigInteger, BigInteger>() { // from class: fj.function.BigIntegers.6
        @Override // fj.F2
        public BigInteger f(BigInteger a1, BigInteger a2) {
            return a1.remainder(a2);
        }
    });
    public static final F<BigInteger, F<Integer, BigInteger>> power = Function.curry(new F2<BigInteger, Integer, BigInteger>() { // from class: fj.function.BigIntegers.7
        @Override // fj.F2
        public BigInteger f(BigInteger a1, Integer a2) {
            return a1.pow(a2.intValue());
        }
    });

    private BigIntegers() {
        throw new UnsupportedOperationException();
    }

    public static BigInteger sum(List<BigInteger> ints) {
        return Monoid.bigintAdditionMonoid.sumLeft(ints);
    }

    public static BigInteger product(List<BigInteger> ints) {
        return Monoid.bigintMultiplicationMonoid.sumLeft(ints);
    }
}
