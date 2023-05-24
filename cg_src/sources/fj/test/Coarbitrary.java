package fj.test;

import fj.F;
import fj.F2;
import fj.F3;
import fj.F4;
import fj.F5;
import fj.F6;
import fj.F7;
import fj.F8;
import fj.Function;
import fj.LcgRng;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.P4;
import fj.P5;
import fj.P6;
import fj.P7;
import fj.P8;
import fj.data.Array;
import fj.data.Either;
import fj.data.List;
import fj.data.Option;
import fj.data.State;
import fj.data.Stream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Coarbitrary.class */
public abstract class Coarbitrary<A> {
    public static final Coarbitrary<Boolean> coarbBoolean = new Coarbitrary<Boolean>() { // from class: fj.test.Coarbitrary.12
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(Boolean b, Gen<B> g) {
            return Variant.variant(b.booleanValue() ? 0L : 1L, g);
        }
    };
    public static final Coarbitrary<Integer> coarbInteger = new Coarbitrary<Integer>() { // from class: fj.test.Coarbitrary.13
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(Integer i, Gen<B> g) {
            return Variant.variant(i.intValue() >= 0 ? 2 * i.intValue() : ((-2) * i.intValue()) + 1, g);
        }
    };
    public static final Coarbitrary<Byte> coarbByte = new Coarbitrary<Byte>() { // from class: fj.test.Coarbitrary.14
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(Byte b, Gen<B> g) {
            return Variant.variant(b.byteValue() >= 0 ? 2 * b.byteValue() : ((-2) * b.byteValue()) + 1, g);
        }
    };
    public static final Coarbitrary<Short> coarbShort = new Coarbitrary<Short>() { // from class: fj.test.Coarbitrary.15
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(Short s, Gen<B> g) {
            return Variant.variant(s.shortValue() >= 0 ? 2 * s.shortValue() : ((-2) * s.shortValue()) + 1, g);
        }
    };
    public static final Coarbitrary<Long> coarbLong = new Coarbitrary<Long>() { // from class: fj.test.Coarbitrary.16
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(Long l, Gen<B> g) {
            return Variant.variant(l.longValue() >= 0 ? 2 * l.longValue() : ((-2) * l.longValue()) + 1, g);
        }
    };
    public static final Coarbitrary<Character> coarbCharacter = new Coarbitrary<Character>() { // from class: fj.test.Coarbitrary.17
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(Character c, Gen<B> g) {
            return Variant.variant(c.charValue() << 1, g);
        }
    };
    public static final Coarbitrary<Float> coarbFloat = new Coarbitrary<Float>() { // from class: fj.test.Coarbitrary.18
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(Float f, Gen<B> g) {
            return coarbInteger.coarbitrary(Integer.valueOf(Float.floatToRawIntBits(f.floatValue())), g);
        }
    };
    public static final Coarbitrary<Double> coarbDouble = new Coarbitrary<Double>() { // from class: fj.test.Coarbitrary.19
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(Double d, Gen<B> g) {
            return coarbLong.coarbitrary(Long.valueOf(Double.doubleToRawLongBits(d.doubleValue())), g);
        }
    };
    public static final Coarbitrary<String> coarbString = new Coarbitrary<String>() { // from class: fj.test.Coarbitrary.23
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(String s, Gen<B> g) {
            return coarbList(coarbCharacter).coarbitrary(List.fromString(s), g);
        }
    };
    public static final Coarbitrary<StringBuffer> coarbStringBuffer = new Coarbitrary<StringBuffer>() { // from class: fj.test.Coarbitrary.24
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(StringBuffer s, Gen<B> g) {
            return coarbString.coarbitrary(s.toString(), g);
        }
    };
    public static final Coarbitrary<StringBuilder> coarbStringBuilder = new Coarbitrary<StringBuilder>() { // from class: fj.test.Coarbitrary.25
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(StringBuilder s, Gen<B> g) {
            return coarbString.coarbitrary(s.toString(), g);
        }
    };
    public static final Coarbitrary<Throwable> coarbThrowable = coarbThrowable(coarbString);
    public static final Coarbitrary<BitSet> coarbBitSet = new Coarbitrary<BitSet>() { // from class: fj.test.Coarbitrary.32
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(BitSet s, Gen<B> g) {
            List<Boolean> x = List.nil();
            for (int i = 0; i < s.size(); i++) {
                x = x.snoc(Boolean.valueOf(s.get(i)));
            }
            return coarbList(coarbBoolean).coarbitrary(x, g);
        }
    };
    public static final Coarbitrary<Calendar> coarbCalendar = new Coarbitrary<Calendar>() { // from class: fj.test.Coarbitrary.33
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(Calendar c, Gen<B> g) {
            return coarbLong.coarbitrary(Long.valueOf(c.getTime().getTime()), g);
        }
    };
    public static final Coarbitrary<Date> coarbDate = new Coarbitrary<Date>() { // from class: fj.test.Coarbitrary.34
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(Date d, Gen<B> g) {
            return coarbLong.coarbitrary(Long.valueOf(d.getTime()), g);
        }
    };
    public static final Coarbitrary<GregorianCalendar> coarbGregorianCalendar = new Coarbitrary<GregorianCalendar>() { // from class: fj.test.Coarbitrary.37
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(GregorianCalendar c, Gen<B> g) {
            return coarbLong.coarbitrary(Long.valueOf(c.getTime().getTime()), g);
        }
    };
    public static final Coarbitrary<Properties> coarbProperties = new Coarbitrary<Properties>() { // from class: fj.test.Coarbitrary.46
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(Properties p, Gen<B> g) {
            Hashtable<String, String> t = new Hashtable<>();
            for (Object s : p.keySet()) {
                t.put((String) s, p.getProperty((String) s));
            }
            return coarbHashtable(coarbString, coarbString).coarbitrary(t, g);
        }
    };
    public static final Coarbitrary<java.sql.Date> coarbSQLDate = new Coarbitrary<java.sql.Date>() { // from class: fj.test.Coarbitrary.61
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(java.sql.Date d, Gen<B> g) {
            return coarbLong.coarbitrary(Long.valueOf(d.getTime()), g);
        }
    };
    public static final Coarbitrary<Timestamp> coarbTimestamp = new Coarbitrary<Timestamp>() { // from class: fj.test.Coarbitrary.62
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(Timestamp t, Gen<B> g) {
            return coarbLong.coarbitrary(Long.valueOf(t.getTime()), g);
        }
    };
    public static final Coarbitrary<Time> coarbTime = new Coarbitrary<Time>() { // from class: fj.test.Coarbitrary.63
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(Time t, Gen<B> g) {
            return coarbLong.coarbitrary(Long.valueOf(t.getTime()), g);
        }
    };
    public static final Coarbitrary<BigInteger> coarbBigInteger = new Coarbitrary<BigInteger>() { // from class: fj.test.Coarbitrary.64
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(BigInteger i, Gen<B> g) {
            BigInteger multiply;
            if (i.compareTo(BigInteger.ZERO) >= 0) {
                multiply = i.multiply(BigInteger.valueOf(2L));
            } else {
                multiply = i.multiply(BigInteger.valueOf(-2L).add(BigInteger.ONE));
            }
            return Variant.variant(multiply.longValue(), g);
        }
    };
    public static final Coarbitrary<BigDecimal> coarbBigDecimal = new Coarbitrary<BigDecimal>() { // from class: fj.test.Coarbitrary.65
        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(BigDecimal d, Gen<B> g) {
            BigDecimal multiply;
            if (d.compareTo(BigDecimal.ZERO) >= 0) {
                multiply = d.multiply(BigDecimal.valueOf(2L));
            } else {
                multiply = d.multiply(BigDecimal.valueOf(-2L).add(BigDecimal.ONE));
            }
            return Variant.variant(multiply.longValue(), g);
        }
    };

    public abstract <B> Gen<B> coarbitrary(A a, Gen<B> gen);

    public final <B> F<Gen<B>, Gen<B>> coarbitrary(final A a) {
        return new F<Gen<B>, Gen<B>>() { // from class: fj.test.Coarbitrary.1
            {
                Coarbitrary.this = this;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Gen<B> f(Gen<B> g) {
                return Coarbitrary.this.coarbitrary(a, g);
            }
        };
    }

    public final <B> Coarbitrary<B> compose(final F<B, A> f) {
        return new Coarbitrary<B>() { // from class: fj.test.Coarbitrary.2
            {
                Coarbitrary.this = this;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(B b, Gen<X> g) {
                return Coarbitrary.this.coarbitrary(f.f(b), g);
            }
        };
    }

    public final <B> Coarbitrary<B> comap(final F<B, A> f) {
        return new Coarbitrary<B>() { // from class: fj.test.Coarbitrary.3
            {
                Coarbitrary.this = this;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(B b, Gen<X> g) {
                return Coarbitrary.this.coarbitrary(f.f(b), g);
            }
        };
    }

    public static <A, B> Coarbitrary<F<A, B>> coarbF(final Arbitrary<A> a, final Coarbitrary<B> c) {
        return new Coarbitrary<F<A, B>>() { // from class: fj.test.Coarbitrary.4
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(final F<A, B> f, final Gen<X> g) {
                return (Gen<X>) a.gen.bind((F<A, Gen<X>>) new F<A, Gen<X>>() { // from class: fj.test.Coarbitrary.4.1
                    {
                        AnonymousClass4.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Gen<X> f(A a2) {
                        return c.coarbitrary(f.f(a2), g);
                    }
                });
            }
        };
    }

    public static <A, B, C> Coarbitrary<F2<A, B, C>> coarbF2(final Arbitrary<A> aa, final Arbitrary<B> ab, final Coarbitrary<C> c) {
        return new Coarbitrary<F2<A, B, C>>() { // from class: fj.test.Coarbitrary.5
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(F2<A, B, C> f, Gen<X> g) {
                return coarbF(aa, coarbF(ab, c)).coarbitrary(Function.curry(f), g);
            }
        };
    }

    public static <A, B, C, D> Coarbitrary<F3<A, B, C, D>> coarbF3(final Arbitrary<A> aa, final Arbitrary<B> ab, final Arbitrary<C> ac, final Coarbitrary<D> c) {
        return new Coarbitrary<F3<A, B, C, D>>() { // from class: fj.test.Coarbitrary.6
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(F3<A, B, C, D> f, Gen<X> g) {
                return coarbF(aa, coarbF(ab, coarbF(ac, c))).coarbitrary(Function.curry(f), g);
            }
        };
    }

    public static <A, B, C, D, E> Coarbitrary<F4<A, B, C, D, E>> coarbF4(final Arbitrary<A> aa, final Arbitrary<B> ab, final Arbitrary<C> ac, final Arbitrary<D> ad, final Coarbitrary<E> c) {
        return new Coarbitrary<F4<A, B, C, D, E>>() { // from class: fj.test.Coarbitrary.7
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(F4<A, B, C, D, E> f, Gen<X> g) {
                return coarbF(aa, coarbF(ab, coarbF(ac, coarbF(ad, c)))).coarbitrary(Function.curry(f), g);
            }
        };
    }

    public static <A, B, C, D, E, F$> Coarbitrary<F5<A, B, C, D, E, F$>> coarbF5(final Arbitrary<A> aa, final Arbitrary<B> ab, final Arbitrary<C> ac, final Arbitrary<D> ad, final Arbitrary<E> ae, final Coarbitrary<F$> c) {
        return new Coarbitrary<F5<A, B, C, D, E, F$>>() { // from class: fj.test.Coarbitrary.8
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(F5<A, B, C, D, E, F$> f, Gen<X> g) {
                return coarbF(aa, coarbF(ab, coarbF(ac, coarbF(ad, coarbF(ae, c))))).coarbitrary(Function.curry(f), g);
            }
        };
    }

    public static <A, B, C, D, E, F$, G> Coarbitrary<F6<A, B, C, D, E, F$, G>> coarbF6(final Arbitrary<A> aa, final Arbitrary<B> ab, final Arbitrary<C> ac, final Arbitrary<D> ad, final Arbitrary<E> ae, final Arbitrary<F$> af, final Coarbitrary<G> c) {
        return new Coarbitrary<F6<A, B, C, D, E, F$, G>>() { // from class: fj.test.Coarbitrary.9
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(F6<A, B, C, D, E, F$, G> f, Gen<X> g) {
                return coarbF(aa, coarbF(ab, coarbF(ac, coarbF(ad, coarbF(ae, coarbF(af, c)))))).coarbitrary(Function.curry(f), g);
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H> Coarbitrary<F7<A, B, C, D, E, F$, G, H>> coarbF7(final Arbitrary<A> aa, final Arbitrary<B> ab, final Arbitrary<C> ac, final Arbitrary<D> ad, final Arbitrary<E> ae, final Arbitrary<F$> af, final Arbitrary<G> ag, final Coarbitrary<H> c) {
        return new Coarbitrary<F7<A, B, C, D, E, F$, G, H>>() { // from class: fj.test.Coarbitrary.10
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(F7<A, B, C, D, E, F$, G, H> f, Gen<X> g) {
                return coarbF(aa, coarbF(ab, coarbF(ac, coarbF(ad, coarbF(ae, coarbF(af, coarbF(ag, c))))))).coarbitrary(Function.curry(f), g);
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H, I> Coarbitrary<F8<A, B, C, D, E, F$, G, H, I>> coarbF8(final Arbitrary<A> aa, final Arbitrary<B> ab, final Arbitrary<C> ac, final Arbitrary<D> ad, final Arbitrary<E> ae, final Arbitrary<F$> af, final Arbitrary<G> ag, final Arbitrary<H> ah, final Coarbitrary<I> c) {
        return new Coarbitrary<F8<A, B, C, D, E, F$, G, H, I>>() { // from class: fj.test.Coarbitrary.11
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(F8<A, B, C, D, E, F$, G, H, I> f, Gen<X> g) {
                return coarbF(aa, coarbF(ab, coarbF(ac, coarbF(ad, coarbF(ae, coarbF(af, coarbF(ag, coarbF(ah, c)))))))).coarbitrary(Function.curry(f), g);
            }
        };
    }

    public static <A> Coarbitrary<Option<A>> coarbOption(Coarbitrary<A> ca) {
        return new Coarbitrary<Option<A>>() { // from class: fj.test.Coarbitrary.20
            {
                Coarbitrary.this = ca;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((Option) ((Option) obj), gen);
            }

            public <B> Gen<B> coarbitrary(Option<A> o, Gen<B> g) {
                return o.isNone() ? Variant.variant(0L, g) : Variant.variant(1L, Coarbitrary.this.coarbitrary(o.some(), g));
            }
        };
    }

    public static <A, B> Coarbitrary<Either<A, B>> coarbEither(Coarbitrary<A> ca, final Coarbitrary<B> cb) {
        return new Coarbitrary<Either<A, B>>() { // from class: fj.test.Coarbitrary.21
            {
                Coarbitrary.this = ca;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(Either<A, B> e, Gen<X> g) {
                if (e.isLeft()) {
                    return Variant.variant(0L, Coarbitrary.this.coarbitrary(e.left().value(), g));
                }
                return Variant.variant(1L, cb.coarbitrary(e.right().value(), g));
            }
        };
    }

    public static <A> Coarbitrary<List<A>> coarbList(Coarbitrary<A> ca) {
        return new Coarbitrary<List<A>>() { // from class: fj.test.Coarbitrary.22
            {
                Coarbitrary.this = ca;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((List) ((List) obj), gen);
            }

            public <B> Gen<B> coarbitrary(List<A> as, Gen<B> g) {
                if (as.isEmpty()) {
                    return Variant.variant(0L, g);
                }
                return Variant.variant(1L, Coarbitrary.this.coarbitrary(as.head(), coarbitrary((List) as.tail(), (Gen) g)));
            }
        };
    }

    public static <A> Coarbitrary<Stream<A>> coarbStream(Coarbitrary<A> ca) {
        return new Coarbitrary<Stream<A>>() { // from class: fj.test.Coarbitrary.26
            {
                Coarbitrary.this = ca;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((Stream) ((Stream) obj), gen);
            }

            public <B> Gen<B> coarbitrary(Stream<A> as, Gen<B> g) {
                if (as.isEmpty()) {
                    return Variant.variant(0L, g);
                }
                return Variant.variant(1L, Coarbitrary.this.coarbitrary(as.head(), coarbitrary((Stream) as.tail()._1(), (Gen) g)));
            }
        };
    }

    public static Coarbitrary<LcgRng> coarbLcgRng() {
        return new Coarbitrary<LcgRng>() { // from class: fj.test.Coarbitrary.27
            @Override // fj.test.Coarbitrary
            public <B> Gen<B> coarbitrary(LcgRng rng, Gen<B> g) {
                long i = rng.getSeed();
                return Variant.variant(i >= 0 ? 2 * i : ((-2) * i) + 1, g);
            }
        };
    }

    /* renamed from: fj.test.Coarbitrary$28 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Coarbitrary$28.class */
    static class AnonymousClass28 extends Coarbitrary<State<S, A>> {
        final /* synthetic */ Arbitrary val$as;
        final /* synthetic */ F2 val$f;

        AnonymousClass28(Arbitrary arbitrary, F2 f2) {
            this.val$as = arbitrary;
            this.val$f = f2;
        }

        @Override // fj.test.Coarbitrary
        public <B> Gen<B> coarbitrary(State<S, A> s1, Gen<B> g) {
            return this.val$as.gen.bind(Coarbitrary$28$$Lambda$1.lambdaFactory$(s1, this.val$f, g));
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Gen lambda$coarbitrary$160(State state, F2 f2, Gen gen, Object r) {
            P2 run = state.run(r);
            return Variant.variant(((Long) f2.f(run._1(), run._2())).longValue(), gen);
        }
    }

    public static <S, A> Coarbitrary<State<S, A>> coarbState(Arbitrary<S> as, F2<S, A, Long> f) {
        return new AnonymousClass28(as, f);
    }

    public static <A> Coarbitrary<Array<A>> coarbArray(Coarbitrary<A> ca) {
        return new Coarbitrary<Array<A>>() { // from class: fj.test.Coarbitrary.29
            {
                Coarbitrary.this = ca;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((Array) ((Array) obj), gen);
            }

            public <B> Gen<B> coarbitrary(Array<A> as, Gen<B> g) {
                return coarbList(Coarbitrary.this).coarbitrary(as.toList(), g);
            }
        };
    }

    public static Coarbitrary<Throwable> coarbThrowable(Coarbitrary<String> cs) {
        return cs.comap(new F<Throwable, String>() { // from class: fj.test.Coarbitrary.30
            @Override // fj.F
            public String f(Throwable t) {
                return t.getMessage();
            }
        });
    }

    public static <A> Coarbitrary<ArrayList<A>> coarbArrayList(Coarbitrary<A> ca) {
        return new Coarbitrary<ArrayList<A>>() { // from class: fj.test.Coarbitrary.31
            {
                Coarbitrary.this = ca;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((ArrayList) ((ArrayList) obj), gen);
            }

            public <B> Gen<B> coarbitrary(ArrayList<A> as, Gen<B> g) {
                return coarbArray(Coarbitrary.this).coarbitrary(Array.array(as.toArray(new Object[as.size()])), g);
            }
        };
    }

    public static <K extends Enum<K>, V> Coarbitrary<EnumMap<K, V>> coarbEnumMap(Coarbitrary<K> ck, final Coarbitrary<V> cv) {
        return (Coarbitrary<EnumMap<K, V>>) new Coarbitrary<EnumMap<K, V>>() { // from class: fj.test.Coarbitrary.35
            {
                Coarbitrary.this = ck;
            }

            @Override // fj.test.Coarbitrary
            public <B> Gen<B> coarbitrary(EnumMap<K, V> m, Gen<B> g) {
                return coarbHashtable(Coarbitrary.this, cv).coarbitrary(new Hashtable(m), g);
            }
        };
    }

    public static <A extends Enum<A>> Coarbitrary<EnumSet<A>> coarbEnumSet(Coarbitrary<A> c) {
        return new Coarbitrary<EnumSet<A>>() { // from class: fj.test.Coarbitrary.36
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((EnumSet) ((EnumSet) obj), gen);
            }

            public <B> Gen<B> coarbitrary(EnumSet<A> as, Gen<B> g) {
                return coarbHashSet(Coarbitrary.this).coarbitrary(new HashSet(as), g);
            }
        };
    }

    public static <K, V> Coarbitrary<HashMap<K, V>> coarbHashMap(Coarbitrary<K> ck, final Coarbitrary<V> cv) {
        return new Coarbitrary<HashMap<K, V>>() { // from class: fj.test.Coarbitrary.38
            {
                Coarbitrary.this = ck;
            }

            @Override // fj.test.Coarbitrary
            public <B> Gen<B> coarbitrary(HashMap<K, V> m, Gen<B> g) {
                return coarbHashtable(Coarbitrary.this, cv).coarbitrary(new Hashtable(m), g);
            }
        };
    }

    public static <A> Coarbitrary<HashSet<A>> coarbHashSet(Coarbitrary<A> c) {
        return new Coarbitrary<HashSet<A>>() { // from class: fj.test.Coarbitrary.39
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((HashSet) ((HashSet) obj), gen);
            }

            public <B> Gen<B> coarbitrary(HashSet<A> as, Gen<B> g) {
                return coarbArray(Coarbitrary.this).coarbitrary(Array.array(as.toArray(new Object[as.size()])), g);
            }
        };
    }

    public static <K, V> Coarbitrary<Hashtable<K, V>> coarbHashtable(Coarbitrary<K> ck, final Coarbitrary<V> cv) {
        return new Coarbitrary<Hashtable<K, V>>() { // from class: fj.test.Coarbitrary.40
            {
                Coarbitrary.this = ck;
            }

            @Override // fj.test.Coarbitrary
            public <B> Gen<B> coarbitrary(Hashtable<K, V> h, Gen<B> g) {
                List nil = List.nil();
                for (Object obj : h.keySet()) {
                    nil = nil.snoc(P.p(obj, h.get(obj)));
                }
                return coarbList(coarbP2(Coarbitrary.this, cv)).coarbitrary(nil, g);
            }
        };
    }

    public static <K, V> Coarbitrary<IdentityHashMap<K, V>> coarbIdentityHashMap(Coarbitrary<K> ck, final Coarbitrary<V> cv) {
        return new Coarbitrary<IdentityHashMap<K, V>>() { // from class: fj.test.Coarbitrary.41
            {
                Coarbitrary.this = ck;
            }

            @Override // fj.test.Coarbitrary
            public <B> Gen<B> coarbitrary(IdentityHashMap<K, V> m, Gen<B> g) {
                return coarbHashtable(Coarbitrary.this, cv).coarbitrary(new Hashtable(m), g);
            }
        };
    }

    public static <K, V> Coarbitrary<LinkedHashMap<K, V>> coarbLinkedHashMap(Coarbitrary<K> ck, final Coarbitrary<V> cv) {
        return new Coarbitrary<LinkedHashMap<K, V>>() { // from class: fj.test.Coarbitrary.42
            {
                Coarbitrary.this = ck;
            }

            @Override // fj.test.Coarbitrary
            public <B> Gen<B> coarbitrary(LinkedHashMap<K, V> m, Gen<B> g) {
                return coarbHashtable(Coarbitrary.this, cv).coarbitrary(new Hashtable(m), g);
            }
        };
    }

    public static <A> Coarbitrary<LinkedHashSet<A>> coarbLinkedHashSet(Coarbitrary<A> c) {
        return new Coarbitrary<LinkedHashSet<A>>() { // from class: fj.test.Coarbitrary.43
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((LinkedHashSet) ((LinkedHashSet) obj), gen);
            }

            public <B> Gen<B> coarbitrary(LinkedHashSet<A> as, Gen<B> g) {
                return coarbHashSet(Coarbitrary.this).coarbitrary(new HashSet(as), g);
            }
        };
    }

    public static <A> Coarbitrary<LinkedList<A>> coarbLinkedList(Coarbitrary<A> c) {
        return new Coarbitrary<LinkedList<A>>() { // from class: fj.test.Coarbitrary.44
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((LinkedList) ((LinkedList) obj), gen);
            }

            public <B> Gen<B> coarbitrary(LinkedList<A> as, Gen<B> g) {
                return coarbArray(Coarbitrary.this).coarbitrary(Array.array(as.toArray(new Object[as.size()])), g);
            }
        };
    }

    public static <A> Coarbitrary<PriorityQueue<A>> coarbPriorityQueue(Coarbitrary<A> c) {
        return new Coarbitrary<PriorityQueue<A>>() { // from class: fj.test.Coarbitrary.45
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((PriorityQueue) ((PriorityQueue) obj), gen);
            }

            public <B> Gen<B> coarbitrary(PriorityQueue<A> as, Gen<B> g) {
                return coarbArray(Coarbitrary.this).coarbitrary(Array.array(as.toArray(new Object[as.size()])), g);
            }
        };
    }

    public static <A> Coarbitrary<Stack<A>> coarbStack(Coarbitrary<A> c) {
        return new Coarbitrary<Stack<A>>() { // from class: fj.test.Coarbitrary.47
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((Stack) ((Stack) obj), gen);
            }

            public <B> Gen<B> coarbitrary(Stack<A> as, Gen<B> g) {
                return coarbArray(Coarbitrary.this).coarbitrary(Array.array(as.toArray(new Object[as.size()])), g);
            }
        };
    }

    public static <K, V> Coarbitrary<TreeMap<K, V>> coarbTreeMap(Coarbitrary<K> ck, final Coarbitrary<V> cv) {
        return new Coarbitrary<TreeMap<K, V>>() { // from class: fj.test.Coarbitrary.48
            {
                Coarbitrary.this = ck;
            }

            @Override // fj.test.Coarbitrary
            public <B> Gen<B> coarbitrary(TreeMap<K, V> m, Gen<B> g) {
                return coarbHashtable(Coarbitrary.this, cv).coarbitrary(new Hashtable(m), g);
            }
        };
    }

    public static <A> Coarbitrary<TreeSet<A>> coarbTreeSet(Coarbitrary<A> c) {
        return new Coarbitrary<TreeSet<A>>() { // from class: fj.test.Coarbitrary.49
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((TreeSet) ((TreeSet) obj), gen);
            }

            public <B> Gen<B> coarbitrary(TreeSet<A> as, Gen<B> g) {
                return coarbHashSet(Coarbitrary.this).coarbitrary(new HashSet(as), g);
            }
        };
    }

    public static <A> Coarbitrary<Vector<A>> coarbVector(Coarbitrary<A> c) {
        return new Coarbitrary<Vector<A>>() { // from class: fj.test.Coarbitrary.50
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((Vector) ((Vector) obj), gen);
            }

            public <B> Gen<B> coarbitrary(Vector<A> as, Gen<B> g) {
                return coarbArray(Coarbitrary.this).coarbitrary(Array.array(as.toArray(new Object[as.size()])), g);
            }
        };
    }

    public static <K, V> Coarbitrary<WeakHashMap<K, V>> coarbWeakHashMap(Coarbitrary<K> ck, final Coarbitrary<V> cv) {
        return new Coarbitrary<WeakHashMap<K, V>>() { // from class: fj.test.Coarbitrary.51
            {
                Coarbitrary.this = ck;
            }

            @Override // fj.test.Coarbitrary
            public <B> Gen<B> coarbitrary(WeakHashMap<K, V> m, Gen<B> g) {
                return coarbHashtable(Coarbitrary.this, cv).coarbitrary(new Hashtable(m), g);
            }
        };
    }

    public static <A> Coarbitrary<ArrayBlockingQueue<A>> coarbArrayBlockingQueue(Coarbitrary<A> c) {
        return new Coarbitrary<ArrayBlockingQueue<A>>() { // from class: fj.test.Coarbitrary.52
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((ArrayBlockingQueue) ((ArrayBlockingQueue) obj), gen);
            }

            public <B> Gen<B> coarbitrary(ArrayBlockingQueue<A> as, Gen<B> g) {
                return coarbArray(Coarbitrary.this).coarbitrary(Array.array(as.toArray(new Object[as.size()])), g);
            }
        };
    }

    public static <K, V> Coarbitrary<ConcurrentHashMap<K, V>> coarbConcurrentHashMap(Coarbitrary<K> ck, final Coarbitrary<V> cv) {
        return new Coarbitrary<ConcurrentHashMap<K, V>>() { // from class: fj.test.Coarbitrary.53
            {
                Coarbitrary.this = ck;
            }

            @Override // fj.test.Coarbitrary
            public <B> Gen<B> coarbitrary(ConcurrentHashMap<K, V> m, Gen<B> g) {
                return coarbHashtable(Coarbitrary.this, cv).coarbitrary(new Hashtable(m), g);
            }
        };
    }

    public static <A> Coarbitrary<ConcurrentLinkedQueue<A>> coarbConcurrentLinkedQueue(Coarbitrary<A> c) {
        return new Coarbitrary<ConcurrentLinkedQueue<A>>() { // from class: fj.test.Coarbitrary.54
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((ConcurrentLinkedQueue) ((ConcurrentLinkedQueue) obj), gen);
            }

            public <B> Gen<B> coarbitrary(ConcurrentLinkedQueue<A> as, Gen<B> g) {
                return coarbArray(Coarbitrary.this).coarbitrary(Array.array(as.toArray(new Object[as.size()])), g);
            }
        };
    }

    public static <A> Coarbitrary<CopyOnWriteArrayList<A>> coarbCopyOnWriteArrayList(Coarbitrary<A> c) {
        return new Coarbitrary<CopyOnWriteArrayList<A>>() { // from class: fj.test.Coarbitrary.55
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((CopyOnWriteArrayList) ((CopyOnWriteArrayList) obj), gen);
            }

            public <B> Gen<B> coarbitrary(CopyOnWriteArrayList<A> as, Gen<B> g) {
                return coarbArray(Coarbitrary.this).coarbitrary(Array.array(as.toArray(new Object[as.size()])), g);
            }
        };
    }

    public static <A> Coarbitrary<CopyOnWriteArraySet<A>> coarbCopyOnWriteArraySet(Coarbitrary<A> c) {
        return new Coarbitrary<CopyOnWriteArraySet<A>>() { // from class: fj.test.Coarbitrary.56
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((CopyOnWriteArraySet) ((CopyOnWriteArraySet) obj), gen);
            }

            public <B> Gen<B> coarbitrary(CopyOnWriteArraySet<A> as, Gen<B> g) {
                return coarbArray(Coarbitrary.this).coarbitrary(Array.array(as.toArray(new Object[as.size()])), g);
            }
        };
    }

    public static <A extends Delayed> Coarbitrary<DelayQueue<A>> coarbDelayQueue(Coarbitrary<A> c) {
        return new Coarbitrary<DelayQueue<A>>() { // from class: fj.test.Coarbitrary.57
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((DelayQueue) ((DelayQueue) obj), gen);
            }

            public <B> Gen<B> coarbitrary(DelayQueue<A> as, Gen<B> g) {
                return coarbArray(Coarbitrary.this).coarbitrary(Array.array(as.toArray((Delayed[]) new Object[as.size()])), g);
            }
        };
    }

    public static <A> Coarbitrary<LinkedBlockingQueue<A>> coarbLinkedBlockingQueue(Coarbitrary<A> c) {
        return new Coarbitrary<LinkedBlockingQueue<A>>() { // from class: fj.test.Coarbitrary.58
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((LinkedBlockingQueue) ((LinkedBlockingQueue) obj), gen);
            }

            public <B> Gen<B> coarbitrary(LinkedBlockingQueue<A> as, Gen<B> g) {
                return coarbArray(Coarbitrary.this).coarbitrary(Array.array(as.toArray(new Object[as.size()])), g);
            }
        };
    }

    public static <A> Coarbitrary<PriorityBlockingQueue<A>> coarbPriorityBlockingQueue(Coarbitrary<A> c) {
        return new Coarbitrary<PriorityBlockingQueue<A>>() { // from class: fj.test.Coarbitrary.59
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((PriorityBlockingQueue) ((PriorityBlockingQueue) obj), gen);
            }

            public <B> Gen<B> coarbitrary(PriorityBlockingQueue<A> as, Gen<B> g) {
                return coarbArray(Coarbitrary.this).coarbitrary(Array.array(as.toArray(new Object[as.size()])), g);
            }
        };
    }

    public static <A> Coarbitrary<SynchronousQueue<A>> coarbSynchronousQueue(Coarbitrary<A> c) {
        return new Coarbitrary<SynchronousQueue<A>>() { // from class: fj.test.Coarbitrary.60
            {
                Coarbitrary.this = c;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((SynchronousQueue) ((SynchronousQueue) obj), gen);
            }

            public <B> Gen<B> coarbitrary(SynchronousQueue<A> as, Gen<B> g) {
                return coarbArray(Coarbitrary.this).coarbitrary(Array.array(as.toArray(new Object[as.size()])), g);
            }
        };
    }

    public static <A> Coarbitrary<P1<A>> coarbP1(Coarbitrary<A> ca) {
        return new Coarbitrary<P1<A>>() { // from class: fj.test.Coarbitrary.66
            {
                Coarbitrary.this = ca;
            }

            @Override // fj.test.Coarbitrary
            public /* bridge */ /* synthetic */ Gen coarbitrary(Object obj, Gen gen) {
                return coarbitrary((P1) ((P1) obj), gen);
            }

            public <B> Gen<B> coarbitrary(P1<A> p, Gen<B> g) {
                return Coarbitrary.this.coarbitrary(p._1(), g);
            }
        };
    }

    public static <A, B> Coarbitrary<P2<A, B>> coarbP2(Coarbitrary<A> ca, final Coarbitrary<B> cb) {
        return new Coarbitrary<P2<A, B>>() { // from class: fj.test.Coarbitrary.67
            {
                Coarbitrary.this = ca;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(P2<A, B> p, Gen<X> g) {
                return Coarbitrary.this.coarbitrary(p._1(), cb.coarbitrary(p._2(), g));
            }
        };
    }

    public static <A, B, C> Coarbitrary<P3<A, B, C>> coarbP3(Coarbitrary<A> ca, final Coarbitrary<B> cb, final Coarbitrary<C> cc) {
        return new Coarbitrary<P3<A, B, C>>() { // from class: fj.test.Coarbitrary.68
            {
                Coarbitrary.this = ca;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(P3<A, B, C> p, Gen<X> g) {
                return Coarbitrary.this.coarbitrary(p._1(), cb.coarbitrary(p._2(), cc.coarbitrary(p._3(), g)));
            }
        };
    }

    public static <A, B, C, D> Coarbitrary<P4<A, B, C, D>> coarbP4(Coarbitrary<A> ca, final Coarbitrary<B> cb, final Coarbitrary<C> cc, final Coarbitrary<D> cd) {
        return new Coarbitrary<P4<A, B, C, D>>() { // from class: fj.test.Coarbitrary.69
            {
                Coarbitrary.this = ca;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(P4<A, B, C, D> p, Gen<X> g) {
                return Coarbitrary.this.coarbitrary(p._1(), cb.coarbitrary(p._2(), cc.coarbitrary(p._3(), cd.coarbitrary(p._4(), g))));
            }
        };
    }

    public static <A, B, C, D, E> Coarbitrary<P5<A, B, C, D, E>> coarbP5(Coarbitrary<A> ca, final Coarbitrary<B> cb, final Coarbitrary<C> cc, final Coarbitrary<D> cd, final Coarbitrary<E> ce) {
        return new Coarbitrary<P5<A, B, C, D, E>>() { // from class: fj.test.Coarbitrary.70
            {
                Coarbitrary.this = ca;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(P5<A, B, C, D, E> p, Gen<X> g) {
                return Coarbitrary.this.coarbitrary(p._1(), cb.coarbitrary(p._2(), cc.coarbitrary(p._3(), cd.coarbitrary(p._4(), ce.coarbitrary(p._5(), g)))));
            }
        };
    }

    public static <A, B, C, D, E, F$> Coarbitrary<P6<A, B, C, D, E, F$>> coarbP6(Coarbitrary<A> ca, final Coarbitrary<B> cb, final Coarbitrary<C> cc, final Coarbitrary<D> cd, final Coarbitrary<E> ce, final Coarbitrary<F$> cf) {
        return new Coarbitrary<P6<A, B, C, D, E, F$>>() { // from class: fj.test.Coarbitrary.71
            {
                Coarbitrary.this = ca;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(P6<A, B, C, D, E, F$> p, Gen<X> g) {
                return Coarbitrary.this.coarbitrary(p._1(), cb.coarbitrary(p._2(), cc.coarbitrary(p._3(), cd.coarbitrary(p._4(), ce.coarbitrary(p._5(), cf.coarbitrary(p._6(), g))))));
            }
        };
    }

    public static <A, B, C, D, E, F$, G> Coarbitrary<P7<A, B, C, D, E, F$, G>> coarbP7(Coarbitrary<A> ca, final Coarbitrary<B> cb, final Coarbitrary<C> cc, final Coarbitrary<D> cd, final Coarbitrary<E> ce, final Coarbitrary<F$> cf, final Coarbitrary<G> cg) {
        return new Coarbitrary<P7<A, B, C, D, E, F$, G>>() { // from class: fj.test.Coarbitrary.72
            {
                Coarbitrary.this = ca;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(P7<A, B, C, D, E, F$, G> p, Gen<X> g) {
                return Coarbitrary.this.coarbitrary(p._1(), cb.coarbitrary(p._2(), cc.coarbitrary(p._3(), cd.coarbitrary(p._4(), ce.coarbitrary(p._5(), cf.coarbitrary(p._6(), cg.coarbitrary(p._7(), g)))))));
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H> Coarbitrary<P8<A, B, C, D, E, F$, G, H>> coarbP8(Coarbitrary<A> ca, final Coarbitrary<B> cb, final Coarbitrary<C> cc, final Coarbitrary<D> cd, final Coarbitrary<E> ce, final Coarbitrary<F$> cf, final Coarbitrary<G> cg, final Coarbitrary<H> ch) {
        return new Coarbitrary<P8<A, B, C, D, E, F$, G, H>>() { // from class: fj.test.Coarbitrary.73
            {
                Coarbitrary.this = ca;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.test.Coarbitrary
            public <X> Gen<X> coarbitrary(P8<A, B, C, D, E, F$, G, H> p, Gen<X> g) {
                return Coarbitrary.this.coarbitrary(p._1(), cb.coarbitrary(p._2(), cc.coarbitrary(p._3(), cd.coarbitrary(p._4(), ce.coarbitrary(p._5(), cf.coarbitrary(p._6(), cg.coarbitrary(p._7(), ch.coarbitrary(p._8(), g))))))));
            }
        };
    }
}
