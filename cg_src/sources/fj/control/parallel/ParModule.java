package fj.control.parallel;

import fj.F;
import fj.F1Functions;
import fj.F2;
import fj.F2Functions;
import fj.Function;
import fj.Monoid;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.P4;
import fj.Unit;
import fj.data.Array;
import fj.data.IterableW;
import fj.data.List;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Stream;
import fj.data.Tree;
import fj.data.TreeZipper;
import fj.data.Zipper;
import fj.function.Effect1;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/parallel/ParModule.class */
public final class ParModule {
    private final Strategy<Unit> strategy;

    private ParModule(Strategy<Unit> strategy) {
        this.strategy = strategy;
    }

    public static ParModule parModule(Strategy<Unit> u) {
        return new ParModule(u);
    }

    public <A> Promise<A> promise(P1<A> p) {
        return Promise.promise(this.strategy, p);
    }

    public <A> F<P1<A>, Promise<A>> promise() {
        return new F<P1<A>, Promise<A>>() { // from class: fj.control.parallel.ParModule.1
            @Override // fj.F
            public Promise<A> f(P1<A> ap1) {
                return ParModule.this.promise(ap1);
            }
        };
    }

    public <A, B> F<A, Promise<B>> promise(F<A, B> f) {
        return F1Functions.promiseK(f, this.strategy);
    }

    public <A, B> F<F<A, B>, F<A, Promise<B>>> promisePure() {
        return new F<F<A, B>, F<A, Promise<B>>>() { // from class: fj.control.parallel.ParModule.2
            @Override // fj.F
            public F<A, Promise<B>> f(F<A, B> abf) {
                return ParModule.this.promise(abf);
            }
        };
    }

    public <A, B, C> F2<A, B, Promise<C>> promise(F2<A, B, C> f) {
        return P2.untuple(F1Functions.promiseK(F2Functions.tuple(f), this.strategy));
    }

    public <A> Actor<A> effect(Effect1<A> e) {
        return Actor.actor(this.strategy, e);
    }

    public <A> F<Effect1<A>, Actor<A>> effect() {
        return new F<Effect1<A>, Actor<A>>() { // from class: fj.control.parallel.ParModule.3
            @Override // fj.F
            public Actor<A> f(Effect1<A> effect) {
                return ParModule.this.effect(effect);
            }
        };
    }

    public <A> Actor<A> actor(Effect1<A> e) {
        return Actor.queueActor(this.strategy, e);
    }

    public <A> F<Effect1<A>, Actor<A>> actor() {
        return new F<Effect1<A>, Actor<A>>() { // from class: fj.control.parallel.ParModule.4
            @Override // fj.F
            public Actor<A> f(Effect1<A> effect) {
                return ParModule.this.actor(effect);
            }
        };
    }

    public <A> Promise<List<A>> sequence(List<Promise<A>> ps) {
        return Promise.sequence(this.strategy, ps);
    }

    public <A> F<List<Promise<A>>, Promise<List<A>>> sequenceList() {
        return new F<List<Promise<A>>, Promise<List<A>>>() { // from class: fj.control.parallel.ParModule.5
            @Override // fj.F
            public Promise<List<A>> f(List<Promise<A>> list) {
                return ParModule.this.sequence(list);
            }
        };
    }

    public <A> Promise<Stream<A>> sequence(Stream<Promise<A>> ps) {
        return Promise.sequence(this.strategy, ps);
    }

    public <A> F<Stream<Promise<A>>, Promise<Stream<A>>> sequenceStream() {
        return new F<Stream<Promise<A>>, Promise<Stream<A>>>() { // from class: fj.control.parallel.ParModule.6
            @Override // fj.F
            public Promise<Stream<A>> f(Stream<Promise<A>> stream) {
                return ParModule.this.sequence(stream);
            }
        };
    }

    public <A> Promise<P1<A>> sequence(P1<Promise<A>> p) {
        return Promise.sequence(this.strategy, p);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <A, B> Promise<List<B>> mapM(List<A> as, F<A, Promise<B>> f) {
        return sequence(as.map(f));
    }

    public <A, B> F<F<A, Promise<B>>, F<List<A>, Promise<List<B>>>> mapList() {
        return Function.curry(new F2<F<A, Promise<B>>, List<A>, Promise<List<B>>>() { // from class: fj.control.parallel.ParModule.7
            @Override // fj.F2
            public Promise<List<B>> f(F<A, Promise<B>> f, List<A> list) {
                return ParModule.this.mapM(list, f);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <A, B> Promise<Stream<B>> mapM(Stream<A> as, F<A, Promise<B>> f) {
        return sequence(as.map(f));
    }

    public <A, B> F<F<A, Promise<B>>, F<Stream<A>, Promise<Stream<B>>>> mapStream() {
        return Function.curry(new F2<F<A, Promise<B>>, Stream<A>, Promise<Stream<B>>>() { // from class: fj.control.parallel.ParModule.8
            @Override // fj.F2
            public Promise<Stream<B>> f(F<A, Promise<B>> f, Stream<A> stream) {
                return ParModule.this.mapM(stream, f);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <A, B> Promise<P1<B>> mapM(P1<A> a, F<A, Promise<B>> f) {
        return sequence(a.map(f));
    }

    public <A, B> Promise<List<B>> parMap(List<A> as, F<A, B> f) {
        return mapM(as, promise(f));
    }

    public <A, B> F<F<A, B>, F<List<A>, Promise<List<B>>>> parMapList() {
        return Function.curry(new F2<F<A, B>, List<A>, Promise<List<B>>>() { // from class: fj.control.parallel.ParModule.9
            @Override // fj.F2
            public Promise<List<B>> f(F<A, B> abf, List<A> list) {
                return ParModule.this.parMap((List) list, (F) abf);
            }
        });
    }

    public <A, B> Promise<NonEmptyList<B>> parMap(NonEmptyList<A> as, F<A, B> f) {
        return (Promise<B>) mapM(as.toList(), promise(f)).fmap((F<List<B>, NonEmptyList<B>>) new F<List<B>, NonEmptyList<B>>() { // from class: fj.control.parallel.ParModule.10
            @Override // fj.F
            public NonEmptyList<B> f(List<B> list) {
                return (NonEmptyList) NonEmptyList.fromList(list).some();
            }
        });
    }

    public <A, B> Promise<Stream<B>> parMap(Stream<A> as, F<A, B> f) {
        return mapM(as, promise(f));
    }

    public <A, B> F<F<A, B>, F<Stream<A>, Promise<Stream<B>>>> parMapStream() {
        return Function.curry(new F2<F<A, B>, Stream<A>, Promise<Stream<B>>>() { // from class: fj.control.parallel.ParModule.11
            @Override // fj.F2
            public Promise<Stream<B>> f(F<A, B> abf, Stream<A> stream) {
                return ParModule.this.parMap((Stream) stream, (F) abf);
            }
        });
    }

    public <A, B> Promise<Iterable<B>> parMap(Iterable<A> as, F<A, B> f) {
        return (Promise<B>) parMap((Stream) Stream.iterableStream(as), (F) f).fmap(Function.vary(Function.identity()));
    }

    public <A, B> F<F<A, B>, F<Iterable<A>, Promise<Iterable<B>>>> parMapIterable() {
        return Function.curry(new F2<F<A, B>, Iterable<A>, Promise<Iterable<B>>>() { // from class: fj.control.parallel.ParModule.12
            @Override // fj.F2
            public Promise<Iterable<B>> f(F<A, B> abf, Iterable<A> iterable) {
                return ParModule.this.parMap(iterable, abf);
            }
        });
    }

    public <A, B> Promise<Array<B>> parMap(Array<A> as, F<A, B> f) {
        return (Promise<B>) parMap((Stream) as.toStream(), (F) f).fmap((F<Stream<B>, Array<B>>) new F<Stream<B>, Array<B>>() { // from class: fj.control.parallel.ParModule.13
            @Override // fj.F
            public Array<B> f(Stream<B> stream) {
                return stream.toArray();
            }
        });
    }

    public <A, B> F<F<A, B>, F<Array<A>, Promise<Array<B>>>> parMapArray() {
        return Function.curry(new F2<F<A, B>, Array<A>, Promise<Array<B>>>() { // from class: fj.control.parallel.ParModule.14
            @Override // fj.F2
            public Promise<Array<B>> f(F<A, B> abf, Array<A> array) {
                return ParModule.this.parMap((Array) array, (F) abf);
            }
        });
    }

    public <A, B> Promise<Zipper<B>> parMap(Zipper<A> za, F<A, B> f) {
        return (Promise<B>) parMap((Stream) za.rights(), (F) f).apply(promise(f).f(za.focus()).apply(parMap((Stream) za.lefts(), (F) f).fmap(Function.curry(Zipper.zipper()))));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <A, B> Promise<Tree<B>> parMap(Tree<A> ta, F<A, B> f) {
        return (Promise<B>) mapM(ta.subForest(), (F) mapStream().f(parMapTree().f(f))).apply(((Promise) promise(f).f(ta.root())).fmap(Tree.node()));
    }

    public <A, B> F<F<A, B>, F<Tree<A>, Promise<Tree<B>>>> parMapTree() {
        return Function.curry(new F2<F<A, B>, Tree<A>, Promise<Tree<B>>>() { // from class: fj.control.parallel.ParModule.15
            @Override // fj.F2
            public Promise<Tree<B>> f(F<A, B> abf, Tree<A> tree) {
                return ParModule.this.parMap((Tree) tree, (F) abf);
            }
        });
    }

    public <A, B> Promise<TreeZipper<B>> parMap(TreeZipper<A> za, final F<A, B> f) {
        final F<A, B> f2 = (F) Tree.fmap_().f(f);
        P4<Tree<A>, Stream<Tree<A>>, Stream<Tree<A>>, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>> p = za.p();
        return mapM(p._4(), new F<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>, Promise<P3<Stream<Tree<B>>, B, Stream<Tree<B>>>>>() { // from class: fj.control.parallel.ParModule.16
            @Override // fj.F
            public Promise<P3<Stream<Tree<B>>, B, Stream<Tree<B>>>> f(P3<Stream<Tree<A>>, A, Stream<Tree<A>>> p3) {
                return ParModule.this.parMap((Stream) p3._3(), f2).apply(((Promise) ParModule.this.promise(f).f(p3._2())).apply(ParModule.this.parMap((Stream) p3._1(), f2).fmap(P.p3())));
            }
        }).apply(parMap((Stream) za.rights(), (F) f2).apply(parMap((Stream) za.lefts(), (F) f2).apply(parMap((Tree) p._1(), (F) f).fmap(TreeZipper.treeZipper()))));
    }

    public <A, B> Promise<List<B>> parFlatMap(List<A> as, F<A, List<B>> f) {
        return parFoldMap(as, f, Monoid.listMonoid());
    }

    public <A, B> Promise<Stream<B>> parFlatMap(Stream<A> as, F<A, Stream<B>> f) {
        return parFoldMap((Stream) as, (F) f, (Monoid) Monoid.streamMonoid());
    }

    public <A, B> Promise<Array<B>> parFlatMap(Array<A> as, F<A, Array<B>> f) {
        return (Promise<B>) parMap((Array) as, (F) f).fmap(Array.join());
    }

    public <A, B> Promise<Iterable<B>> parFlatMap(Iterable<A> as, F<A, Iterable<B>> f) {
        return (Promise<B>) parMap(as, f).fmap(IterableW.join()).fmap(Function.vary(Function.identity()));
    }

    public <A, B, C> Promise<List<C>> parZipWith(List<A> as, List<B> bs, F<A, F<B, C>> f) {
        return sequence(as.zipWith(bs, promise(Function.uncurryF2(f))));
    }

    public <A, B, C> Promise<Stream<C>> parZipWith(Stream<A> as, Stream<B> bs, F<A, F<B, C>> f) {
        return sequence(as.zipWith(bs, promise(Function.uncurryF2(f))));
    }

    public <A, B, C> Promise<Array<C>> parZipWith(Array<A> as, Array<B> bs, F<A, F<B, C>> f) {
        return (Promise<B>) parZipWith((Stream) as.toStream(), (Stream) bs.toStream(), (F) f).fmap((F<Stream<C>, Array<C>>) new F<Stream<C>, Array<C>>() { // from class: fj.control.parallel.ParModule.17
            @Override // fj.F
            public Array<C> f(Stream<C> stream) {
                return stream.toArray();
            }
        });
    }

    public <A, B, C> Promise<Iterable<C>> parZipWith(Iterable<A> as, Iterable<B> bs, F<A, F<B, C>> f) {
        return (Promise<B>) parZipWith((Stream) Stream.iterableStream(as), (Stream) Stream.iterableStream(bs), (F) f).fmap(Function.vary(Function.identity()));
    }

    public <A, B> Promise<B> parFoldMap(Stream<A> as, F<A, B> map, Monoid<B> reduce) {
        return as.isEmpty() ? promise(P.p(reduce.zero())) : (Promise) as.map(promise(map)).foldLeft1(Promise.liftM2(reduce.sum()));
    }

    public <A, B> Promise<B> parFoldMap(Stream<A> as, F<A, B> map, final Monoid<B> reduce, final F<Stream<A>, P2<Stream<A>, Stream<A>>> chunking) {
        return parMap((Stream) Stream.unfold(new F<Stream<A>, Option<P2<Stream<A>, Stream<A>>>>() { // from class: fj.control.parallel.ParModule.19
            @Override // fj.F
            public Option<P2<Stream<A>, Stream<A>>> f(Stream<A> stream) {
                return stream.isEmpty() ? Option.none() : Option.some(chunking.f(stream));
            }
        }, as), (F) ((F) Stream.map_().f(map))).bind((F<Stream<Stream<B>>, Promise<B>>) new F<Stream<Stream<B>>, Promise<B>>() { // from class: fj.control.parallel.ParModule.18
            @Override // fj.F
            public Promise<B> f(Stream<Stream<B>> stream) {
                return ParModule.this.parMap((Stream) stream, reduce.sumLeftS()).fmap(reduce.sumLeftS());
            }
        });
    }

    public <A, B> Promise<B> parFoldMap(Iterable<A> as, F<A, B> map, Monoid<B> reduce, final F<Iterable<A>, P2<Iterable<A>, Iterable<A>>> chunking) {
        return parFoldMap((Stream) Stream.iterableStream(as), (F) map, (Monoid) reduce, (F) new F<Stream<A>, P2<Stream<A>, Stream<A>>>() { // from class: fj.control.parallel.ParModule.20
            @Override // fj.F
            public P2<Stream<A>, Stream<A>> f(Stream<A> stream) {
                Object obj = new F<Iterable<A>, Stream<A>>() { // from class: fj.control.parallel.ParModule.20.1
                    @Override // fj.F
                    public Stream<A> f(Iterable<A> iterable) {
                        return Stream.iterableStream(iterable);
                    }
                };
                return ((P2) chunking.f(stream)).map1(obj).map2(obj);
            }
        });
    }

    public <A, B> Promise<B> parFoldMap(Iterable<A> as, F<A, B> map, Monoid<B> reduce) {
        return parFoldMap((Stream) Stream.iterableStream(as), (F) map, (Monoid) reduce);
    }

    public <A, B> Promise<Zipper<B>> parExtend(Zipper<A> za, F<Zipper<A>, B> f) {
        return parMap((Zipper) za.positions(), (F) f);
    }

    public <A, B> Promise<Tree<B>> parExtend(Tree<A> ta, F<Tree<A>, B> f) {
        return parMap((Tree) ta.cojoin(), (F) f);
    }

    public <A, B> Promise<TreeZipper<B>> parExtend(TreeZipper<A> za, F<TreeZipper<A>, B> f) {
        return parMap((TreeZipper) za.positions(), (F) f);
    }

    public <A, B> Promise<NonEmptyList<B>> parExtend(NonEmptyList<A> as, F<NonEmptyList<A>, B> f) {
        return parMap((NonEmptyList) as.tails(), (F) f);
    }
}
