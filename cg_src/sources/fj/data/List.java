package fj.data;

import fj.Bottom;
import fj.Equal;
import fj.F;
import fj.F2;
import fj.F2Functions;
import fj.F3;
import fj.Function;
import fj.Hash;
import fj.Monoid;
import fj.Ord;
import fj.Ordering;
import fj.P;
import fj.P1;
import fj.P2;
import fj.Show;
import fj.Unit;
import fj.control.Trampoline;
import fj.function.Booleans;
import fj.function.Effect1;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/List.class */
public abstract class List<A> implements Iterable<A> {
    public abstract A head();

    public abstract List<A> tail();

    private List() {
    }

    @Override // java.lang.Iterable
    public final Iterator<A> iterator() {
        return toCollection().iterator();
    }

    public final int length() {
        return ((Integer) foldLeft((F<F, F<A, F>>) ((F<Integer, F<A, Integer>>) new F<Integer, F<A, Integer>>() { // from class: fj.data.List.1
            {
                List.this = this;
            }

            @Override // fj.F
            public F<A, Integer> f(final Integer i) {
                return new F<A, Integer>() { // from class: fj.data.List.1.1
                    {
                        AnonymousClass1.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Integer f(Object obj) {
                        return f((C00921) obj);
                    }

                    @Override // fj.F
                    public Integer f(A a) {
                        return Integer.valueOf(i.intValue() + 1);
                    }
                };
            }
        }), (F) 0)).intValue();
    }

    public final boolean isEmpty() {
        return this instanceof Nil;
    }

    public final boolean isNotEmpty() {
        return this instanceof Cons;
    }

    public final <B> B list(B nil, F<A, F<List<A>, B>> cons) {
        return isEmpty() ? nil : cons.f(head()).f(tail());
    }

    public final A orHead(P1<A> a) {
        return isEmpty() ? a._1() : head();
    }

    public final List<A> orTail(P1<List<A>> as) {
        return isEmpty() ? as._1() : tail();
    }

    public final Option<A> toOption() {
        return isEmpty() ? Option.none() : Option.some(head());
    }

    public final <X> Either<X, A> toEither(P1<X> x) {
        return isEmpty() ? Either.left(x._1()) : Either.right(head());
    }

    public final Stream<A> toStream() {
        Stream<A> nil = Stream.nil();
        return (Stream) foldRight((F<A, F<F, F>>) ((F<A, F<Stream<A>, Stream<A>>>) new F<A, F<Stream<A>, Stream<A>>>() { // from class: fj.data.List.2
            {
                List.this = this;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass2) obj);
            }

            @Override // fj.F
            public F<Stream<A>, Stream<A>> f(final A a) {
                return new F<Stream<A>, Stream<A>>() { // from class: fj.data.List.2.1
                    {
                        AnonymousClass2.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Stream) ((Stream) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public Stream<A> f(Stream<A> as) {
                        return as.cons(a);
                    }
                };
            }
        }), (F) nil);
    }

    public final Array<A> toArray() {
        Object[] a = new Object[length()];
        List<A> x = this;
        for (int i = 0; i < length(); i++) {
            a[i] = x.head();
            x = x.tail();
        }
        return Array.mkArray(a);
    }

    public final Array<A> toArray(Class<A[]> c) {
        Object[] objArr = (Object[]) java.lang.reflect.Array.newInstance(c.getComponentType(), length());
        List<A> x = this;
        for (int i = 0; i < length(); i++) {
            objArr[i] = x.head();
            x = x.tail();
        }
        return Array.array(objArr);
    }

    public final A[] array(Class<A[]> c) {
        return toArray(c).array(c);
    }

    public final List<A> cons(A a) {
        return new Cons(a, this);
    }

    public final List<A> conss(A a) {
        return new Cons(a, this);
    }

    public final <B> List<B> map(F<A, B> f) {
        Buffer<B> bs = Buffer.empty();
        List<A> list = this;
        while (true) {
            List<A> xs = list;
            if (xs.isNotEmpty()) {
                bs.snoc(f.f(xs.head()));
                list = xs.tail();
            } else {
                return bs.toList();
            }
        }
    }

    public final Unit foreach(F<A, Unit> f) {
        List<A> list = this;
        while (true) {
            List<A> xs = list;
            if (xs.isNotEmpty()) {
                f.f(xs.head());
                list = xs.tail();
            } else {
                return Unit.unit();
            }
        }
    }

    public final void foreachDoEffect(Effect1<A> f) {
        List<A> list = this;
        while (true) {
            List<A> xs = list;
            if (xs.isNotEmpty()) {
                f.f(xs.head());
                list = xs.tail();
            } else {
                return;
            }
        }
    }

    public final List<A> filter(F<A, Boolean> f) {
        Buffer<A> b = Buffer.empty();
        List<A> list = this;
        while (true) {
            List<A> xs = list;
            if (xs.isNotEmpty()) {
                A h = xs.head();
                if (f.f(h).booleanValue()) {
                    b.snoc(h);
                }
                list = xs.tail();
            } else {
                return b.toList();
            }
        }
    }

    public final List<A> removeAll(F<A, Boolean> f) {
        return filter(Function.compose(Booleans.not, f));
    }

    public final List<A> delete(A a, Equal<A> e) {
        P2<List<A>, List<A>> p = span(Function.compose(Booleans.not, e.eq(a)));
        return p._2().isEmpty() ? p._1() : p._1().append(p._2().tail());
    }

    public final List<A> takeWhile(F<A, Boolean> f) {
        Buffer<A> b = Buffer.empty();
        boolean taking = true;
        List<A> list = this;
        while (true) {
            List<A> xs = list;
            if (!xs.isNotEmpty() || !taking) {
                break;
            }
            A h = xs.head();
            if (f.f(h).booleanValue()) {
                b.snoc(h);
            } else {
                taking = false;
            }
            list = xs.tail();
        }
        return b.toList();
    }

    public final List<A> dropWhile(F<A, Boolean> f) {
        List<A> xs;
        List<A> list = this;
        while (true) {
            xs = list;
            if (!xs.isNotEmpty() || !f.f(xs.head()).booleanValue()) {
                break;
            }
            list = xs.tail();
        }
        return xs;
    }

    public final P2<List<A>, List<A>> span(F<A, Boolean> p) {
        Buffer<A> b = Buffer.empty();
        List<A> list = this;
        while (true) {
            List<A> xs = list;
            if (xs.isNotEmpty()) {
                if (p.f(xs.head()).booleanValue()) {
                    b.snoc(xs.head());
                    list = xs.tail();
                } else {
                    return P.p(b.toList(), xs);
                }
            } else {
                return P.p(b.toList(), nil());
            }
        }
    }

    public final P2<List<A>, List<A>> breakk(final F<A, Boolean> p) {
        return span(new F<A, Boolean>() { // from class: fj.data.List.3
            {
                List.this = this;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                return f((AnonymousClass3) obj);
            }

            @Override // fj.F
            public Boolean f(A a) {
                return Boolean.valueOf(!((Boolean) p.f(a)).booleanValue());
            }
        });
    }

    public final List<List<A>> group(Equal<A> e) {
        if (isEmpty()) {
            return nil();
        }
        P2<List<A>, List<A>> z = tail().span(e.eq(head()));
        return cons(z._1().cons((List<A>) head()), z._2().group(e));
    }

    public final <B> List<B> bind(F<A, List<B>> f) {
        Buffer<B> b = Buffer.empty();
        List<A> list = this;
        while (true) {
            List<A> xs = list;
            if (xs.isNotEmpty()) {
                b.append(f.f(xs.head()));
                list = xs.tail();
            } else {
                return b.toList();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C> List<C> bind(List<B> lb, F<A, F<B, C>> f) {
        return (List<B>) lb.apply(map(f));
    }

    public final <B, C> List<C> bind(List<B> lb, F2<A, B, C> f) {
        return bind(lb, Function.curry(f));
    }

    public static <A, B, C> F<List<A>, F<List<B>, List<C>>> liftM2(final F<A, F<B, C>> f) {
        return Function.curry(new F2<List<A>, List<B>, List<C>>() { // from class: fj.data.List.4
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((List) ((List) obj), (List) obj2);
            }

            public List<C> f(List<A> as, List<B> bs) {
                return as.bind(bs, f);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D> List<D> bind(List<B> lb, List<C> lc, F<A, F<B, F<C, D>>> f) {
        return (List<B>) lc.apply(bind(lb, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E> List<E> bind(List<B> lb, List<C> lc, List<D> ld, F<A, F<B, F<C, F<D, E>>>> f) {
        return (List<B>) ld.apply(bind(lb, lc, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E, F$> List<F$> bind(List<B> lb, List<C> lc, List<D> ld, List<E> le, F<A, F<B, F<C, F<D, F<E, F$>>>>> f) {
        return (List<B>) le.apply(bind(lb, lc, ld, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E, F$, G> List<G> bind(List<B> lb, List<C> lc, List<D> ld, List<E> le, List<F$> lf, F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f) {
        return (List<B>) lf.apply(bind(lb, lc, ld, le, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E, F$, G, H> List<H> bind(List<B> lb, List<C> lc, List<D> ld, List<E> le, List<F$> lf, List<G> lg, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f) {
        return (List<B>) lg.apply(bind(lb, lc, ld, le, lf, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E, F$, G, H, I> List<I> bind(List<B> lb, List<C> lc, List<D> ld, List<E> le, List<F$> lf, List<G> lg, List<H> lh, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f) {
        return (List<B>) lh.apply(bind(lb, lc, ld, le, lf, lg, f));
    }

    public final <B> List<B> sequence(List<B> bs) {
        F<A, List<B>> c = Function.constant(bs);
        return bind(c);
    }

    public final <B> List<B> apply(List<F<A, B>> lf) {
        return lf.bind(new F<F<A, B>, List<B>>() { // from class: fj.data.List.5
            {
                List.this = this;
            }

            @Override // fj.F
            public List<B> f(F<A, B> f) {
                return List.this.map(f);
            }
        });
    }

    public final List<A> append(List<A> as) {
        return Buffer.fromList(this).append(as).toList();
    }

    public final <B> B foldRight(F<A, F<B, B>> f, B b) {
        return isEmpty() ? b : f.f(head()).f((B) tail().foldRight((F<A, F<F<A, F<B, B>>, F<A, F<B, B>>>>) f, (F<A, F<B, B>>) b));
    }

    public final <B> B foldRight(F2<A, B, B> f, B b) {
        return (B) foldRight((F<A, F<F<A, F<B, B>>, F<A, F<B, B>>>>) Function.curry(f), (F<A, F<B, B>>) b);
    }

    public final <B> Trampoline<B> foldRightC(final F2<A, B, B> f, final B b) {
        return Trampoline.suspend(new P1<Trampoline<B>>() { // from class: fj.data.List.6
            {
                List.this = this;
            }

            @Override // fj.P1
            public Trampoline<B> _1() {
                return List.this.isEmpty() ? Trampoline.pure(b) : List.this.tail().foldRightC(f, b).map(F2Functions.f(f, List.this.head()));
            }
        });
    }

    public final <B> B foldLeft(F<B, F<A, B>> f, B b) {
        B x = b;
        List<A> list = this;
        while (true) {
            List<A> xs = list;
            if (!xs.isEmpty()) {
                x = f.f(x).f(xs.head());
                list = xs.tail();
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
        return (A) tail().foldLeft((F<F<A, F<A, A>>, F<A, F<A, F<A, A>>>>) f, (F<A, F<A, A>>) head());
    }

    public final List<A> reverse() {
        return (List) foldLeft((F<F, F<A, F>>) ((F<List<A>, F<A, List<A>>>) new F<List<A>, F<A, List<A>>>() { // from class: fj.data.List.7
            {
                List.this = this;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public F<A, List<A>> f(final List<A> as) {
                return new F<A, List<A>>() { // from class: fj.data.List.7.1
                    {
                        AnonymousClass7.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public List<A> f(A a) {
                        return List.cons(a, as);
                    }
                };
            }
        }), (F) nil());
    }

    public final A index(int i) {
        if (i < 0 || i > length() - 1) {
            throw Bottom.error("index " + i + " out of range on list with length " + length());
        }
        List<A> xs = this;
        for (int c = 0; c < i; c++) {
            xs = xs.tail();
        }
        return xs.head();
    }

    public final List<A> take(int i) {
        return (i <= 0 || isEmpty()) ? nil() : cons(head(), tail().take(i - 1));
    }

    public final List<A> drop(int i) {
        List<A> xs;
        int c = 0;
        List<A> list = this;
        while (true) {
            xs = list;
            if (!xs.isNotEmpty() || c >= i) {
                break;
            }
            c++;
            list = xs.tail();
        }
        return xs;
    }

    public final P2<List<A>, List<A>> splitAt(int i) {
        P2 p = P.p(nil(), nil());
        int c = 0;
        List<A> list = this;
        while (true) {
            List<A> xs = list;
            if (xs.isNotEmpty()) {
                final A h = xs.head();
                p = c < i ? p.map1(new F<List<A>, List<A>>() { // from class: fj.data.List.8
                    {
                        List.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((List) ((List) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public List<A> f(List<A> as) {
                        return as.snoc(h);
                    }
                }) : p.map2(new F<List<A>, List<A>>() { // from class: fj.data.List.9
                    {
                        List.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((List) ((List) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public List<A> f(List<A> as) {
                        return as.snoc(h);
                    }
                });
                c++;
                list = xs.tail();
            } else {
                return p;
            }
        }
    }

    public final List<List<A>> partition(final int n) {
        if (n < 1) {
            throw Bottom.error("Can't create list partitions shorter than 1 element long.");
        }
        if (isEmpty()) {
            throw Bottom.error("Partition on empty list.");
        }
        return unfold(new F<List<A>, Option<P2<List<A>, List<A>>>>() { // from class: fj.data.List.10
            {
                List.this = this;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public Option<P2<List<A>, List<A>>> f(List<A> as) {
                return as.isEmpty() ? Option.none() : Option.some(as.splitAt(n));
            }
        }, this);
    }

    public final List<List<A>> inits() {
        List<List<A>> s = single(nil());
        if (isNotEmpty()) {
            s = s.append(tail().inits().map((F) cons().f(head())));
        }
        return s;
    }

    public final List<List<A>> tails() {
        return isEmpty() ? single(nil()) : cons(this, tail().tails());
    }

    /* JADX WARN: Type inference failed for: r0v7, types: [fj.data.List$1Merge] */
    public final List<A> sort(Ord<A> o) {
        if (isEmpty()) {
            return nil();
        }
        if (tail().isEmpty()) {
            return this;
        }
        P2<List<A>, List<A>> s = splitAt(length() / 2);
        return new Object() { // from class: fj.data.List.1Merge
            {
                List.this = this;
            }

            List<A> merge(List<A> xs, List<A> ys, Ord<A> o2) {
                Buffer<A> buf = Buffer.empty();
                while (true) {
                    if (xs.isEmpty()) {
                        buf.append(ys);
                        break;
                    } else if (ys.isEmpty()) {
                        buf.append(xs);
                        break;
                    } else {
                        A x = xs.head();
                        A y = ys.head();
                        if (o2.isLessThan(x, y)) {
                            buf.snoc(x);
                            xs = xs.tail();
                        } else {
                            buf.snoc(y);
                            ys = ys.tail();
                        }
                    }
                }
                return buf.toList();
            }
        }.merge(s._1().sort(o), s._2().sort(o), o);
    }

    public final <B, C> List<C> zipWith(List<B> bs, F<A, F<B, C>> f) {
        Buffer<C> buf = Buffer.empty();
        List<A> as = this;
        while (as.isNotEmpty() && bs.isNotEmpty()) {
            buf.snoc(f.f(as.head()).f(bs.head()));
            as = as.tail();
            bs = bs.tail();
        }
        return buf.toList();
    }

    public final <B, C> List<C> zipWith(List<B> bs, F2<A, B, C> f) {
        return zipWith(bs, Function.curry(f));
    }

    public static <A, B, C> F<List<A>, F<List<B>, F<F<A, F<B, C>>, List<C>>>> zipWith() {
        return Function.curry(new F3<List<A>, List<B>, F<A, F<B, C>>, List<C>>() { // from class: fj.data.List.11
            @Override // fj.F3
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2, Object obj3) {
                return f((List) ((List) obj), (List) obj2, (F) obj3);
            }

            public List<C> f(List<A> as, List<B> bs, F<A, F<B, C>> f) {
                return as.zipWith(bs, f);
            }
        });
    }

    public final <B> List<P2<A, B>> zip(List<B> bs) {
        return (List<P2<A, B>>) zipWith(bs, P.p2());
    }

    public static <A, B> F<List<A>, F<List<B>, List<P2<A, B>>>> zip() {
        return Function.curry(new F2<List<A>, List<B>, List<P2<A, B>>>() { // from class: fj.data.List.12
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((List) ((List) obj), (List) obj2);
            }

            public List<P2<A, B>> f(List<A> as, List<B> bs) {
                return as.zip(bs);
            }
        });
    }

    public final List<P2<A, Integer>> zipIndex() {
        return (List<P2<A, Integer>>) zipWith(range(0, length()), (F<A, F<Integer, P2<A, Integer>>>) new F<A, F<Integer, P2<A, Integer>>>() { // from class: fj.data.List.13
            {
                List.this = this;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass13) obj);
            }

            @Override // fj.F
            public F<Integer, P2<A, Integer>> f(final A a) {
                return new F<Integer, P2<A, Integer>>() { // from class: fj.data.List.13.1
                    {
                        AnonymousClass13.this = this;
                    }

                    @Override // fj.F
                    public P2<A, Integer> f(Integer i) {
                        return P.p(a, i);
                    }
                };
            }
        });
    }

    public final List<A> snoc(A a) {
        return Buffer.fromList(this).snoc(a).toList();
    }

    public final boolean forall(F<A, Boolean> f) {
        return isEmpty() || (f.f(head()).booleanValue() && tail().forall(f));
    }

    public final boolean exists(F<A, Boolean> f) {
        return find(f).isSome();
    }

    public final Option<A> find(F<A, Boolean> f) {
        List<A> list = this;
        while (true) {
            List<A> as = list;
            if (as.isNotEmpty()) {
                if (!f.f(as.head()).booleanValue()) {
                    list = as.tail();
                } else {
                    return Option.some(as.head());
                }
            } else {
                return Option.none();
            }
        }
    }

    public final List<A> intersperse(A a) {
        return (isEmpty() || tail().isEmpty()) ? this : cons(head(), cons(a, tail().intersperse(a)));
    }

    public final List<A> intercalate(List<List<A>> as) {
        return join(as.intersperse(this));
    }

    public final List<A> nub() {
        return nub(Equal.anyEqual());
    }

    public final List<A> nub(final Equal<A> eq) {
        return isEmpty() ? this : cons(head(), tail().filter(new F<A, Boolean>() { // from class: fj.data.List.14
            {
                List.this = this;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                return f((AnonymousClass14) obj);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Boolean f(A a) {
                return Boolean.valueOf(!eq.eq(a, List.this.head()));
            }
        }).nub(eq));
    }

    public final List<A> nub(Ord<A> o) {
        return (List<A>) sort(o).group(o.equal()).map(head_());
    }

    public static <A> F<List<A>, A> head_() {
        return new F<List<A>, A>() { // from class: fj.data.List.15
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List<Object>) obj);
            }

            public A f(List<A> list) {
                return list.head();
            }
        };
    }

    public static <A> F<List<A>, List<A>> tail_() {
        return new F<List<A>, List<A>>() { // from class: fj.data.List.16
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public List<A> f(List<A> list) {
                return list.tail();
            }
        };
    }

    public final List<A> minus(Equal<A> eq, List<A> xs) {
        return removeAll(Function.compose(Monoid.disjunctionMonoid.sumLeft(), xs.mapM(Function.curry(eq.eq()))));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C> F<B, List<C>> mapM(F<A, F<B, C>> f) {
        return sequence_(map(f));
    }

    public final <B> Option<List<B>> mapMOption(final F<A, Option<B>> f) {
        return (Option) foldRight((F2<A, F2<A, Option<List<B>>, Option<List<B>>>, F2<A, Option<List<B>>, Option<List<B>>>>) new F2<A, Option<List<B>>, Option<List<B>>>() { // from class: fj.data.List.17
            {
                List.this = this;
            }

            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((AnonymousClass17) obj, (Option) obj2);
            }

            public Option<List<B>> f(A a, final Option<List<B>> bs) {
                return ((Option) f.f(a)).bind(new F<B, Option<List<B>>>() { // from class: fj.data.List.17.1
                    {
                        AnonymousClass17.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public Option<List<B>> f(final B b) {
                        return bs.map(new F<List<B>, List<B>>() { // from class: fj.data.List.17.1.1
                            {
                                AnonymousClass1.this = this;
                            }

                            @Override // fj.F
                            public List<B> f(List<B> bbs) {
                                return bbs.cons((List<B>) b);
                            }
                        });
                    }
                });
            }
        }, (F2<A, Option<List<B>>, Option<List<B>>>) Option.some(nil()));
    }

    public final <B> Trampoline<List<B>> mapMTrampoline(final F<A, Trampoline<B>> f) {
        return (Trampoline) foldRight((F2<A, F2<A, Trampoline<List<B>>, Trampoline<List<B>>>, F2<A, Trampoline<List<B>>, Trampoline<List<B>>>>) new F2<A, Trampoline<List<B>>, Trampoline<List<B>>>() { // from class: fj.data.List.18
            {
                List.this = this;
            }

            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((AnonymousClass18) obj, (Trampoline) obj2);
            }

            public Trampoline<List<B>> f(A a, final Trampoline<List<B>> bs) {
                return ((Trampoline) f.f(a)).bind(new F<B, Trampoline<List<B>>>() { // from class: fj.data.List.18.1
                    {
                        AnonymousClass18.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public Trampoline<List<B>> f(final B b) {
                        return bs.map(new F<List<B>, List<B>>() { // from class: fj.data.List.18.1.1
                            {
                                AnonymousClass1.this = this;
                            }

                            @Override // fj.F
                            public List<B> f(List<B> bbs) {
                                return bbs.cons((List<B>) b);
                            }
                        });
                    }
                });
            }
        }, (F2<A, Trampoline<List<B>>, Trampoline<List<B>>>) Trampoline.pure(nil()));
    }

    public final Option<Integer> elementIndex(Equal<A> e, A a) {
        return lookup(e, zipIndex(), a);
    }

    public final A last() {
        A a = head();
        List<A> tail = tail();
        while (true) {
            List<A> xs = tail;
            if (xs.isNotEmpty()) {
                a = xs.head();
                tail = xs.tail();
            } else {
                return a;
            }
        }
    }

    public final List<A> init() {
        Buffer<A> a = Buffer.empty();
        for (List<A> ys = this; ys.isNotEmpty() && ys.tail().isNotEmpty(); ys = ys.tail()) {
            a.snoc(head());
        }
        return a.toList();
    }

    public final List<A> insertBy(F<A, F<A, Ordering>> f, A x) {
        List<A> ys = this;
        Buffer<A> xs = Buffer.empty();
        while (ys.isNotEmpty() && f.f(x).f(ys.head()) == Ordering.GT) {
            xs = xs.snoc(ys.head());
            ys = ys.tail();
        }
        return xs.append(ys.cons((List<A>) x)).toList();
    }

    public final A mode(Ord<A> o) {
        return sort(o).group(o.equal()).maximum(Ord.intOrd.comap(length_())).head();
    }

    public final <B> TreeMap<B, List<A>> groupBy(F<A, B> keyFunction) {
        return groupBy(keyFunction, Ord.hashOrd());
    }

    public final <B> TreeMap<B, List<A>> groupBy(F<A, B> keyFunction, Ord<B> keyOrd) {
        return (TreeMap<B, List<A>>) groupBy(keyFunction, Function.identity(), keyOrd);
    }

    public final <B, C> TreeMap<B, List<C>> groupBy(F<A, B> keyFunction, F<A, C> valueFunction) {
        return groupBy(keyFunction, valueFunction, Ord.hashOrd());
    }

    public final <B, C> TreeMap<B, List<C>> groupBy(F<A, B> keyFunction, F<A, C> valueFunction, Ord<B> keyOrd) {
        F2<C, D, D> f2;
        List nil = nil();
        f2 = List$$Lambda$1.instance;
        return (TreeMap<B, List<C>>) groupBy(keyFunction, valueFunction, nil, f2, keyOrd);
    }

    public final <B, C> TreeMap<B, C> groupBy(F<A, B> keyFunction, F<A, C> valueFunction, Monoid<C> monoid, Ord<B> keyOrd) {
        return (TreeMap<B, C>) groupBy(keyFunction, valueFunction, monoid.zero(), Function.uncurryF2(monoid.sum()), keyOrd);
    }

    public final <B, C, D> TreeMap<B, D> groupBy(F<A, B> keyFunction, F<A, C> valueFunction, D groupingIdentity, F2<C, D, D> groupingAcc, Ord<B> keyOrd) {
        return (TreeMap) foldLeft((F<F<B, F<A, B>>, F<A, F<B, F<A, B>>>>) List$$Lambda$2.lambdaFactory$(keyFunction, valueFunction, groupingAcc, groupingIdentity), (F<B, F<A, B>>) TreeMap.empty(keyOrd));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ TreeMap lambda$null$36(F f, F f2, TreeMap treeMap, F2 f22, Object obj, Object element) {
        Object f3 = f.f(element);
        Object f4 = f2.f(element);
        return treeMap.set(f3, treeMap.get(f3).map(List$$Lambda$5.lambdaFactory$(f22, f4)).orSome((Option) f22.f(f4, obj)));
    }

    public boolean allEqual(Equal<A> eq) {
        return isEmpty() || tail().isEmpty() || (eq.eq(head(), tail().head()) && tail().allEqual(eq));
    }

    public static <A> F<List<A>, Integer> length_() {
        return new F<List<A>, Integer>() { // from class: fj.data.List.19
            @Override // fj.F
            public /* bridge */ /* synthetic */ Integer f(Object obj) {
                return f((List) ((List) obj));
            }

            public Integer f(List<A> a) {
                return Integer.valueOf(a.length());
            }
        };
    }

    public final A maximum(Ord<A> o) {
        return foldLeft1(o.max);
    }

    public final A minimum(Ord<A> o) {
        return foldLeft1(o.min);
    }

    public final Collection<A> toCollection() {
        return new AbstractCollection<A>() { // from class: fj.data.List.20
            {
                List.this = this;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            public Iterator<A> iterator() {
                return new Iterator<A>() { // from class: fj.data.List.20.1
                    private List<A> xs;

                    {
                        AnonymousClass20.this = this;
                        this.xs = List.this;
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
                        this.xs = this.xs.tail();
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
                return List.this.length();
            }
        };
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/List$Nil.class */
    public static final class Nil<A> extends List<A> {
        private Nil() {
            super();
        }

        @Override // fj.data.List
        public A head() {
            throw Bottom.error("head on empty list");
        }

        @Override // fj.data.List
        public List<A> tail() {
            throw Bottom.error("tail on empty list");
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/List$Cons.class */
    public static final class Cons<A> extends List<A> {
        private final A head;
        private List<A> tail;

        Cons(A head, List<A> tail) {
            super();
            this.head = head;
            this.tail = tail;
        }

        @Override // fj.data.List
        public A head() {
            return this.head;
        }

        @Override // fj.data.List
        public List<A> tail() {
            return this.tail;
        }

        public void tail(List<A> tail) {
            this.tail = tail;
        }
    }

    public static <A> List<A> list(A... as) {
        return Array.array(as).toList();
    }

    public static <A> List<A> nil() {
        return new Nil();
    }

    public static <A> F<A, F<List<A>, List<A>>> cons() {
        return new F<A, F<List<A>, List<A>>>() { // from class: fj.data.List.21
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass21) obj);
            }

            @Override // fj.F
            public F<List<A>, List<A>> f(final A a) {
                return new F<List<A>, List<A>>() { // from class: fj.data.List.21.1
                    {
                        AnonymousClass21.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((List) ((List) obj));
                    }

                    public List<A> f(List<A> tail) {
                        return List.cons(a, tail);
                    }
                };
            }
        };
    }

    public static <A> F2<A, List<A>, List<A>> cons_() {
        F2<A, List<A>, List<A>> f2;
        f2 = List$$Lambda$3.instance;
        return f2;
    }

    public static <A> F<A, List<A>> cons(List<A> tail) {
        return new F<A, List<A>>() { // from class: fj.data.List.22
            {
                List.this = tail;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass22) obj);
            }

            @Override // fj.F
            public List<A> f(A a) {
                return List.this.cons((List) a);
            }
        };
    }

    public static <A> F<List<A>, List<A>> cons_(final A a) {
        return new F<List<A>, List<A>>() { // from class: fj.data.List.23
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            /* JADX WARN: Multi-variable type inference failed */
            public List<A> f(List<A> as) {
                return as.cons((List<A>) a);
            }
        };
    }

    public static <A> List<A> cons(A head, List<A> tail) {
        return new Cons(head, tail);
    }

    public static <A> F<List<A>, Boolean> isEmpty_() {
        return new F<List<A>, Boolean>() { // from class: fj.data.List.24
            @Override // fj.F
            public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                return f((List) ((List) obj));
            }

            public Boolean f(List<A> as) {
                return Boolean.valueOf(as.isEmpty());
            }
        };
    }

    public static <A> F<List<A>, Boolean> isNotEmpty_() {
        return new F<List<A>, Boolean>() { // from class: fj.data.List.25
            @Override // fj.F
            public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                return f((List) ((List) obj));
            }

            public Boolean f(List<A> as) {
                return Boolean.valueOf(as.isNotEmpty());
            }
        };
    }

    public static <A> List<A> join(List<List<A>> o) {
        return (List<A>) o.bind(Function.identity());
    }

    public static <A> F<List<List<A>>, List<A>> join() {
        return new F<List<List<A>>, List<A>>() { // from class: fj.data.List.26
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public List<A> f(List<List<A>> as) {
                return List.join(as);
            }
        };
    }

    public static <A, B> List<A> unfold(F<B, Option<P2<A, B>>> f, B b) {
        Buffer<A> buf = Buffer.empty();
        Option<P2<A, B>> f2 = f.f(b);
        while (true) {
            Option<P2<A, B>> o = f2;
            if (o.isSome()) {
                buf = buf.snoc(o.some()._1());
                f2 = f.f(o.some()._2());
            } else {
                return buf.toList();
            }
        }
    }

    public static <A, B> P2<List<A>, List<B>> unzip(List<P2<A, B>> xs) {
        Buffer<A> ba = Buffer.empty();
        Buffer<B> bb = Buffer.empty();
        Iterator<P2<A, B>> it = xs.iterator();
        while (it.hasNext()) {
            P2<A, B> p = it.next();
            ba = ba.snoc(p._1());
            bb = bb.snoc(p._2());
        }
        return P.p(ba.toList(), bb.toList());
    }

    public static <A> List<A> replicate(int n, A a) {
        return n <= 0 ? nil() : replicate(n - 1, a).cons((List) a);
    }

    public static List<Integer> range(int from, int to) {
        return from >= to ? nil() : cons(Integer.valueOf(from), range(from + 1, to));
    }

    public static List<Character> fromString(String s) {
        List<Character> cs = nil();
        for (int i = s.length() - 1; i >= 0; i--) {
            cs = cons(Character.valueOf(s.charAt(i)), cs);
        }
        return cs;
    }

    public static F<String, List<Character>> fromString() {
        return new F<String, List<Character>>() { // from class: fj.data.List.27
            @Override // fj.F
            public List<Character> f(String s) {
                return List.fromString(s);
            }
        };
    }

    public static String asString(List<Character> cs) {
        final StringBuilder sb = new StringBuilder();
        cs.foreach(new F<Character, Unit>() { // from class: fj.data.List.28
            @Override // fj.F
            public Unit f(Character c) {
                sb.append(c);
                return Unit.unit();
            }
        });
        return sb.toString();
    }

    public static F<List<Character>, String> asString() {
        return new F<List<Character>, String>() { // from class: fj.data.List.29
            @Override // fj.F
            public String f(List<Character> cs) {
                return List.asString(cs);
            }
        };
    }

    public static <A> List<A> single(A a) {
        return cons(a, nil());
    }

    public static <A> List<A> iterateWhile(final F<A, A> f, final F<A, Boolean> p, A a) {
        return unfold(new F<A, Option<P2<A, A>>>() { // from class: fj.data.List.30
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass30) obj);
            }

            @Override // fj.F
            public Option<P2<A, A>> f(final A o) {
                return Option.iif(new F<P2<A, A>, Boolean>() { // from class: fj.data.List.30.1
                    {
                        AnonymousClass30.this = this;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                        return f((P2) ((P2) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public Boolean f(P2<A, A> p2) {
                        return (Boolean) p.f(o);
                    }
                }, P.p(o, f.f(o)));
            }
        }, a);
    }

    public static <A, B> Option<B> lookup(final Equal<A> e, List<P2<A, B>> x, final A a) {
        return x.find(new F<P2<A, B>, Boolean>() { // from class: fj.data.List.31
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Boolean f(P2<A, B> p) {
                return Boolean.valueOf(e.eq(p._1(), a));
            }
        }).map(P2.__2());
    }

    public static <A, B> F2<List<P2<A, B>>, A, Option<B>> lookup(final Equal<A> e) {
        return new F2<List<P2<A, B>>, A, Option<B>>() { // from class: fj.data.List.32
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((List<P2<List, B>>) obj, (List) obj2);
            }

            public Option<B> f(List<P2<A, B>> x, A a) {
                return List.lookup(e, x, a);
            }
        };
    }

    public static <A, B> F<F<A, List<B>>, F<List<A>, List<B>>> bind_() {
        return Function.curry(new F2<F<A, List<B>>, List<A>, List<B>>() { // from class: fj.data.List.33
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((F) obj, (List) ((List) obj2));
            }

            public List<B> f(F<A, List<B>> f, List<A> as) {
                return as.bind(f);
            }
        });
    }

    public static <A, B> F<F<A, B>, F<List<A>, List<B>>> map_() {
        return Function.curry(new F2<F<A, B>, List<A>, List<B>>() { // from class: fj.data.List.34
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((F) obj, (List) ((List) obj2));
            }

            public List<B> f(F<A, B> f, List<A> as) {
                return as.map(f);
            }
        });
    }

    public static <A, B> F<B, List<A>> sequence_(List<F<B, A>> fs) {
        return (F) fs.foldRight((F<F<B, A>, F<F<F<B, A>, F<B, B>>, F<F<B, A>, F<B, B>>>>) Function.lift(cons()), (F<F<B, A>, F<B, B>>) Function.constant(nil()));
    }

    public static <A, B> F<F<B, F<A, B>>, F<B, F<List<A>, B>>> foldLeft() {
        return Function.curry(new F3<F<B, F<A, B>>, B, List<A>, B>() { // from class: fj.data.List.35
            @Override // fj.F3
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2, Object obj3) {
                return f((F<F, F<A, F>>) obj, (F) obj2, (List) ((List) obj3));
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            public B f(F<B, F<A, B>> f, B b, List<A> as) {
                return as.foldLeft((F<F<B, F<A, B>>, F<A, F<B, F<A, B>>>>) f, (F<B, F<A, B>>) b);
            }
        });
    }

    public static <A> F<Integer, F<List<A>, List<A>>> take() {
        return Function.curry(new F2<Integer, List<A>, List<A>>() { // from class: fj.data.List.36
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Integer num, Object obj) {
                return f(num, (List) ((List) obj));
            }

            public List<A> f(Integer n, List<A> as) {
                return as.take(n.intValue());
            }
        });
    }

    public static <A> List<A> iterableList(Iterable<A> i) {
        Buffer<A> bs = Buffer.empty();
        for (A a : i) {
            bs.snoc(a);
        }
        return bs.toList();
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/List$Buffer.class */
    public static final class Buffer<A> implements Iterable<A> {
        private List<A> start = List.nil();
        private Cons<A> tail;
        private boolean exported;

        @Override // java.lang.Iterable
        public Iterator<A> iterator() {
            return this.start.iterator();
        }

        public Buffer<A> snoc(A a) {
            if (this.exported) {
                copy();
            }
            Cons<A> t = new Cons<>(a, List.nil());
            if (this.tail != null) {
                this.tail.tail(t);
            } else {
                this.start = t;
            }
            this.tail = t;
            return this;
        }

        public Buffer<A> append(List<A> as) {
            List<A> list = as;
            while (true) {
                List<A> xs = list;
                if (xs.isNotEmpty()) {
                    snoc(xs.head());
                    list = xs.tail();
                } else {
                    return this;
                }
            }
        }

        public List<A> toList() {
            this.exported = !this.start.isEmpty();
            return this.start;
        }

        public Collection<A> toCollection() {
            return this.start.toCollection();
        }

        public static <A> Buffer<A> empty() {
            return new Buffer<>();
        }

        public static <A> Buffer<A> fromList(List<A> as) {
            Buffer<A> b = new Buffer<>();
            List<A> list = as;
            while (true) {
                List<A> xs = list;
                if (xs.isNotEmpty()) {
                    b.snoc(xs.head());
                    list = xs.tail();
                } else {
                    return b;
                }
            }
        }

        public static <A> Buffer<A> iterableBuffer(Iterable<A> i) {
            Buffer<A> b = empty();
            for (A a : i) {
                b.snoc(a);
            }
            return b;
        }

        private void copy() {
            Cons<A> t = this.tail;
            this.start = List.nil();
            this.exported = false;
            for (List<A> s = this.start; s != t; s = s.tail()) {
                snoc(s.head());
            }
            if (t != null) {
                snoc(t.head());
            }
        }
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof List)) {
            return false;
        }
        return Equal.listEqual(Equal.anyEqual()).eq(this, (List) obj);
    }

    public int hashCode() {
        return Hash.listHash(Hash.anyHash()).hash((Hash) this);
    }

    public String toString() {
        return (String) Show.listShow(Show.anyShow()).show((Show) this).foldLeft((F2<F2, Character, F2>) new F2<String, Character, String>() { // from class: fj.data.List.37
            {
                List.this = this;
            }

            @Override // fj.F2
            public String f(String s, Character c) {
                return s + c;
            }
        }, (F2) "");
    }
}
