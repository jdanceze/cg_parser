package fj;

import fj.data.Array;
import fj.data.List;
import fj.data.Natural;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Set;
import fj.data.Stream;
import java.math.BigDecimal;
import java.math.BigInteger;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Semigroup.class */
public final class Semigroup<A> {
    private final F<A, F<A, A>> sum;
    public static final Semigroup<Integer> intAdditionSemigroup = semigroup(new F2<Integer, Integer, Integer>() { // from class: fj.Semigroup.1
        @Override // fj.F2
        public Integer f(Integer i1, Integer i2) {
            return Integer.valueOf(i1.intValue() + i2.intValue());
        }
    });
    public static final Semigroup<Double> doubleAdditionSemigroup = semigroup(new F2<Double, Double, Double>() { // from class: fj.Semigroup.2
        @Override // fj.F2
        public Double f(Double d1, Double d2) {
            return Double.valueOf(d1.doubleValue() + d2.doubleValue());
        }
    });
    public static final Semigroup<Integer> intMultiplicationSemigroup = semigroup(new F2<Integer, Integer, Integer>() { // from class: fj.Semigroup.3
        @Override // fj.F2
        public Integer f(Integer i1, Integer i2) {
            return Integer.valueOf(i1.intValue() * i2.intValue());
        }
    });
    public static final Semigroup<Double> doubleMultiplicationSemigroup = semigroup(new F2<Double, Double, Double>() { // from class: fj.Semigroup.4
        @Override // fj.F2
        public Double f(Double d1, Double d2) {
            return Double.valueOf(d1.doubleValue() * d2.doubleValue());
        }
    });
    public static final Semigroup<Integer> intMaximumSemigroup = semigroup(Ord.intOrd.max);
    public static final Semigroup<Integer> intMinimumSemigroup = semigroup(Ord.intOrd.min);
    public static final Semigroup<BigInteger> bigintAdditionSemigroup = semigroup(new F2<BigInteger, BigInteger, BigInteger>() { // from class: fj.Semigroup.5
        @Override // fj.F2
        public BigInteger f(BigInteger i1, BigInteger i2) {
            return i1.add(i2);
        }
    });
    public static final Semigroup<BigInteger> bigintMultiplicationSemigroup = semigroup(new F2<BigInteger, BigInteger, BigInteger>() { // from class: fj.Semigroup.6
        @Override // fj.F2
        public BigInteger f(BigInteger i1, BigInteger i2) {
            return i1.multiply(i2);
        }
    });
    public static final Semigroup<BigInteger> bigintMaximumSemigroup = semigroup(Ord.bigintOrd.max);
    public static final Semigroup<BigInteger> bigintMinimumSemigroup = semigroup(Ord.bigintOrd.min);
    public static final Semigroup<BigDecimal> bigdecimalAdditionSemigroup = semigroup(new F2<BigDecimal, BigDecimal, BigDecimal>() { // from class: fj.Semigroup.7
        @Override // fj.F2
        public BigDecimal f(BigDecimal i1, BigDecimal i2) {
            return i1.add(i2);
        }
    });
    public static final Semigroup<BigDecimal> bigdecimalMultiplicationSemigroup = semigroup(new F2<BigDecimal, BigDecimal, BigDecimal>() { // from class: fj.Semigroup.8
        @Override // fj.F2
        public BigDecimal f(BigDecimal i1, BigDecimal i2) {
            return i1.multiply(i2);
        }
    });
    public static final Semigroup<BigDecimal> bigDecimalMaximumSemigroup = semigroup(Ord.bigdecimalOrd.max);
    public static final Semigroup<BigDecimal> bigDecimalMinimumSemigroup = semigroup(Ord.bigdecimalOrd.min);
    public static final Semigroup<Natural> naturalMultiplicationSemigroup = semigroup(new F2<Natural, Natural, Natural>() { // from class: fj.Semigroup.9
        @Override // fj.F2
        public Natural f(Natural n1, Natural n2) {
            return n1.multiply(n2);
        }
    });
    public static final Semigroup<Natural> naturalAdditionSemigroup = semigroup(new F2<Natural, Natural, Natural>() { // from class: fj.Semigroup.10
        @Override // fj.F2
        public Natural f(Natural n1, Natural n2) {
            return n1.add(n2);
        }
    });
    public static final Semigroup<Natural> naturalMaximumSemigroup = semigroup(Ord.naturalOrd.max);
    public static final Semigroup<Natural> naturalMinimumSemigroup = semigroup(Ord.naturalOrd.min);
    public static final Semigroup<Long> longAdditionSemigroup = semigroup(new F2<Long, Long, Long>() { // from class: fj.Semigroup.11
        @Override // fj.F2
        public Long f(Long x, Long y) {
            return Long.valueOf(x.longValue() + y.longValue());
        }
    });
    public static final Semigroup<Long> longMultiplicationSemigroup = semigroup(new F2<Long, Long, Long>() { // from class: fj.Semigroup.12
        @Override // fj.F2
        public Long f(Long x, Long y) {
            return Long.valueOf(x.longValue() * y.longValue());
        }
    });
    public static final Semigroup<Long> longMaximumSemigroup = semigroup(Ord.longOrd.max);
    public static final Semigroup<Long> longMinimumSemigroup = semigroup(Ord.longOrd.min);
    public static final Semigroup<Boolean> disjunctionSemigroup = semigroup(new F2<Boolean, Boolean, Boolean>() { // from class: fj.Semigroup.13
        @Override // fj.F2
        public Boolean f(Boolean b1, Boolean b2) {
            return Boolean.valueOf(b1.booleanValue() || b2.booleanValue());
        }
    });
    public static final Semigroup<Boolean> exclusiveDisjunctionSemiGroup = semigroup(new F2<Boolean, Boolean, Boolean>() { // from class: fj.Semigroup.14
        @Override // fj.F2
        public Boolean f(Boolean p, Boolean q) {
            return Boolean.valueOf((p.booleanValue() && !q.booleanValue()) || (!p.booleanValue() && q.booleanValue()));
        }
    });
    public static final Semigroup<Boolean> conjunctionSemigroup = semigroup(new F2<Boolean, Boolean, Boolean>() { // from class: fj.Semigroup.15
        @Override // fj.F2
        public Boolean f(Boolean b1, Boolean b2) {
            return Boolean.valueOf(b1.booleanValue() && b2.booleanValue());
        }
    });
    public static final Semigroup<String> stringSemigroup = semigroup(new F2<String, String, String>() { // from class: fj.Semigroup.16
        @Override // fj.F2
        public String f(String s1, String s2) {
            return s1 + s2;
        }
    });
    public static final Semigroup<StringBuffer> stringBufferSemigroup = semigroup(new F2<StringBuffer, StringBuffer, StringBuffer>() { // from class: fj.Semigroup.17
        @Override // fj.F2
        public StringBuffer f(StringBuffer s1, StringBuffer s2) {
            return new StringBuffer(s1).append(s2);
        }
    });
    public static final Semigroup<StringBuilder> stringBuilderSemigroup = semigroup(new F2<StringBuilder, StringBuilder, StringBuilder>() { // from class: fj.Semigroup.18
        @Override // fj.F2
        public StringBuilder f(StringBuilder s1, StringBuilder s2) {
            return new StringBuilder(s1).append((CharSequence) s2);
        }
    });
    public static final Semigroup<Unit> unitSemigroup = semigroup(new F2<Unit, Unit, Unit>() { // from class: fj.Semigroup.29
        @Override // fj.F2
        public Unit f(Unit u1, Unit u2) {
            return Unit.unit();
        }
    });

    private Semigroup(F<A, F<A, A>> sum) {
        this.sum = sum;
    }

    public A sum(A a1, A a2) {
        return this.sum.f(a1).f(a2);
    }

    public F<A, A> sum(A a1) {
        return this.sum.f(a1);
    }

    public F<A, F<A, A>> sum() {
        return this.sum;
    }

    public static <A> Semigroup<A> semigroup(F<A, F<A, A>> sum) {
        return new Semigroup<>(sum);
    }

    public static <A> Semigroup<A> semigroup(F2<A, A, A> sum) {
        return new Semigroup<>(Function.curry(sum));
    }

    public static <A, B> Semigroup<F<A, B>> functionSemigroup(Semigroup<B> sb) {
        return semigroup(new F2<F<A, B>, F<A, B>, F<A, B>>() { // from class: fj.Semigroup.19
            @Override // fj.F2
            public F<A, B> f(final F<A, B> a1, final F<A, B> a2) {
                return new F<A, B>() { // from class: fj.Semigroup.19.1
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX WARN: Type inference failed for: r0v3, types: [B, java.lang.Object] */
                    @Override // fj.F
                    public B f(A a) {
                        return Semigroup.this.sum(a1.f(a), a2.f(a));
                    }
                };
            }
        });
    }

    public static <A> Semigroup<List<A>> listSemigroup() {
        return semigroup(new F2<List<A>, List<A>, List<A>>() { // from class: fj.Semigroup.20
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((List) ((List) obj), (List) ((List) obj2));
            }

            public List<A> f(List<A> a1, List<A> a2) {
                return a1.append(a2);
            }
        });
    }

    public static <A> Semigroup<NonEmptyList<A>> nonEmptyListSemigroup() {
        return semigroup(new F2<NonEmptyList<A>, NonEmptyList<A>, NonEmptyList<A>>() { // from class: fj.Semigroup.21
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((NonEmptyList) ((NonEmptyList) obj), (NonEmptyList) ((NonEmptyList) obj2));
            }

            public NonEmptyList<A> f(NonEmptyList<A> a1, NonEmptyList<A> a2) {
                return a1.append(a2);
            }
        });
    }

    public static <A> Semigroup<Option<A>> optionSemigroup() {
        return semigroup(new F2<Option<A>, Option<A>, Option<A>>() { // from class: fj.Semigroup.22
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Option) ((Option) obj), (Option) ((Option) obj2));
            }

            public Option<A> f(Option<A> a1, Option<A> a2) {
                return a1.isSome() ? a1 : a2;
            }
        });
    }

    public static <A> Semigroup<Option<A>> firstOptionSemigroup() {
        return semigroup(new F2<Option<A>, Option<A>, Option<A>>() { // from class: fj.Semigroup.23
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Option) ((Option) obj), (Option) ((Option) obj2));
            }

            public Option<A> f(Option<A> a1, Option<A> a2) {
                return a1.orElse(a2);
            }
        });
    }

    public static <A> Semigroup<Option<A>> lastOptionSemigroup() {
        return semigroup(new F2<Option<A>, Option<A>, Option<A>>() { // from class: fj.Semigroup.24
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Option) ((Option) obj), (Option) ((Option) obj2));
            }

            public Option<A> f(Option<A> a1, Option<A> a2) {
                return a2.orElse(a1);
            }
        });
    }

    public static <A> Semigroup<Stream<A>> streamSemigroup() {
        return semigroup(new F2<Stream<A>, Stream<A>, Stream<A>>() { // from class: fj.Semigroup.25
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Stream) ((Stream) obj), (Stream) ((Stream) obj2));
            }

            public Stream<A> f(Stream<A> a1, Stream<A> a2) {
                return a1.append(a2);
            }
        });
    }

    public static <A> Semigroup<Array<A>> arraySemigroup() {
        return semigroup(new F2<Array<A>, Array<A>, Array<A>>() { // from class: fj.Semigroup.26
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Array) ((Array) obj), (Array) ((Array) obj2));
            }

            public Array<A> f(Array<A> a1, Array<A> a2) {
                return a1.append(a2);
            }
        });
    }

    public static <A> Semigroup<P1<A>> p1Semigroup(Semigroup<A> sa) {
        return semigroup(new F2<P1<A>, P1<A>, P1<A>>() { // from class: fj.Semigroup.27
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((P1) ((P1) obj), (P1) ((P1) obj2));
            }

            public P1<A> f(final P1<A> a1, final P1<A> a2) {
                return new P1<A>() { // from class: fj.Semigroup.27.1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.P1
                    public A _1() {
                        return (A) Semigroup.this.sum(a1._1(), a2._1());
                    }
                };
            }
        });
    }

    public static <A, B> Semigroup<P2<A, B>> p2Semigroup(Semigroup<A> sa, final Semigroup<B> sb) {
        return semigroup(new F2<P2<A, B>, P2<A, B>, P2<A, B>>() { // from class: fj.Semigroup.28
            @Override // fj.F2
            public P2<A, B> f(final P2<A, B> a1, final P2<A, B> a2) {
                return new P2<A, B>() { // from class: fj.Semigroup.28.1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.P2
                    public A _1() {
                        return (A) Semigroup.this.sum(a1._1(), a2._1());
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX WARN: Type inference failed for: r0v3, types: [B, java.lang.Object] */
                    @Override // fj.P2
                    public B _2() {
                        return sb.sum(a1._2(), a2._2());
                    }
                };
            }
        });
    }

    public static <A> Semigroup<Set<A>> setSemigroup() {
        return semigroup(new F2<Set<A>, Set<A>, Set<A>>() { // from class: fj.Semigroup.30
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Set) ((Set) obj), (Set) ((Set) obj2));
            }

            public Set<A> f(Set<A> a, Set<A> b) {
                return a.union(b);
            }
        });
    }
}
