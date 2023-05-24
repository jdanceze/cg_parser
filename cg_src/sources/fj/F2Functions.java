package fj;

import fj.control.parallel.Promise;
import fj.data.Array;
import fj.data.IterableW;
import fj.data.List;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Set;
import fj.data.Stream;
import fj.data.Tree;
import fj.data.TreeZipper;
import fj.data.Zipper;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/F2Functions.class */
public class F2Functions {
    public static <A, B, C> F<B, C> f(final F2<A, B, C> f, final A a) {
        return new F<B, C>() { // from class: fj.F2Functions.1
            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.F
            public C f(B b) {
                return f.f(a, b);
            }
        };
    }

    public static <A, B, C> F<A, F<B, C>> curry(final F2<A, B, C> f) {
        return new F<A, F<B, C>>() { // from class: fj.F2Functions.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass2) obj);
            }

            @Override // fj.F
            public F<B, C> f(final A a) {
                return new F<B, C>() { // from class: fj.F2Functions.2.1
                    {
                        AnonymousClass2.this = this;
                    }

                    /* JADX WARN: Type inference failed for: r0v3, types: [C, java.lang.Object] */
                    @Override // fj.F
                    public C f(B b) {
                        return f.f(a, b);
                    }
                };
            }
        };
    }

    public static <A, B, C> F2<B, A, C> flip(final F2<A, B, C> f) {
        return new F2<B, A, C>() { // from class: fj.F2Functions.3
            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.F2
            public C f(B b, A a) {
                return f.f(a, b);
            }
        };
    }

    public static <A, B, C> F<P2<A, B>, C> tuple(final F2<A, B, C> f) {
        return new F<P2<A, B>, C>() { // from class: fj.F2Functions.4
            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.F
            public C f(P2<A, B> p) {
                return f.f(p._1(), p._2());
            }
        };
    }

    public static <A, B, C> F2<Array<A>, Array<B>, Array<C>> arrayM(final F2<A, B, C> f) {
        return new F2<Array<A>, Array<B>, Array<C>>() { // from class: fj.F2Functions.5
            @Override // fj.F2
            public Array<C> f(Array<A> a, Array<B> b) {
                return a.bind(b, F2Functions.curry(f));
            }
        };
    }

    public static <A, B, C> F2<Promise<A>, Promise<B>, Promise<C>> promiseM(final F2<A, B, C> f) {
        return new F2<Promise<A>, Promise<B>, Promise<C>>() { // from class: fj.F2Functions.6
            @Override // fj.F2
            public Promise<C> f(Promise<A> a, Promise<B> b) {
                return a.bind(b, F2Functions.curry(f));
            }
        };
    }

    public static <A, B, C> F2<Iterable<A>, Iterable<B>, IterableW<C>> iterableM(final F2<A, B, C> f) {
        return new F2<Iterable<A>, Iterable<B>, IterableW<C>>() { // from class: fj.F2Functions.7
            @Override // fj.F2
            public IterableW<C> f(Iterable<A> a, Iterable<B> b) {
                return (IterableW) ((F) IterableW.liftM2(F2Functions.curry(f)).f(a)).f(b);
            }
        };
    }

    public static <A, B, C> F2<List<A>, List<B>, List<C>> listM(final F2<A, B, C> f) {
        return new F2<List<A>, List<B>, List<C>>() { // from class: fj.F2Functions.8
            @Override // fj.F2
            public List<C> f(List<A> a, List<B> b) {
                return (List) ((F) List.liftM2(F2Functions.curry(f)).f(a)).f(b);
            }
        };
    }

    public static <A, B, C> F2<NonEmptyList<A>, NonEmptyList<B>, NonEmptyList<C>> nelM(final F2<A, B, C> f) {
        return new F2<NonEmptyList<A>, NonEmptyList<B>, NonEmptyList<C>>() { // from class: fj.F2Functions.9
            @Override // fj.F2
            public NonEmptyList<C> f(NonEmptyList<A> as, NonEmptyList<B> bs) {
                return (NonEmptyList) NonEmptyList.fromList(as.toList().bind(bs.toList(), f)).some();
            }
        };
    }

    public static <A, B, C> F2<Option<A>, Option<B>, Option<C>> optionM(final F2<A, B, C> f) {
        return new F2<Option<A>, Option<B>, Option<C>>() { // from class: fj.F2Functions.10
            @Override // fj.F2
            public Option<C> f(Option<A> a, Option<B> b) {
                return (Option) ((F) Option.liftM2(F2Functions.curry(f)).f(a)).f(b);
            }
        };
    }

    public static <A, B, C> F2<Set<A>, Set<B>, Set<C>> setM(final F2<A, B, C> f, final Ord<C> o) {
        return new F2<Set<A>, Set<B>, Set<C>>() { // from class: fj.F2Functions.11
            @Override // fj.F2
            public Set<C> f(Set<A> as, Set<B> bs) {
                Set empty = Set.empty(o);
                Iterator it = as.iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    Iterator it2 = bs.iterator();
                    while (it2.hasNext()) {
                        empty = empty.insert(f.f(next, it2.next()));
                    }
                }
                return empty;
            }
        };
    }

    public static <A, B, C> F2<Stream<A>, Stream<B>, Stream<C>> streamM(final F2<A, B, C> f) {
        return new F2<Stream<A>, Stream<B>, Stream<C>>() { // from class: fj.F2Functions.12
            @Override // fj.F2
            public Stream<C> f(Stream<A> as, Stream<B> bs) {
                return as.bind(bs, f);
            }
        };
    }

    public static <A, B, C> F2<Tree<A>, Tree<B>, Tree<C>> treeM(final F2<A, B, C> f) {
        return new F2<Tree<A>, Tree<B>, Tree<C>>() { // from class: fj.F2Functions.13
            @Override // fj.F2
            public Tree<C> f(final Tree<A> as, final Tree<B> bs) {
                return Tree.node(f.f(as.root(), bs.root()), new P1<Stream<Tree<C>>>() { // from class: fj.F2Functions.13.1
                    {
                        AnonymousClass13.this = this;
                    }

                    @Override // fj.P1
                    public Stream<Tree<C>> _1() {
                        return (Stream) F2Functions.streamM(this).f(as.subForest()._1(), bs.subForest()._1());
                    }
                });
            }
        };
    }

    public static <A, B, C> F2<Array<A>, Array<B>, Array<C>> zipArrayM(final F2<A, B, C> f) {
        return new F2<Array<A>, Array<B>, Array<C>>() { // from class: fj.F2Functions.14
            @Override // fj.F2
            public Array<C> f(Array<A> as, Array<B> bs) {
                return as.zipWith(bs, f);
            }
        };
    }

    public static <A, B, C> F2<Iterable<A>, Iterable<B>, Iterable<C>> zipIterableM(final F2<A, B, C> f) {
        return new F2<Iterable<A>, Iterable<B>, Iterable<C>>() { // from class: fj.F2Functions.15
            @Override // fj.F2
            public Iterable<C> f(Iterable<A> as, Iterable<B> bs) {
                return IterableW.wrap(as).zipWith(bs, f);
            }
        };
    }

    public static <A, B, C> F2<List<A>, List<B>, List<C>> zipListM(final F2<A, B, C> f) {
        return new F2<List<A>, List<B>, List<C>>() { // from class: fj.F2Functions.16
            @Override // fj.F2
            public List<C> f(List<A> as, List<B> bs) {
                return as.zipWith(bs, f);
            }
        };
    }

    public static <A, B, C> F2<Stream<A>, Stream<B>, Stream<C>> zipStreamM(final F2<A, B, C> f) {
        return new F2<Stream<A>, Stream<B>, Stream<C>>() { // from class: fj.F2Functions.17
            @Override // fj.F2
            public Stream<C> f(Stream<A> as, Stream<B> bs) {
                return as.zipWith(bs, f);
            }
        };
    }

    public static <A, B, C> F2<NonEmptyList<A>, NonEmptyList<B>, NonEmptyList<C>> zipNelM(final F2<A, B, C> f) {
        return new F2<NonEmptyList<A>, NonEmptyList<B>, NonEmptyList<C>>() { // from class: fj.F2Functions.18
            @Override // fj.F2
            public NonEmptyList<C> f(NonEmptyList<A> as, NonEmptyList<B> bs) {
                return (NonEmptyList) NonEmptyList.fromList(as.toList().zipWith(bs.toList(), f)).some();
            }
        };
    }

    public static <A, B, C> F2<Set<A>, Set<B>, Set<C>> zipSetM(final F2<A, B, C> f, final Ord<C> o) {
        return new F2<Set<A>, Set<B>, Set<C>>() { // from class: fj.F2Functions.19
            @Override // fj.F2
            public Set<C> f(Set<A> as, Set<B> bs) {
                return Set.iterableSet(o, as.toStream().zipWith(bs.toStream(), f));
            }
        };
    }

    public static <A, B, C> F2<Tree<A>, Tree<B>, Tree<C>> zipTreeM(final F2<A, B, C> f) {
        return new F2<Tree<A>, Tree<B>, Tree<C>>() { // from class: fj.F2Functions.20
            @Override // fj.F2
            public Tree<C> f(final Tree<A> ta, final Tree<B> tb) {
                return Tree.node(f.f(ta.root(), tb.root()), new P1<Stream<Tree<C>>>() { // from class: fj.F2Functions.20.1
                    {
                        AnonymousClass20.this = this;
                    }

                    @Override // fj.P1
                    public Stream<Tree<C>> _1() {
                        return (Stream) F2Functions.zipStreamM(this).f(ta.subForest()._1(), tb.subForest()._1());
                    }
                });
            }
        };
    }

    public static <A, B, C> F2<Zipper<A>, Zipper<B>, Zipper<C>> zipZipperM(final F2<A, B, C> f) {
        return new F2<Zipper<A>, Zipper<B>, Zipper<C>>() { // from class: fj.F2Functions.21
            @Override // fj.F2
            public Zipper<C> f(Zipper<A> ta, Zipper<B> tb) {
                F2 zipStreamM = F2Functions.zipStreamM(f);
                return Zipper.zipper((Stream) zipStreamM.f(ta.lefts(), tb.lefts()), f.f(ta.focus(), tb.focus()), (Stream) zipStreamM.f(ta.rights(), tb.rights()));
            }
        };
    }

    public static <A, B, C> F2<TreeZipper<A>, TreeZipper<B>, TreeZipper<C>> zipTreeZipperM(final F2<A, B, C> f) {
        return new F2<TreeZipper<A>, TreeZipper<B>, TreeZipper<C>>() { // from class: fj.F2Functions.22
            @Override // fj.F2
            public TreeZipper<C> f(TreeZipper<A> ta, TreeZipper<B> tb) {
                F2 zipStreamM = F2Functions.zipStreamM(F2Functions.treeM(f));
                return TreeZipper.treeZipper((Tree) F2Functions.treeM(f).f(ta.p()._1(), tb.p()._1()), (Stream) zipStreamM.f(ta.lefts(), tb.lefts()), (Stream) zipStreamM.f(ta.rights(), tb.rights()), (Stream) F2Functions.zipStreamM(new F2<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>, P3<Stream<Tree<B>>, B, Stream<Tree<B>>>, P3<Stream<Tree<C>>, C, Stream<Tree<C>>>>() { // from class: fj.F2Functions.22.1
                    {
                        AnonymousClass22.this = this;
                    }

                    @Override // fj.F2
                    public P3<Stream<Tree<C>>, C, Stream<Tree<C>>> f(P3<Stream<Tree<A>>, A, Stream<Tree<A>>> pa, P3<Stream<Tree<B>>, B, Stream<Tree<B>>> pb) {
                        return P.p(F2Functions.zipStreamM(F2Functions.treeM(f)).f(pa._1(), pb._1()), f.f(pa._2(), pb._2()), F2Functions.zipStreamM(F2Functions.treeM(f)).f(pa._3(), pb._3()));
                    }
                }).f(ta.p()._4(), tb.p()._4()));
            }
        };
    }

    public static <A, B, C, Z> F2<Z, B, C> contramapFirst(F2<A, B, C> target, F<Z, A> f) {
        return F2Functions$$Lambda$1.lambdaFactory$(target, f);
    }

    public static /* synthetic */ Object lambda$contramapFirst$48(F2 f2, F f, Object z, Object b) {
        return f2.f(f.f(z), b);
    }

    public static <A, B, C, Z> F2<A, Z, C> contramapSecond(F2<A, B, C> target, F<Z, B> f) {
        return F2Functions$$Lambda$2.lambdaFactory$(target, f);
    }

    public static /* synthetic */ Object lambda$contramapSecond$49(F2 f2, F f, Object a, Object z) {
        return f2.f(a, f.f(z));
    }

    public static <A, B, C, X, Y> F2<X, Y, C> contramap(F2<A, B, C> target, F<X, A> f, F<Y, B> g) {
        return contramapSecond(contramapFirst(target, f), g);
    }

    public static <A, B, C, Z> F2<A, B, Z> map(F2<A, B, C> target, F<C, Z> f) {
        return F2Functions$$Lambda$3.lambdaFactory$(f, target);
    }

    public static /* synthetic */ Object lambda$map$50(F f, F2 f2, Object a, Object b) {
        return f.f(f2.f(a, b));
    }
}
