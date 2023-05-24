package fj.data;

import fj.F;
import fj.F2;
import fj.Function;
import fj.Ord;
import fj.Ordering;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.resource.spi.work.WorkManager;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Enumerator.class */
public final class Enumerator<A> {
    private final F<A, Option<A>> successor;
    private final F<A, Option<A>> predecessor;
    private final Option<A> max;
    private final Option<A> min;
    private final Ord<A> order;
    private final F<A, F<Long, Option<A>>> plus;
    public static final Enumerator<Boolean> booleanEnumerator = enumerator(new F<Boolean, Option<Boolean>>() { // from class: fj.data.Enumerator.3
        @Override // fj.F
        public Option<Boolean> f(Boolean b) {
            return b.booleanValue() ? Option.none() : Option.some(true);
        }
    }, new F<Boolean, Option<Boolean>>() { // from class: fj.data.Enumerator.4
        @Override // fj.F
        public Option<Boolean> f(Boolean b) {
            return b.booleanValue() ? Option.some(false) : Option.none();
        }
    }, Option.some(true), Option.some(false), Ord.booleanOrd);
    public static final Enumerator<Byte> byteEnumerator = enumerator(new F<Byte, Option<Byte>>() { // from class: fj.data.Enumerator.5
        @Override // fj.F
        public Option<Byte> f(Byte b) {
            return b.byteValue() == Byte.MAX_VALUE ? Option.none() : Option.some(Byte.valueOf((byte) (b.byteValue() + 1)));
        }
    }, new F<Byte, Option<Byte>>() { // from class: fj.data.Enumerator.6
        @Override // fj.F
        public Option<Byte> f(Byte b) {
            return b.byteValue() == Byte.MIN_VALUE ? Option.none() : Option.some(Byte.valueOf((byte) (b.byteValue() - 1)));
        }
    }, Option.some(Byte.MAX_VALUE), Option.some(Byte.MIN_VALUE), Ord.byteOrd);
    public static final Enumerator<Character> charEnumerator = enumerator(new F<Character, Option<Character>>() { // from class: fj.data.Enumerator.7
        @Override // fj.F
        public Option<Character> f(Character c) {
            return c.charValue() == 65535 ? Option.none() : Option.some(Character.valueOf((char) (c.charValue() + 1)));
        }
    }, new F<Character, Option<Character>>() { // from class: fj.data.Enumerator.8
        @Override // fj.F
        public Option<Character> f(Character c) {
            return c.charValue() == 0 ? Option.none() : Option.some(Character.valueOf((char) (c.charValue() - 1)));
        }
    }, Option.some((char) 65535), Option.some((char) 0), Ord.charOrd);
    public static final Enumerator<Double> doubleEnumerator = enumerator(new F<Double, Option<Double>>() { // from class: fj.data.Enumerator.9
        @Override // fj.F
        public Option<Double> f(Double d) {
            return d.doubleValue() == Double.MAX_VALUE ? Option.none() : Option.some(Double.valueOf(d.doubleValue() + 1.0d));
        }
    }, new F<Double, Option<Double>>() { // from class: fj.data.Enumerator.10
        @Override // fj.F
        public Option<Double> f(Double d) {
            return d.doubleValue() == Double.MIN_VALUE ? Option.none() : Option.some(Double.valueOf(d.doubleValue() - 1.0d));
        }
    }, Option.some(Double.valueOf(Double.MAX_VALUE)), Option.some(Double.valueOf(Double.MIN_VALUE)), Ord.doubleOrd);
    public static final Enumerator<Float> floatEnumerator = enumerator(new F<Float, Option<Float>>() { // from class: fj.data.Enumerator.11
        @Override // fj.F
        public Option<Float> f(Float f) {
            return f.floatValue() == Float.MAX_VALUE ? Option.none() : Option.some(Float.valueOf(f.floatValue() + 1.0f));
        }
    }, new F<Float, Option<Float>>() { // from class: fj.data.Enumerator.12
        @Override // fj.F
        public Option<Float> f(Float f) {
            return f.floatValue() == Float.MIN_VALUE ? Option.none() : Option.some(Float.valueOf(f.floatValue() - 1.0f));
        }
    }, Option.some(Float.valueOf(Float.MAX_VALUE)), Option.some(Float.valueOf(Float.MIN_VALUE)), Ord.floatOrd);
    public static final Enumerator<Integer> intEnumerator = enumerator(new F<Integer, Option<Integer>>() { // from class: fj.data.Enumerator.13
        @Override // fj.F
        public Option<Integer> f(Integer i) {
            return i.intValue() == Integer.MAX_VALUE ? Option.none() : Option.some(Integer.valueOf(i.intValue() + 1));
        }
    }, new F<Integer, Option<Integer>>() { // from class: fj.data.Enumerator.14
        @Override // fj.F
        public Option<Integer> f(Integer i) {
            return i.intValue() == Integer.MIN_VALUE ? Option.none() : Option.some(Integer.valueOf(i.intValue() - 1));
        }
    }, Option.some(Integer.MAX_VALUE), Option.some(Integer.MIN_VALUE), Ord.intOrd);
    public static final Enumerator<BigInteger> bigintEnumerator = enumerator(new F<BigInteger, Option<BigInteger>>() { // from class: fj.data.Enumerator.15
        @Override // fj.F
        public Option<BigInteger> f(BigInteger i) {
            return Option.some(i.add(BigInteger.ONE));
        }
    }, new F<BigInteger, Option<BigInteger>>() { // from class: fj.data.Enumerator.16
        @Override // fj.F
        public Option<BigInteger> f(BigInteger i) {
            return Option.some(i.subtract(BigInteger.ONE));
        }
    }, Option.none(), Option.none(), Ord.bigintOrd, Function.curry(new F2<BigInteger, Long, Option<BigInteger>>() { // from class: fj.data.Enumerator.17
        @Override // fj.F2
        public Option<BigInteger> f(BigInteger i, Long l) {
            return Option.some(i.add(BigInteger.valueOf(l.longValue())));
        }
    }));
    public static final Enumerator<BigDecimal> bigdecimalEnumerator = enumerator(new F<BigDecimal, Option<BigDecimal>>() { // from class: fj.data.Enumerator.18
        @Override // fj.F
        public Option<BigDecimal> f(BigDecimal i) {
            return Option.some(i.add(BigDecimal.ONE));
        }
    }, new F<BigDecimal, Option<BigDecimal>>() { // from class: fj.data.Enumerator.19
        @Override // fj.F
        public Option<BigDecimal> f(BigDecimal i) {
            return Option.some(i.subtract(BigDecimal.ONE));
        }
    }, Option.none(), Option.none(), Ord.bigdecimalOrd, Function.curry(new F2<BigDecimal, Long, Option<BigDecimal>>() { // from class: fj.data.Enumerator.20
        @Override // fj.F2
        public Option<BigDecimal> f(BigDecimal d, Long l) {
            return Option.some(d.add(BigDecimal.valueOf(l.longValue())));
        }
    }));
    public static final Enumerator<Long> longEnumerator = enumerator(new F<Long, Option<Long>>() { // from class: fj.data.Enumerator.21
        @Override // fj.F
        public Option<Long> f(Long i) {
            return i.longValue() == WorkManager.INDEFINITE ? Option.none() : Option.some(Long.valueOf(i.longValue() + 1));
        }
    }, new F<Long, Option<Long>>() { // from class: fj.data.Enumerator.22
        @Override // fj.F
        public Option<Long> f(Long i) {
            return i.longValue() == Long.MIN_VALUE ? Option.none() : Option.some(Long.valueOf(i.longValue() - 1));
        }
    }, Option.some(Long.valueOf((long) WorkManager.INDEFINITE)), Option.some(Long.MIN_VALUE), Ord.longOrd);
    public static final Enumerator<Short> shortEnumerator = enumerator(new F<Short, Option<Short>>() { // from class: fj.data.Enumerator.23
        @Override // fj.F
        public Option<Short> f(Short i) {
            return i.shortValue() == Short.MAX_VALUE ? Option.none() : Option.some(Short.valueOf((short) (i.shortValue() + 1)));
        }
    }, new F<Short, Option<Short>>() { // from class: fj.data.Enumerator.24
        @Override // fj.F
        public Option<Short> f(Short i) {
            return i.shortValue() == Short.MIN_VALUE ? Option.none() : Option.some(Short.valueOf((short) (i.shortValue() - 1)));
        }
    }, Option.some(Short.MAX_VALUE), Option.some(Short.MIN_VALUE), Ord.shortOrd);
    public static final Enumerator<Ordering> orderingEnumerator = enumerator(new F<Ordering, Option<Ordering>>() { // from class: fj.data.Enumerator.25
        @Override // fj.F
        public Option<Ordering> f(Ordering o) {
            return o == Ordering.LT ? Option.some(Ordering.EQ) : o == Ordering.EQ ? Option.some(Ordering.GT) : Option.none();
        }
    }, new F<Ordering, Option<Ordering>>() { // from class: fj.data.Enumerator.26
        @Override // fj.F
        public Option<Ordering> f(Ordering o) {
            return o == Ordering.GT ? Option.some(Ordering.EQ) : o == Ordering.EQ ? Option.some(Ordering.LT) : Option.none();
        }
    }, Option.some(Ordering.GT), Option.some(Ordering.LT), Ord.orderingOrd);
    public static final Enumerator<Natural> naturalEnumerator = enumerator(new F<Natural, Option<Natural>>() { // from class: fj.data.Enumerator.27
        @Override // fj.F
        public Option<Natural> f(Natural n) {
            return Option.some(n.succ());
        }
    }, new F<Natural, Option<Natural>>() { // from class: fj.data.Enumerator.28
        @Override // fj.F
        public Option<Natural> f(Natural n) {
            return n.pred();
        }
    }, Option.none(), Option.some(Natural.ZERO), Ord.naturalOrd, Function.curry(new F2<Natural, Long, Option<Natural>>() { // from class: fj.data.Enumerator.29
        @Override // fj.F2
        public Option<Natural> f(Natural n, Long l) {
            return Option.some(n).apply(Natural.natural(l.longValue()).map(Function.curry(new F2<Natural, Natural, Natural>() { // from class: fj.data.Enumerator.29.1
                @Override // fj.F2
                public Natural f(Natural n1, Natural n2) {
                    return n1.add(n2);
                }
            })));
        }
    }));

    private Enumerator(F<A, Option<A>> successor, F<A, Option<A>> predecessor, Option<A> max, Option<A> min, Ord<A> order, F<A, F<Long, Option<A>>> plus) {
        this.successor = successor;
        this.predecessor = predecessor;
        this.max = max;
        this.min = min;
        this.order = order;
        this.plus = plus;
    }

    public F<A, Option<A>> successor() {
        return this.successor;
    }

    public Option<A> successor(A a) {
        return this.successor.f(a);
    }

    public F<A, Option<A>> predecessor() {
        return this.predecessor;
    }

    public Option<A> predecessor(A a) {
        return this.predecessor.f(a);
    }

    public Option<A> max() {
        return this.max;
    }

    public Option<A> min() {
        return this.min;
    }

    public F<A, F<Long, Option<A>>> plus() {
        return this.plus;
    }

    public F<Long, Option<A>> plus(A a) {
        return this.plus.f(a);
    }

    public F<A, Option<A>> plus(long l) {
        return (F) Function.flip(this.plus).f(Long.valueOf(l));
    }

    public Option<A> plus(A a, long l) {
        return this.plus.f(a).f(Long.valueOf(l));
    }

    public Ord<A> order() {
        return this.order;
    }

    public <B> Enumerator<B> xmap(final F<A, B> f, F<B, A> g) {
        F<Option<A>, Option<B>> of = new F<Option<A>, Option<B>>() { // from class: fj.data.Enumerator.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Option) ((Option) obj));
            }

            public Option<B> f(Option<A> o) {
                return o.map(f);
            }
        };
        return enumerator(Function.compose(Function.compose(of, this.successor), g), Function.compose(Function.compose(of, this.predecessor), g), this.max.map(f), this.min.map(f), this.order.comap(g), Function.compose(Function.compose((F) Function.compose().f(of), this.plus), g));
    }

    public Stream<A> toStream(A a) {
        F<A, A> id = Function.identity();
        return Stream.fromFunction(this, id, a);
    }

    public Enumerator<A> setMin(Option<A> min) {
        return enumerator(this.successor, this.predecessor, this.max, min, this.order, this.plus);
    }

    public Enumerator<A> setMax(Option<A> max) {
        return enumerator(this.successor, this.predecessor, max, this.min, this.order, this.plus);
    }

    public static <A> Enumerator<A> enumerator(F<A, Option<A>> successor, F<A, Option<A>> predecessor, Option<A> max, Option<A> min, Ord<A> order, F<A, F<Long, Option<A>>> plus) {
        return new Enumerator<>(successor, predecessor, max, min, order, plus);
    }

    public static <A> Enumerator<A> enumerator(final F<A, Option<A>> successor, final F<A, Option<A>> predecessor, Option<A> max, Option<A> min, Ord<A> order) {
        return new Enumerator<>(successor, predecessor, max, min, order, Function.curry(new F2<A, Long, Option<A>>() { // from class: fj.data.Enumerator.2
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Long l) {
                return f2((AnonymousClass2) obj, l);
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* renamed from: f  reason: avoid collision after fix types in other method */
            public Option<A> f2(A a, Long l) {
                if (l.longValue() == 0) {
                    return Option.some(a);
                }
                if (l.longValue() < 0) {
                    A aa = a;
                    long longValue = l.longValue();
                    while (true) {
                        long x = longValue;
                        if (x < 0) {
                            Option<A> s = (Option) F.this.f(aa);
                            if (s.isNone()) {
                                return Option.none();
                            }
                            aa = s.some();
                            longValue = x + 1;
                        } else {
                            return Option.some(aa);
                        }
                    }
                } else {
                    A aa2 = a;
                    long longValue2 = l.longValue();
                    while (true) {
                        long x2 = longValue2;
                        if (x2 > 0) {
                            Option<A> s2 = (Option) successor.f(aa2);
                            if (s2.isNone()) {
                                return Option.none();
                            }
                            aa2 = s2.some();
                            longValue2 = x2 - 1;
                        } else {
                            return Option.some(aa2);
                        }
                    }
                }
            }
        }));
    }
}
