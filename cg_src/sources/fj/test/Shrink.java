package fj.test;

import fj.F;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.P4;
import fj.P5;
import fj.P6;
import fj.P7;
import fj.P8;
import fj.Primitive;
import fj.data.Array;
import fj.data.Conversions;
import fj.data.Either;
import fj.data.Java;
import fj.data.List;
import fj.data.Option;
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
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Shrink.class */
public final class Shrink<A> {
    private final F<A, Stream<A>> f;
    public static final Shrink<Long> shrinkLong = new Shrink<>(new F<Long, Stream<Long>>() { // from class: fj.test.Shrink.3
        @Override // fj.F
        public Stream<Long> f(final Long i) {
            if (i.longValue() == 0) {
                return Stream.nil();
            }
            final Stream<Long> is = Stream.cons(0L, new P1<Stream<Long>>() { // from class: fj.test.Shrink.3.1
                {
                    AnonymousClass3.this = this;
                }

                @Override // fj.P1
                public Stream<Long> _1() {
                    return Stream.iterate(new F<Long, Long>() { // from class: fj.test.Shrink.3.1.3
                        {
                            AnonymousClass1.this = this;
                        }

                        @Override // fj.F
                        public Long f(Long x) {
                            return Long.valueOf(x.longValue() / 2);
                        }
                    }, i).takeWhile(new F<Long, Boolean>() { // from class: fj.test.Shrink.3.1.2
                        {
                            AnonymousClass1.this = this;
                        }

                        @Override // fj.F
                        public Boolean f(Long x) {
                            return Boolean.valueOf(x.longValue() != 0);
                        }
                    }).map(new F<Long, Long>() { // from class: fj.test.Shrink.3.1.1
                        {
                            AnonymousClass1.this = this;
                        }

                        @Override // fj.F
                        public Long f(Long x) {
                            return Long.valueOf(i.longValue() - x.longValue());
                        }
                    });
                }
            });
            return i.longValue() < 0 ? Stream.cons(Long.valueOf(-i.longValue()), new P1<Stream<Long>>() { // from class: fj.test.Shrink.3.2
                {
                    AnonymousClass3.this = this;
                }

                @Override // fj.P1
                public Stream<Long> _1() {
                    return is;
                }
            }) : is;
        }
    });
    public static final Shrink<Boolean> shrinkBoolean = shrink(Function.constant(Stream.single(false)));
    public static final Shrink<Integer> shrinkInteger = shrinkLong.map(Primitive.Long_Integer, Primitive.Integer_Long);
    public static final Shrink<Byte> shrinkByte = shrinkLong.map(Primitive.Long_Byte, Primitive.Byte_Long);
    public static final Shrink<Character> shrinkCharacter = shrinkLong.map(Primitive.Long_Character, Primitive.Character_Long);
    public static final Shrink<Short> shrinkShort = shrinkLong.map(Primitive.Long_Short, Primitive.Short_Long);
    public static final Shrink<Float> shrinkFloat = shrinkLong.map(Primitive.Long_Float, Primitive.Float_Long);
    public static final Shrink<Double> shrinkDouble = shrinkLong.map(Primitive.Long_Double, Primitive.Double_Long);
    public static final Shrink<String> shrinkString = shrinkList(shrinkCharacter).map(Conversions.List_String, Conversions.String_List);
    public static final Shrink<StringBuffer> shrinkStringBuffer = shrinkList(shrinkCharacter).map(Conversions.List_StringBuffer, Conversions.StringBuffer_List);
    public static final Shrink<StringBuilder> shrinkStringBuilder = shrinkList(shrinkCharacter).map(Conversions.List_StringBuilder, Conversions.StringBuilder_List);
    public static final Shrink<Throwable> shrinkThrowable = shrinkThrowable(shrinkString);
    public static final Shrink<BitSet> shrinkBitSet = shrinkList(shrinkBoolean).map(Java.List_BitSet, Java.BitSet_List);
    public static final Shrink<Calendar> shrinkCalendar = shrinkLong.map(new F<Long, Calendar>() { // from class: fj.test.Shrink.9
        @Override // fj.F
        public Calendar f(Long i) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(i.longValue());
            return c;
        }
    }, new F<Calendar, Long>() { // from class: fj.test.Shrink.10
        @Override // fj.F
        public Long f(Calendar c) {
            return Long.valueOf(c.getTime().getTime());
        }
    });
    public static final Shrink<Date> shrinkDate = shrinkLong.map(new F<Long, Date>() { // from class: fj.test.Shrink.11
        @Override // fj.F
        public Date f(Long i) {
            return new Date(i.longValue());
        }
    }, new F<Date, Long>() { // from class: fj.test.Shrink.12
        @Override // fj.F
        public Long f(Date d) {
            return Long.valueOf(d.getTime());
        }
    });
    public static final Shrink<GregorianCalendar> shrinkGregorianCalendar = shrinkLong.map(new F<Long, GregorianCalendar>() { // from class: fj.test.Shrink.15
        @Override // fj.F
        public GregorianCalendar f(Long i) {
            GregorianCalendar c = new GregorianCalendar();
            c.setTimeInMillis(i.longValue());
            return c;
        }
    }, new F<GregorianCalendar, Long>() { // from class: fj.test.Shrink.16
        @Override // fj.F
        public Long f(GregorianCalendar c) {
            return Long.valueOf(c.getTime().getTime());
        }
    });
    public static final Shrink<Properties> shrinkProperties = shrinkHashtable(shrinkString, shrinkString).map(new F<Hashtable<String, String>, Properties>() { // from class: fj.test.Shrink.25
        @Override // fj.F
        public Properties f(Hashtable<String, String> h) {
            Properties p = new Properties();
            for (String k : h.keySet()) {
                p.setProperty(k, h.get(k));
            }
            return p;
        }
    }, new F<Properties, Hashtable<String, String>>() { // from class: fj.test.Shrink.26
        @Override // fj.F
        public Hashtable<String, String> f(Properties p) {
            Hashtable<String, String> t = new Hashtable<>();
            for (Object s : p.keySet()) {
                t.put((String) s, p.getProperty((String) s));
            }
            return t;
        }
    });
    public static final Shrink<java.sql.Date> shrinkSQLDate = shrinkLong.map(new F<Long, java.sql.Date>() { // from class: fj.test.Shrink.33
        @Override // fj.F
        public java.sql.Date f(Long i) {
            return new java.sql.Date(i.longValue());
        }
    }, new F<java.sql.Date, Long>() { // from class: fj.test.Shrink.34
        @Override // fj.F
        public Long f(java.sql.Date c) {
            return Long.valueOf(c.getTime());
        }
    });
    public static final Shrink<Time> shrinkTime = shrinkLong.map(new F<Long, Time>() { // from class: fj.test.Shrink.35
        @Override // fj.F
        public Time f(Long i) {
            return new Time(i.longValue());
        }
    }, new F<Time, Long>() { // from class: fj.test.Shrink.36
        @Override // fj.F
        public Long f(Time c) {
            return Long.valueOf(c.getTime());
        }
    });
    public static final Shrink<Timestamp> shrinkTimestamp = shrinkLong.map(new F<Long, Timestamp>() { // from class: fj.test.Shrink.37
        @Override // fj.F
        public Timestamp f(Long i) {
            return new Timestamp(i.longValue());
        }
    }, new F<Timestamp, Long>() { // from class: fj.test.Shrink.38
        @Override // fj.F
        public Long f(Timestamp c) {
            return Long.valueOf(c.getTime());
        }
    });
    public static final Shrink<BigInteger> shrinkBigInteger = shrinkP2(shrinkByte, shrinkArray(shrinkByte)).map(new F<P2<Byte, Array<Byte>>, BigInteger>() { // from class: fj.test.Shrink.39
        @Override // fj.F
        public BigInteger f(P2<Byte, Array<Byte>> bs) {
            byte[] x = new byte[bs._2().length() + 1];
            for (int i = 0; i < bs._2().array().length; i++) {
                x[i] = bs._2().get(i).byteValue();
            }
            x[bs._2().length()] = bs._1().byteValue();
            return new BigInteger(x);
        }
    }, new F<BigInteger, P2<Byte, Array<Byte>>>() { // from class: fj.test.Shrink.40
        @Override // fj.F
        public P2<Byte, Array<Byte>> f(BigInteger i) {
            byte[] b = i.toByteArray();
            Byte[] x = new Byte[b.length - 1];
            System.arraycopy(b, 0, x, 0, b.length - 1);
            return P.p(Byte.valueOf(b[0]), Array.array(x));
        }
    });
    public static final Shrink<BigDecimal> shrinkBigDecimal = shrinkBigInteger.map(new F<BigInteger, BigDecimal>() { // from class: fj.test.Shrink.41
        @Override // fj.F
        public BigDecimal f(BigInteger i) {
            return new BigDecimal(i);
        }
    }, new F<BigDecimal, BigInteger>() { // from class: fj.test.Shrink.42
        @Override // fj.F
        public BigInteger f(BigDecimal d) {
            return d.toBigInteger();
        }
    });

    private Shrink(F<A, Stream<A>> f) {
        this.f = f;
    }

    public Stream<A> shrink(A a) {
        return this.f.f(a);
    }

    public <B> Shrink<B> map(final F<A, B> f, final F<B, A> g) {
        return new Shrink<>(new F<B, Stream<B>>() { // from class: fj.test.Shrink.1
            {
                Shrink.this = this;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<B> f(B b) {
                return ((Stream) Shrink.this.f.f(g.f(b))).map(f);
            }
        });
    }

    public static <A> Shrink<A> shrink(F<A, Stream<A>> f) {
        return new Shrink<>(f);
    }

    public static <A> Shrink<A> empty() {
        return new Shrink<>(new F<A, Stream<A>>() { // from class: fj.test.Shrink.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass2) obj);
            }

            @Override // fj.F
            public Stream<A> f(A a) {
                return Stream.nil();
            }
        });
    }

    public static <A> Shrink<Option<A>> shrinkOption(Shrink<A> sa) {
        return new Shrink<>(new F<Option<A>, Stream<Option<A>>>() { // from class: fj.test.Shrink.4
            {
                Shrink.this = sa;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Option) ((Option) obj));
            }

            public Stream<Option<A>> f(final Option<A> o) {
                if (o.isNone()) {
                    return Stream.nil();
                }
                return Stream.cons(Option.none(), new P1<Stream<Option<A>>>() { // from class: fj.test.Shrink.4.1
                    {
                        AnonymousClass4.this = this;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.P1
                    public Stream<Option<A>> _1() {
                        return Shrink.this.shrink((Shrink) o.some()).map(Option.some_());
                    }
                });
            }
        });
    }

    public static <A, B> Shrink<Either<A, B>> shrinkEither(Shrink<A> sa, final Shrink<B> sb) {
        return new Shrink<>(new F<Either<A, B>, Stream<Either<A, B>>>() { // from class: fj.test.Shrink.5
            {
                Shrink.this = sa;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<Either<A, B>> f(Either<A, B> e) {
                if (e.isLeft()) {
                    return Shrink.this.shrink((Shrink) e.left().value()).map(Either.left_());
                }
                return sb.shrink((Shrink) e.right().value()).map(Either.right_());
            }
        });
    }

    /* renamed from: fj.test.Shrink$1Util */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Shrink$1Util.class */
    public final class C1Util {
        C1Util() {
            Shrink.this = r4;
        }

        Stream<List<A>> removeChunks(int n, List<A> as) {
            if (as.isEmpty()) {
                return Stream.nil();
            }
            if (as.tail().isEmpty()) {
                return Stream.cons(List.nil(), Stream.nil_());
            }
            int n1 = n / 2;
            int n2 = n - n1;
            List<A> as1 = as.take(n1);
            F<List<A>, Boolean> isNotEmpty = List.isNotEmpty_();
            return Stream.cons(as1, new AnonymousClass1(as, n1, as1, isNotEmpty, n2));
        }

        /* renamed from: fj.test.Shrink$1Util$1 */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Shrink$1Util$1.class */
        public class AnonymousClass1 extends P1<Stream<List<A>>> {
            final /* synthetic */ List val$as;
            final /* synthetic */ int val$n1;
            final /* synthetic */ List val$as1;
            final /* synthetic */ F val$isNotEmpty;
            final /* synthetic */ int val$n2;

            AnonymousClass1(List list, int i, List list2, F f, int i2) {
                C1Util.this = this$0;
                this.val$as = list;
                this.val$n1 = i;
                this.val$as1 = list2;
                this.val$isNotEmpty = f;
                this.val$n2 = i2;
            }

            @Override // fj.P1
            public Stream<List<A>> _1() {
                final List<A> as2 = this.val$as.drop(this.val$n1);
                return Stream.cons(as2, new P1<Stream<List<A>>>() { // from class: fj.test.Shrink.1Util.1.1
                    {
                        AnonymousClass1.this = this;
                    }

                    @Override // fj.P1
                    public Stream<List<A>> _1() {
                        return C1Util.this.removeChunks(AnonymousClass1.this.val$n1, AnonymousClass1.this.val$as1).filter(AnonymousClass1.this.val$isNotEmpty).map((F<List<A>, List<A>>) new F<List<A>, List<A>>() { // from class: fj.test.Shrink.1Util.1.1.1
                            {
                                C01731.this = this;
                            }

                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((List) ((List) obj));
                            }

                            public List<A> f(List<A> aas) {
                                return aas.append(as2);
                            }
                        }).interleave(C1Util.this.removeChunks(AnonymousClass1.this.val$n2, as2).filter(AnonymousClass1.this.val$isNotEmpty).map((F<List<A>, List<A>>) new F<List<A>, List<A>>() { // from class: fj.test.Shrink.1Util.1.1.2
                            {
                                C01731.this = this;
                            }

                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((List) ((List) obj));
                            }

                            public List<A> f(List<A> aas) {
                                return AnonymousClass1.this.val$as1.append(aas);
                            }
                        }));
                    }
                });
            }
        }

        Stream<List<A>> shrinkOne(final List<A> as) {
            if (as.isEmpty()) {
                return Stream.nil();
            }
            return Shrink.this.shrink((Shrink) as.head()).map((F<A, List<A>>) new F<A, List<A>>() { // from class: fj.test.Shrink.1Util.2
                {
                    C1Util.this = this;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass2) obj);
                }

                @Override // fj.F
                public List<A> f(A a) {
                    return as.tail().cons((List<A>) a);
                }
            }).append((Stream<B>) shrinkOne(as.tail()).map((F<List<A>, List<A>>) new F<List<A>, List<A>>() { // from class: fj.test.Shrink.1Util.3
                {
                    C1Util.this = this;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((List) ((List) obj));
                }

                /* JADX WARN: Multi-variable type inference failed */
                public List<A> f(List<A> aas) {
                    return aas.cons((List<A>) as.head());
                }
            }));
        }
    }

    public static <A> Shrink<List<A>> shrinkList(Shrink<A> sa) {
        return new Shrink<>(new F<List<A>, Stream<List<A>>>() { // from class: fj.test.Shrink.6
            {
                Shrink.this = sa;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public Stream<List<A>> f(List<A> as) {
                C1Util u = new C1Util();
                return u.removeChunks(as.length(), as).append(u.shrinkOne(as));
            }
        });
    }

    public static <A> Shrink<Array<A>> shrinkArray(Shrink<A> sa) {
        return shrinkList(sa).map(Conversions.List_Array(), Conversions.Array_List());
    }

    public static <A> Shrink<Stream<A>> shrinkStream(Shrink<A> sa) {
        return shrinkList(sa).map(Conversions.List_Stream(), Conversions.Stream_List());
    }

    public static Shrink<Throwable> shrinkThrowable(Shrink<String> ss) {
        return ss.map(new F<String, Throwable>() { // from class: fj.test.Shrink.7
            @Override // fj.F
            public Throwable f(String s) {
                return new Throwable(s);
            }
        }, new F<Throwable, String>() { // from class: fj.test.Shrink.8
            @Override // fj.F
            public String f(Throwable t) {
                return t.getMessage();
            }
        });
    }

    public static <A> Shrink<ArrayList<A>> shrinkArrayList(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_ArrayList(), Java.ArrayList_List());
    }

    public static <K extends Enum<K>, V> Shrink<EnumMap<K, V>> shrinkEnumMap(Shrink<K> sk, Shrink<V> sv) {
        return shrinkHashtable(sk, sv).map(new F<Hashtable<K, V>, EnumMap<K, V>>() { // from class: fj.test.Shrink.13
            @Override // fj.F
            public EnumMap<K, V> f(Hashtable<K, V> h) {
                return new EnumMap<>(h);
            }
        }, new F<EnumMap<K, V>, Hashtable<K, V>>() { // from class: fj.test.Shrink.14
            @Override // fj.F
            public Hashtable<K, V> f(EnumMap<K, V> m) {
                return new Hashtable<>(m);
            }
        });
    }

    public static <A extends Enum<A>> Shrink<EnumSet<A>> shrinkEnumSet(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_EnumSet(), Java.EnumSet_List());
    }

    public static <K, V> Shrink<HashMap<K, V>> shrinkHashMap(Shrink<K> sk, Shrink<V> sv) {
        return shrinkHashtable(sk, sv).map(new F<Hashtable<K, V>, HashMap<K, V>>() { // from class: fj.test.Shrink.17
            @Override // fj.F
            public HashMap<K, V> f(Hashtable<K, V> h) {
                return new HashMap<>(h);
            }
        }, new F<HashMap<K, V>, Hashtable<K, V>>() { // from class: fj.test.Shrink.18
            @Override // fj.F
            public Hashtable<K, V> f(HashMap<K, V> m) {
                return new Hashtable<>(m);
            }
        });
    }

    public static <A> Shrink<HashSet<A>> shrinkHashSet(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_HashSet(), Java.HashSet_List());
    }

    /* renamed from: fj.test.Shrink$19 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Shrink$19.class */
    public static class AnonymousClass19 implements F<List<P2<K, V>>, Hashtable<K, V>> {
        AnonymousClass19() {
        }

        @Override // fj.F
        public Hashtable<K, V> f(List<P2<K, V>> kvs) {
            Hashtable<K, V> h = new Hashtable<>();
            kvs.foreachDoEffect(Shrink$19$$Lambda$1.lambdaFactory$(h));
            return h;
        }

        public static /* synthetic */ void lambda$f$161(Hashtable hashtable, P2 kv) {
            hashtable.put(kv._1(), kv._2());
        }
    }

    public static <K, V> Shrink<Hashtable<K, V>> shrinkHashtable(Shrink<K> sk, Shrink<V> sv) {
        return shrinkList(shrinkP2(sk, sv)).map(new AnonymousClass19(), new F<Hashtable<K, V>, List<P2<K, V>>>() { // from class: fj.test.Shrink.20
            @Override // fj.F
            public List<P2<K, V>> f(Hashtable<K, V> h) {
                List nil = List.nil();
                for (Object obj : h.keySet()) {
                    nil = nil.snoc(P.p(obj, h.get(obj)));
                }
                return nil;
            }
        });
    }

    public static <K, V> Shrink<IdentityHashMap<K, V>> shrinkIdentityHashMap(Shrink<K> sk, Shrink<V> sv) {
        return shrinkHashtable(sk, sv).map(new F<Hashtable<K, V>, IdentityHashMap<K, V>>() { // from class: fj.test.Shrink.21
            @Override // fj.F
            public IdentityHashMap<K, V> f(Hashtable<K, V> h) {
                return new IdentityHashMap<>(h);
            }
        }, new F<IdentityHashMap<K, V>, Hashtable<K, V>>() { // from class: fj.test.Shrink.22
            @Override // fj.F
            public Hashtable<K, V> f(IdentityHashMap<K, V> m) {
                return new Hashtable<>(m);
            }
        });
    }

    public static <K, V> Shrink<LinkedHashMap<K, V>> shrinkLinkedHashMap(Shrink<K> sk, Shrink<V> sv) {
        return shrinkHashtable(sk, sv).map(new F<Hashtable<K, V>, LinkedHashMap<K, V>>() { // from class: fj.test.Shrink.23
            @Override // fj.F
            public LinkedHashMap<K, V> f(Hashtable<K, V> h) {
                return new LinkedHashMap<>(h);
            }
        }, new F<LinkedHashMap<K, V>, Hashtable<K, V>>() { // from class: fj.test.Shrink.24
            @Override // fj.F
            public Hashtable<K, V> f(LinkedHashMap<K, V> m) {
                return new Hashtable<>(m);
            }
        });
    }

    public static <A> Shrink<LinkedHashSet<A>> shrinkLinkedHashSet(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_LinkedHashSet(), Java.LinkedHashSet_List());
    }

    public static <A> Shrink<LinkedList<A>> shrinkLinkedList(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_LinkedList(), Java.LinkedList_List());
    }

    public static <A> Shrink<PriorityQueue<A>> shrinkPriorityQueue(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_PriorityQueue(), Java.PriorityQueue_List());
    }

    public static <A> Shrink<Stack<A>> shrinkStack(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_Stack(), Java.Stack_List());
    }

    public static <K, V> Shrink<TreeMap<K, V>> shrinkTreeMap(Shrink<K> sk, Shrink<V> sv) {
        return shrinkHashtable(sk, sv).map(new F<Hashtable<K, V>, TreeMap<K, V>>() { // from class: fj.test.Shrink.27
            @Override // fj.F
            public TreeMap<K, V> f(Hashtable<K, V> h) {
                return new TreeMap<>(h);
            }
        }, new F<TreeMap<K, V>, Hashtable<K, V>>() { // from class: fj.test.Shrink.28
            @Override // fj.F
            public Hashtable<K, V> f(TreeMap<K, V> m) {
                return new Hashtable<>(m);
            }
        });
    }

    public static <A> Shrink<TreeSet<A>> shrinkTreeSet(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_TreeSet(), Java.TreeSet_List());
    }

    public static <A> Shrink<Vector<A>> shrinkVector(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_Vector(), Java.Vector_List());
    }

    public static <K, V> Shrink<WeakHashMap<K, V>> shrinkWeakHashMap(Shrink<K> sk, Shrink<V> sv) {
        return shrinkHashtable(sk, sv).map(new F<Hashtable<K, V>, WeakHashMap<K, V>>() { // from class: fj.test.Shrink.29
            @Override // fj.F
            public WeakHashMap<K, V> f(Hashtable<K, V> h) {
                return new WeakHashMap<>(h);
            }
        }, new F<WeakHashMap<K, V>, Hashtable<K, V>>() { // from class: fj.test.Shrink.30
            @Override // fj.F
            public Hashtable<K, V> f(WeakHashMap<K, V> m) {
                return new Hashtable<>(m);
            }
        });
    }

    public static <A> Shrink<ArrayBlockingQueue<A>> shrinkArrayBlockingQueue(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_ArrayBlockingQueue(false), Java.ArrayBlockingQueue_List());
    }

    public static <K, V> Shrink<ConcurrentHashMap<K, V>> shrinkConcurrentHashMap(Shrink<K> sk, Shrink<V> sv) {
        return shrinkHashtable(sk, sv).map(new F<Hashtable<K, V>, ConcurrentHashMap<K, V>>() { // from class: fj.test.Shrink.31
            @Override // fj.F
            public ConcurrentHashMap<K, V> f(Hashtable<K, V> h) {
                return new ConcurrentHashMap<>(h);
            }
        }, new F<ConcurrentHashMap<K, V>, Hashtable<K, V>>() { // from class: fj.test.Shrink.32
            @Override // fj.F
            public Hashtable<K, V> f(ConcurrentHashMap<K, V> m) {
                return new Hashtable<>(m);
            }
        });
    }

    public static <A> Shrink<ConcurrentLinkedQueue<A>> shrinkConcurrentLinkedQueue(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_ConcurrentLinkedQueue(), Java.ConcurrentLinkedQueue_List());
    }

    public static <A> Shrink<CopyOnWriteArrayList<A>> shrinkCopyOnWriteArrayList(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_CopyOnWriteArrayList(), Java.CopyOnWriteArrayList_List());
    }

    public static <A> Shrink<CopyOnWriteArraySet<A>> shrinkCopyOnWriteArraySet(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_CopyOnWriteArraySet(), Java.CopyOnWriteArraySet_List());
    }

    public static <A extends Delayed> Shrink<DelayQueue<A>> shrinkDelayQueue(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_DelayQueue(), Java.DelayQueue_List());
    }

    public static <A> Shrink<LinkedBlockingQueue<A>> shrinkLinkedBlockingQueue(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_LinkedBlockingQueue(), Java.LinkedBlockingQueue_List());
    }

    public static <A> Shrink<PriorityBlockingQueue<A>> shrinkPriorityBlockingQueue(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_PriorityBlockingQueue(), Java.PriorityBlockingQueue_List());
    }

    public static <A> Shrink<SynchronousQueue<A>> shrinkSynchronousQueue(Shrink<A> sa) {
        return shrinkList(sa).map(Java.List_SynchronousQueue(false), Java.SynchronousQueue_List());
    }

    public static <A> Shrink<P1<A>> shrinkP1(Shrink<A> sa) {
        return new Shrink<>(new F<P1<A>, Stream<P1<A>>>() { // from class: fj.test.Shrink.43
            {
                Shrink.this = sa;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P1) ((P1) obj));
            }

            public Stream<P1<A>> f(P1<A> p) {
                return (Stream<P1<A>>) Shrink.this.shrink((Shrink) p._1()).map((F<A, P1<A>>) new F<A, P1<A>>() { // from class: fj.test.Shrink.43.1
                    {
                        AnonymousClass43.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public P1<A> f(A a) {
                        return P.p(a);
                    }
                });
            }
        });
    }

    public static <A, B> Shrink<P2<A, B>> shrinkP2(Shrink<A> sa, final Shrink<B> sb) {
        return new Shrink<>(new F<P2<A, B>, Stream<P2<A, B>>>() { // from class: fj.test.Shrink.44
            {
                Shrink.this = sa;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<P2<A, B>> f(P2<A, B> p) {
                return Shrink.this.shrink((Shrink) p._1()).bind(sb.shrink((Shrink) p._2()), P.p2());
            }
        });
    }

    public static <A, B, C> Shrink<P3<A, B, C>> shrinkP3(Shrink<A> sa, final Shrink<B> sb, final Shrink<C> sc) {
        return new Shrink<>(new F<P3<A, B, C>, Stream<P3<A, B, C>>>() { // from class: fj.test.Shrink.45
            {
                Shrink.this = sa;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<P3<A, B, C>> f(P3<A, B, C> p) {
                return Shrink.this.shrink((Shrink) p._1()).bind(sb.shrink((Shrink) p._2()), sc.shrink((Shrink) p._3()), P.p3());
            }
        });
    }

    public static <A, B, C, D> Shrink<P4<A, B, C, D>> shrinkP4(Shrink<A> sa, final Shrink<B> sb, final Shrink<C> sc, final Shrink<D> sd) {
        return new Shrink<>(new F<P4<A, B, C, D>, Stream<P4<A, B, C, D>>>() { // from class: fj.test.Shrink.46
            {
                Shrink.this = sa;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<P4<A, B, C, D>> f(P4<A, B, C, D> p) {
                return Shrink.this.shrink((Shrink) p._1()).bind(sb.shrink((Shrink) p._2()), sc.shrink((Shrink) p._3()), sd.shrink((Shrink) p._4()), P.p4());
            }
        });
    }

    public static <A, B, C, D, E> Shrink<P5<A, B, C, D, E>> shrinkP5(Shrink<A> sa, final Shrink<B> sb, final Shrink<C> sc, final Shrink<D> sd, final Shrink<E> se) {
        return new Shrink<>(new F<P5<A, B, C, D, E>, Stream<P5<A, B, C, D, E>>>() { // from class: fj.test.Shrink.47
            {
                Shrink.this = sa;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<P5<A, B, C, D, E>> f(P5<A, B, C, D, E> p) {
                return Shrink.this.shrink((Shrink) p._1()).bind(sb.shrink((Shrink) p._2()), sc.shrink((Shrink) p._3()), sd.shrink((Shrink) p._4()), se.shrink((Shrink) p._5()), P.p5());
            }
        });
    }

    public static <A, B, C, D, E, F$> Shrink<P6<A, B, C, D, E, F$>> shrinkP6(Shrink<A> sa, final Shrink<B> sb, final Shrink<C> sc, final Shrink<D> sd, final Shrink<E> se, final Shrink<F$> sf) {
        return new Shrink<>(new F<P6<A, B, C, D, E, F$>, Stream<P6<A, B, C, D, E, F$>>>() { // from class: fj.test.Shrink.48
            {
                Shrink.this = sa;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<P6<A, B, C, D, E, F$>> f(P6<A, B, C, D, E, F$> p) {
                return Shrink.this.shrink((Shrink) p._1()).bind(sb.shrink((Shrink) p._2()), sc.shrink((Shrink) p._3()), sd.shrink((Shrink) p._4()), se.shrink((Shrink) p._5()), sf.shrink((Shrink) p._6()), P.p6());
            }
        });
    }

    public static <A, B, C, D, E, F$, G> Shrink<P7<A, B, C, D, E, F$, G>> shrinkP7(Shrink<A> sa, final Shrink<B> sb, final Shrink<C> sc, final Shrink<D> sd, final Shrink<E> se, final Shrink<F$> sf, final Shrink<G> sg) {
        return new Shrink<>(new F<P7<A, B, C, D, E, F$, G>, Stream<P7<A, B, C, D, E, F$, G>>>() { // from class: fj.test.Shrink.49
            {
                Shrink.this = sa;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<P7<A, B, C, D, E, F$, G>> f(P7<A, B, C, D, E, F$, G> p) {
                return Shrink.this.shrink((Shrink) p._1()).bind(sb.shrink((Shrink) p._2()), sc.shrink((Shrink) p._3()), sd.shrink((Shrink) p._4()), se.shrink((Shrink) p._5()), sf.shrink((Shrink) p._6()), sg.shrink((Shrink) p._7()), P.p7());
            }
        });
    }

    public static <A, B, C, D, E, F$, G, H> Shrink<P8<A, B, C, D, E, F$, G, H>> shrinkP8(Shrink<A> sa, final Shrink<B> sb, final Shrink<C> sc, final Shrink<D> sd, final Shrink<E> se, final Shrink<F$> sf, final Shrink<G> sg, final Shrink<H> sh) {
        return new Shrink<>(new F<P8<A, B, C, D, E, F$, G, H>, Stream<P8<A, B, C, D, E, F$, G, H>>>() { // from class: fj.test.Shrink.50
            {
                Shrink.this = sa;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<P8<A, B, C, D, E, F$, G, H>> f(P8<A, B, C, D, E, F$, G, H> p) {
                return Shrink.this.shrink((Shrink) p._1()).bind(sb.shrink((Shrink) p._2()), sc.shrink((Shrink) p._3()), sd.shrink((Shrink) p._4()), se.shrink((Shrink) p._5()), sf.shrink((Shrink) p._6()), sg.shrink((Shrink) p._7()), sh.shrink((Shrink) p._8()), P.p8());
            }
        });
    }
}
