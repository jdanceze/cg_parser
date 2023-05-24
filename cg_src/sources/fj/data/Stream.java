package fj.data;

import fj.Bottom;
import fj.Equal;
import fj.F;
import fj.F2;
import fj.F3;
import fj.Function;
import fj.Monoid;
import fj.Ord;
import fj.Ordering;
import fj.P;
import fj.P1;
import fj.P2;
import fj.Unit;
import fj.control.parallel.Promise;
import fj.control.parallel.Strategy;
import fj.function.Booleans;
import fj.function.Effect1;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Stream.class */
public abstract class Stream<A> implements Iterable<A> {
    public abstract A head();

    public abstract P1<Stream<A>> tail();

    private Stream() {
    }

    @Override // java.lang.Iterable
    public final Iterator<A> iterator() {
        return toCollection().iterator();
    }

    public final boolean isEmpty() {
        return this instanceof Nil;
    }

    public final boolean isNotEmpty() {
        return this instanceof Cons;
    }

    public final <B> B stream(B nil, F<A, F<P1<Stream<A>>, B>> cons) {
        return isEmpty() ? nil : cons.f(head()).f(tail());
    }

    public final <B> B foldRight(final F<A, F<P1<B>, B>> f, final B b) {
        return isEmpty() ? b : f.f(head()).f(new P1<B>() { // from class: fj.data.Stream.1
            /* JADX WARN: Type inference failed for: r0v5, types: [B, java.lang.Object] */
            @Override // fj.P1
            public B _1() {
                return Stream.this.tail()._1().foldRight((F<A, F<P1<F<A, F<P1<B>, B>>>, F<A, F<P1<B>, B>>>>) f, (F<A, F<P1<B>, B>>) b);
            }
        });
    }

    public final <B> B foldRight(F2<A, P1<B>, B> f, B b) {
        return (B) foldRight((F<A, F<P1<F<A, F<P1<B>, B>>>, F<A, F<P1<B>, B>>>>) Function.curry(f), (F<A, F<P1<B>, B>>) b);
    }

    public final <B> B foldRight1(F<A, F<B, B>> f, B b) {
        return (B) foldRight((F<A, F<P1<F<A, F<P1<B>, B>>>, F<A, F<P1<B>, B>>>>) Function.compose((F) Function.andThen().f(P1.__1()), f), (F<A, F<P1<B>, B>>) b);
    }

    public final <B> B foldRight1(F2<A, B, B> f, B b) {
        return (B) foldRight1((F<A, F<F<A, F<B, B>>, F<A, F<B, B>>>>) Function.curry(f), (F<A, F<B, B>>) b);
    }

    public final <B> B foldLeft(F<B, F<A, B>> f, B b) {
        B x = b;
        Stream<A> stream = this;
        while (true) {
            Stream<A> xs = stream;
            if (!xs.isEmpty()) {
                x = f.f(x).f(xs.head());
                stream = xs.tail()._1();
            } else {
                return x;
            }
        }
    }

    public final <B> B foldLeft(F2<B, A, B> f, B b) {
        return (B) foldLeft((F<F<B, F<A, B>>, F<A, F<B, F<A, B>>>>) Function.curry(f), (F<B, F<A, B>>) b);
    }

    public final A foldLeft1(F2<A, A, A> f) {
        return foldLeft1(Function.curry(f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final A foldLeft1(F<A, F<A, A>> f) {
        if (isEmpty()) {
            throw Bottom.error("Undefined: foldLeft1 on empty list");
        }
        return (A) tail()._1().foldLeft((F<F<A, F<A, A>>, F<A, F<A, F<A, A>>>>) f, (F<A, F<A, A>>) head());
    }

    public final A orHead(P1<A> a) {
        return isEmpty() ? a._1() : head();
    }

    public final P1<Stream<A>> orTail(P1<Stream<A>> as) {
        return isEmpty() ? as : tail();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.data.Stream$2  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Stream$2.class */
    public class AnonymousClass2 extends P1<Stream<A>> {
        final /* synthetic */ Object val$a;

        AnonymousClass2(Object obj) {
            this.val$a = obj;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // fj.P1
        public Stream<A> _1() {
            return prefix(this.val$a, Stream.this.tail()._1());
        }

        public Stream<A> prefix(A x, final Stream<A> xs) {
            return xs.isEmpty() ? xs : Stream.cons(x, P.p(Stream.cons(xs.head(), new P1<Stream<A>>() { // from class: fj.data.Stream.2.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // fj.P1
                public Stream<A> _1() {
                    return AnonymousClass2.this.prefix(AnonymousClass2.this.val$a, xs.tail()._1());
                }
            })));
        }
    }

    public final Stream<A> intersperse(A a) {
        return isEmpty() ? this : cons(head(), new AnonymousClass2(a));
    }

    public final <B> Stream<B> map(final F<A, B> f) {
        return isEmpty() ? nil() : cons(f.f(head()), new P1<Stream<B>>() { // from class: fj.data.Stream.3
            @Override // fj.P1
            public Stream<B> _1() {
                return Stream.this.tail()._1().map(f);
            }
        });
    }

    public static <A, B> F<F<A, B>, F<Stream<A>, Stream<B>>> map_() {
        return new F<F<A, B>, F<Stream<A>, Stream<B>>>() { // from class: fj.data.Stream.4
            @Override // fj.F
            public F<Stream<A>, Stream<B>> f(final F<A, B> f) {
                return new F<Stream<A>, Stream<B>>() { // from class: fj.data.Stream.4.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Stream) ((Stream) obj));
                    }

                    public Stream<B> f(Stream<A> as) {
                        return as.map(f);
                    }
                };
            }
        };
    }

    public final Unit foreach(F<A, Unit> f) {
        Stream<A> stream = this;
        while (true) {
            Stream<A> xs = stream;
            if (xs.isNotEmpty()) {
                f.f(xs.head());
                stream = xs.tail()._1();
            } else {
                return Unit.unit();
            }
        }
    }

    public final void foreachDoEffect(Effect1<A> f) {
        Stream<A> stream = this;
        while (true) {
            Stream<A> xs = stream;
            if (xs.isNotEmpty()) {
                f.f(xs.head());
                stream = xs.tail()._1();
            } else {
                return;
            }
        }
    }

    public final Stream<A> filter(final F<A, Boolean> f) {
        final Stream<A> as = dropWhile(Booleans.not(f));
        return as.isNotEmpty() ? cons(as.head(), new P1<Stream<A>>() { // from class: fj.data.Stream.5
            @Override // fj.P1
            public Stream<A> _1() {
                return as.tail()._1().filter(f);
            }
        }) : as;
    }

    public final Stream<A> append(final Stream<A> as) {
        return isEmpty() ? as : cons(head(), new P1<Stream<A>>() { // from class: fj.data.Stream.6
            @Override // fj.P1
            public Stream<A> _1() {
                return Stream.this.tail()._1().append(as);
            }
        });
    }

    public final Stream<A> append(final P1<Stream<A>> as) {
        return isEmpty() ? as._1() : cons(head(), new P1<Stream<A>>() { // from class: fj.data.Stream.7
            @Override // fj.P1
            public Stream<A> _1() {
                return Stream.this.tail()._1().append(as);
            }
        });
    }

    public final Stream<A> minus(Equal<A> eq, Stream<A> xs) {
        return removeAll(Function.compose(Monoid.disjunctionMonoid.sumLeftS(), xs.mapM(Function.curry(eq.eq()))));
    }

    public final Stream<A> removeAll(F<A, Boolean> f) {
        return filter(Function.compose(Booleans.not, f));
    }

    public static <A, B> F<B, Stream<A>> sequence_(Stream<F<B, A>> fs) {
        return (F) fs.foldRight((F2<F<B, A>, P1<F2<F<B, A>, P1<F<B, Stream<A>>>, F<B, Stream<A>>>>, F2<F<B, A>, P1<F<B, Stream<A>>>, F<B, Stream<A>>>>) new F2<F<B, A>, P1<F<B, Stream<A>>>, F<B, Stream<A>>>() { // from class: fj.data.Stream.8
            @Override // fj.F2
            public F<B, Stream<A>> f(F<B, A> baf, P1<F<B, Stream<A>>> p1) {
                return Function.bind(baf, (F) p1._1(), Function.curry(new F2<A, Stream<A>, Stream<A>>() { // from class: fj.data.Stream.8.1
                    @Override // fj.F2
                    public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                        return f((AnonymousClass1) obj, (Stream<AnonymousClass1>) obj2);
                    }

                    public Stream<A> f(A a, Stream<A> stream) {
                        return Stream.cons(a, P.p(stream));
                    }
                }));
            }
        }, (F2<F<B, A>, P1<F<B, Stream<A>>>, F<B, Stream<A>>>) Function.constant(nil()));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C> F<B, Stream<C>> mapM(F<A, F<B, C>> f) {
        return sequence_(map(f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B> Stream<B> bind(F<A, Stream<B>> f) {
        return ((Stream) map(f).foldLeft((F2<F2, B, F2>) ((F2<Stream<B>, Stream<B>, Stream<B>>) new F2<Stream<B>, Stream<B>, Stream<B>>() { // from class: fj.data.Stream.9
            @Override // fj.F2
            public Stream<B> f(Stream<B> accumulator, Stream<B> element) {
                Stream stream = accumulator;
                Iterator it = element.iterator();
                while (it.hasNext()) {
                    stream = stream.cons(it.next());
                }
                return stream;
            }
        }), (F2) nil())).reverse();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C> Stream<C> bind(Stream<B> sb, F<A, F<B, C>> f) {
        return (Stream<B>) sb.apply(map(f));
    }

    public final <B, C> Stream<C> bind(Stream<B> sb, F2<A, B, C> f) {
        return bind(sb, Function.curry(f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D> Stream<D> bind(Stream<B> sb, Stream<C> sc, F<A, F<B, F<C, D>>> f) {
        return (Stream<B>) sc.apply(bind(sb, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E> Stream<E> bind(Stream<B> sb, Stream<C> sc, Stream<D> sd, F<A, F<B, F<C, F<D, E>>>> f) {
        return (Stream<B>) sd.apply(bind(sb, sc, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E, F$> Stream<F$> bind(Stream<B> sb, Stream<C> sc, Stream<D> sd, Stream<E> se, F<A, F<B, F<C, F<D, F<E, F$>>>>> f) {
        return (Stream<B>) se.apply(bind(sb, sc, sd, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E, F$, G> Stream<G> bind(Stream<B> sb, Stream<C> sc, Stream<D> sd, Stream<E> se, Stream<F$> sf, F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f) {
        return (Stream<B>) sf.apply(bind(sb, sc, sd, se, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E, F$, G, H> Stream<H> bind(Stream<B> sb, Stream<C> sc, Stream<D> sd, Stream<E> se, Stream<F$> sf, Stream<G> sg, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f) {
        return (Stream<B>) sg.apply(bind(sb, sc, sd, se, sf, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E, F$, G, H, I> Stream<I> bind(Stream<B> sb, Stream<C> sc, Stream<D> sd, Stream<E> se, Stream<F$> sf, Stream<G> sg, Stream<H> sh, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f) {
        return (Stream<B>) sh.apply(bind(sb, sc, sd, se, sf, sg, f));
    }

    public final <B> Stream<B> sequence(Stream<B> bs) {
        F<A, Stream<B>> c = Function.constant(bs);
        return bind(c);
    }

    public final <B> Stream<B> apply(Stream<F<A, B>> sf) {
        return sf.bind(new F<F<A, B>, Stream<B>>() { // from class: fj.data.Stream.10
            @Override // fj.F
            public Stream<B> f(final F<A, B> f) {
                return Stream.this.map(new F<A, B>() { // from class: fj.data.Stream.10.1
                    /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
                    @Override // fj.F
                    public B f(A a) {
                        return f.f(a);
                    }
                });
            }
        });
    }

    public final Stream<A> interleave(final Stream<A> as) {
        return isEmpty() ? as : as.isEmpty() ? this : cons(head(), new P1<Stream<A>>() { // from class: fj.data.Stream.11
            @Override // fj.P1
            public Stream<A> _1() {
                return as.interleave(Stream.this.tail()._1());
            }
        });
    }

    public final Stream<A> sort(Ord<A> o) {
        return mergesort(o, map((F) Function.flip(cons()).f(P.p(nil()))));
    }

    private static <A> Stream<A> mergesort(Ord<A> o, Stream<Stream<A>> s) {
        if (s.isEmpty()) {
            return nil();
        }
        Stream<Stream<A>> stream = s;
        while (true) {
            Stream<Stream<A>> xss = stream;
            if (xss.tail()._1().isNotEmpty()) {
                stream = mergePairs(o, xss);
            } else {
                return xss.head();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <A> Stream<Stream<A>> mergePairs(final Ord<A> o, Stream<Stream<A>> s) {
        if (s.isEmpty() || s.tail()._1().isEmpty()) {
            return s;
        }
        final Stream<Stream<A>> t = s.tail()._1();
        return cons(merge(o, s.head(), t.head()), new P1<Stream<Stream<A>>>() { // from class: fj.data.Stream.12
            @Override // fj.P1
            public Stream<Stream<A>> _1() {
                return Stream.mergePairs(Ord.this, t.tail()._1());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <A> Stream<A> merge(final Ord<A> o, final Stream<A> xs, final Stream<A> ys) {
        if (xs.isEmpty()) {
            return ys;
        }
        if (ys.isEmpty()) {
            return xs;
        }
        A x = xs.head();
        A y = ys.head();
        if (o.isGreaterThan(x, y)) {
            return cons(y, new P1<Stream<A>>() { // from class: fj.data.Stream.13
                @Override // fj.P1
                public Stream<A> _1() {
                    return Stream.merge(Ord.this, xs, ys.tail()._1());
                }
            });
        }
        return cons(x, new P1<Stream<A>>() { // from class: fj.data.Stream.14
            @Override // fj.P1
            public Stream<A> _1() {
                return Stream.merge(Ord.this, xs.tail()._1(), ys);
            }
        });
    }

    public final Stream<A> sort(Ord<A> o, Strategy<Unit> s) {
        return qs(o, s).claim();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Promise<Stream<A>> qs(Ord<A> o, Strategy<Unit> s) {
        if (isEmpty()) {
            return Promise.promise(s, P.p(this));
        }
        F<Boolean, Boolean> id = Function.identity();
        A x = head();
        P1<Stream<A>> xs = tail();
        Promise<Stream<A>> left = Promise.join(s, xs.map(flt(o, s, x, id)));
        Promise<Stream<A>> right = (Promise) xs.map(flt(o, s, x, Booleans.not))._1();
        Monoid<Stream<A>> m = Monoid.streamMonoid();
        return right.fmap(m.sum(single(x))).apply(left.fmap(m.sum()));
    }

    private static <A> F<Stream<A>, Promise<Stream<A>>> qs_(final Ord<A> o, final Strategy<Unit> s) {
        return new F<Stream<A>, Promise<Stream<A>>>() { // from class: fj.data.Stream.15
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Stream) ((Stream) obj));
            }

            public Promise<Stream<A>> f(Stream<A> xs) {
                return xs.qs(Ord.this, s);
            }
        };
    }

    private static <A> F<Stream<A>, Promise<Stream<A>>> flt(Ord<A> o, Strategy<Unit> s, A x, F<Boolean, Boolean> f) {
        F<F<A, Boolean>, F<Stream<A>, Stream<A>>> filter = filter();
        F<A, Boolean> lt = o.isLessThan(x);
        return Function.compose(qs_(o, s), filter.f(Function.compose(f, lt)));
    }

    public final Collection<A> toCollection() {
        return new AbstractCollection<A>() { // from class: fj.data.Stream.16
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            public Iterator<A> iterator() {
                return new Iterator<A>() { // from class: fj.data.Stream.16.1
                    private Stream<A> xs;

                    {
                        this.xs = Stream.this;
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.xs.isNotEmpty();
                    }

                    @Override // java.util.Iterator
                    public A next() {
                        if (this.xs.isEmpty()) {
                            throw new NoSuchElementException();
                        }
                        A a = this.xs.head();
                        this.xs = this.xs.tail()._1();
                        return a;
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return Stream.this.length();
            }
        };
    }

    public static Stream<Integer> range(final int from, final long to) {
        return ((long) from) >= to ? nil() : cons(Integer.valueOf(from), new P1<Stream<Integer>>() { // from class: fj.data.Stream.17
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // fj.P1
            public Stream<Integer> _1() {
                return Stream.range(from + 1, to);
            }
        });
    }

    public static <A> Stream<A> stream(A... as) {
        return as.length == 0 ? nil() : unfold(P2.tuple(new F2<A[], Integer, Option<P2<A, P2<A[], Integer>>>>() { // from class: fj.data.Stream.18
            @Override // fj.F2
            public Option<P2<A, P2<A[], Integer>>> f(A[] as2, Integer i) {
                return i.intValue() >= as2.length ? Option.none() : Option.some(P.p(as2[i.intValue()], P.p(as2, Integer.valueOf(i.intValue() + 1))));
            }
        }), P.p(as, 0));
    }

    public static <A> Stream<A> forever(Enumerator<A> e, A from) {
        return forever(e, from, 1L);
    }

    public static <A> Stream<A> forever(final Enumerator<A> e, final A from, final long step) {
        return cons(from, new P1<Stream<A>>() { // from class: fj.data.Stream.19
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.P1
            public Stream<A> _1() {
                return (Stream) Enumerator.this.plus(from, step).map(new F<A, Stream<A>>() { // from class: fj.data.Stream.19.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public Stream<A> f(A a) {
                        return Stream.forever(Enumerator.this, a, step);
                    }
                }).orSome((Option) Stream.nil());
            }
        });
    }

    public static <A> Stream<A> range(Enumerator<A> e, A from, A to) {
        return range(e, from, to, 1L);
    }

    public static <A> Stream<A> range(final Enumerator<A> e, final A from, final A to, final long step) {
        final Ordering o = e.order().compare(from, to);
        return (o == Ordering.EQ || (step > 0 && o == Ordering.GT) || (step < 0 && o == Ordering.LT)) ? single(from) : cons(from, new P1<Stream<A>>() { // from class: fj.data.Stream.20
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.P1
            public Stream<A> _1() {
                return Stream.join(Enumerator.this.plus(from, step).filter(new F<A, Boolean>() { // from class: fj.data.Stream.20.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                        return f((AnonymousClass2) obj);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // fj.F
                    public Boolean f(A a) {
                        return Boolean.valueOf(o != Ordering.LT ? !Enumerator.this.order().isGreaterThan((A) to, a) : !Enumerator.this.order().isLessThan((A) to, a));
                    }
                }).map((F<A, Stream<A>>) new F<A, Stream<A>>() { // from class: fj.data.Stream.20.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public Stream<A> f(A a) {
                        return Stream.range(Enumerator.this, a, to, step);
                    }
                }).toStream());
            }
        });
    }

    public static Stream<Integer> range(final int from) {
        return cons(Integer.valueOf(from), new P1<Stream<Integer>>() { // from class: fj.data.Stream.21
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // fj.P1
            public Stream<Integer> _1() {
                return Stream.range(from + 1);
            }
        });
    }

    public static <A> F<F<A, Boolean>, F<Stream<A>, Stream<A>>> filter() {
        return Function.curry(new F2<F<A, Boolean>, Stream<A>, Stream<A>>() { // from class: fj.data.Stream.22
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((F) ((F) obj), (Stream) ((Stream) obj2));
            }

            public Stream<A> f(F<A, Boolean> f, Stream<A> as) {
                return as.filter(f);
            }
        });
    }

    public final <B> Stream<B> zapp(final Stream<F<A, B>> fs) {
        return (fs.isEmpty() || isEmpty()) ? nil() : cons(fs.head().f(head()), new P1<Stream<B>>() { // from class: fj.data.Stream.23
            @Override // fj.P1
            public Stream<B> _1() {
                return Stream.this.tail()._1().zapp((Stream<A>) fs.tail()._1());
            }
        });
    }

    public final <B, C> Stream<C> zipWith(Stream<B> bs, F<A, F<B, C>> f) {
        return (Stream<B>) bs.zapp(zapp(repeat(f)));
    }

    public final <B, C> Stream<C> zipWith(Stream<B> bs, F2<A, B, C> f) {
        return zipWith(bs, Function.curry(f));
    }

    public final <B, C> F<Stream<B>, Stream<C>> zipWith(final F<A, F<B, C>> f) {
        return new F<Stream<B>, Stream<C>>() { // from class: fj.data.Stream.24
            @Override // fj.F
            public Stream<C> f(Stream<B> stream) {
                return Stream.this.zipWith(stream, f);
            }
        };
    }

    public final <B> Stream<P2<A, B>> zip(Stream<B> bs) {
        return (Stream<P2<A, B>>) zipWith(bs, P.p2());
    }

    public final Stream<P2<A, Integer>> zipIndex() {
        return (Stream<P2<A, Integer>>) zipWith(range(0), (F2<A, Integer, P2<A, Integer>>) new F2<A, Integer, P2<A, Integer>>() { // from class: fj.data.Stream.25
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Integer num) {
                return f2((AnonymousClass25) obj, num);
            }

            /* renamed from: f  reason: avoid collision after fix types in other method */
            public P2<A, Integer> f2(A a, Integer i) {
                return P.p(a, i);
            }
        });
    }

    public final <X> Either<X, A> toEither(P1<X> x) {
        return isEmpty() ? Either.left(x._1()) : Either.right(head());
    }

    public final Option<A> toOption() {
        return isEmpty() ? Option.none() : Option.some(head());
    }

    public final List<A> toList() {
        List<A> as = List.nil();
        Stream<A> stream = this;
        while (true) {
            Stream<A> x = stream;
            if (!x.isEmpty()) {
                as = as.snoc(x.head());
                stream = x.tail()._1();
            } else {
                return as;
            }
        }
    }

    public final Array<A> toArray() {
        int l = length();
        Object[] a = new Object[l];
        Stream<A> x = this;
        for (int i = 0; i < l; i++) {
            a[i] = x.head();
            x = x.tail()._1();
        }
        return Array.mkArray(a);
    }

    public final Array<A> toArray(Class<A[]> c) {
        Object[] objArr = (Object[]) java.lang.reflect.Array.newInstance(c.getComponentType(), length());
        int i = 0;
        Iterator<A> it = iterator();
        while (it.hasNext()) {
            A x = it.next();
            objArr[i] = x;
            i++;
        }
        return Array.array(objArr);
    }

    public final A[] array(Class<A[]> c) {
        return toArray(c).array(c);
    }

    public final Stream<A> cons(A a) {
        return new Cons(a, new P1<Stream<A>>() { // from class: fj.data.Stream.26
            @Override // fj.P1
            public Stream<A> _1() {
                return Stream.this;
            }
        });
    }

    public static String asString(Stream<Character> cs) {
        return LazyString.fromStream(cs).toString();
    }

    public static Stream<Character> fromString(String s) {
        return LazyString.str(s).toStream();
    }

    public final Stream<A> snoc(A a) {
        return snoc((P1) P.p(a));
    }

    public final Stream<A> snoc(final P1<A> a) {
        return append(new P1<Stream<A>>() { // from class: fj.data.Stream.27
            @Override // fj.P1
            public Stream<A> _1() {
                return Stream.single(a._1());
            }
        });
    }

    public final Stream<A> take(final int n) {
        if (n <= 0 || isEmpty()) {
            return nil();
        }
        return cons(head(), new P1<Stream<A>>() { // from class: fj.data.Stream.28
            @Override // fj.P1
            public Stream<A> _1() {
                return Stream.this.tail()._1().take(n - 1);
            }
        });
    }

    public final Stream<A> drop(int i) {
        Stream<A> xs;
        int c = 0;
        Stream<A> stream = this;
        while (true) {
            xs = stream;
            if (!xs.isNotEmpty() || c >= i) {
                break;
            }
            c++;
            stream = xs.tail()._1();
        }
        return xs;
    }

    public final Stream<A> takeWhile(final F<A, Boolean> f) {
        if (isEmpty()) {
            return this;
        }
        if (f.f(head()).booleanValue()) {
            return cons(head(), new P1<Stream<A>>() { // from class: fj.data.Stream.29
                @Override // fj.P1
                public Stream<A> _1() {
                    return Stream.this.tail()._1().takeWhile(f);
                }
            });
        }
        return nil();
    }

    public final Stream<A> dropWhile(F<A, Boolean> f) {
        Stream<A> as;
        Stream<A> stream = this;
        while (true) {
            as = stream;
            if (as.isEmpty() || !f.f(as.head()).booleanValue()) {
                break;
            }
            stream = as.tail()._1();
        }
        return as;
    }

    public final P2<Stream<A>, Stream<A>> span(final F<A, Boolean> p) {
        if (isEmpty()) {
            return P.p(this, this);
        }
        if (p.f(head()).booleanValue()) {
            final P1<P2<Stream<A>, Stream<A>>> yszs = new P1<P2<Stream<A>, Stream<A>>>() { // from class: fj.data.Stream.30
                @Override // fj.P1
                public P2<Stream<A>, Stream<A>> _1() {
                    return Stream.this.tail()._1().span(p);
                }
            };
            return new P2<Stream<A>, Stream<A>>() { // from class: fj.data.Stream.31
                @Override // fj.P2
                public Stream<A> _1() {
                    return Stream.cons(Stream.this.head(), yszs.map(P2.__1()));
                }

                @Override // fj.P2
                public Stream<A> _2() {
                    return (Stream) ((P2) yszs._1())._2();
                }
            };
        }
        return P.p(nil(), this);
    }

    public final Stream<A> replace(final F<A, Boolean> p, final A a) {
        if (isEmpty()) {
            return nil();
        }
        final P2<Stream<A>, Stream<A>> s = span(p);
        return s._1().append(cons(a, new P1<Stream<A>>() { // from class: fj.data.Stream.32
            @Override // fj.P1
            public Stream<A> _1() {
                return ((Stream) s._2()).tail()._1().replace(p, (A) a);
            }
        }));
    }

    public final P2<Stream<A>, Stream<A>> split(F<A, Boolean> p) {
        return span(Function.compose(Booleans.not, p));
    }

    public final Stream<A> reverse() {
        return (Stream) foldLeft((F<F, F<A, F>>) ((F<Stream<A>, F<A, Stream<A>>>) new F<Stream<A>, F<A, Stream<A>>>() { // from class: fj.data.Stream.33
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Stream) ((Stream) obj));
            }

            public F<A, Stream<A>> f(final Stream<A> as) {
                return new F<A, Stream<A>>() { // from class: fj.data.Stream.33.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public Stream<A> f(A a) {
                        return Stream.cons(a, new P1<Stream<A>>() { // from class: fj.data.Stream.33.1.1
                            @Override // fj.P1
                            public Stream<A> _1() {
                                return as;
                            }
                        });
                    }
                };
            }
        }), (F) nil());
    }

    public final A last() {
        return reverse().head();
    }

    public final int length() {
        Stream<A> xs = this;
        int i = 0;
        while (!xs.isEmpty()) {
            xs = xs.tail()._1();
            i++;
        }
        return i;
    }

    public final A index(int i) {
        if (i < 0) {
            throw Bottom.error("index " + i + " out of range on stream");
        }
        Stream<A> xs = this;
        for (int c = 0; c < i; c++) {
            if (xs.isEmpty()) {
                throw Bottom.error("index " + i + " out of range on stream");
            }
            xs = xs.tail()._1();
        }
        if (xs.isEmpty()) {
            throw Bottom.error("index " + i + " out of range on stream");
        }
        return xs.head();
    }

    public final boolean forall(F<A, Boolean> f) {
        return isEmpty() || (f.f(head()).booleanValue() && tail()._1().forall(f));
    }

    public final boolean exists(F<A, Boolean> f) {
        return dropWhile(Booleans.not(f)).isNotEmpty();
    }

    public final Option<A> find(F<A, Boolean> f) {
        Stream<A> stream = this;
        while (true) {
            Stream<A> as = stream;
            if (as.isNotEmpty()) {
                if (!f.f(as.head()).booleanValue()) {
                    stream = as.tail()._1();
                } else {
                    return Option.some(as.head());
                }
            } else {
                return Option.none();
            }
        }
    }

    public final <B> Stream<B> cobind(F<Stream<A>, B> k) {
        return substreams().map(k);
    }

    public final Stream<Stream<A>> tails() {
        return isEmpty() ? nil() : cons(this, new P1<Stream<Stream<A>>>() { // from class: fj.data.Stream.34
            @Override // fj.P1
            public Stream<Stream<A>> _1() {
                return Stream.this.tail()._1().tails();
            }
        });
    }

    public final Stream<Stream<A>> inits() {
        Stream<Stream<A>> nil = cons(nil(), new P1<Stream<Stream<A>>>() { // from class: fj.data.Stream.35
            @Override // fj.P1
            public Stream<Stream<A>> _1() {
                return Stream.nil();
            }
        });
        return isEmpty() ? nil : nil.append(new P1<Stream<Stream<A>>>() { // from class: fj.data.Stream.36
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.P1
            public Stream<Stream<A>> _1() {
                return (Stream<Stream<A>>) Stream.this.tail()._1().inits().map((F) Stream.cons_().f(Stream.this.head()));
            }
        });
    }

    public final Stream<Stream<A>> substreams() {
        return (Stream<Stream<A>>) tails().bind((F<Stream<A>, Stream<Stream<A>>>) new F<Stream<A>, Stream<Stream<A>>>() { // from class: fj.data.Stream.37
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Stream) ((Stream) obj));
            }

            public Stream<Stream<A>> f(Stream<A> stream) {
                return stream.inits();
            }
        });
    }

    public final Option<Integer> indexOf(final F<A, Boolean> p) {
        return zipIndex().find(new F<P2<A, Integer>, Boolean>() { // from class: fj.data.Stream.38
            @Override // fj.F
            public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                return f((P2) ((P2) obj));
            }

            public Boolean f(P2<A, Integer> p2) {
                return (Boolean) p.f(p2._1());
            }
        }).map(P2.__2());
    }

    public final <B> Stream<B> sequenceW(final Stream<F<Stream<A>, B>> fs) {
        if (fs.isEmpty()) {
            return nil();
        }
        return cons(fs.head().f(this), new P1<Stream<B>>() { // from class: fj.data.Stream.39
            @Override // fj.P1
            public Stream<B> _1() {
                return Stream.this.sequenceW(fs.tail()._1());
            }
        });
    }

    public final F<Integer, A> toFunction() {
        return new F<Integer, A>() { // from class: fj.data.Stream.40
            @Override // fj.F
            public A f(Integer i) {
                return (A) Stream.this.index(i.intValue());
            }
        };
    }

    public static <A> Stream<A> fromFunction(F<Natural, A> f) {
        return fromFunction(Enumerator.naturalEnumerator, f, Natural.ZERO);
    }

    public static <A, B> Stream<A> fromFunction(final Enumerator<B> e, final F<B, A> f, final B i) {
        return cons(f.f(i), new P1<Stream<A>>() { // from class: fj.data.Stream.41
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.P1
            public Stream<A> _1() {
                Option successor = Enumerator.this.successor(i);
                if (successor.isSome()) {
                    return Stream.fromFunction(Enumerator.this, f, successor.some());
                }
                return Stream.nil();
            }
        });
    }

    public static <A, B> P2<Stream<A>, Stream<B>> unzip(Stream<P2<A, B>> xs) {
        return (P2) xs.foldRight((F2<P2<A, B>, P1<F2<P2<A, B>, P1<P2<Stream<A>, Stream<B>>>, P2<Stream<A>, Stream<B>>>>, F2<P2<A, B>, P1<P2<Stream<A>, Stream<B>>>, P2<Stream<A>, Stream<B>>>>) new F2<P2<A, B>, P1<P2<Stream<A>, Stream<B>>>, P2<Stream<A>, Stream<B>>>() { // from class: fj.data.Stream.42
            @Override // fj.F2
            public P2<Stream<A>, Stream<B>> f(P2<A, B> p, P1<P2<Stream<A>, Stream<B>>> ps) {
                P2 p2 = (P2) ps._1();
                return P.p(Stream.cons(p._1(), P.p(p2._1())), Stream.cons(p._2(), P.p(p2._2())));
            }
        }, (F2<P2<A, B>, P1<P2<Stream<A>, Stream<B>>>, P2<Stream<A>, Stream<B>>>) P.p(nil(), nil()));
    }

    public static <A, B, C> F<Stream<A>, F<Stream<B>, F<F<A, F<B, C>>, Stream<C>>>> zipWith() {
        return Function.curry(new F3<Stream<A>, Stream<B>, F<A, F<B, C>>, Stream<C>>() { // from class: fj.data.Stream.43
            @Override // fj.F3
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2, Object obj3) {
                return f((Stream) ((Stream) obj), (Stream) obj2, (F) obj3);
            }

            public Stream<C> f(Stream<A> as, Stream<B> bs, F<A, F<B, C>> f) {
                return as.zipWith(bs, f);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Stream$Nil.class */
    public static final class Nil<A> extends Stream<A> {
        private Nil() {
            super();
        }

        @Override // fj.data.Stream
        public A head() {
            throw Bottom.error("head on empty stream");
        }

        @Override // fj.data.Stream
        public P1<Stream<A>> tail() {
            throw Bottom.error("tail on empty stream");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Stream$Cons.class */
    public static final class Cons<A> extends Stream<A> {
        private final A head;
        private final P1<Stream<A>> tail;

        Cons(A head, P1<Stream<A>> tail) {
            super();
            this.head = head;
            this.tail = tail.memo();
        }

        @Override // fj.data.Stream
        public A head() {
            return this.head;
        }

        @Override // fj.data.Stream
        public P1<Stream<A>> tail() {
            return this.tail;
        }
    }

    public static <A> F<A, F<P1<Stream<A>>, Stream<A>>> cons() {
        return new F<A, F<P1<Stream<A>>, Stream<A>>>() { // from class: fj.data.Stream.44
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass44) obj);
            }

            @Override // fj.F
            public F<P1<Stream<A>>, Stream<A>> f(final A a) {
                return new F<P1<Stream<A>>, Stream<A>>() { // from class: fj.data.Stream.44.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((P1) ((P1) obj));
                    }

                    public Stream<A> f(P1<Stream<A>> list) {
                        return Stream.cons(a, list);
                    }
                };
            }
        };
    }

    public static <A> F<A, F<Stream<A>, Stream<A>>> cons_() {
        return Function.curry(new F2<A, Stream<A>, Stream<A>>() { // from class: fj.data.Stream.45
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((AnonymousClass45) obj, (Stream<AnonymousClass45>) obj2);
            }

            public Stream<A> f(A a, Stream<A> as) {
                return as.cons(a);
            }
        });
    }

    public static <A> Stream<A> nil() {
        return new Nil();
    }

    public static <A> P1<Stream<A>> nil_() {
        return new P1<Stream<A>>() { // from class: fj.data.Stream.46
            @Override // fj.P1
            public Stream<A> _1() {
                return new Nil();
            }
        };
    }

    public static <A> F<Stream<A>, Boolean> isEmpty_() {
        return new F<Stream<A>, Boolean>() { // from class: fj.data.Stream.47
            @Override // fj.F
            public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                return f((Stream) ((Stream) obj));
            }

            public Boolean f(Stream<A> as) {
                return Boolean.valueOf(as.isEmpty());
            }
        };
    }

    public static <A> F<Stream<A>, Boolean> isNotEmpty_() {
        return new F<Stream<A>, Boolean>() { // from class: fj.data.Stream.48
            @Override // fj.F
            public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                return f((Stream) ((Stream) obj));
            }

            public Boolean f(Stream<A> as) {
                return Boolean.valueOf(as.isNotEmpty());
            }
        };
    }

    public static <A> Stream<A> single(A a) {
        return cons(a, new P1<Stream<A>>() { // from class: fj.data.Stream.49
            @Override // fj.P1
            public Stream<A> _1() {
                return Stream.nil();
            }
        });
    }

    public static <A> F<A, Stream<A>> single() {
        return new F<A, Stream<A>>() { // from class: fj.data.Stream.50
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass50) obj);
            }

            @Override // fj.F
            public Stream<A> f(A a) {
                return Stream.single(a);
            }
        };
    }

    public static <A> Stream<A> cons(A head, P1<Stream<A>> tail) {
        return new Cons(head, tail);
    }

    public static <A> Stream<A> join(Stream<Stream<A>> o) {
        return (Stream) Monoid.streamMonoid().sumRight(o);
    }

    public static <A> F<Stream<Stream<A>>, Stream<A>> join() {
        return new F<Stream<Stream<A>>, Stream<A>>() { // from class: fj.data.Stream.51
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Stream) ((Stream) obj));
            }

            public Stream<A> f(Stream<Stream<A>> as) {
                return Stream.join(as);
            }
        };
    }

    public static <A, B> Stream<A> unfold(final F<B, Option<P2<A, B>>> f, B b) {
        Option<P2<A, B>> o = f.f(b);
        if (o.isNone()) {
            return nil();
        }
        final P2<A, B> p = o.some();
        return cons(p._1(), new P1<Stream<A>>() { // from class: fj.data.Stream.52
            @Override // fj.P1
            public Stream<A> _1() {
                return Stream.unfold(F.this, p._2());
            }
        });
    }

    public static <A> Stream<A> iterateWhile(final F<A, A> f, final F<A, Boolean> p, A a) {
        return unfold(new F<A, Option<P2<A, A>>>() { // from class: fj.data.Stream.53
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass53) obj);
            }

            @Override // fj.F
            public Option<P2<A, A>> f(final A o) {
                return Option.iif(new F<P2<A, A>, Boolean>() { // from class: fj.data.Stream.53.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                        return f((P2) ((P2) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public Boolean f(P2<A, A> p2) {
                        return (Boolean) F.this.f(o);
                    }
                }, P.p(o, f.f(o)));
            }
        }, a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.data.Stream$1Util  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Stream$1Util.class */
    public final class C1Util {
        C1Util() {
        }

        public <A> Stream<A> iteratorStream(final Iterator<A> i) {
            if (i.hasNext()) {
                A a = i.next();
                return Stream.cons(a, new P1<Stream<A>>() { // from class: fj.data.Stream.1Util.1
                    @Override // fj.P1
                    public Stream<A> _1() {
                        return C1Util.this.iteratorStream(i);
                    }
                });
            }
            return Stream.nil();
        }
    }

    public static <A> Stream<A> iterableStream(Iterable<A> i) {
        return new C1Util().iteratorStream(i.iterator());
    }

    public static <A> Stream<A> repeat(final A a) {
        return cons(a, new P1<Stream<A>>() { // from class: fj.data.Stream.54
            @Override // fj.P1
            public Stream<A> _1() {
                return Stream.repeat(a);
            }
        });
    }

    public static <A> Stream<A> cycle(Stream<A> as) {
        if (as.isEmpty()) {
            throw Bottom.error("cycle on empty list");
        }
        return as.append(new P1<Stream<A>>() { // from class: fj.data.Stream.55
            @Override // fj.P1
            public Stream<A> _1() {
                return Stream.cycle(Stream.this);
            }
        });
    }

    public static <A> Stream<A> iterate(final F<A, A> f, final A a) {
        return cons(a, new P1<Stream<A>>() { // from class: fj.data.Stream.56
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.P1
            public Stream<A> _1() {
                return Stream.iterate(F.this, F.this.f(a));
            }
        });
    }

    public static <A> F<F<A, A>, F<A, Stream<A>>> iterate() {
        return Function.curry(new F2<F<A, A>, A, Stream<A>>() { // from class: fj.data.Stream.57
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((F<F<A, A>, F<A, A>>) obj, (F<A, A>) obj2);
            }

            public Stream<A> f(F<A, A> f, A a) {
                return Stream.iterate(f, a);
            }
        });
    }

    public static <A, B> F<F<A, Stream<B>>, F<Stream<A>, Stream<B>>> bind_() {
        return Function.curry(new F2<F<A, Stream<B>>, Stream<A>, Stream<B>>() { // from class: fj.data.Stream.58
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((F) obj, (Stream) ((Stream) obj2));
            }

            public Stream<B> f(F<A, Stream<B>> f, Stream<A> as) {
                return as.bind(f);
            }
        });
    }

    public static <A, B> F<F<A, F<P1<B>, B>>, F<B, F<Stream<A>, B>>> foldRight() {
        return Function.curry(new F3<F<A, F<P1<B>, B>>, B, Stream<A>, B>() { // from class: fj.data.Stream.59
            @Override // fj.F3
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2, Object obj3) {
                return f((F<A, F<P1<F>, F>>) obj, (F) obj2, (Stream) ((Stream) obj3));
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            public B f(F<A, F<P1<B>, B>> f, B b, Stream<A> as) {
                return as.foldRight((F<A, F<P1<F<A, F<P1<B>, B>>>, F<A, F<P1<B>, B>>>>) f, (F<A, F<P1<B>, B>>) b);
            }
        });
    }
}
