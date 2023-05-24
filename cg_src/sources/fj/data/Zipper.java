package fj.data;

import fj.Equal;
import fj.F;
import fj.F2;
import fj.F2Functions;
import fj.F3;
import fj.Function;
import fj.Ord;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.Show;
import fj.function.Integers;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Zipper.class */
public final class Zipper<A> implements Iterable<Zipper<A>> {
    private final Stream<A> left;
    private final A focus;
    private final Stream<A> right;

    private Zipper(Stream<A> left, A focus, Stream<A> right) {
        this.left = left;
        this.focus = focus;
        this.right = right;
    }

    public static <A> Zipper<A> zipper(Stream<A> left, A focus, Stream<A> right) {
        return new Zipper<>(left, focus, right);
    }

    public static <A> Zipper<A> zipper(P3<Stream<A>, A, Stream<A>> p) {
        return new Zipper<>(p._1(), p._2(), p._3());
    }

    public static <A> F3<Stream<A>, A, Stream<A>, Zipper<A>> zipper() {
        return new F3<Stream<A>, A, Stream<A>, Zipper<A>>() { // from class: fj.data.Zipper.1
            @Override // fj.F3
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2, Object obj3) {
                return f((Stream<Stream<A>>) obj, (Stream<A>) obj2, (Stream<Stream<A>>) obj3);
            }

            public Zipper<A> f(Stream<A> l, A a, Stream<A> r) {
                return Zipper.zipper(l, a, r);
            }
        };
    }

    public P3<Stream<A>, A, Stream<A>> p() {
        return P.p(this.left, this.focus, this.right);
    }

    public static <A> F<Zipper<A>, P3<Stream<A>, A, Stream<A>>> p_() {
        return new F<Zipper<A>, P3<Stream<A>, A, Stream<A>>>() { // from class: fj.data.Zipper.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Zipper) ((Zipper) obj));
            }

            public P3<Stream<A>, A, Stream<A>> f(Zipper<A> a) {
                return a.p();
            }
        };
    }

    public static <A> Ord<Zipper<A>> ord(Ord<A> o) {
        Ord<Stream<A>> so = Ord.streamOrd(o);
        return Ord.p3Ord(so, o, so).comap(p_());
    }

    public static <A> Equal<Zipper<A>> eq(Equal<A> e) {
        Equal<Stream<A>> se = Equal.streamEqual(e);
        return Equal.p3Equal(se, e, se).comap(p_());
    }

    public static <A> Show<Zipper<A>> show(Show<A> s) {
        Show<Stream<A>> ss = Show.streamShow(s);
        return Show.p3Show(ss, s, ss).comap(p_());
    }

    public <B> Zipper<B> map(F<A, B> f) {
        return zipper(this.left.map(f), f.f(this.focus), this.right.map(f));
    }

    public <B> B foldRight(F<A, F<B, B>> f, B z) {
        return (B) this.left.foldLeft((F<F<B, F<A, B>>, F<A, F<B, F<A, B>>>>) Function.flip(f), (F<B, F<A, B>>) this.right.cons(this.focus).foldRight((F<A, F<P1<F<A, F<P1<B>, B>>>, F<A, F<P1<B>, B>>>>) Function.compose((F) Function.andThen().f(P1.__1()), f), (F<A, F<P1<B>, B>>) z));
    }

    public static <A> Zipper<A> single(A a) {
        return zipper(Stream.nil(), a, Stream.nil());
    }

    public static <A> Option<Zipper<A>> fromStream(Stream<A> a) {
        if (a.isEmpty()) {
            return Option.none();
        }
        return Option.some(zipper(Stream.nil(), a.head(), a.tail()._1()));
    }

    public static <A> Option<Zipper<A>> fromStreamEnd(Stream<A> a) {
        if (a.isEmpty()) {
            return Option.none();
        }
        Stream<A> xs = a.reverse();
        return Option.some(zipper(xs.tail()._1(), xs.head(), Stream.nil()));
    }

    public A focus() {
        return this.focus;
    }

    public Option<Zipper<A>> next() {
        return this.right.isEmpty() ? Option.none() : Option.some(tryNext());
    }

    public Zipper<A> tryNext() {
        if (this.right.isEmpty()) {
            throw new Error("Tried next at the end of a zipper.");
        }
        return zipper(this.left.cons(this.focus), this.right.head(), this.right.tail()._1());
    }

    public Option<Zipper<A>> previous() {
        return this.left.isEmpty() ? Option.none() : Option.some(tryPrevious());
    }

    public Zipper<A> tryPrevious() {
        if (this.left.isEmpty()) {
            throw new Error("Tried previous at the beginning of a zipper.");
        }
        return zipper(this.left.tail()._1(), this.left.head(), this.right.cons(this.focus));
    }

    public static <A> F<Zipper<A>, Option<Zipper<A>>> next_() {
        return new F<Zipper<A>, Option<Zipper<A>>>() { // from class: fj.data.Zipper.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Zipper) ((Zipper) obj));
            }

            public Option<Zipper<A>> f(Zipper<A> as) {
                return as.next();
            }
        };
    }

    public static <A> F<Zipper<A>, Option<Zipper<A>>> previous_() {
        return new F<Zipper<A>, Option<Zipper<A>>>() { // from class: fj.data.Zipper.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Zipper) ((Zipper) obj));
            }

            public Option<Zipper<A>> f(Zipper<A> as) {
                return as.previous();
            }
        };
    }

    public Zipper<A> insertLeft(A a) {
        return zipper(this.left, a, this.right.cons(this.focus));
    }

    public Zipper<A> insertRight(A a) {
        return zipper(this.left.cons(this.focus), a, this.right);
    }

    public Option<Zipper<A>> deleteLeft() {
        if (this.left.isEmpty() && this.right.isEmpty()) {
            return Option.none();
        }
        return Option.some(zipper(this.left.isEmpty() ? this.left : this.left.tail()._1(), this.left.isEmpty() ? this.right.head() : this.left.head(), this.left.isEmpty() ? this.right.tail()._1() : this.right));
    }

    public Option<Zipper<A>> deleteRight() {
        if (this.left.isEmpty() && this.right.isEmpty()) {
            return Option.none();
        }
        return Option.some(zipper(this.right.isEmpty() ? this.left.tail()._1() : this.left, this.right.isEmpty() ? this.left.head() : this.right.head(), this.right.isEmpty() ? this.right : this.right.tail()._1()));
    }

    public Zipper<A> deleteOthers() {
        Stream<A> nil = Stream.nil();
        return zipper(nil, this.focus, nil);
    }

    public int length() {
        return ((Integer) foldRight(Function.constant(Integers.add.f(1)), 0)).intValue();
    }

    public boolean atStart() {
        return this.left.isEmpty();
    }

    public boolean atEnd() {
        return this.right.isEmpty();
    }

    public Zipper<Zipper<A>> positions() {
        Stream<Zipper<A>> left = Stream.unfold(new F<Zipper<A>, Option<P2<Zipper<A>, Zipper<A>>>>() { // from class: fj.data.Zipper.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Zipper) ((Zipper) obj));
            }

            public Option<P2<Zipper<A>, Zipper<A>>> f(Zipper<A> p) {
                return (Option<P2<Zipper<A>, Zipper<A>>>) p.previous().map(Function.join(P.p2()));
            }
        }, this);
        Stream<Zipper<A>> right = Stream.unfold(new F<Zipper<A>, Option<P2<Zipper<A>, Zipper<A>>>>() { // from class: fj.data.Zipper.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Zipper) ((Zipper) obj));
            }

            public Option<P2<Zipper<A>, Zipper<A>>> f(Zipper<A> p) {
                return (Option<P2<Zipper<A>, Zipper<A>>>) p.next().map(Function.join(P.p2()));
            }
        }, this);
        return zipper(left, this, right);
    }

    public <B> Zipper<B> cobind(F<Zipper<A>, B> f) {
        return positions().map(f);
    }

    public Zipper<P2<A, Boolean>> zipWithFocus() {
        return zipper(this.left.zip(Stream.repeat(false)), P.p(this.focus, true), this.right.zip(Stream.repeat(false)));
    }

    public Option<Zipper<A>> move(int n) {
        int ll = this.left.length();
        int rl = this.right.length();
        Option<Zipper<A>> p = Option.some(this);
        if (n < 0 || n >= length()) {
            return Option.none();
        }
        if (ll >= n) {
            for (int i = ll - n; i > 0; i--) {
                p = p.bind(previous_());
            }
        } else if (rl >= n) {
            for (int i2 = rl - n; i2 > 0; i2--) {
                p = p.bind(next_());
            }
        }
        return p;
    }

    public static <A> F<Integer, F<Zipper<A>, Option<Zipper<A>>>> move() {
        return Function.curry(new F2<Integer, Zipper<A>, Option<Zipper<A>>>() { // from class: fj.data.Zipper.7
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Integer num, Object obj) {
                return f(num, (Zipper) ((Zipper) obj));
            }

            public Option<Zipper<A>> f(Integer i, Zipper<A> a) {
                return a.move(i.intValue());
            }
        });
    }

    public Option<Zipper<A>> find(final F<A, Boolean> p) {
        if (p.f(focus()).booleanValue()) {
            return Option.some(this);
        }
        Zipper<Zipper<A>> ps = positions();
        return ps.lefts().interleave(ps.rights()).find(new F<Zipper<A>, Boolean>() { // from class: fj.data.Zipper.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                return f((Zipper) ((Zipper) obj));
            }

            public Boolean f(Zipper<A> zipper) {
                return (Boolean) p.f(zipper.focus());
            }
        });
    }

    public int index() {
        return this.left.length();
    }

    public Zipper<A> cycleNext() {
        if (this.left.isEmpty() && this.right.isEmpty()) {
            return this;
        }
        if (this.right.isEmpty()) {
            Stream<A> xs = this.left.reverse();
            return zipper(Stream.nil(), xs.head(), xs.tail()._1().snoc((P1) P.p(this.focus)));
        }
        return tryNext();
    }

    public Zipper<A> cyclePrevious() {
        if (this.left.isEmpty() && this.right.isEmpty()) {
            return this;
        }
        if (this.left.isEmpty()) {
            Stream<A> xs = this.right.reverse();
            return zipper(xs.tail()._1().snoc((P1) P.p(this.focus)), xs.head(), Stream.nil());
        }
        return tryPrevious();
    }

    public Option<Zipper<A>> deleteLeftCycle() {
        if (this.left.isEmpty() && this.right.isEmpty()) {
            return Option.none();
        }
        if (this.left.isNotEmpty()) {
            return Option.some(zipper(this.left.tail()._1(), this.left.head(), this.right));
        }
        Stream<A> xs = this.right.reverse();
        return Option.some(zipper(xs.tail()._1(), xs.head(), Stream.nil()));
    }

    public Option<Zipper<A>> deleteRightCycle() {
        if (this.left.isEmpty() && this.right.isEmpty()) {
            return Option.none();
        }
        if (this.right.isNotEmpty()) {
            return Option.some(zipper(this.left, this.right.head(), this.right.tail()._1()));
        }
        Stream<A> xs = this.left.reverse();
        return Option.some(zipper(Stream.nil(), xs.head(), xs.tail()._1()));
    }

    public Zipper<A> replace(A a) {
        return zipper(this.left, a, this.right);
    }

    public Stream<A> toStream() {
        return this.left.reverse().snoc((P1) P.p(this.focus)).append(this.right);
    }

    public Stream<A> lefts() {
        return this.left;
    }

    public Stream<A> rights() {
        return this.right;
    }

    public <B, C> Zipper<C> zipWith(Zipper<B> bs, F2<A, B, C> f) {
        return (Zipper) F2Functions.zipZipperM(f).f(this, bs);
    }

    public <B, C> Zipper<C> zipWith(Zipper<B> bs, F<A, F<B, C>> f) {
        return zipWith(bs, Function.uncurryF2(f));
    }

    @Override // java.lang.Iterable
    public Iterator<Zipper<A>> iterator() {
        return positions().toStream().iterator();
    }
}
