package fj.data;

import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.Ord;
import fj.Ordering;
import fj.P;
import fj.P2;
import fj.P3;
import fj.function.Booleans;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Set.class */
public abstract class Set<A> implements Iterable<A> {
    private final Ord<A> ord;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Set$Color.class */
    public enum Color {
        R,
        B
    }

    abstract Color color();

    abstract Set<A> l();

    abstract A head();

    abstract Set<A> r();

    private Set(Ord<A> ord) {
        this.ord = ord;
    }

    public final boolean isEmpty() {
        return this instanceof Empty;
    }

    public final Ord<A> ord() {
        return this.ord;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Set$Empty.class */
    public static final class Empty<A> extends Set<A> {
        private Empty(Ord<A> ord) {
            super(ord);
        }

        @Override // fj.data.Set
        public Color color() {
            return Color.B;
        }

        @Override // fj.data.Set
        public Set<A> l() {
            throw new Error("Left on empty set.");
        }

        @Override // fj.data.Set
        public Set<A> r() {
            throw new Error("Right on empty set.");
        }

        @Override // fj.data.Set
        public A head() {
            throw new Error("Head on empty set.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Set$Tree.class */
    public static final class Tree<A> extends Set<A> {
        private final Color c;
        private final Set<A> a;
        private final A x;
        private final Set<A> b;

        private Tree(Ord<A> ord, Color c, Set<A> a, A x, Set<A> b) {
            super(ord);
            this.c = c;
            this.a = a;
            this.x = x;
            this.b = b;
        }

        @Override // fj.data.Set
        public Color color() {
            return this.c;
        }

        @Override // fj.data.Set
        public Set<A> l() {
            return this.a;
        }

        @Override // fj.data.Set
        public A head() {
            return this.x;
        }

        @Override // fj.data.Set
        public Set<A> r() {
            return this.b;
        }
    }

    public final P2<Boolean, Set<A>> update(final A a, F<A, A> f) {
        if (isEmpty()) {
            return P.p(false, this);
        }
        return (P2) tryUpdate(a, f).either((F<A, P2<Boolean, Set<A>>>) new F<A, P2<Boolean, Set<A>>>() { // from class: fj.data.Set.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public P2<Boolean, Set<A>> f(A a2) {
                return P.p(true, Set.this.delete(a).insert(a2));
            }
        }, Function.identity());
    }

    private Either<A, P2<Boolean, Set<A>>> tryUpdate(A a, F<A, A> f) {
        if (isEmpty()) {
            return Either.right(P.p(false, this));
        }
        if (this.ord.isLessThan(a, head())) {
            return (Either<A, P2<Boolean, Set<A>>>) l().tryUpdate(a, f).right().map((F<P2<Boolean, Set<A>>, P2<Boolean, Set<A>>>) new F<P2<Boolean, Set<A>>, P2<Boolean, Set<A>>>() { // from class: fj.data.Set.2
                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((P2) ((P2) obj));
                }

                public P2<Boolean, Set<A>> f(P2<Boolean, Set<A>> set) {
                    return set._1().booleanValue() ? P.p(true, new Tree(Set.this.ord, Set.this.color(), set._2(), Set.this.head(), Set.this.r())) : set;
                }
            });
        }
        if (this.ord.eq(a, head())) {
            A h = f.f(head());
            if (this.ord.eq(head(), h)) {
                return Either.right(P.p(true, new Tree(this.ord, color(), l(), h, r())));
            }
            return Either.left(h);
        }
        return (Either<A, P2<Boolean, Set<A>>>) r().tryUpdate(a, f).right().map((F<P2<Boolean, Set<A>>, P2<Boolean, Set<A>>>) new F<P2<Boolean, Set<A>>, P2<Boolean, Set<A>>>() { // from class: fj.data.Set.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P2) ((P2) obj));
            }

            public P2<Boolean, Set<A>> f(P2<Boolean, Set<A>> set) {
                return set._1().booleanValue() ? P.p(true, new Tree(Set.this.ord, Set.this.color(), Set.this.l(), Set.this.head(), set._2())) : set;
            }
        });
    }

    public static <A> Set<A> empty(Ord<A> ord) {
        return new Empty(ord);
    }

    public final boolean member(A x) {
        return !isEmpty() && (!this.ord.isLessThan(x, head()) ? !(this.ord.eq(head(), x) || r().member(x)) : !l().member(x));
    }

    public static <A> F<Set<A>, F<A, Boolean>> member() {
        return Function.curry(new F2<Set<A>, A, Boolean>() { // from class: fj.data.Set.4
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Boolean f(Object obj, Object obj2) {
                return f((Set<Set<A>>) obj, (Set<A>) obj2);
            }

            public Boolean f(Set<A> s, A a) {
                return Boolean.valueOf(s.member(a));
            }
        });
    }

    public final Set<A> insert(A x) {
        return ins(x).makeBlack();
    }

    public static <A> F<A, F<Set<A>, Set<A>>> insert() {
        return Function.curry(new F2<A, Set<A>, Set<A>>() { // from class: fj.data.Set.5
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((AnonymousClass5) obj, (Set<AnonymousClass5>) obj2);
            }

            public Set<A> f(A a, Set<A> set) {
                return set.insert(a);
            }
        });
    }

    private Set<A> ins(A x) {
        if (isEmpty()) {
            return new Tree(this.ord, Color.R, empty(this.ord), x, empty(this.ord));
        }
        if (this.ord.isLessThan(x, head())) {
            return balance(this.ord, color(), l().ins(x), head(), r());
        }
        if (this.ord.eq(x, head())) {
            return new Tree(this.ord, color(), l(), x, r());
        }
        return balance(this.ord, color(), l(), head(), r().ins(x));
    }

    private Set<A> makeBlack() {
        return new Tree(this.ord, Color.B, l(), head(), r());
    }

    private static <A> Tree<A> tr(Ord<A> o, Set<A> a, A x, Set<A> b, A y, Set<A> c, A z, Set<A> d) {
        return new Tree<>(o, Color.R, new Tree(o, Color.B, a, x, b), y, new Tree(o, Color.B, c, z, d));
    }

    private static <A> Set<A> balance(Ord<A> ord, Color c, Set<A> l, A h, Set<A> r) {
        return (c == Color.B && l.isTR() && l.l().isTR()) ? tr(ord, l.l().l(), l.l().head(), l.l().r(), l.head(), l.r(), h, r) : (c == Color.B && l.isTR() && l.r().isTR()) ? tr(ord, l.l(), l.head(), l.r().l(), l.r().head(), l.r().r(), h, r) : (c == Color.B && r.isTR() && r.l().isTR()) ? tr(ord, l, h, r.l().l(), r.l().head(), r.l().r(), r.head(), r.r()) : (c == Color.B && r.isTR() && r.r().isTR()) ? tr(ord, l, h, r.l(), r.head(), r.r().l(), r.r().head(), r.r().r()) : new Tree(ord, c, l, h, r);
    }

    private boolean isTR() {
        return !isEmpty() && color() == Color.R;
    }

    @Override // java.lang.Iterable
    public final Iterator<A> iterator() {
        return toStream().iterator();
    }

    public static <A> Set<A> single(Ord<A> o, A a) {
        return empty(o).insert(a);
    }

    public final <B> Set<B> map(Ord<B> o, F<A, B> f) {
        return iterableSet(o, toStream().map(f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B> B foldMap(F<A, B> f, Monoid<B> m) {
        if (isEmpty()) {
            return (B) m.zero();
        }
        return (B) m.sum(m.sum(r().foldMap(f, m), f.f(head())), l().foldMap(f, m));
    }

    public final List<A> toList() {
        return (List) foldMap(List.cons(List.nil()), Monoid.listMonoid());
    }

    public final Stream<A> toStream() {
        return (Stream) foldMap(Stream.single(), Monoid.streamMonoid());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B> Set<B> bind(Ord<B> o, F<A, Set<B>> f) {
        return join(o, map(Ord.setOrd(o), f));
    }

    public final Set<A> union(Set<A> s) {
        return iterableSet(this.ord, s.toStream().append(toStream()));
    }

    public static <A> F<Set<A>, F<Set<A>, Set<A>>> union() {
        return Function.curry(new F2<Set<A>, Set<A>, Set<A>>() { // from class: fj.data.Set.6
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Set) ((Set) obj), (Set) ((Set) obj2));
            }

            public Set<A> f(Set<A> s1, Set<A> s2) {
                return s1.union(s2);
            }
        });
    }

    public final Set<A> filter(F<A, Boolean> f) {
        return iterableSet(this.ord, toStream().filter(f));
    }

    public final Set<A> delete(A a) {
        return minus(single(this.ord, a));
    }

    public final F<A, F<Set<A>, Set<A>>> delete() {
        return Function.curry(new F2<A, Set<A>, Set<A>>() { // from class: fj.data.Set.7
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((AnonymousClass7) obj, (Set<AnonymousClass7>) obj2);
            }

            public Set<A> f(A a, Set<A> set) {
                return set.delete(a);
            }
        });
    }

    public final Set<A> intersect(Set<A> s) {
        return filter((F) member().f(s));
    }

    public static <A> F<Set<A>, F<Set<A>, Set<A>>> intersect() {
        return Function.curry(new F2<Set<A>, Set<A>, Set<A>>() { // from class: fj.data.Set.8
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Set) ((Set) obj), (Set) ((Set) obj2));
            }

            public Set<A> f(Set<A> s1, Set<A> s2) {
                return s1.intersect(s2);
            }
        });
    }

    public final Set<A> minus(Set<A> s) {
        return filter(Function.compose(Booleans.not, (F) member().f(s)));
    }

    public static <A> F<Set<A>, F<Set<A>, Set<A>>> minus() {
        return Function.curry(new F2<Set<A>, Set<A>, Set<A>>() { // from class: fj.data.Set.9
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Set) ((Set) obj), (Set) ((Set) obj2));
            }

            public Set<A> f(Set<A> s1, Set<A> s2) {
                return s1.minus(s2);
            }
        });
    }

    public final int size() {
        return ((Integer) foldMap(Function.constant(1), Monoid.intAdditionMonoid)).intValue();
    }

    public final P3<Set<A>, Option<A>, Set<A>> split(A a) {
        if (isEmpty()) {
            return P.p(empty(this.ord), Option.none(), empty(this.ord));
        }
        A h = head();
        Ordering i = this.ord.compare(a, h);
        if (i == Ordering.LT) {
            P3<Set<A>, Option<A>, Set<A>> lg = l().split(a);
            return P.p(lg._1(), lg._2(), lg._3().insert(h).union(r()));
        } else if (i == Ordering.GT) {
            P3<Set<A>, Option<A>, Set<A>> lg2 = r().split(a);
            return P.p(lg2._1().insert(h).union(l()), lg2._2(), lg2._3());
        } else {
            return P.p(l(), Option.some(h), r());
        }
    }

    public final boolean subsetOf(Set<A> s) {
        if (isEmpty() || s.isEmpty()) {
            return isEmpty();
        }
        P3<Set<A>, Option<A>, Set<A>> find = s.split(head());
        return find._2().isSome() && l().subsetOf(find._1()) && r().subsetOf(find._3());
    }

    public static <A> Set<A> join(Ord<A> o, Set<Set<A>> s) {
        return (Set) s.foldMap(Function.identity(), Monoid.setMonoid(o));
    }

    public static <A> Set<A> iterableSet(Ord<A> o, Iterable<A> as) {
        Set<A> s = empty(o);
        for (A a : as) {
            s = s.insert(a);
        }
        return s;
    }

    public static <A> Set<A> set(Ord<A> o, A... as) {
        Set<A> s = empty(o);
        for (A a : as) {
            s = s.insert(a);
        }
        return s;
    }
}
