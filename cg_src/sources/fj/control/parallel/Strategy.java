package fj.control.parallel;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.data.Array;
import fj.data.Java;
import fj.data.List;
import fj.function.Effect1;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/parallel/Strategy.class */
public final class Strategy<A> {
    private final F<P1<A>, P1<A>> f;

    private Strategy(F<P1<A>, P1<A>> f) {
        this.f = f;
    }

    public F<P1<A>, P1<A>> f() {
        return this.f;
    }

    public static <A> Strategy<A> strategy(F<P1<A>, P1<A>> f) {
        return new Strategy<>(f);
    }

    public P1<A> par(P1<A> a) {
        return f().f(a);
    }

    public <B> F<B, P1<A>> concurry(F<B, A> f) {
        return Function.compose(f(), P1.curry(f));
    }

    public <B, C> F<B, F<C, P1<A>>> concurry(final F2<B, C, A> f) {
        return new F<B, F<C, P1<A>>>() { // from class: fj.control.parallel.Strategy.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            @Override // fj.F
            public F<C, P1<A>> f(B b) {
                return Strategy.this.concurry((F) Function.curry(f).f(b));
            }
        };
    }

    public static <A> List<P1<A>> mergeAll(List<Future<A>> xs) {
        return (List<P1<A>>) xs.map(obtain());
    }

    public P1<List<A>> parList(List<P1<A>> ps) {
        return P1.sequence(ps.map(f()));
    }

    public <B> P1<List<A>> parMap(F<B, A> f, List<B> bs) {
        return P1.sequence(bs.map(concurry(f)));
    }

    public <B> P1<Array<A>> parMap(F<B, A> f, Array<B> bs) {
        return P1.sequence(bs.map(concurry(f)));
    }

    public <B> List<A> parMap1(F<B, A> f, List<B> bs) {
        return (List) Function.compose(P1.__1(), parMapList(f)).f(bs);
    }

    public <B> Array<A> parMap1(F<B, A> f, Array<B> bs) {
        return (Array) Function.compose(P1.__1(), parMapArray(f)).f(bs);
    }

    public <B> F<List<B>, P1<List<A>>> parMapList(final F<B, A> f) {
        return new F<List<B>, P1<List<A>>>() { // from class: fj.control.parallel.Strategy.2
            @Override // fj.F
            public P1<List<A>> f(List<B> as) {
                return Strategy.this.parMap(f, as);
            }
        };
    }

    public <B> F<F<B, A>, F<List<B>, P1<List<A>>>> parMapList() {
        return new F<F<B, A>, F<List<B>, P1<List<A>>>>() { // from class: fj.control.parallel.Strategy.3
            @Override // fj.F
            public F<List<B>, P1<List<A>>> f(F<B, A> f) {
                return Strategy.this.parMapList(f);
            }
        };
    }

    public <B> F<F<B, A>, F<List<B>, List<A>>> parMapList1() {
        return new F<F<B, A>, F<List<B>, List<A>>>() { // from class: fj.control.parallel.Strategy.4
            @Override // fj.F
            public F<List<B>, List<A>> f(final F<B, A> f) {
                return new F<List<B>, List<A>>() { // from class: fj.control.parallel.Strategy.4.1
                    @Override // fj.F
                    public List<A> f(List<B> bs) {
                        return Strategy.this.parMap1(f, bs);
                    }
                };
            }
        };
    }

    public <B> F<Array<B>, P1<Array<A>>> parMapArray(final F<B, A> f) {
        return new F<Array<B>, P1<Array<A>>>() { // from class: fj.control.parallel.Strategy.5
            @Override // fj.F
            public P1<Array<A>> f(Array<B> as) {
                return Strategy.this.parMap(f, as);
            }
        };
    }

    public <B> F<F<B, A>, F<Array<B>, P1<Array<A>>>> parMapArray() {
        return new F<F<B, A>, F<Array<B>, P1<Array<A>>>>() { // from class: fj.control.parallel.Strategy.6
            @Override // fj.F
            public F<Array<B>, P1<Array<A>>> f(F<B, A> f) {
                return Strategy.this.parMapArray(f);
            }
        };
    }

    public <B> F<F<B, A>, F<Array<B>, Array<A>>> parMapArray1() {
        return new F<F<B, A>, F<Array<B>, Array<A>>>() { // from class: fj.control.parallel.Strategy.7
            @Override // fj.F
            public F<Array<B>, Array<A>> f(final F<B, A> f) {
                return new F<Array<B>, Array<A>>() { // from class: fj.control.parallel.Strategy.7.1
                    @Override // fj.F
                    public Array<A> f(Array<B> bs) {
                        return Strategy.this.parMap1(f, bs);
                    }
                };
            }
        };
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <A, B> P1<List<B>> parFlatMap(Strategy<List<B>> s, F<A, List<B>> f, List<A> as) {
        return (P1) P1.fmap(List.join()).f(s.parMap((F<B, List<B>>) f, (List) as));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <A, B> P1<Array<B>> parFlatMap(Strategy<Array<B>> s, F<A, Array<B>> f, Array<A> as) {
        return (P1) P1.fmap(Array.join()).f(s.parMap((F<B, Array<B>>) f, (Array) as));
    }

    public static <A> P1<List<A>> parListChunk(Strategy<List<A>> s, int chunkLength, List<P1<A>> as) {
        return (P1) P1.fmap(List.join()).f(s.parList(as.partition(chunkLength).map(P1.sequenceList())));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C> P1<List<A>> parZipWith(F2<B, C, A> f, List<B> bs, List<C> cs) {
        return P1.sequence(bs.zipWith((List) cs, (F<B, F<B, C>>) concurry(f)));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C> P1<Array<A>> parZipWith(F2<B, C, A> f, Array<B> bs, Array<C> cs) {
        return P1.sequence(bs.zipWith((Array) cs, (F<B, F<B, C>>) concurry(f)));
    }

    public <B, C> F2<List<B>, List<C>, P1<List<A>>> parZipListWith(final F2<B, C, A> f) {
        return new F2<List<B>, List<C>, P1<List<A>>>() { // from class: fj.control.parallel.Strategy.8
            @Override // fj.F2
            public P1<List<A>> f(List<B> bs, List<C> cs) {
                return Strategy.this.parZipWith(f, bs, cs);
            }
        };
    }

    public <B, C> F2<Array<B>, Array<C>, P1<Array<A>>> parZipArrayWith(final F2<B, C, A> f) {
        return new F2<Array<B>, Array<C>, P1<Array<A>>>() { // from class: fj.control.parallel.Strategy.9
            @Override // fj.F2
            public P1<Array<A>> f(Array<B> bs, Array<C> cs) {
                return Strategy.this.parZipWith(f, bs, cs);
            }
        };
    }

    public static <A> F<Future<A>, P1<A>> obtain() {
        return new F<Future<A>, P1<A>>() { // from class: fj.control.parallel.Strategy.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Future) ((Future) obj));
            }

            public P1<A> f(Future<A> t) {
                return Strategy.obtain(t);
            }
        };
    }

    public static <A> P1<A> obtain(final Future<A> t) {
        return new P1<A>() { // from class: fj.control.parallel.Strategy.11
            @Override // fj.P1
            public A _1() {
                try {
                    return (A) t.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new Error(e);
                } catch (ExecutionException e2) {
                    throw new Error(e2);
                }
            }
        };
    }

    public static <A> Effect1<Future<A>> discard() {
        return new Effect1<Future<A>>() { // from class: fj.control.parallel.Strategy.12
            @Override // fj.function.Effect1
            public /* bridge */ /* synthetic */ void f(Object obj) {
                f((Future) ((Future) obj));
            }

            public void f(Future<A> a) {
                ((P1) Strategy.obtain().f(a))._1();
            }
        };
    }

    public static <A> Strategy<A> simpleThreadStrategy() {
        return strategy(new F<P1<A>, P1<A>>() { // from class: fj.control.parallel.Strategy.13
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P1) ((P1) obj));
            }

            public P1<A> f(P1<A> p) {
                FutureTask<A> t = new FutureTask<>((Callable) Java.P1_Callable().f(p));
                new Thread(t).start();
                return Strategy.obtain(t);
            }
        });
    }

    public static <A> Strategy<A> executorStrategy(final ExecutorService s) {
        return strategy(new F<P1<A>, P1<A>>() { // from class: fj.control.parallel.Strategy.14
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P1) ((P1) obj));
            }

            public P1<A> f(P1<A> p) {
                return Strategy.obtain(s.submit((Callable) Java.P1_Callable().f(p)));
            }
        });
    }

    public static <A> Strategy<A> completionStrategy(final CompletionService<A> s) {
        return strategy(new F<P1<A>, P1<A>>() { // from class: fj.control.parallel.Strategy.15
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P1) ((P1) obj));
            }

            public P1<A> f(P1<A> p) {
                return Strategy.obtain(s.submit((Callable) Java.P1_Callable().f(p)));
            }
        });
    }

    public static <A> Strategy<A> seqStrategy() {
        return strategy(new F<P1<A>, P1<A>>() { // from class: fj.control.parallel.Strategy.16
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P1) ((P1) obj));
            }

            public P1<A> f(P1<A> a) {
                return P.p(a._1());
            }
        });
    }

    public static <A> Strategy<A> idStrategy() {
        return strategy(Function.identity());
    }

    public <B> Strategy<B> xmap(F<P1<A>, P1<B>> f, F<P1<B>, P1<A>> g) {
        return strategy(Function.compose(f, Function.compose(f(), g)));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Strategy<A> map(F<P1<A>, P1<A>> f) {
        return (Strategy<A>) xmap(f, Function.identity());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Strategy<A> comap(F<P1<A>, P1<A>> f) {
        return (Strategy<A>) xmap(Function.identity(), f);
    }

    public Strategy<A> errorStrategy(Effect1<Error> e) {
        return errorStrategy(this, e);
    }

    public static <A> Strategy<A> errorStrategy(Strategy<A> s, final Effect1<Error> e) {
        return s.comap(new F<P1<A>, P1<A>>() { // from class: fj.control.parallel.Strategy.17
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P1) ((P1) obj));
            }

            public P1<A> f(final P1<A> a) {
                return new P1<A>() { // from class: fj.control.parallel.Strategy.17.1
                    @Override // fj.P1
                    public A _1() {
                        try {
                            return (A) a._1();
                        } catch (Throwable t) {
                            Error error = new Error(t);
                            Effect1.this.f(error);
                            throw error;
                        }
                    }
                };
            }
        });
    }

    public static <A> Strategy<Callable<A>> callableStrategy(Strategy<Callable<A>> s) {
        return s.comap(new F<P1<Callable<A>>, P1<Callable<A>>>() { // from class: fj.control.parallel.Strategy.18
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P1) ((P1) obj));
            }

            public P1<Callable<A>> f(P1<Callable<A>> a) {
                return (P1) P1.curry(Callables.normalise()).f(a._1());
            }
        });
    }
}
