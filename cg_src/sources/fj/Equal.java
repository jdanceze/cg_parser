package fj;

import fj.data.Array;
import fj.data.Either;
import fj.data.LazyString;
import fj.data.List;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Set;
import fj.data.Stream;
import fj.data.Tree;
import fj.data.Validation;
import fj.data.Writer;
import fj.data.hlist.HList;
import fj.data.vector.V2;
import fj.data.vector.V3;
import fj.data.vector.V4;
import fj.data.vector.V5;
import fj.data.vector.V6;
import fj.data.vector.V7;
import fj.data.vector.V8;
import java.math.BigDecimal;
import java.math.BigInteger;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Equal.class */
public final class Equal<A> {
    private final F<A, F<A, Boolean>> f;
    public static final Equal<Boolean> booleanEqual = anyEqual();
    public static final Equal<Byte> byteEqual = anyEqual();
    public static final Equal<Character> charEqual = anyEqual();
    public static final Equal<Double> doubleEqual = anyEqual();
    public static final Equal<Float> floatEqual = anyEqual();
    public static final Equal<Integer> intEqual = anyEqual();
    public static final Equal<BigInteger> bigintEqual = anyEqual();
    public static final Equal<BigDecimal> bigdecimalEqual = anyEqual();
    public static final Equal<Long> longEqual = anyEqual();
    public static final Equal<Short> shortEqual = anyEqual();
    public static final Equal<String> stringEqual = anyEqual();
    public static final Equal<StringBuffer> stringBufferEqual = new Equal<>(new F<StringBuffer, F<StringBuffer, Boolean>>() { // from class: fj.Equal.4
        @Override // fj.F
        public F<StringBuffer, Boolean> f(final StringBuffer sb1) {
            return new F<StringBuffer, Boolean>() { // from class: fj.Equal.4.1
                {
                    AnonymousClass4.this = this;
                }

                @Override // fj.F
                public Boolean f(StringBuffer sb2) {
                    if (sb1.length() == sb2.length()) {
                        for (int i = 0; i < sb1.length(); i++) {
                            if (sb1.charAt(i) != sb2.charAt(i)) {
                                return false;
                            }
                        }
                        return true;
                    }
                    return false;
                }
            };
        }
    });
    public static final Equal<StringBuilder> stringBuilderEqual = new Equal<>(new F<StringBuilder, F<StringBuilder, Boolean>>() { // from class: fj.Equal.5
        @Override // fj.F
        public F<StringBuilder, Boolean> f(final StringBuilder sb1) {
            return new F<StringBuilder, Boolean>() { // from class: fj.Equal.5.1
                {
                    AnonymousClass5.this = this;
                }

                @Override // fj.F
                public Boolean f(StringBuilder sb2) {
                    if (sb1.length() == sb2.length()) {
                        for (int i = 0; i < sb1.length(); i++) {
                            if (sb1.charAt(i) != sb2.charAt(i)) {
                                return false;
                            }
                        }
                        return true;
                    }
                    return false;
                }
            };
        }
    });
    public static final Equal<LazyString> eq = streamEqual(charEqual).comap(LazyString.toStream);
    public static final Equal<HList.HNil> hListEqual = anyEqual();

    private Equal(F<A, F<A, Boolean>> f) {
        this.f = f;
    }

    public boolean eq(A a1, A a2) {
        return this.f.f(a1).f(a2).booleanValue();
    }

    public F2<A, A, Boolean> eq() {
        return new F2<A, A, Boolean>() { // from class: fj.Equal.1
            {
                Equal.this = this;
            }

            @Override // fj.F2
            public Boolean f(A a, A a1) {
                return Boolean.valueOf(Equal.this.eq(a, a1));
            }
        };
    }

    public F<A, Boolean> eq(final A a) {
        return new F<A, Boolean>() { // from class: fj.Equal.2
            {
                Equal.this = this;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                return f((AnonymousClass2) obj);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Boolean f(A a1) {
                return Boolean.valueOf(Equal.this.eq(a, a1));
            }
        };
    }

    public <B> Equal<B> comap(F<B, A> f) {
        return equal(F1Functions.o(F1Functions.o(F1Functions.andThen(f), this.f), f));
    }

    public static <A> Equal<A> equal(F<A, F<A, Boolean>> f) {
        return new Equal<>(f);
    }

    public static <A> Equal<A> anyEqual() {
        return new Equal<>(new F<A, F<A, Boolean>>() { // from class: fj.Equal.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass3) obj);
            }

            @Override // fj.F
            public F<A, Boolean> f(final A a1) {
                return new F<A, Boolean>() { // from class: fj.Equal.3.1
                    {
                        AnonymousClass3.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public Boolean f(A a2) {
                        return Boolean.valueOf(a1.equals(a2));
                    }
                };
            }
        });
    }

    public static <A, B> Equal<Either<A, B>> eitherEqual(Equal<A> ea, final Equal<B> eb) {
        return new Equal<>(new F<Either<A, B>, F<Either<A, B>, Boolean>>() { // from class: fj.Equal.6
            {
                Equal.this = ea;
            }

            @Override // fj.F
            public F<Either<A, B>, Boolean> f(final Either<A, B> e1) {
                return new F<Either<A, B>, Boolean>() { // from class: fj.Equal.6.1
                    {
                        AnonymousClass6.this = this;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Boolean f(Either<A, B> e2) {
                        return Boolean.valueOf((e1.isLeft() && e2.isLeft() && ((Boolean) ((F) Equal.this.f.f(e1.left().value())).f(e2.left().value())).booleanValue()) || (e1.isRight() && e2.isRight() && ((Boolean) ((F) eb.f.f(e1.right().value())).f(e2.right().value())).booleanValue()));
                    }
                };
            }
        });
    }

    public static <A, B> Equal<Validation<A, B>> validationEqual(Equal<A> ea, Equal<B> eb) {
        return eitherEqual(ea, eb).comap(Validation.either());
    }

    public static <A> Equal<List<A>> listEqual(Equal<A> ea) {
        return new Equal<>(new F<List<A>, F<List<A>, Boolean>>() { // from class: fj.Equal.7
            {
                Equal.this = ea;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public F<List<A>, Boolean> f(final List<A> a1) {
                return new F<List<A>, Boolean>() { // from class: fj.Equal.7.1
                    {
                        AnonymousClass7.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                        return f((List) ((List) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public Boolean f(List<A> a2) {
                        List<A> x2;
                        List<A> x1 = a1;
                        List<A> list = a2;
                        while (true) {
                            x2 = list;
                            if (!x1.isNotEmpty() || !x2.isNotEmpty()) {
                                break;
                            } else if (!Equal.this.eq(x1.head(), x2.head())) {
                                return false;
                            } else {
                                x1 = x1.tail();
                                list = x2.tail();
                            }
                        }
                        return Boolean.valueOf(x1.isEmpty() && x2.isEmpty());
                    }
                };
            }
        });
    }

    public static <A> Equal<NonEmptyList<A>> nonEmptyListEqual(Equal<A> ea) {
        return listEqual(ea).comap(NonEmptyList.toList_());
    }

    public static <A> Equal<Option<A>> optionEqual(Equal<A> ea) {
        return new Equal<>(new F<Option<A>, F<Option<A>, Boolean>>() { // from class: fj.Equal.8
            {
                Equal.this = ea;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Option) ((Option) obj));
            }

            public F<Option<A>, Boolean> f(final Option<A> o1) {
                return new F<Option<A>, Boolean>() { // from class: fj.Equal.8.1
                    {
                        AnonymousClass8.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                        return f((Option) ((Option) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public Boolean f(Option<A> o2) {
                        return Boolean.valueOf((o1.isNone() && o2.isNone()) || (o1.isSome() && o2.isSome() && ((Boolean) ((F) Equal.this.f.f(o1.some())).f(o2.some())).booleanValue()));
                    }
                };
            }
        });
    }

    public static <A> Equal<Stream<A>> streamEqual(Equal<A> ea) {
        return new Equal<>(new F<Stream<A>, F<Stream<A>, Boolean>>() { // from class: fj.Equal.9
            {
                Equal.this = ea;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Stream) ((Stream) obj));
            }

            public F<Stream<A>, Boolean> f(final Stream<A> a1) {
                return new F<Stream<A>, Boolean>() { // from class: fj.Equal.9.1
                    {
                        AnonymousClass9.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                        return f((Stream) ((Stream) obj));
                    }

                    public Boolean f(Stream<A> a2) {
                        Stream<A> x2;
                        Stream<A> x1 = a1;
                        Stream<A> stream = a2;
                        while (true) {
                            x2 = stream;
                            if (!x1.isNotEmpty() || !x2.isNotEmpty()) {
                                break;
                            } else if (!Equal.this.eq(x1.head(), x2.head())) {
                                return false;
                            } else {
                                x1 = x1.tail()._1();
                                stream = x2.tail()._1();
                            }
                        }
                        return Boolean.valueOf(x1.isEmpty() && x2.isEmpty());
                    }
                };
            }
        });
    }

    public static <A> Equal<Array<A>> arrayEqual(Equal<A> ea) {
        return new Equal<>(new F<Array<A>, F<Array<A>, Boolean>>() { // from class: fj.Equal.10
            {
                Equal.this = ea;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public F<Array<A>, Boolean> f(final Array<A> a1) {
                return new F<Array<A>, Boolean>() { // from class: fj.Equal.10.1
                    {
                        AnonymousClass10.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                        return f((Array) ((Array) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public Boolean f(Array<A> a2) {
                        if (a1.length() == a2.length()) {
                            for (int i = 0; i < a1.length(); i++) {
                                if (!Equal.this.eq(a1.get(i), a2.get(i))) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        return false;
                    }
                };
            }
        });
    }

    public static <A> Equal<Tree<A>> treeEqual(Equal<A> ea) {
        return new Equal<>(Function.curry(new F2<Tree<A>, Tree<A>, Boolean>() { // from class: fj.Equal.11
            {
                Equal.this = ea;
            }

            @Override // fj.F2
            public /* bridge */ /* synthetic */ Boolean f(Object obj, Object obj2) {
                return f((Tree) ((Tree) obj), (Tree) ((Tree) obj2));
            }

            public Boolean f(Tree<A> t1, Tree<A> t2) {
                return Boolean.valueOf(Equal.this.eq(t1.root(), t2.root()) && Equal.p1Equal(Equal.streamEqual(Equal.treeEqual(Equal.this))).eq(t2.subForest(), t1.subForest()));
            }
        }));
    }

    public static <A> Equal<P1<A>> p1Equal(Equal<A> ea) {
        return new Equal<>(new F<P1<A>, F<P1<A>, Boolean>>() { // from class: fj.Equal.12
            {
                Equal.this = ea;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P1) ((P1) obj));
            }

            public F<P1<A>, Boolean> f(final P1<A> p1) {
                return new F<P1<A>, Boolean>() { // from class: fj.Equal.12.1
                    {
                        AnonymousClass12.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                        return f((P1) ((P1) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public Boolean f(P1<A> p2) {
                        return Boolean.valueOf(Equal.this.eq(p1._1(), p2._1()));
                    }
                };
            }
        });
    }

    public static <A, B> Equal<P2<A, B>> p2Equal(Equal<A> ea, final Equal<B> eb) {
        return new Equal<>(new F<P2<A, B>, F<P2<A, B>, Boolean>>() { // from class: fj.Equal.13
            {
                Equal.this = ea;
            }

            @Override // fj.F
            public F<P2<A, B>, Boolean> f(final P2<A, B> p1) {
                return new F<P2<A, B>, Boolean>() { // from class: fj.Equal.13.1
                    {
                        AnonymousClass13.this = this;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Boolean f(P2<A, B> p2) {
                        return Boolean.valueOf(Equal.this.eq(p1._1(), p2._1()) && eb.eq(p1._2(), p2._2()));
                    }
                };
            }
        });
    }

    public static <A, B, C> Equal<P3<A, B, C>> p3Equal(Equal<A> ea, final Equal<B> eb, final Equal<C> ec) {
        return new Equal<>(new F<P3<A, B, C>, F<P3<A, B, C>, Boolean>>() { // from class: fj.Equal.14
            {
                Equal.this = ea;
            }

            @Override // fj.F
            public F<P3<A, B, C>, Boolean> f(final P3<A, B, C> p1) {
                return new F<P3<A, B, C>, Boolean>() { // from class: fj.Equal.14.1
                    {
                        AnonymousClass14.this = this;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Boolean f(P3<A, B, C> p2) {
                        return Boolean.valueOf(Equal.this.eq(p1._1(), p2._1()) && eb.eq(p1._2(), p2._2()) && ec.eq(p1._3(), p2._3()));
                    }
                };
            }
        });
    }

    public static <A, B, C, D> Equal<P4<A, B, C, D>> p4Equal(Equal<A> ea, final Equal<B> eb, final Equal<C> ec, final Equal<D> ed) {
        return new Equal<>(new F<P4<A, B, C, D>, F<P4<A, B, C, D>, Boolean>>() { // from class: fj.Equal.15
            {
                Equal.this = ea;
            }

            @Override // fj.F
            public F<P4<A, B, C, D>, Boolean> f(final P4<A, B, C, D> p1) {
                return new F<P4<A, B, C, D>, Boolean>() { // from class: fj.Equal.15.1
                    {
                        AnonymousClass15.this = this;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Boolean f(P4<A, B, C, D> p2) {
                        return Boolean.valueOf(Equal.this.eq(p1._1(), p2._1()) && eb.eq(p1._2(), p2._2()) && ec.eq(p1._3(), p2._3()) && ed.eq(p1._4(), p2._4()));
                    }
                };
            }
        });
    }

    public static <A, B, C, D, E> Equal<P5<A, B, C, D, E>> p5Equal(Equal<A> ea, final Equal<B> eb, final Equal<C> ec, final Equal<D> ed, final Equal<E> ee) {
        return new Equal<>(new F<P5<A, B, C, D, E>, F<P5<A, B, C, D, E>, Boolean>>() { // from class: fj.Equal.16
            {
                Equal.this = ea;
            }

            @Override // fj.F
            public F<P5<A, B, C, D, E>, Boolean> f(final P5<A, B, C, D, E> p1) {
                return new F<P5<A, B, C, D, E>, Boolean>() { // from class: fj.Equal.16.1
                    {
                        AnonymousClass16.this = this;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Boolean f(P5<A, B, C, D, E> p2) {
                        return Boolean.valueOf(Equal.this.eq(p1._1(), p2._1()) && eb.eq(p1._2(), p2._2()) && ec.eq(p1._3(), p2._3()) && ed.eq(p1._4(), p2._4()) && ee.eq(p1._5(), p2._5()));
                    }
                };
            }
        });
    }

    public static <A, B, C, D, E, F$> Equal<P6<A, B, C, D, E, F$>> p6Equal(Equal<A> ea, final Equal<B> eb, final Equal<C> ec, final Equal<D> ed, final Equal<E> ee, final Equal<F$> ef) {
        return new Equal<>(new F<P6<A, B, C, D, E, F$>, F<P6<A, B, C, D, E, F$>, Boolean>>() { // from class: fj.Equal.17
            {
                Equal.this = ea;
            }

            @Override // fj.F
            public F<P6<A, B, C, D, E, F$>, Boolean> f(final P6<A, B, C, D, E, F$> p1) {
                return new F<P6<A, B, C, D, E, F$>, Boolean>() { // from class: fj.Equal.17.1
                    {
                        AnonymousClass17.this = this;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Boolean f(P6<A, B, C, D, E, F$> p2) {
                        return Boolean.valueOf(Equal.this.eq(p1._1(), p2._1()) && eb.eq(p1._2(), p2._2()) && ec.eq(p1._3(), p2._3()) && ed.eq(p1._4(), p2._4()) && ee.eq(p1._5(), p2._5()) && ef.eq(p1._6(), p2._6()));
                    }
                };
            }
        });
    }

    public static <A, B, C, D, E, F$, G> Equal<P7<A, B, C, D, E, F$, G>> p7Equal(Equal<A> ea, final Equal<B> eb, final Equal<C> ec, final Equal<D> ed, final Equal<E> ee, final Equal<F$> ef, final Equal<G> eg) {
        return new Equal<>(new F<P7<A, B, C, D, E, F$, G>, F<P7<A, B, C, D, E, F$, G>, Boolean>>() { // from class: fj.Equal.18
            {
                Equal.this = ea;
            }

            @Override // fj.F
            public F<P7<A, B, C, D, E, F$, G>, Boolean> f(final P7<A, B, C, D, E, F$, G> p1) {
                return new F<P7<A, B, C, D, E, F$, G>, Boolean>() { // from class: fj.Equal.18.1
                    {
                        AnonymousClass18.this = this;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Boolean f(P7<A, B, C, D, E, F$, G> p2) {
                        return Boolean.valueOf(Equal.this.eq(p1._1(), p2._1()) && eb.eq(p1._2(), p2._2()) && ec.eq(p1._3(), p2._3()) && ed.eq(p1._4(), p2._4()) && ee.eq(p1._5(), p2._5()) && ef.eq(p1._6(), p2._6()) && eg.eq(p1._7(), p2._7()));
                    }
                };
            }
        });
    }

    public static <A, B, C, D, E, F$, G, H> Equal<P8<A, B, C, D, E, F$, G, H>> p8Equal(Equal<A> ea, final Equal<B> eb, final Equal<C> ec, final Equal<D> ed, final Equal<E> ee, final Equal<F$> ef, final Equal<G> eg, final Equal<H> eh) {
        return new Equal<>(new F<P8<A, B, C, D, E, F$, G, H>, F<P8<A, B, C, D, E, F$, G, H>, Boolean>>() { // from class: fj.Equal.19
            {
                Equal.this = ea;
            }

            @Override // fj.F
            public F<P8<A, B, C, D, E, F$, G, H>, Boolean> f(final P8<A, B, C, D, E, F$, G, H> p1) {
                return new F<P8<A, B, C, D, E, F$, G, H>, Boolean>() { // from class: fj.Equal.19.1
                    {
                        AnonymousClass19.this = this;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Boolean f(P8<A, B, C, D, E, F$, G, H> p2) {
                        return Boolean.valueOf(Equal.this.eq(p1._1(), p2._1()) && eb.eq(p1._2(), p2._2()) && ec.eq(p1._3(), p2._3()) && ed.eq(p1._4(), p2._4()) && ee.eq(p1._5(), p2._5()) && ef.eq(p1._6(), p2._6()) && eg.eq(p1._7(), p2._7()) && eh.eq(p1._8(), p2._8()));
                    }
                };
            }
        });
    }

    public static <A> Equal<V2<A>> v2Equal(Equal<A> ea) {
        return streamEqual(ea).comap(V2.toStream_());
    }

    public static <A> Equal<V3<A>> v3Equal(Equal<A> ea) {
        return streamEqual(ea).comap(V3.toStream_());
    }

    public static <A> Equal<V4<A>> v4Equal(Equal<A> ea) {
        return streamEqual(ea).comap(V4.toStream_());
    }

    public static <A> Equal<V5<A>> v5Equal(Equal<A> ea) {
        return streamEqual(ea).comap(V5.toStream_());
    }

    public static <A> Equal<V6<A>> v6Equal(Equal<A> ea) {
        return streamEqual(ea).comap(V6.toStream_());
    }

    public static <A> Equal<V7<A>> v7Equal(Equal<A> ea) {
        return streamEqual(ea).comap(V7.toStream_());
    }

    public static <A> Equal<V8<A>> v8Equal(Equal<A> ea) {
        return streamEqual(ea).comap(V8.toStream_());
    }

    public static <E, L extends HList<L>> Equal<HList.HCons<E, L>> hListEqual(Equal<E> e, final Equal<L> l) {
        return equal(Function.curry(new F2<HList.HCons<E, L>, HList.HCons<E, L>, Boolean>() { // from class: fj.Equal.20
            {
                Equal.this = e;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F2
            public Boolean f(HList.HCons<E, L> c1, HList.HCons<E, L> c2) {
                return Boolean.valueOf(Equal.this.eq(c1.head(), c2.head()) && l.eq(c1.tail(), c2.tail()));
            }
        }));
    }

    public static <A> Equal<Set<A>> setEqual(Equal<A> e) {
        return equal(Function.curry(new F2<Set<A>, Set<A>, Boolean>() { // from class: fj.Equal.21
            {
                Equal.this = e;
            }

            @Override // fj.F2
            public /* bridge */ /* synthetic */ Boolean f(Object obj, Object obj2) {
                return f((Set) ((Set) obj), (Set) ((Set) obj2));
            }

            public Boolean f(Set<A> a, Set<A> b) {
                return Boolean.valueOf(Equal.streamEqual(Equal.this).eq(a.toStream(), b.toStream()));
            }
        }));
    }

    public static <A, B> Equal<Writer<A, B>> writerEqual(Equal<A> eq1, Equal<B> eq2) {
        return new Equal<>(Equal$$Lambda$1.lambdaFactory$(eq1, eq2));
    }

    public static /* synthetic */ Boolean lambda$null$46(Equal equal, Equal equal2, Writer writer, Writer w2) {
        return Boolean.valueOf(p2Equal(equal, equal2).eq(writer.run(), w2.run()));
    }
}
