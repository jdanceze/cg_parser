package fj;

import fj.control.parallel.Actor;
import fj.control.parallel.Promise;
import fj.control.parallel.Strategy;
import fj.data.Array;
import fj.data.Either;
import fj.data.IterableW;
import fj.data.List;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Set;
import fj.data.Stream;
import fj.data.Tree;
import fj.data.TreeZipper;
import fj.data.Validation;
import fj.data.Zipper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/F1Functions.class */
public class F1Functions {
    public static <A, B, C> F<C, B> o(final F<A, B> f, final F<C, A> g) {
        return new F<C, B>() { // from class: fj.F1Functions.1
            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.F
            public B f(C c) {
                return f.f(g.f(c));
            }
        };
    }

    public static <A, B, C> F<F<C, A>, F<C, B>> o(final F<A, B> f) {
        return new F<F<C, A>, F<C, B>>() { // from class: fj.F1Functions.2
            @Override // fj.F
            public F<C, B> f(F<C, A> g) {
                return F1Functions.o(f, g);
            }
        };
    }

    public static <A, B, C> F<A, C> andThen(F<A, B> f, F<B, C> g) {
        return o(g, f);
    }

    public static <A, B, C> F<F<B, C>, F<A, C>> andThen(final F<A, B> f) {
        return new F<F<B, C>, F<A, C>>() { // from class: fj.F1Functions.3
            @Override // fj.F
            public F<A, C> f(F<B, C> g) {
                return F1Functions.andThen(f, g);
            }
        };
    }

    public static <A, B, C> F<A, C> bind(final F<A, B> f, final F<B, F<A, C>> g) {
        return new F<A, C>() { // from class: fj.F1Functions.4
            /* JADX WARN: Type inference failed for: r0v4, types: [C, java.lang.Object] */
            @Override // fj.F
            public C f(A a) {
                return ((F) g.f(f.f(a))).f(a);
            }
        };
    }

    public static <A, B, C> F<F<B, F<A, C>>, F<A, C>> bind(final F<A, B> f) {
        return new F<F<B, F<A, C>>, F<A, C>>() { // from class: fj.F1Functions.5
            @Override // fj.F
            public F<A, C> f(F<B, F<A, C>> g) {
                return F1Functions.bind(f, g);
            }
        };
    }

    public static <A, B, C> F<A, C> apply(final F<A, B> f, final F<A, F<B, C>> g) {
        return new F<A, C>() { // from class: fj.F1Functions.6
            /* JADX WARN: Type inference failed for: r0v4, types: [C, java.lang.Object] */
            @Override // fj.F
            public C f(A a) {
                return ((F) g.f(a)).f(f.f(a));
            }
        };
    }

    public static <A, B, C> F<F<A, F<B, C>>, F<A, C>> apply(final F<A, B> f) {
        return new F<F<A, F<B, C>>, F<A, C>>() { // from class: fj.F1Functions.7
            @Override // fj.F
            public F<A, C> f(F<A, F<B, C>> g) {
                return F1Functions.apply(f, g);
            }
        };
    }

    public static <A, B, C> F<A, F<A, C>> on(final F<A, B> f, final F<B, F<B, C>> g) {
        return new F<A, F<A, C>>() { // from class: fj.F1Functions.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass8) obj);
            }

            @Override // fj.F
            public F<A, C> f(final A a1) {
                return new F<A, C>() { // from class: fj.F1Functions.8.1
                    {
                        AnonymousClass8.this = this;
                    }

                    /* JADX WARN: Type inference failed for: r0v5, types: [C, java.lang.Object] */
                    @Override // fj.F
                    public C f(A a2) {
                        return ((F) g.f(f.f(a1))).f(f.f(a2));
                    }
                };
            }
        };
    }

    public static <A, B, C> F<F<B, F<B, C>>, F<A, F<A, C>>> on(final F<A, B> f) {
        return new F<F<B, F<B, C>>, F<A, F<A, C>>>() { // from class: fj.F1Functions.9
            @Override // fj.F
            public F<A, F<A, C>> f(F<B, F<B, C>> g) {
                return F1Functions.on(f, g);
            }
        };
    }

    public static <A, B> F<A, P1<B>> lazy(final F<A, B> f) {
        return new F<A, P1<B>>() { // from class: fj.F1Functions.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass10) obj);
            }

            @Override // fj.F
            public P1<B> f(final A a) {
                return new P1<B>() { // from class: fj.F1Functions.10.1
                    {
                        AnonymousClass10.this = this;
                    }

                    /* JADX WARN: Type inference failed for: r0v3, types: [B, java.lang.Object] */
                    @Override // fj.P1
                    public B _1() {
                        return f.f(a);
                    }
                };
            }
        };
    }

    public static <A, B> P1<B> f(F<A, B> f, A a) {
        return P.lazy(F1Functions$$Lambda$1.lambdaFactory$(f, a));
    }

    public static <A, B> F<P1<A>, P1<B>> mapP1(final F<A, B> f) {
        return new F<P1<A>, P1<B>>() { // from class: fj.F1Functions.11
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public P1<B> f(P1<A> p) {
                return p.map(f);
            }
        };
    }

    public static <A, B> F<A, Option<B>> optionK(final F<A, B> f) {
        return new F<A, Option<B>>() { // from class: fj.F1Functions.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass12) obj);
            }

            @Override // fj.F
            public Option<B> f(A a) {
                return Option.some(f.f(a));
            }
        };
    }

    public static <A, B> F<Option<A>, Option<B>> mapOption(final F<A, B> f) {
        return new F<Option<A>, Option<B>>() { // from class: fj.F1Functions.13
            @Override // fj.F
            public Option<B> f(Option<A> o) {
                return o.map(f);
            }
        };
    }

    public static <A, B> F<A, List<B>> listK(final F<A, B> f) {
        return new F<A, List<B>>() { // from class: fj.F1Functions.14
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass14) obj);
            }

            @Override // fj.F
            public List<B> f(A a) {
                return List.single(f.f(a));
            }
        };
    }

    public static <A, B> F<List<A>, List<B>> mapList(final F<A, B> f) {
        return new F<List<A>, List<B>>() { // from class: fj.F1Functions.15
            @Override // fj.F
            public List<B> f(List<A> x) {
                return x.map(f);
            }
        };
    }

    public static <A, B> F<A, Stream<B>> streamK(final F<A, B> f) {
        return new F<A, Stream<B>>() { // from class: fj.F1Functions.16
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass16) obj);
            }

            @Override // fj.F
            public Stream<B> f(A a) {
                return Stream.single(f.f(a));
            }
        };
    }

    public static <A, B> F<Stream<A>, Stream<B>> mapStream(final F<A, B> f) {
        return new F<Stream<A>, Stream<B>>() { // from class: fj.F1Functions.17
            @Override // fj.F
            public Stream<B> f(Stream<A> x) {
                return x.map(f);
            }
        };
    }

    public static <A, B> F<A, Array<B>> arrayK(final F<A, B> f) {
        return new F<A, Array<B>>() { // from class: fj.F1Functions.18
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass18) obj);
            }

            @Override // fj.F
            public Array<B> f(A a) {
                return Array.single(f.f(a));
            }
        };
    }

    public static <A, B> F<Array<A>, Array<B>> mapArray(final F<A, B> f) {
        return new F<Array<A>, Array<B>>() { // from class: fj.F1Functions.19
            @Override // fj.F
            public Array<B> f(Array<A> x) {
                return x.map(f);
            }
        };
    }

    public static <A, B> F<Actor<B>, Actor<A>> comapActor(final F<A, B> f) {
        return new F<Actor<B>, Actor<A>>() { // from class: fj.F1Functions.20
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Actor<A> f(Actor<B> a) {
                return a.comap(f);
            }
        };
    }

    public static <A, B> F<A, Promise<B>> promiseK(F<A, B> f, Strategy<Unit> s) {
        return Promise.promise(s, f);
    }

    public static <A, B> F<Promise<A>, Promise<B>> mapPromise(final F<A, B> f) {
        return new F<Promise<A>, Promise<B>>() { // from class: fj.F1Functions.21
            @Override // fj.F
            public Promise<B> f(Promise<A> p) {
                return p.fmap(f);
            }
        };
    }

    public static <A, B, C> F<A, Either<B, C>> eitherLeftK(F<A, B> f) {
        return o(Either.left_(), f);
    }

    public static <A, B, C> F<A, Either<C, B>> eitherRightK(F<A, B> f) {
        return o(Either.right_(), f);
    }

    public static <A, B, X> F<Either<A, X>, Either<B, X>> mapLeft(F<A, B> f) {
        return (F) Either.leftMap_().f(f);
    }

    public static <A, B, X> F<Either<X, A>, Either<X, B>> mapRight(F<A, B> f) {
        return (F) Either.rightMap_().f(f);
    }

    public static <A, B> F<Either<B, A>, B> onLeft(final F<A, B> f) {
        return new F<Either<B, A>, B>() { // from class: fj.F1Functions.22
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Either<Object, A>) obj);
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            public B f(Either<B, A> either) {
                return either.left().on(f);
            }
        };
    }

    public static <A, B> F<Either<A, B>, B> onRight(final F<A, B> f) {
        return new F<Either<A, B>, B>() { // from class: fj.F1Functions.23
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Either<A, Object>) obj);
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            public B f(Either<A, B> either) {
                return either.right().on(f);
            }
        };
    }

    public static <A, B> F<A, IterableW<B>> iterableK(F<A, B> f) {
        return (F) IterableW.arrow().f(f);
    }

    public static <A, B> F<Iterable<A>, IterableW<B>> mapIterable(F<A, B> f) {
        return o((F) IterableW.map().f(f), IterableW.wrap());
    }

    public static <A, B> F<A, NonEmptyList<B>> nelK(F<A, B> f) {
        return o(NonEmptyList.nel(), f);
    }

    public static <A, B> F<NonEmptyList<A>, NonEmptyList<B>> mapNel(final F<A, B> f) {
        return new F<NonEmptyList<A>, NonEmptyList<B>>() { // from class: fj.F1Functions.24
            @Override // fj.F
            public NonEmptyList<B> f(NonEmptyList<A> list) {
                return list.map(f);
            }
        };
    }

    public static <A, B> F<A, Set<B>> setK(final F<A, B> f, final Ord<B> o) {
        return new F<A, Set<B>>() { // from class: fj.F1Functions.25
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass25) obj);
            }

            @Override // fj.F
            public Set<B> f(A a) {
                return Set.single(o, f.f(a));
            }
        };
    }

    public static <A, B> F<Set<A>, Set<B>> mapSet(final F<A, B> f, final Ord<B> o) {
        return new F<Set<A>, Set<B>>() { // from class: fj.F1Functions.26
            @Override // fj.F
            public Set<B> f(Set<A> set) {
                return set.map(o, f);
            }
        };
    }

    public static <A, B> F<A, Tree<B>> treeK(final F<A, B> f) {
        return new F<A, Tree<B>>() { // from class: fj.F1Functions.27
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass27) obj);
            }

            @Override // fj.F
            public Tree<B> f(A a) {
                return Tree.leaf(f.f(a));
            }
        };
    }

    public static <A, B> F<Tree<A>, Tree<B>> mapTree(F<A, B> f) {
        return (F) Tree.fmap_().f(f);
    }

    public static <A, B> F<Tree<A>, B> foldMapTree(F<A, B> f, Monoid<B> m) {
        return Tree.foldMap_(f, m);
    }

    public static <A, B> F<A, TreeZipper<B>> treeZipperK(F<A, B> f) {
        return andThen(treeK(f), TreeZipper.fromTree());
    }

    public static <A, B> F<TreeZipper<A>, TreeZipper<B>> mapTreeZipper(final F<A, B> f) {
        return new F<TreeZipper<A>, TreeZipper<B>>() { // from class: fj.F1Functions.28
            @Override // fj.F
            public TreeZipper<B> f(TreeZipper<A> zipper) {
                return zipper.map(f);
            }
        };
    }

    public static <A, B, C> F<A, Validation<B, C>> failK(final F<A, B> f) {
        return new F<A, Validation<B, C>>() { // from class: fj.F1Functions.29
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass29) obj);
            }

            @Override // fj.F
            public Validation<B, C> f(A a) {
                return Validation.fail(f.f(a));
            }
        };
    }

    public static <A, B, C> F<A, Validation<C, B>> successK(final F<A, B> f) {
        return new F<A, Validation<C, B>>() { // from class: fj.F1Functions.30
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass30) obj);
            }

            @Override // fj.F
            public Validation<C, B> f(A a) {
                return Validation.success(f.f(a));
            }
        };
    }

    public static <A, B, X> F<Validation<A, X>, Validation<B, X>> mapFail(final F<A, B> f) {
        return new F<Validation<A, X>, Validation<B, X>>() { // from class: fj.F1Functions.31
            @Override // fj.F
            public Validation<B, X> f(Validation<A, X> validation) {
                return validation.f().map(f);
            }
        };
    }

    public static <A, B, X> F<Validation<X, A>, Validation<X, B>> mapSuccess(final F<A, B> f) {
        return new F<Validation<X, A>, Validation<X, B>>() { // from class: fj.F1Functions.32
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Validation<X, B> f(Validation<X, A> validation) {
                return validation.map(f);
            }
        };
    }

    public static <A, B> F<Validation<B, A>, B> onFail(final F<A, B> f) {
        return new F<Validation<B, A>, B>() { // from class: fj.F1Functions.33
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Validation<Object, A>) obj);
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            public B f(Validation<B, A> v) {
                return v.f().on(f);
            }
        };
    }

    public static <A, B> F<Validation<A, B>, B> onSuccess(final F<A, B> f) {
        return new F<Validation<A, B>, B>() { // from class: fj.F1Functions.34
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Validation<A, Object>) obj);
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            public B f(Validation<A, B> v) {
                return v.on(f);
            }
        };
    }

    public static <A, B> F<A, Zipper<B>> zipperK(F<A, B> f) {
        return andThen(streamK(f), new F<Stream<B>, Zipper<B>>() { // from class: fj.F1Functions.35
            @Override // fj.F
            public Zipper<B> f(Stream<B> stream) {
                return (Zipper) Zipper.fromStream(stream).some();
            }
        });
    }

    public static <A, B> F<Zipper<A>, Zipper<B>> mapZipper(final F<A, B> f) {
        return new F<Zipper<A>, Zipper<B>>() { // from class: fj.F1Functions.36
            @Override // fj.F
            public Zipper<B> f(Zipper<A> zipper) {
                return zipper.map(f);
            }
        };
    }

    public static <A, B> F<Equal<B>, Equal<A>> comapEqual(final F<A, B> f) {
        return new F<Equal<B>, Equal<A>>() { // from class: fj.F1Functions.37
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Equal<A> f(Equal<B> equal) {
                return equal.comap(f);
            }
        };
    }

    public static <A, B> F<Hash<B>, Hash<A>> comapHash(final F<A, B> f) {
        return new F<Hash<B>, Hash<A>>() { // from class: fj.F1Functions.38
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Hash<A> f(Hash<B> hash) {
                return hash.comap(f);
            }
        };
    }

    public static <A, B> F<Show<B>, Show<A>> comapShow(final F<A, B> f) {
        return new F<Show<B>, Show<A>>() { // from class: fj.F1Functions.39
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Show<A> f(Show<B> s) {
                return s.comap(f);
            }
        };
    }

    public static <A, B, C> F<P2<A, C>, P2<B, C>> mapFst(F<A, B> f) {
        return P2.map1_(f);
    }

    public static <A, B, C> F<P2<C, A>, P2<C, B>> mapSnd(F<A, B> f) {
        return P2.map2_(f);
    }

    public static <A, B> F<P2<A, A>, P2<B, B>> mapBoth(final F<A, B> f) {
        return new F<P2<A, A>, P2<B, B>>() { // from class: fj.F1Functions.40
            @Override // fj.F
            public P2<B, B> f(P2<A, A> aap2) {
                return P2.map(f, aap2);
            }
        };
    }

    public static <A, B> SynchronousQueue<B> mapJ(F<A, B> f, SynchronousQueue<A> as) {
        SynchronousQueue<B> bs = new SynchronousQueue<>();
        bs.addAll(Stream.iterableStream(as).map(f).toCollection());
        return bs;
    }

    public static <A, B> PriorityBlockingQueue<B> mapJ(F<A, B> f, PriorityBlockingQueue<A> as) {
        return new PriorityBlockingQueue<>((Collection<? extends B>) Stream.iterableStream(as).map(f).toCollection());
    }

    public static <A, B> LinkedBlockingQueue<B> mapJ(F<A, B> f, LinkedBlockingQueue<A> as) {
        return new LinkedBlockingQueue<>((Collection<? extends B>) Stream.iterableStream(as).map(f).toCollection());
    }

    public static <A, B> CopyOnWriteArraySet<B> mapJ(F<A, B> f, CopyOnWriteArraySet<A> as) {
        return new CopyOnWriteArraySet<>(Stream.iterableStream(as).map(f).toCollection());
    }

    public static <A, B> CopyOnWriteArrayList<B> mapJ(F<A, B> f, CopyOnWriteArrayList<A> as) {
        return new CopyOnWriteArrayList<>((Collection<? extends B>) Stream.iterableStream(as).map(f).toCollection());
    }

    public static <A, B> ConcurrentLinkedQueue<B> mapJ(F<A, B> f, ConcurrentLinkedQueue<A> as) {
        return new ConcurrentLinkedQueue<>(Stream.iterableStream(as).map(f).toCollection());
    }

    public static <A, B> ArrayBlockingQueue<B> mapJ(F<A, B> f, ArrayBlockingQueue<A> as) {
        ArrayBlockingQueue<B> bs = new ArrayBlockingQueue<>(as.size());
        bs.addAll(Stream.iterableStream(as).map(f).toCollection());
        return bs;
    }

    public static <A, B> TreeSet<B> mapJ(F<A, B> f, TreeSet<A> as) {
        return new TreeSet<>((Collection<? extends B>) Stream.iterableStream(as).map(f).toCollection());
    }

    public static <A, B> PriorityQueue<B> mapJ(F<A, B> f, PriorityQueue<A> as) {
        return new PriorityQueue<>((Collection<? extends B>) Stream.iterableStream(as).map(f).toCollection());
    }

    public static <A, B> LinkedList<B> mapJ(F<A, B> f, LinkedList<A> as) {
        return new LinkedList<>(Stream.iterableStream(as).map(f).toCollection());
    }

    public static <A, B> ArrayList<B> mapJ(F<A, B> f, ArrayList<A> as) {
        return new ArrayList<>((Collection<? extends B>) Stream.iterableStream(as).map(f).toCollection());
    }

    public static <A, B, C> F<A, C> map(F<A, B> target, F<B, C> f) {
        return andThen(target, f);
    }

    public static <A, B, C> F<C, B> contramap(F<A, B> target, F<C, A> f) {
        return andThen(f, target);
    }
}
