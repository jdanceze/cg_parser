package fj.data.hlist;

import fj.F;
import fj.F2;
import fj.F3;
import fj.Function;
import fj.P;
import fj.P2;
import fj.Unit;
import fj.data.hlist.HList;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HList.class */
public abstract class HList<A extends HList<A>> {
    private static final HNil nil = new HNil();

    public abstract <E> HCons<E, A> extend(E e);

    public abstract <E> Apply<Unit, P2<E, A>, HCons<E, A>> extender();

    HList() {
    }

    public static HNil nil() {
        return nil;
    }

    public static <E, L extends HList<L>> HCons<E, L> cons(E e, L l) {
        return new HCons<>(e, l);
    }

    public static <E> HCons<E, HNil> single(E e) {
        return cons(e, nil());
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HList$HAppend.class */
    public static final class HAppend<A, B, C> {
        private final F2<A, B, C> append;

        private HAppend(F2<A, B, C> f) {
            this.append = f;
        }

        public C append(A a, B b) {
            return this.append.f(a, b);
        }

        public static <L extends HList<L>> HAppend<HNil, L, L> append() {
            return new HAppend<>(new F2<HNil, L, L>() { // from class: fj.data.hlist.HList.HAppend.1
                /* JADX WARN: Incorrect return type in method signature: (Lfj/data/hlist/HList$HNil;TL;)TL; */
                /* JADX WARN: Unknown type variable: L in type: L */
                @Override // fj.F2
                public HList f(HNil hNil, HList hList) {
                    return hList;
                }
            });
        }

        public static <X, A extends HList<A>, B, C extends HList<C>, H extends HAppend<A, B, C>> HAppend<HCons<X, A>, B, HCons<X, C>> append(H h) {
            return new HAppend<>(new F2<HCons<X, A>, B, HCons<X, C>>() { // from class: fj.data.hlist.HList.HAppend.2
                @Override // fj.F2
                public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                    return f((HCons) obj, (HCons) obj2);
                }

                /* JADX WARN: Unknown type variable: X in type: fj.data.hlist.HList$HCons<X, A> */
                /* JADX WARN: Unknown type variable: X in type: fj.data.hlist.HList$HCons<X, C> */
                public HCons<X, C> f(HCons<X, A> c, B l) {
                    return HList.cons(c.head(), (HList) HAppend.this.append(c.tail(), l));
                }
            });
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HList$Apply.class */
    public static abstract class Apply<F$, A, R> {
        public abstract R apply(F$ f_, A a);

        public static <X, Y> Apply<F<X, Y>, X, Y> f() {
            return new Apply<F<X, Y>, X, Y>() { // from class: fj.data.hlist.HList.Apply.1
                @Override // fj.data.hlist.HList.Apply
                public /* bridge */ /* synthetic */ Object apply(Object obj, Object obj2) {
                    return apply((F<F, Object>) obj, (F) obj2);
                }

                /* JADX WARN: Type inference failed for: r0v1, types: [Y, java.lang.Object] */
                /* JADX WARN: Unknown type variable: X in type: X */
                /* JADX WARN: Unknown type variable: X in type: fj.F<X, Y> */
                /* JADX WARN: Unknown type variable: Y in type: Y */
                /* JADX WARN: Unknown type variable: Y in type: fj.F<X, Y> */
                public Y apply(F<X, Y> f, X x) {
                    return f.f(x);
                }
            };
        }

        public static <X> Apply<Unit, X, X> id() {
            return new Apply<Unit, X, X>() { // from class: fj.data.hlist.HList.Apply.2
                @Override // fj.data.hlist.HList.Apply
                public /* bridge */ /* synthetic */ Object apply(Unit unit, Object obj) {
                    return apply2(unit, (Unit) obj);
                }

                /* JADX WARN: Unknown type variable: X in type: X */
                /* renamed from: apply  reason: avoid collision after fix types in other method */
                public X apply2(Unit f, X x) {
                    return x;
                }
            };
        }

        public static <X, Y, Z> Apply<Unit, P2<F<X, Y>, F<Y, Z>>, F<X, Z>> comp() {
            return new Apply<Unit, P2<F<X, Y>, F<Y, Z>>, F<X, Z>>() { // from class: fj.data.hlist.HList.Apply.3
                /* JADX WARN: Unknown type variable: X in type: fj.F<X, Z> */
                /* JADX WARN: Unknown type variable: X in type: fj.P2<fj.F<X, Y>, fj.F<Y, Z>> */
                /* JADX WARN: Unknown type variable: Y in type: fj.P2<fj.F<X, Y>, fj.F<Y, Z>> */
                /* JADX WARN: Unknown type variable: Z in type: fj.F<X, Z> */
                /* JADX WARN: Unknown type variable: Z in type: fj.P2<fj.F<X, Y>, fj.F<Y, Z>> */
                @Override // fj.data.hlist.HList.Apply
                public F<X, Z> apply(Unit f, P2<F<X, Y>, F<Y, Z>> fs) {
                    return Function.compose((F) fs._2(), (F) fs._1());
                }
            };
        }

        public static <E, L extends HList<L>> Apply<Unit, P2<E, L>, HCons<E, L>> cons() {
            return (Apply<Unit, P2<E, L>, HCons<E, L>>) new Apply<Unit, P2<E, L>, HCons<E, L>>() { // from class: fj.data.hlist.HList.Apply.4
                /* JADX WARN: Unknown type variable: E in type: fj.P2<E, L> */
                /* JADX WARN: Unknown type variable: E in type: fj.data.hlist.HList$HCons<E, L> */
                /* JADX WARN: Unknown type variable: L in type: fj.P2<E, L> */
                /* JADX WARN: Unknown type variable: L in type: fj.data.hlist.HList$HCons<E, L> */
                @Override // fj.data.hlist.HList.Apply
                public HCons<E, L> apply(Unit f, P2<E, L> p) {
                    return HList.cons(p._1(), (HList) p._2());
                }
            };
        }

        public static <A, B, C> Apply<HAppend<A, B, C>, P2<A, B>, C> append() {
            return new Apply<HAppend<A, B, C>, P2<A, B>, C>() { // from class: fj.data.hlist.HList.Apply.5
                @Override // fj.data.hlist.HList.Apply
                public /* bridge */ /* synthetic */ Object apply(Object obj, Object obj2) {
                    return apply((HAppend<A, B, Object>) obj, (P2) obj2);
                }

                /* JADX WARN: Type inference failed for: r0v1, types: [C, java.lang.Object] */
                /* JADX WARN: Unknown type variable: B in type: fj.P2<A, B> */
                /* JADX WARN: Unknown type variable: B in type: fj.data.hlist.HList$HAppend<A, B, C> */
                /* JADX WARN: Unknown type variable: C in type: C */
                /* JADX WARN: Unknown type variable: C in type: fj.data.hlist.HList$HAppend<A, B, C> */
                public C apply(HAppend<A, B, C> f, P2<A, B> p) {
                    return f.append(p._1(), p._2());
                }
            };
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HList$HFoldr.class */
    public static final class HFoldr<G, V, L, R> {
        private final F3<G, V, L, R> foldRight;

        private HFoldr(F3<G, V, L, R> foldRight) {
            this.foldRight = foldRight;
        }

        public static <G, V> HFoldr<G, V, HNil, V> hFoldr() {
            return new HFoldr<>(new F3<G, V, HNil, V>() { // from class: fj.data.hlist.HList.HFoldr.1
                @Override // fj.F3
                public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2, HNil hNil) {
                    return f2((AnonymousClass1) obj, obj2, hNil);
                }

                /* renamed from: f  reason: avoid collision after fix types in other method */
                public V f2(G f, V v, HNil hNil) {
                    return v;
                }
            });
        }

        public static <E, G, V, L extends HList<L>, R, RR, H extends HFoldr<G, V, L, R>, PP extends Apply<G, P2<E, R>, RR>> HFoldr<G, V, HCons<E, L>, RR> hFoldr(final PP p, final H h) {
            return new HFoldr<>(new F3<G, V, HCons<E, L>, RR>() { // from class: fj.data.hlist.HList.HFoldr.2
                @Override // fj.F3
                public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2, Object obj3) {
                    return f((AnonymousClass2) obj, obj2, (HCons) obj3);
                }

                /* JADX WARN: Type inference failed for: r0v2, types: [RR, java.lang.Object] */
                /* JADX WARN: Unknown type variable: E in type: fj.data.hlist.HList$HCons<E, L> */
                /* JADX WARN: Unknown type variable: RR in type: RR */
                public RR f(G f, V v, HCons<E, L> c) {
                    return Apply.this.apply(f, P.p(c.head(), h.foldRight(f, v, c.tail())));
                }
            });
        }

        public R foldRight(G f, V v, L l) {
            return this.foldRight.f(f, v, l);
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HList$HCons.class */
    public static final class HCons<E, L extends HList<L>> extends HList<HCons<E, L>> {
        private final E e;
        private final L l;

        HCons(E e, L l) {
            this.e = e;
            this.l = l;
        }

        public E head() {
            return this.e;
        }

        public L tail() {
            return this.l;
        }

        @Override // fj.data.hlist.HList
        public <X> Apply<Unit, P2<X, HCons<E, L>>, HCons<X, HCons<E, L>>> extender() {
            return Apply.cons();
        }

        @Override // fj.data.hlist.HList
        public <X> HCons<X, HCons<E, L>> extend(X e) {
            return cons(e, this);
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HList$HNil.class */
    public static final class HNil extends HList<HNil> {
        HNil() {
        }

        @Override // fj.data.hlist.HList
        public <E> HCons<E, HNil> extend(E e) {
            return cons(e, this);
        }

        @Override // fj.data.hlist.HList
        public <E> Apply<Unit, P2<E, HNil>, HCons<E, HNil>> extender() {
            return Apply.cons();
        }
    }
}
