package fj.data;

import fj.Bottom;
import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.data.vector.V;
import fj.data.vector.V2;
import java.math.BigInteger;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Natural.class */
public final class Natural extends Number {
    private final BigInteger value;
    private static final long serialVersionUID = -588673650944359682L;
    public static final F<BigInteger, Option<Natural>> fromBigInt = new F<BigInteger, Option<Natural>>() { // from class: fj.data.Natural.1
        @Override // fj.F
        public Option<Natural> f(BigInteger i) {
            return Natural.natural(i);
        }
    };
    public static final Natural ZERO = natural(0).some();
    public static final Natural ONE = natural(1).some();
    public static final F<Natural, F<Natural, Natural>> add = Function.curry(new F2<Natural, Natural, Natural>() { // from class: fj.data.Natural.4
        @Override // fj.F2
        public Natural f(Natural n1, Natural n2) {
            return n1.add(n2);
        }
    });
    public static final F<Natural, F<Natural, Option<Natural>>> subtract = Function.curry(new F2<Natural, Natural, Option<Natural>>() { // from class: fj.data.Natural.5
        @Override // fj.F2
        public Option<Natural> f(Natural o, Natural o1) {
            return o1.subtract(o);
        }
    });
    public static final F<Natural, F<Natural, Natural>> multiply = Function.curry(new F2<Natural, Natural, Natural>() { // from class: fj.data.Natural.6
        @Override // fj.F2
        public Natural f(Natural n1, Natural n2) {
            return n1.multiply(n2);
        }
    });
    public static final F<Natural, F<Natural, Natural>> divide = Function.curry(new F2<Natural, Natural, Natural>() { // from class: fj.data.Natural.7
        @Override // fj.F2
        public Natural f(Natural n1, Natural n2) {
            return n2.divide(n1);
        }
    });
    public static final F<Natural, F<Natural, Natural>> mod = Function.curry(new F2<Natural, Natural, Natural>() { // from class: fj.data.Natural.8
        @Override // fj.F2
        public Natural f(Natural n1, Natural n2) {
            return n2.mod(n1);
        }
    });
    public static final F<Natural, F<Natural, V2<Natural>>> divmod = Function.curry(new F2<Natural, Natural, V2<Natural>>() { // from class: fj.data.Natural.9
        @Override // fj.F2
        public V2<Natural> f(Natural n1, Natural n2) {
            return n2.divmod(n1);
        }
    });
    public static final F<Natural, BigInteger> bigIntegerValue = new F<Natural, BigInteger>() { // from class: fj.data.Natural.10
        @Override // fj.F
        public BigInteger f(Natural n) {
            return n.bigIntegerValue();
        }
    };

    private Natural(BigInteger i) {
        if (i.compareTo(BigInteger.ZERO) < 0) {
            throw Bottom.error("Natural less than zero");
        }
        this.value = i;
    }

    public static Option<Natural> natural(BigInteger i) {
        if (i.compareTo(BigInteger.ZERO) < 0) {
            return Option.none();
        }
        return Option.some(new Natural(i));
    }

    public static Option<Natural> natural(long i) {
        return natural(BigInteger.valueOf(i));
    }

    public Natural succ() {
        return add(ONE);
    }

    public static F<Natural, Natural> succ_() {
        return new F<Natural, Natural>() { // from class: fj.data.Natural.2
            @Override // fj.F
            public Natural f(Natural natural) {
                return natural.succ();
            }
        };
    }

    public Option<Natural> pred() {
        return subtract(ONE);
    }

    public static F<Natural, Option<Natural>> pred_() {
        return new F<Natural, Option<Natural>>() { // from class: fj.data.Natural.3
            @Override // fj.F
            public Option<Natural> f(Natural natural) {
                return natural.pred();
            }
        };
    }

    public Natural add(Natural n) {
        return natural(n.value.add(this.value)).some();
    }

    public Option<Natural> subtract(Natural n) {
        return natural(n.value.subtract(this.value));
    }

    public Natural multiply(Natural n) {
        return natural(n.value.multiply(this.value)).some();
    }

    public Natural divide(Natural n) {
        return natural(this.value.divide(n.value)).some();
    }

    public Natural mod(Natural n) {
        return natural(this.value.mod(n.value)).some();
    }

    public V2<Natural> divmod(Natural n) {
        BigInteger[] x = this.value.divideAndRemainder(n.value);
        return V.v(natural(x[0]).some(), natural(x[1]).some());
    }

    public BigInteger bigIntegerValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public long longValue() {
        return this.value.longValue();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return this.value.floatValue();
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return this.value.doubleValue();
    }

    @Override // java.lang.Number
    public int intValue() {
        return this.value.intValue();
    }

    public static Natural sum(Stream<Natural> ns) {
        return Monoid.naturalAdditionMonoid.sumLeft(ns);
    }

    public static Natural product(Stream<Natural> ns) {
        return Monoid.naturalMultiplicationMonoid.sumLeft(ns);
    }

    public static Natural sum(List<Natural> ns) {
        return Monoid.naturalAdditionMonoid.sumLeft(ns);
    }

    public static Natural product(List<Natural> ns) {
        return Monoid.naturalMultiplicationMonoid.sumLeft(ns);
    }
}
