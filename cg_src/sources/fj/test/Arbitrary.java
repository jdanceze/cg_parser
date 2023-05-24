package fj.test;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
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
import fj.data.Enumerator;
import fj.data.List;
import fj.data.Option;
import fj.data.Reader;
import fj.data.State;
import fj.data.Stream;
import fj.function.Effect1;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
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
import java.util.Locale;
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
import javax.resource.spi.work.WorkManager;
import soot.jimple.spark.geom.geomPA.Constants;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary.class */
public final class Arbitrary<A> {
    public final Gen<A> gen;
    public static final Arbitrary<Boolean> arbBoolean = arbitrary(Gen.elements(true, false));
    public static final Arbitrary<Integer> arbInteger = arbitrary(Gen.sized(new F<Integer, Gen<Integer>>() { // from class: fj.test.Arbitrary.2
        @Override // fj.F
        public Gen<Integer> f(Integer i) {
            return Gen.choose(-i.intValue(), i.intValue());
        }
    }));
    public static final Arbitrary<Integer> arbIntegerBoundaries = arbitrary(Gen.sized(new F<Integer, Gen<Integer>>() { // from class: fj.test.Arbitrary.3
        @Override // fj.F
        public Gen<Integer> f(Integer i) {
            return Gen.frequency(List.list(P.p(1, Gen.value(0)), P.p(1, Gen.value(1)), P.p(1, Gen.value(-1)), P.p(1, Gen.value(Integer.MAX_VALUE)), P.p(1, Gen.value(Integer.MIN_VALUE)), P.p(1, Gen.value(2147483646)), P.p(1, Gen.value(-2147483647)), P.p(93, Arbitrary.arbInteger.gen)));
        }
    }));
    public static final Arbitrary<Long> arbLong = arbitrary(arbInteger.gen.bind(arbInteger.gen, new F<Integer, F<Integer, Long>>() { // from class: fj.test.Arbitrary.4
        @Override // fj.F
        public F<Integer, Long> f(final Integer i1) {
            return new F<Integer, Long>() { // from class: fj.test.Arbitrary.4.1
                {
                    AnonymousClass4.this = this;
                }

                @Override // fj.F
                public Long f(Integer i2) {
                    return Long.valueOf((i1.intValue() << 32) & i2.intValue());
                }
            };
        }
    }));
    public static final Arbitrary<Long> arbLongBoundaries = arbitrary(Gen.sized(new F<Integer, Gen<Long>>() { // from class: fj.test.Arbitrary.5
        @Override // fj.F
        public Gen<Long> f(Integer i) {
            return Gen.frequency(List.list(P.p(1, Gen.value(0L)), P.p(1, Gen.value(1L)), P.p(1, Gen.value(-1L)), P.p(1, Gen.value(Long.valueOf((long) WorkManager.INDEFINITE))), P.p(1, Gen.value(Long.MIN_VALUE)), P.p(1, Gen.value(Long.valueOf((long) Constants.MAX_CONTEXTS))), P.p(1, Gen.value(-9223372036854775807L)), P.p(93, Arbitrary.arbLong.gen)));
        }
    }));
    public static final Arbitrary<Byte> arbByte = arbitrary(arbInteger.gen.map(new F<Integer, Byte>() { // from class: fj.test.Arbitrary.6
        @Override // fj.F
        public Byte f(Integer i) {
            return Byte.valueOf((byte) i.intValue());
        }
    }));
    public static final Arbitrary<Byte> arbByteBoundaries = arbitrary(Gen.sized(new F<Integer, Gen<Byte>>() { // from class: fj.test.Arbitrary.7
        @Override // fj.F
        public Gen<Byte> f(Integer i) {
            return Gen.frequency(List.list(P.p(1, Gen.value((byte) 0)), P.p(1, Gen.value((byte) 1)), P.p(1, Gen.value((byte) -1)), P.p(1, Gen.value(Byte.MAX_VALUE)), P.p(1, Gen.value(Byte.MIN_VALUE)), P.p(1, Gen.value((byte) 126)), P.p(1, Gen.value((byte) -127)), P.p(93, Arbitrary.arbByte.gen)));
        }
    }));
    public static final Arbitrary<Short> arbShort = arbitrary(arbInteger.gen.map(new F<Integer, Short>() { // from class: fj.test.Arbitrary.8
        @Override // fj.F
        public Short f(Integer i) {
            return Short.valueOf((short) i.intValue());
        }
    }));
    public static final Arbitrary<Short> arbShortBoundaries = arbitrary(Gen.sized(new F<Integer, Gen<Short>>() { // from class: fj.test.Arbitrary.9
        @Override // fj.F
        public Gen<Short> f(Integer i) {
            return Gen.frequency(List.list(P.p(1, Gen.value((short) 0)), P.p(1, Gen.value((short) 1)), P.p(1, Gen.value((short) -1)), P.p(1, Gen.value(Short.MAX_VALUE)), P.p(1, Gen.value(Short.MIN_VALUE)), P.p(1, Gen.value((short) 32766)), P.p(1, Gen.value((short) -32767)), P.p(93, Arbitrary.arbShort.gen)));
        }
    }));
    public static final Arbitrary<Character> arbCharacter = arbitrary(Gen.choose(0, 65536).map(new F<Integer, Character>() { // from class: fj.test.Arbitrary.10
        @Override // fj.F
        public Character f(Integer i) {
            return Character.valueOf((char) i.intValue());
        }
    }));
    public static final Arbitrary<Character> arbCharacterBoundaries = arbitrary(Gen.sized(new F<Integer, Gen<Character>>() { // from class: fj.test.Arbitrary.11
        @Override // fj.F
        public Gen<Character> f(Integer i) {
            return Gen.frequency(List.list(P.p(1, Gen.value((char) 0)), P.p(1, Gen.value((char) 1)), P.p(1, Gen.value((char) 65535)), P.p(1, Gen.value((char) 65534)), P.p(95, Arbitrary.arbCharacter.gen)));
        }
    }));
    public static final Arbitrary<Double> arbDouble = arbitrary(Gen.sized(new F<Integer, Gen<Double>>() { // from class: fj.test.Arbitrary.12
        @Override // fj.F
        public Gen<Double> f(Integer i) {
            return Gen.choose(-i.intValue(), i.intValue());
        }
    }));
    public static final Arbitrary<Double> arbDoubleBoundaries = arbitrary(Gen.sized(new F<Integer, Gen<Double>>() { // from class: fj.test.Arbitrary.13
        @Override // fj.F
        public Gen<Double> f(Integer i) {
            return Gen.frequency(List.list(P.p(1, Gen.value(Double.valueOf((double) Const.default_value_double))), P.p(1, Gen.value(Double.valueOf(1.0d))), P.p(1, Gen.value(Double.valueOf(-1.0d))), P.p(1, Gen.value(Double.valueOf(Double.MAX_VALUE))), P.p(1, Gen.value(Double.valueOf(Double.MIN_VALUE))), P.p(1, Gen.value(Double.valueOf(Double.NaN))), P.p(1, Gen.value(Double.valueOf(Double.NEGATIVE_INFINITY))), P.p(1, Gen.value(Double.valueOf(Double.POSITIVE_INFINITY))), P.p(1, Gen.value(Double.valueOf(Double.MAX_VALUE))), P.p(91, Arbitrary.arbDouble.gen)));
        }
    }));
    public static final Arbitrary<Float> arbFloat = arbitrary(arbDouble.gen.map(new F<Double, Float>() { // from class: fj.test.Arbitrary.14
        @Override // fj.F
        public Float f(Double d) {
            return Float.valueOf((float) d.doubleValue());
        }
    }));
    public static final Arbitrary<Float> arbFloatBoundaries = arbitrary(Gen.sized(new F<Integer, Gen<Float>>() { // from class: fj.test.Arbitrary.15
        @Override // fj.F
        public Gen<Float> f(Integer i) {
            return Gen.frequency(List.list(P.p(1, Gen.value(Float.valueOf(0.0f))), P.p(1, Gen.value(Float.valueOf(1.0f))), P.p(1, Gen.value(Float.valueOf(-1.0f))), P.p(1, Gen.value(Float.valueOf(Float.MAX_VALUE))), P.p(1, Gen.value(Float.valueOf(Float.MIN_VALUE))), P.p(1, Gen.value(Float.valueOf(Float.NaN))), P.p(1, Gen.value(Float.valueOf(Float.NEGATIVE_INFINITY))), P.p(1, Gen.value(Float.valueOf(Float.POSITIVE_INFINITY))), P.p(1, Gen.value(Float.valueOf(Float.MAX_VALUE))), P.p(91, Arbitrary.arbFloat.gen)));
        }
    }));
    public static final Arbitrary<String> arbString = arbitrary(arbList(arbCharacter).gen.map(new F<List<Character>, String>() { // from class: fj.test.Arbitrary.16
        @Override // fj.F
        public String f(List<Character> cs) {
            return List.asString(cs);
        }
    }));
    public static final Arbitrary<String> arbUSASCIIString = arbitrary(arbList(arbCharacter).gen.map(new F<List<Character>, String>() { // from class: fj.test.Arbitrary.17
        @Override // fj.F
        public String f(List<Character> cs) {
            return List.asString(cs.map(new F<Character, Character>() { // from class: fj.test.Arbitrary.17.1
                {
                    AnonymousClass17.this = this;
                }

                @Override // fj.F
                public Character f(Character c) {
                    return Character.valueOf((char) (c.charValue() % 128));
                }
            }));
        }
    }));
    public static final Arbitrary<String> arbAlphaNumString = arbitrary(arbList(arbitrary(Gen.elements(Stream.range(Enumerator.charEnumerator, 'a', 'z').append(Stream.range(Enumerator.charEnumerator, 'A', 'Z')).append(Stream.range(Enumerator.charEnumerator, '0', '9')).toArray().array(Character[].class)))).gen.map(List.asString()));
    public static final Arbitrary<StringBuffer> arbStringBuffer = arbitrary(arbString.gen.map(new F<String, StringBuffer>() { // from class: fj.test.Arbitrary.18
        @Override // fj.F
        public StringBuffer f(String s) {
            return new StringBuffer(s);
        }
    }));
    public static final Arbitrary<StringBuilder> arbStringBuilder = arbitrary(arbString.gen.map(new F<String, StringBuilder>() { // from class: fj.test.Arbitrary.19
        @Override // fj.F
        public StringBuilder f(String s) {
            return new StringBuilder(s);
        }
    }));
    public static final Arbitrary<Throwable> arbThrowable = arbThrowable(arbString);
    public static final Arbitrary<BitSet> arbBitSet = arbitrary(arbList(arbBoolean).gen.map(new F<List<Boolean>, BitSet>() { // from class: fj.test.Arbitrary.28
        @Override // fj.F
        public BitSet f(List<Boolean> bs) {
            final BitSet s = new BitSet(bs.length());
            bs.zipIndex().foreachDoEffect(new Effect1<P2<Boolean, Integer>>() { // from class: fj.test.Arbitrary.28.1
                {
                    AnonymousClass28.this = this;
                }

                @Override // fj.function.Effect1
                public void f(P2<Boolean, Integer> bi) {
                    s.set(bi._2().intValue(), bi._1().booleanValue());
                }
            });
            return s;
        }
    }));
    public static final Arbitrary<Calendar> arbCalendar = arbitrary(arbLong.gen.map(new F<Long, Calendar>() { // from class: fj.test.Arbitrary.29
        @Override // fj.F
        public Calendar f(Long i) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(i.longValue());
            return c;
        }
    }));
    public static final Arbitrary<Date> arbDate = arbitrary(arbLong.gen.map(new F<Long, Date>() { // from class: fj.test.Arbitrary.30
        @Override // fj.F
        public Date f(Long i) {
            return new Date(i.longValue());
        }
    }));
    public static final Arbitrary<GregorianCalendar> arbGregorianCalendar = arbitrary(arbLong.gen.map(new F<Long, GregorianCalendar>() { // from class: fj.test.Arbitrary.33
        @Override // fj.F
        public GregorianCalendar f(Long i) {
            GregorianCalendar c = new GregorianCalendar();
            c.setTimeInMillis(i.longValue());
            return c;
        }
    }));
    public static final Arbitrary<Properties> arbProperties = arbitrary(arbHashtable(arbString, arbString).gen.map(new F<Hashtable<String, String>, Properties>() { // from class: fj.test.Arbitrary.42
        @Override // fj.F
        public Properties f(Hashtable<String, String> ht) {
            Properties p = new Properties();
            for (String k : ht.keySet()) {
                p.setProperty(k, ht.get(k));
            }
            return p;
        }
    }));
    public static final Arbitrary<java.sql.Date> arbSQLDate = arbitrary(arbLong.gen.map(new F<Long, java.sql.Date>() { // from class: fj.test.Arbitrary.57
        @Override // fj.F
        public java.sql.Date f(Long i) {
            return new java.sql.Date(i.longValue());
        }
    }));
    public static final Arbitrary<Time> arbTime = arbitrary(arbLong.gen.map(new F<Long, Time>() { // from class: fj.test.Arbitrary.58
        @Override // fj.F
        public Time f(Long i) {
            return new Time(i.longValue());
        }
    }));
    public static final Arbitrary<Timestamp> arbTimestamp = arbitrary(arbLong.gen.map(new F<Long, Timestamp>() { // from class: fj.test.Arbitrary.59
        @Override // fj.F
        public Timestamp f(Long i) {
            return new Timestamp(i.longValue());
        }
    }));
    public static final Arbitrary<BigInteger> arbBigInteger = arbitrary(arbArray(arbByte).gen.bind(arbByte.gen, new F<Array<Byte>, F<Byte, BigInteger>>() { // from class: fj.test.Arbitrary.60
        @Override // fj.F
        public F<Byte, BigInteger> f(final Array<Byte> a) {
            return new F<Byte, BigInteger>() { // from class: fj.test.Arbitrary.60.1
                {
                    AnonymousClass60.this = this;
                }

                @Override // fj.F
                public BigInteger f(Byte b) {
                    byte[] x = new byte[a.length() + 1];
                    for (int i = 0; i < a.array().length; i++) {
                        x[i] = ((Byte) a.get(i)).byteValue();
                    }
                    x[a.length()] = b.byteValue();
                    return new BigInteger(x);
                }
            };
        }
    }));
    public static final Arbitrary<BigDecimal> arbBigDecimal = arbitrary(arbBigInteger.gen.map(new F<BigInteger, BigDecimal>() { // from class: fj.test.Arbitrary.61
        @Override // fj.F
        public BigDecimal f(BigInteger i) {
            return new BigDecimal(i);
        }
    }));
    public static final Arbitrary<Locale> arbLocale = arbitrary(Gen.elements(Locale.getAvailableLocales()));

    private Arbitrary(Gen<A> gen) {
        this.gen = gen;
    }

    public static <A> Arbitrary<A> arbitrary(Gen<A> g) {
        return new Arbitrary<>(g);
    }

    public static <A, B> Arbitrary<F<A, B>> arbF(final Coarbitrary<A> c, final Arbitrary<B> a) {
        return arbitrary(Gen.promote(new F<A, Gen<B>>() { // from class: fj.test.Arbitrary.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            @Override // fj.F
            public Gen<B> f(A x) {
                return c.coarbitrary(x, a.gen);
            }
        }));
    }

    public static <A, B> Arbitrary<Reader<A, B>> arbReader(Coarbitrary<A> aa, Arbitrary<B> ab) {
        F<A, B> f;
        Gen<A> gen = arbF(aa, ab).gen;
        f = Arbitrary$$Lambda$1.instance;
        return arbitrary(gen.map(f));
    }

    public static <S, A> Arbitrary<State<S, A>> arbState(Arbitrary<S> as, Coarbitrary<S> cs, Arbitrary<A> aa) {
        F<A, B> f;
        Gen<A> gen = arbF(cs, arbP2(as, aa)).gen;
        f = Arbitrary$$Lambda$2.instance;
        return arbitrary(gen.map(f));
    }

    public static <A> Arbitrary<LcgRng> arbLcgRng() {
        F<Long, B> f;
        Gen<Long> gen = arbLong.gen;
        f = Arbitrary$$Lambda$3.instance;
        return arbitrary(gen.map(f));
    }

    public static /* synthetic */ LcgRng lambda$arbLcgRng$159(Long l) {
        return new LcgRng(l.longValue());
    }

    public static <A, B> Arbitrary<F<A, B>> arbFInvariant(Arbitrary<B> a) {
        return arbitrary(a.gen.map(Function.constant()));
    }

    public static <A, B, C> Arbitrary<F2<A, B, C>> arbF2(Coarbitrary<A> ca, Coarbitrary<B> cb, Arbitrary<C> a) {
        return arbitrary(arbF(ca, arbF(cb, a)).gen.map(Function.uncurryF2()));
    }

    public static <A, B, C> Arbitrary<F2<A, B, C>> arbF2Invariant(Arbitrary<C> a) {
        return arbitrary(a.gen.map(Function.compose(Function.uncurryF2(), Function.compose(Function.constant(), Function.constant()))));
    }

    public static <A, B, C, D> Arbitrary<F3<A, B, C, D>> arbF3(Coarbitrary<A> ca, Coarbitrary<B> cb, Coarbitrary<C> cc, Arbitrary<D> a) {
        return arbitrary(arbF(ca, arbF(cb, arbF(cc, a))).gen.map(Function.uncurryF3()));
    }

    public static <A, B, C, D> Arbitrary<F3<A, B, C, D>> arbF3Invariant(Arbitrary<D> a) {
        return arbitrary(a.gen.map(Function.compose(Function.uncurryF3(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.constant())))));
    }

    public static <A, B, C, D, E> Arbitrary<F4<A, B, C, D, E>> arbF4(Coarbitrary<A> ca, Coarbitrary<B> cb, Coarbitrary<C> cc, Coarbitrary<D> cd, Arbitrary<E> a) {
        return arbitrary(arbF(ca, arbF(cb, arbF(cc, arbF(cd, a)))).gen.map(Function.uncurryF4()));
    }

    public static <A, B, C, D, E> Arbitrary<F4<A, B, C, D, E>> arbF4Invariant(Arbitrary<E> a) {
        return arbitrary(a.gen.map(Function.compose(Function.uncurryF4(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.constant()))))));
    }

    public static <A, B, C, D, E, F$> Arbitrary<F5<A, B, C, D, E, F$>> arbF5(Coarbitrary<A> ca, Coarbitrary<B> cb, Coarbitrary<C> cc, Coarbitrary<D> cd, Coarbitrary<E> ce, Arbitrary<F$> a) {
        return arbitrary(arbF(ca, arbF(cb, arbF(cc, arbF(cd, arbF(ce, a))))).gen.map(Function.uncurryF5()));
    }

    public static <A, B, C, D, E, F$> Arbitrary<F5<A, B, C, D, E, F$>> arbF5Invariant(Arbitrary<F$> a) {
        return arbitrary(a.gen.map(Function.compose(Function.uncurryF5(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.constant())))))));
    }

    public static <A, B, C, D, E, F$, G> Arbitrary<F6<A, B, C, D, E, F$, G>> arbF6(Coarbitrary<A> ca, Coarbitrary<B> cb, Coarbitrary<C> cc, Coarbitrary<D> cd, Coarbitrary<E> ce, Coarbitrary<F$> cf, Arbitrary<G> a) {
        return arbitrary(arbF(ca, arbF(cb, arbF(cc, arbF(cd, arbF(ce, arbF(cf, a)))))).gen.map(Function.uncurryF6()));
    }

    public static <A, B, C, D, E, F$, G> Arbitrary<F6<A, B, C, D, E, F$, G>> arbF6Invariant(Arbitrary<G> a) {
        return arbitrary(a.gen.map(Function.compose(Function.uncurryF6(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.constant()))))))));
    }

    public static <A, B, C, D, E, F$, G, H> Arbitrary<F7<A, B, C, D, E, F$, G, H>> arbF7(Coarbitrary<A> ca, Coarbitrary<B> cb, Coarbitrary<C> cc, Coarbitrary<D> cd, Coarbitrary<E> ce, Coarbitrary<F$> cf, Coarbitrary<G> cg, Arbitrary<H> a) {
        return arbitrary(arbF(ca, arbF(cb, arbF(cc, arbF(cd, arbF(ce, arbF(cf, arbF(cg, a))))))).gen.map(Function.uncurryF7()));
    }

    public static <A, B, C, D, E, F$, G, H> Arbitrary<F7<A, B, C, D, E, F$, G, H>> arbF7Invariant(Arbitrary<H> a) {
        return arbitrary(a.gen.map(Function.compose(Function.uncurryF7(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.constant())))))))));
    }

    public static <A, B, C, D, E, F$, G, H, I> Arbitrary<F8<A, B, C, D, E, F$, G, H, I>> arbF8(Coarbitrary<A> ca, Coarbitrary<B> cb, Coarbitrary<C> cc, Coarbitrary<D> cd, Coarbitrary<E> ce, Coarbitrary<F$> cf, Coarbitrary<G> cg, Coarbitrary<H> ch, Arbitrary<I> a) {
        return arbitrary(arbF(ca, arbF(cb, arbF(cc, arbF(cd, arbF(ce, arbF(cf, arbF(cg, arbF(ch, a)))))))).gen.map(Function.uncurryF8()));
    }

    public static <A, B, C, D, E, F$, G, H, I> Arbitrary<F8<A, B, C, D, E, F$, G, H, I>> arbF8Invariant(Arbitrary<I> a) {
        return arbitrary(a.gen.map(Function.compose(Function.uncurryF8(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.constant()))))))))));
    }

    public static <A> Arbitrary<Gen<A>> arbGen(Arbitrary<A> aa) {
        return arbitrary(Gen.sized(new F<Integer, Gen<Gen<A>>>() { // from class: fj.test.Arbitrary.20
            {
                Arbitrary.this = aa;
            }

            @Override // fj.F
            public Gen<Gen<A>> f(Integer i) {
                if (i.intValue() == 0) {
                    return Gen.fail();
                }
                return Arbitrary.this.gen.map((F<A, Gen<A>>) new F<A, Gen<A>>() { // from class: fj.test.Arbitrary.20.1
                    {
                        AnonymousClass20.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public Gen<A> f(A a) {
                        return Gen.value(a);
                    }
                }).resize(i.intValue() - 1);
            }
        }));
    }

    public static <A> Arbitrary<Option<A>> arbOption(Arbitrary<A> aa) {
        return arbitrary(Gen.sized(new F<Integer, Gen<Option<A>>>() { // from class: fj.test.Arbitrary.21
            {
                Arbitrary.this = aa;
            }

            @Override // fj.F
            public Gen<Option<A>> f(Integer i) {
                if (i.intValue() == 0) {
                    return Gen.value(Option.none());
                }
                return Arbitrary.this.gen.map((F<A, Option<A>>) new F<A, Option<A>>() { // from class: fj.test.Arbitrary.21.1
                    {
                        AnonymousClass21.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public Option<A> f(A a) {
                        return Option.some(a);
                    }
                }).resize(i.intValue() - 1);
            }
        }));
    }

    public static <A, B> Arbitrary<Either<A, B>> arbEither(Arbitrary<A> aa, Arbitrary<B> ab) {
        return arbitrary(Gen.oneOf(List.list(aa.gen.map(new F<A, Either<A, B>>() { // from class: fj.test.Arbitrary.22
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass22) obj);
            }

            @Override // fj.F
            public Either<A, B> f(A a) {
                return Either.left(a);
            }
        }), ab.gen.map(new F<B, Either<A, B>>() { // from class: fj.test.Arbitrary.23
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass23) obj);
            }

            @Override // fj.F
            public Either<A, B> f(B b) {
                return Either.right(b);
            }
        }))));
    }

    public static <A> Arbitrary<List<A>> arbList(Arbitrary<A> aa) {
        return arbitrary(Gen.listOf(aa.gen));
    }

    public static <A> Arbitrary<Stream<A>> arbStream(Arbitrary<A> aa) {
        return arbitrary(arbList(aa).gen.map((F<List<A>, Stream<A>>) new F<List<A>, Stream<A>>() { // from class: fj.test.Arbitrary.24
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public Stream<A> f(List<A> as) {
                return as.toStream();
            }
        }));
    }

    public static <A> Arbitrary<Array<A>> arbArray(Arbitrary<A> aa) {
        return arbitrary(arbList(aa).gen.map((F<List<A>, Array<A>>) new F<List<A>, Array<A>>() { // from class: fj.test.Arbitrary.25
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public Array<A> f(List<A> as) {
                return as.toArray();
            }
        }));
    }

    public static Arbitrary<Throwable> arbThrowable(Arbitrary<String> as) {
        return arbitrary(as.gen.map(new F<String, Throwable>() { // from class: fj.test.Arbitrary.26
            @Override // fj.F
            public Throwable f(String msg) {
                return new Throwable(msg);
            }
        }));
    }

    public static <A> Arbitrary<ArrayList<A>> arbArrayList(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, ArrayList<A>>) new F<Array<A>, ArrayList<A>>() { // from class: fj.test.Arbitrary.27
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public ArrayList<A> f(Array<A> a) {
                return new ArrayList<>((Collection<? extends A>) a.toCollection());
            }
        }));
    }

    public static <A extends Enum<A>> Arbitrary<A> arbEnumValue(Class<A> clazz) {
        return arbitrary(Gen.elements(clazz.getEnumConstants()));
    }

    public static <K extends Enum<K>, V> Arbitrary<EnumMap<K, V>> arbEnumMap(Arbitrary<K> ak, Arbitrary<V> av) {
        return arbitrary(arbHashtable(ak, av).gen.map(new F<Hashtable<K, V>, EnumMap<K, V>>() { // from class: fj.test.Arbitrary.31
            @Override // fj.F
            public EnumMap<K, V> f(Hashtable<K, V> ht) {
                return new EnumMap<>(ht);
            }
        }));
    }

    public static <A extends Enum<A>> Arbitrary<EnumSet<A>> arbEnumSet(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, EnumSet<A>>) new F<Array<A>, EnumSet<A>>() { // from class: fj.test.Arbitrary.32
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public EnumSet<A> f(Array<A> a) {
                return EnumSet.copyOf((Collection) a.toCollection());
            }
        }));
    }

    public static <K, V> Arbitrary<HashMap<K, V>> arbHashMap(Arbitrary<K> ak, Arbitrary<V> av) {
        return arbitrary(arbHashtable(ak, av).gen.map((F<Hashtable<K, V>, HashMap<K, V>>) new F<Hashtable<K, V>, HashMap<K, V>>() { // from class: fj.test.Arbitrary.34
            @Override // fj.F
            public HashMap<K, V> f(Hashtable<K, V> ht) {
                return new HashMap<>(ht);
            }
        }));
    }

    public static <A> Arbitrary<HashSet<A>> arbHashSet(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, HashSet<A>>) new F<Array<A>, HashSet<A>>() { // from class: fj.test.Arbitrary.35
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public HashSet<A> f(Array<A> a) {
                return new HashSet<>((Collection<? extends A>) a.toCollection());
            }
        }));
    }

    public static <K, V> Arbitrary<Hashtable<K, V>> arbHashtable(Arbitrary<K> ak, Arbitrary<V> av) {
        return arbitrary(arbList(ak).gen.bind((Gen<A>) arbList(av).gen, (F<List<K>, F<List<V>, Hashtable<K, V>>>) new F<List<K>, F<List<V>, Hashtable<K, V>>>() { // from class: fj.test.Arbitrary.36
            @Override // fj.F
            public F<List<V>, Hashtable<K, V>> f(final List<K> ks) {
                return new F<List<V>, Hashtable<K, V>>() { // from class: fj.test.Arbitrary.36.1
                    {
                        AnonymousClass36.this = this;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Hashtable<K, V> f(List<V> vs) {
                        final Hashtable<K, V> t = new Hashtable<>();
                        ks.zip(vs).foreachDoEffect(new Effect1<P2<K, V>>() { // from class: fj.test.Arbitrary.36.1.1
                            {
                                AnonymousClass1.this = this;
                            }

                            @Override // fj.function.Effect1
                            public void f(P2<K, V> kv) {
                                t.put(kv._1(), kv._2());
                            }
                        });
                        return t;
                    }
                };
            }
        }));
    }

    public static <K, V> Arbitrary<IdentityHashMap<K, V>> arbIdentityHashMap(Arbitrary<K> ak, Arbitrary<V> av) {
        return arbitrary(arbHashtable(ak, av).gen.map((F<Hashtable<K, V>, IdentityHashMap<K, V>>) new F<Hashtable<K, V>, IdentityHashMap<K, V>>() { // from class: fj.test.Arbitrary.37
            @Override // fj.F
            public IdentityHashMap<K, V> f(Hashtable<K, V> ht) {
                return new IdentityHashMap<>(ht);
            }
        }));
    }

    public static <K, V> Arbitrary<LinkedHashMap<K, V>> arbLinkedHashMap(Arbitrary<K> ak, Arbitrary<V> av) {
        return arbitrary(arbHashtable(ak, av).gen.map((F<Hashtable<K, V>, LinkedHashMap<K, V>>) new F<Hashtable<K, V>, LinkedHashMap<K, V>>() { // from class: fj.test.Arbitrary.38
            @Override // fj.F
            public LinkedHashMap<K, V> f(Hashtable<K, V> ht) {
                return new LinkedHashMap<>(ht);
            }
        }));
    }

    public static <A> Arbitrary<LinkedHashSet<A>> arbLinkedHashSet(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, LinkedHashSet<A>>) new F<Array<A>, LinkedHashSet<A>>() { // from class: fj.test.Arbitrary.39
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public LinkedHashSet<A> f(Array<A> a) {
                return new LinkedHashSet<>((Collection<? extends A>) a.toCollection());
            }
        }));
    }

    public static <A> Arbitrary<LinkedList<A>> arbLinkedList(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, LinkedList<A>>) new F<Array<A>, LinkedList<A>>() { // from class: fj.test.Arbitrary.40
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public LinkedList<A> f(Array<A> a) {
                return new LinkedList<>(a.toCollection());
            }
        }));
    }

    public static <A> Arbitrary<PriorityQueue<A>> arbPriorityQueue(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, PriorityQueue<A>>) new F<Array<A>, PriorityQueue<A>>() { // from class: fj.test.Arbitrary.41
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public PriorityQueue<A> f(Array<A> a) {
                return new PriorityQueue<>((Collection<? extends A>) a.toCollection());
            }
        }));
    }

    public static <A> Arbitrary<Stack<A>> arbStack(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, Stack<A>>) new F<Array<A>, Stack<A>>() { // from class: fj.test.Arbitrary.43
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public Stack<A> f(Array<A> a) {
                Stack<A> s = new Stack<>();
                s.addAll(a.toCollection());
                return s;
            }
        }));
    }

    public static <K, V> Arbitrary<TreeMap<K, V>> arbTreeMap(Arbitrary<K> ak, Arbitrary<V> av) {
        return arbitrary(arbHashtable(ak, av).gen.map((F<Hashtable<K, V>, TreeMap<K, V>>) new F<Hashtable<K, V>, TreeMap<K, V>>() { // from class: fj.test.Arbitrary.44
            @Override // fj.F
            public TreeMap<K, V> f(Hashtable<K, V> ht) {
                return new TreeMap<>(ht);
            }
        }));
    }

    public static <A> Arbitrary<TreeSet<A>> arbTreeSet(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, TreeSet<A>>) new F<Array<A>, TreeSet<A>>() { // from class: fj.test.Arbitrary.45
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public TreeSet<A> f(Array<A> a) {
                return new TreeSet<>((Collection<? extends A>) a.toCollection());
            }
        }));
    }

    public static <A> Arbitrary<Vector<A>> arbVector(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, Vector<A>>) new F<Array<A>, Vector<A>>() { // from class: fj.test.Arbitrary.46
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public Vector<A> f(Array<A> a) {
                return new Vector<>((Collection<? extends A>) a.toCollection());
            }
        }));
    }

    public static <K, V> Arbitrary<WeakHashMap<K, V>> arbWeakHashMap(Arbitrary<K> ak, Arbitrary<V> av) {
        return arbitrary(arbHashtable(ak, av).gen.map((F<Hashtable<K, V>, WeakHashMap<K, V>>) new F<Hashtable<K, V>, WeakHashMap<K, V>>() { // from class: fj.test.Arbitrary.47
            @Override // fj.F
            public WeakHashMap<K, V> f(Hashtable<K, V> ht) {
                return new WeakHashMap<>(ht);
            }
        }));
    }

    public static <A> Arbitrary<ArrayBlockingQueue<A>> arbArrayBlockingQueue(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.bind(arbInteger.gen, arbBoolean.gen, (F<Array<A>, F<Integer, F<Boolean, ArrayBlockingQueue<A>>>>) new F<Array<A>, F<Integer, F<Boolean, ArrayBlockingQueue<A>>>>() { // from class: fj.test.Arbitrary.48
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public F<Integer, F<Boolean, ArrayBlockingQueue<A>>> f(final Array<A> a) {
                return new F<Integer, F<Boolean, ArrayBlockingQueue<A>>>() { // from class: fj.test.Arbitrary.48.1
                    {
                        AnonymousClass48.this = this;
                    }

                    @Override // fj.F
                    public F<Boolean, ArrayBlockingQueue<A>> f(final Integer capacity) {
                        return new F<Boolean, ArrayBlockingQueue<A>>() { // from class: fj.test.Arbitrary.48.1.1
                            {
                                AnonymousClass1.this = this;
                            }

                            @Override // fj.F
                            public ArrayBlockingQueue<A> f(Boolean fair) {
                                return new ArrayBlockingQueue<>(a.length() + Math.abs(capacity.intValue()), fair.booleanValue(), a.toCollection());
                            }
                        };
                    }
                };
            }
        }));
    }

    public static <K, V> Arbitrary<ConcurrentHashMap<K, V>> arbConcurrentHashMap(Arbitrary<K> ak, Arbitrary<V> av) {
        return arbitrary(arbHashtable(ak, av).gen.map((F<Hashtable<K, V>, ConcurrentHashMap<K, V>>) new F<Hashtable<K, V>, ConcurrentHashMap<K, V>>() { // from class: fj.test.Arbitrary.49
            @Override // fj.F
            public ConcurrentHashMap<K, V> f(Hashtable<K, V> ht) {
                return new ConcurrentHashMap<>(ht);
            }
        }));
    }

    public static <A> Arbitrary<ConcurrentLinkedQueue<A>> arbConcurrentLinkedQueue(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, ConcurrentLinkedQueue<A>>) new F<Array<A>, ConcurrentLinkedQueue<A>>() { // from class: fj.test.Arbitrary.50
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public ConcurrentLinkedQueue<A> f(Array<A> a) {
                return new ConcurrentLinkedQueue<>(a.toCollection());
            }
        }));
    }

    public static <A> Arbitrary<CopyOnWriteArrayList<A>> arbCopyOnWriteArrayList(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, CopyOnWriteArrayList<A>>) new F<Array<A>, CopyOnWriteArrayList<A>>() { // from class: fj.test.Arbitrary.51
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public CopyOnWriteArrayList<A> f(Array<A> a) {
                return new CopyOnWriteArrayList<>((Collection<? extends A>) a.toCollection());
            }
        }));
    }

    public static <A> Arbitrary<CopyOnWriteArraySet<A>> arbCopyOnWriteArraySet(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, CopyOnWriteArraySet<A>>) new F<Array<A>, CopyOnWriteArraySet<A>>() { // from class: fj.test.Arbitrary.52
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public CopyOnWriteArraySet<A> f(Array<A> a) {
                return new CopyOnWriteArraySet<>(a.toCollection());
            }
        }));
    }

    public static <A extends Delayed> Arbitrary<DelayQueue<A>> arbDelayQueue(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, DelayQueue<A>>) new F<Array<A>, DelayQueue<A>>() { // from class: fj.test.Arbitrary.53
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public DelayQueue<A> f(Array<A> a) {
                return new DelayQueue<>(a.toCollection());
            }
        }));
    }

    public static <A> Arbitrary<LinkedBlockingQueue<A>> arbLinkedBlockingQueue(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, LinkedBlockingQueue<A>>) new F<Array<A>, LinkedBlockingQueue<A>>() { // from class: fj.test.Arbitrary.54
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public LinkedBlockingQueue<A> f(Array<A> a) {
                return new LinkedBlockingQueue<>((Collection<? extends A>) a.toCollection());
            }
        }));
    }

    public static <A> Arbitrary<PriorityBlockingQueue<A>> arbPriorityBlockingQueue(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.map((F<Array<A>, PriorityBlockingQueue<A>>) new F<Array<A>, PriorityBlockingQueue<A>>() { // from class: fj.test.Arbitrary.55
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public PriorityBlockingQueue<A> f(Array<A> a) {
                return new PriorityBlockingQueue<>((Collection<? extends A>) a.toCollection());
            }
        }));
    }

    public static <A> Arbitrary<SynchronousQueue<A>> arbSynchronousQueue(Arbitrary<A> aa) {
        return arbitrary(arbArray(aa).gen.bind(arbBoolean.gen, (F<Array<A>, F<Boolean, SynchronousQueue<A>>>) new F<Array<A>, F<Boolean, SynchronousQueue<A>>>() { // from class: fj.test.Arbitrary.56
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public F<Boolean, SynchronousQueue<A>> f(final Array<A> a) {
                return new F<Boolean, SynchronousQueue<A>>() { // from class: fj.test.Arbitrary.56.1
                    {
                        AnonymousClass56.this = this;
                    }

                    @Override // fj.F
                    public SynchronousQueue<A> f(Boolean fair) {
                        SynchronousQueue<A> q = new SynchronousQueue<>(fair.booleanValue());
                        q.addAll(a.toCollection());
                        return q;
                    }
                };
            }
        }));
    }

    public static <A> Arbitrary<P1<A>> arbP1(Arbitrary<A> aa) {
        return arbitrary(aa.gen.map((F<A, P1<A>>) new F<A, P1<A>>() { // from class: fj.test.Arbitrary.62
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass62) obj);
            }

            @Override // fj.F
            public P1<A> f(A a) {
                return P.p(a);
            }
        }));
    }

    public static <A, B> Arbitrary<P2<A, B>> arbP2(Arbitrary<A> aa, Arbitrary<B> ab) {
        return arbitrary(aa.gen.bind(ab.gen, (F<A, F<B, P2<A, B>>>) new F<A, F<B, P2<A, B>>>() { // from class: fj.test.Arbitrary.63
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass63) obj);
            }

            @Override // fj.F
            public F<B, P2<A, B>> f(final A a) {
                return new F<B, P2<A, B>>() { // from class: fj.test.Arbitrary.63.1
                    {
                        AnonymousClass63.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public P2<A, B> f(B b) {
                        return P.p(a, b);
                    }
                };
            }
        }));
    }

    public static <A, B, C> Arbitrary<P3<A, B, C>> arbP3(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac) {
        return arbitrary(aa.gen.bind(ab.gen, ac.gen, (F<A, F<B, F<C, P3<A, B, C>>>>) new F<A, F<B, F<C, P3<A, B, C>>>>() { // from class: fj.test.Arbitrary.64
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass64) obj);
            }

            @Override // fj.F
            public F<B, F<C, P3<A, B, C>>> f(final A a) {
                return new F<B, F<C, P3<A, B, C>>>() { // from class: fj.test.Arbitrary.64.1
                    {
                        AnonymousClass64.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public F<C, P3<A, B, C>> f(final B b) {
                        return new F<C, P3<A, B, C>>() { // from class: fj.test.Arbitrary.64.1.1
                            {
                                AnonymousClass1.this = this;
                            }

                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((C01001) obj);
                            }

                            @Override // fj.F
                            public P3<A, B, C> f(C c) {
                                return P.p(a, b, c);
                            }
                        };
                    }
                };
            }
        }));
    }

    public static <A, B, C, D> Arbitrary<P4<A, B, C, D>> arbP4(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad) {
        return arbitrary(aa.gen.bind(ab.gen, ac.gen, ad.gen, (F<A, F<B, F<C, F<D, P4<A, B, C, D>>>>>) new F<A, F<B, F<C, F<D, P4<A, B, C, D>>>>>() { // from class: fj.test.Arbitrary.65
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass65) obj);
            }

            /* renamed from: fj.test.Arbitrary$65$1 */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$65$1.class */
            public class AnonymousClass1 implements F<B, F<C, F<D, P4<A, B, C, D>>>> {
                final /* synthetic */ Object val$a;

                AnonymousClass1(Object obj) {
                    AnonymousClass65.this = this$0;
                    this.val$a = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                @Override // fj.F
                public F<C, F<D, P4<A, B, C, D>>> f(final B b) {
                    return new F<C, F<D, P4<A, B, C, D>>>() { // from class: fj.test.Arbitrary.65.1.1
                        {
                            AnonymousClass1.this = this;
                        }

                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C01011) obj);
                        }

                        @Override // fj.F
                        public F<D, P4<A, B, C, D>> f(final C c) {
                            return new F<D, P4<A, B, C, D>>() { // from class: fj.test.Arbitrary.65.1.1.1
                                {
                                    C01011.this = this;
                                }

                                @Override // fj.F
                                public /* bridge */ /* synthetic */ Object f(Object obj) {
                                    return f((C01021) obj);
                                }

                                @Override // fj.F
                                public P4<A, B, C, D> f(D d) {
                                    return P.p(AnonymousClass1.this.val$a, b, c, d);
                                }
                            };
                        }
                    };
                }
            }

            @Override // fj.F
            public F<B, F<C, F<D, P4<A, B, C, D>>>> f(A a) {
                return new AnonymousClass1(a);
            }
        }));
    }

    public static <A, B, C, D, E> Arbitrary<P5<A, B, C, D, E>> arbP5(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae) {
        return arbitrary(aa.gen.bind(ab.gen, ac.gen, ad.gen, ae.gen, (F<A, F<B, F<C, F<D, F<E, P5<A, B, C, D, E>>>>>>) new F<A, F<B, F<C, F<D, F<E, P5<A, B, C, D, E>>>>>>() { // from class: fj.test.Arbitrary.66
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass66) obj);
            }

            /* renamed from: fj.test.Arbitrary$66$1 */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$66$1.class */
            public class AnonymousClass1 implements F<B, F<C, F<D, F<E, P5<A, B, C, D, E>>>>> {
                final /* synthetic */ Object val$a;

                AnonymousClass1(Object obj) {
                    AnonymousClass66.this = this$0;
                    this.val$a = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                /* renamed from: fj.test.Arbitrary$66$1$1 */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$66$1$1.class */
                public class C01031 implements F<C, F<D, F<E, P5<A, B, C, D, E>>>> {
                    final /* synthetic */ Object val$b;

                    C01031(Object obj) {
                        AnonymousClass1.this = this$1;
                        this.val$b = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C01031) obj);
                    }

                    @Override // fj.F
                    public F<D, F<E, P5<A, B, C, D, E>>> f(final C c) {
                        return new F<D, F<E, P5<A, B, C, D, E>>>() { // from class: fj.test.Arbitrary.66.1.1.1
                            {
                                C01031.this = this;
                            }

                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((C01041) obj);
                            }

                            @Override // fj.F
                            public F<E, P5<A, B, C, D, E>> f(final D d) {
                                return new F<E, P5<A, B, C, D, E>>() { // from class: fj.test.Arbitrary.66.1.1.1.1
                                    {
                                        C01041.this = this;
                                    }

                                    @Override // fj.F
                                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                                        return f((C01051) obj);
                                    }

                                    @Override // fj.F
                                    public P5<A, B, C, D, E> f(E e) {
                                        return P.p(AnonymousClass1.this.val$a, C01031.this.val$b, c, d, e);
                                    }
                                };
                            }
                        };
                    }
                }

                @Override // fj.F
                public F<C, F<D, F<E, P5<A, B, C, D, E>>>> f(B b) {
                    return new C01031(b);
                }
            }

            @Override // fj.F
            public F<B, F<C, F<D, F<E, P5<A, B, C, D, E>>>>> f(A a) {
                return new AnonymousClass1(a);
            }
        }));
    }

    public static <A, B, C, D, E, F$> Arbitrary<P6<A, B, C, D, E, F$>> arbP6(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af) {
        return arbitrary(aa.gen.bind(ab.gen, ac.gen, ad.gen, ae.gen, af.gen, (F<A, F<B, F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>>>) new F<A, F<B, F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>>>() { // from class: fj.test.Arbitrary.67
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass67) obj);
            }

            /* renamed from: fj.test.Arbitrary$67$1 */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$67$1.class */
            public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>> {
                final /* synthetic */ Object val$a;

                AnonymousClass1(Object obj) {
                    AnonymousClass67.this = this$0;
                    this.val$a = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                /* renamed from: fj.test.Arbitrary$67$1$1 */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$67$1$1.class */
                public class C01061 implements F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>> {
                    final /* synthetic */ Object val$b;

                    C01061(Object obj) {
                        AnonymousClass1.this = this$1;
                        this.val$b = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C01061) obj);
                    }

                    /* renamed from: fj.test.Arbitrary$67$1$1$1 */
                    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$67$1$1$1.class */
                    public class C01071 implements F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>> {
                        final /* synthetic */ Object val$c;

                        C01071(Object obj) {
                            C01061.this = this$2;
                            this.val$c = obj;
                        }

                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C01071) obj);
                        }

                        @Override // fj.F
                        public F<E, F<F$, P6<A, B, C, D, E, F$>>> f(final D d) {
                            return new F<E, F<F$, P6<A, B, C, D, E, F$>>>() { // from class: fj.test.Arbitrary.67.1.1.1.1
                                {
                                    C01071.this = this;
                                }

                                @Override // fj.F
                                public /* bridge */ /* synthetic */ Object f(Object obj) {
                                    return f((C01081) obj);
                                }

                                @Override // fj.F
                                public F<F$, P6<A, B, C, D, E, F$>> f(final E e) {
                                    return new F<F$, P6<A, B, C, D, E, F$>>() { // from class: fj.test.Arbitrary.67.1.1.1.1.1
                                        {
                                            C01081.this = this;
                                        }

                                        @Override // fj.F
                                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                                            return f((C01091) obj);
                                        }

                                        @Override // fj.F
                                        public P6<A, B, C, D, E, F$> f(F$ f) {
                                            return P.p(AnonymousClass1.this.val$a, C01061.this.val$b, C01071.this.val$c, d, e, f);
                                        }
                                    };
                                }
                            };
                        }
                    }

                    @Override // fj.F
                    public F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>> f(C c) {
                        return new C01071(c);
                    }
                }

                @Override // fj.F
                public F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>> f(B b) {
                    return new C01061(b);
                }
            }

            @Override // fj.F
            public F<B, F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>> f(A a) {
                return new AnonymousClass1(a);
            }
        }));
    }

    public static <A, B, C, D, E, F$, G> Arbitrary<P7<A, B, C, D, E, F$, G>> arbP7(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag) {
        return arbitrary(aa.gen.bind(ab.gen, ac.gen, ad.gen, ae.gen, af.gen, ag.gen, (F<A, F<B, F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>>>) new F<A, F<B, F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>>>() { // from class: fj.test.Arbitrary.68
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass68) obj);
            }

            /* renamed from: fj.test.Arbitrary$68$1 */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$68$1.class */
            public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>> {
                final /* synthetic */ Object val$a;

                AnonymousClass1(Object obj) {
                    AnonymousClass68.this = this$0;
                    this.val$a = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                /* renamed from: fj.test.Arbitrary$68$1$1 */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$68$1$1.class */
                public class C01101 implements F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>> {
                    final /* synthetic */ Object val$b;

                    C01101(Object obj) {
                        AnonymousClass1.this = this$1;
                        this.val$b = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C01101) obj);
                    }

                    /* renamed from: fj.test.Arbitrary$68$1$1$1 */
                    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$68$1$1$1.class */
                    public class C01111 implements F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>> {
                        final /* synthetic */ Object val$c;

                        C01111(Object obj) {
                            C01101.this = this$2;
                            this.val$c = obj;
                        }

                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C01111) obj);
                        }

                        /* renamed from: fj.test.Arbitrary$68$1$1$1$1 */
                        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$68$1$1$1$1.class */
                        public class C01121 implements F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>> {
                            final /* synthetic */ Object val$d;

                            C01121(Object obj) {
                                C01111.this = this$3;
                                this.val$d = obj;
                            }

                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((C01121) obj);
                            }

                            @Override // fj.F
                            public F<F$, F<G, P7<A, B, C, D, E, F$, G>>> f(final E e) {
                                return new F<F$, F<G, P7<A, B, C, D, E, F$, G>>>() { // from class: fj.test.Arbitrary.68.1.1.1.1.1
                                    {
                                        C01121.this = this;
                                    }

                                    @Override // fj.F
                                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                                        return f((C01131) obj);
                                    }

                                    @Override // fj.F
                                    public F<G, P7<A, B, C, D, E, F$, G>> f(final F$ f) {
                                        return new F<G, P7<A, B, C, D, E, F$, G>>() { // from class: fj.test.Arbitrary.68.1.1.1.1.1.1
                                            {
                                                C01131.this = this;
                                            }

                                            @Override // fj.F
                                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                                return f((C01141) obj);
                                            }

                                            @Override // fj.F
                                            public P7<A, B, C, D, E, F$, G> f(G g) {
                                                return P.p(AnonymousClass1.this.val$a, C01101.this.val$b, C01111.this.val$c, C01121.this.val$d, e, f, g);
                                            }
                                        };
                                    }
                                };
                            }
                        }

                        @Override // fj.F
                        public F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>> f(D d) {
                            return new C01121(d);
                        }
                    }

                    @Override // fj.F
                    public F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>> f(C c) {
                        return new C01111(c);
                    }
                }

                @Override // fj.F
                public F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>> f(B b) {
                    return new C01101(b);
                }
            }

            @Override // fj.F
            public F<B, F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>> f(A a) {
                return new AnonymousClass1(a);
            }
        }));
    }

    public static <A, B, C, D, E, F$, G, H> Arbitrary<P8<A, B, C, D, E, F$, G, H>> arbP8(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, Arbitrary<H> ah) {
        return arbitrary(aa.gen.bind(ab.gen, ac.gen, ad.gen, ae.gen, af.gen, ag.gen, ah.gen, (F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>>>) new F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>>>() { // from class: fj.test.Arbitrary.69
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass69) obj);
            }

            /* renamed from: fj.test.Arbitrary$69$1 */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$69$1.class */
            public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>> {
                final /* synthetic */ Object val$a;

                AnonymousClass1(Object obj) {
                    AnonymousClass69.this = this$0;
                    this.val$a = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                /* renamed from: fj.test.Arbitrary$69$1$1 */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$69$1$1.class */
                public class C01151 implements F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>> {
                    final /* synthetic */ Object val$b;

                    C01151(Object obj) {
                        AnonymousClass1.this = this$1;
                        this.val$b = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C01151) obj);
                    }

                    /* renamed from: fj.test.Arbitrary$69$1$1$1 */
                    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$69$1$1$1.class */
                    public class C01161 implements F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>> {
                        final /* synthetic */ Object val$c;

                        C01161(Object obj) {
                            C01151.this = this$2;
                            this.val$c = obj;
                        }

                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C01161) obj);
                        }

                        /* renamed from: fj.test.Arbitrary$69$1$1$1$1 */
                        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$69$1$1$1$1.class */
                        public class C01171 implements F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>> {
                            final /* synthetic */ Object val$d;

                            C01171(Object obj) {
                                C01161.this = this$3;
                                this.val$d = obj;
                            }

                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((C01171) obj);
                            }

                            /* renamed from: fj.test.Arbitrary$69$1$1$1$1$1 */
                            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arbitrary$69$1$1$1$1$1.class */
                            public class C01181 implements F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>> {
                                final /* synthetic */ Object val$e;

                                C01181(Object obj) {
                                    C01171.this = this$4;
                                    this.val$e = obj;
                                }

                                @Override // fj.F
                                public /* bridge */ /* synthetic */ Object f(Object obj) {
                                    return f((C01181) obj);
                                }

                                @Override // fj.F
                                public F<G, F<H, P8<A, B, C, D, E, F$, G, H>>> f(final F$ f) {
                                    return new F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>() { // from class: fj.test.Arbitrary.69.1.1.1.1.1.1
                                        {
                                            C01181.this = this;
                                        }

                                        @Override // fj.F
                                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                                            return f((C01191) obj);
                                        }

                                        @Override // fj.F
                                        public F<H, P8<A, B, C, D, E, F$, G, H>> f(final G g) {
                                            return new F<H, P8<A, B, C, D, E, F$, G, H>>() { // from class: fj.test.Arbitrary.69.1.1.1.1.1.1.1
                                                {
                                                    C01191.this = this;
                                                }

                                                @Override // fj.F
                                                public /* bridge */ /* synthetic */ Object f(Object obj) {
                                                    return f((C01201) obj);
                                                }

                                                @Override // fj.F
                                                public P8<A, B, C, D, E, F$, G, H> f(H h) {
                                                    return P.p(AnonymousClass1.this.val$a, C01151.this.val$b, C01161.this.val$c, C01171.this.val$d, C01181.this.val$e, f, g, h);
                                                }
                                            };
                                        }
                                    };
                                }
                            }

                            @Override // fj.F
                            public F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>> f(E e) {
                                return new C01181(e);
                            }
                        }

                        @Override // fj.F
                        public F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>> f(D d) {
                            return new C01171(d);
                        }
                    }

                    @Override // fj.F
                    public F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>> f(C c) {
                        return new C01161(c);
                    }
                }

                @Override // fj.F
                public F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>> f(B b) {
                    return new C01151(b);
                }
            }

            @Override // fj.F
            public F<B, F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>> f(A a) {
                return new AnonymousClass1(a);
            }
        }));
    }
}
