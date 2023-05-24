package fj.test;

import fj.Bottom;
import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.Ord;
import fj.P2;
import fj.Unit;
import fj.data.Array;
import fj.data.List;
import fj.function.Effect1;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Gen.class */
public final class Gen<A> {
    private final F<Integer, F<Rand, A>> f;

    private Gen(F<Integer, F<Rand, A>> f) {
        this.f = f;
    }

    public A gen(int i, Rand r) {
        return this.f.f(Integer.valueOf(i)).f(r);
    }

    public <B> Gen<B> map(final F<A, B> f) {
        return new Gen<>(new F<Integer, F<Rand, B>>() { // from class: fj.test.Gen.1
            @Override // fj.F
            public F<Rand, B> f(final Integer i) {
                return new F<Rand, B>() { // from class: fj.test.Gen.1.1
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX WARN: Type inference failed for: r0v3, types: [B, java.lang.Object] */
                    @Override // fj.F
                    public B f(Rand r) {
                        return f.f(Gen.this.gen(i.intValue(), r));
                    }
                };
            }
        });
    }

    public Gen<A> filter(final F<A, Boolean> f) {
        return gen(Function.curry(new F2<Integer, Rand, A>() { // from class: fj.test.Gen.2
            @Override // fj.F2
            public A f(Integer i, Rand r) {
                A a;
                do {
                    a = (A) Gen.this.gen(i.intValue(), r);
                } while (!((Boolean) f.f(a)).booleanValue());
                return a;
            }
        }));
    }

    public Unit foreach(Integer i, Rand r, F<A, Unit> f) {
        return f.f(this.f.f(i).f(r));
    }

    public void foreachDoEffect(Integer i, Rand r, Effect1<A> f) {
        f.f(this.f.f(i).f(r));
    }

    public <B> Gen<B> bind(final F<A, Gen<B>> f) {
        return new Gen<>(new F<Integer, F<Rand, B>>() { // from class: fj.test.Gen.3
            @Override // fj.F
            public F<Rand, B> f(final Integer i) {
                return new F<Rand, B>() { // from class: fj.test.Gen.3.1
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX WARN: Type inference failed for: r0v8, types: [B, java.lang.Object] */
                    @Override // fj.F
                    public B f(Rand r) {
                        return ((F) ((Gen) f.f(Gen.this.gen(i.intValue(), r))).f.f(i)).f(r);
                    }
                };
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C> Gen<C> bind(Gen<B> gb, F<A, F<B, C>> f) {
        return (Gen<B>) gb.apply(map(f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C, D> Gen<D> bind(Gen<B> gb, Gen<C> gc, F<A, F<B, F<C, D>>> f) {
        return (Gen<B>) gc.apply(bind(gb, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C, D, E> Gen<E> bind(Gen<B> gb, Gen<C> gc, Gen<D> gd, F<A, F<B, F<C, F<D, E>>>> f) {
        return (Gen<B>) gd.apply(bind(gb, gc, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C, D, E, F$> Gen<F$> bind(Gen<B> gb, Gen<C> gc, Gen<D> gd, Gen<E> ge, F<A, F<B, F<C, F<D, F<E, F$>>>>> f) {
        return (Gen<B>) ge.apply(bind(gb, gc, gd, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C, D, E, F$, G> Gen<G> bind(Gen<B> gb, Gen<C> gc, Gen<D> gd, Gen<E> ge, Gen<F$> gf, F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f) {
        return (Gen<B>) gf.apply(bind(gb, gc, gd, ge, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C, D, E, F$, G, H> Gen<H> bind(Gen<B> gb, Gen<C> gc, Gen<D> gd, Gen<E> ge, Gen<F$> gf, Gen<G> gg, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f) {
        return (Gen<B>) gg.apply(bind(gb, gc, gd, ge, gf, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C, D, E, F$, G, H, I> Gen<I> bind(Gen<B> gb, Gen<C> gc, Gen<D> gd, Gen<E> ge, Gen<F$> gf, Gen<G> gg, Gen<H> gh, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f) {
        return (Gen<B>) gh.apply(bind(gb, gc, gd, ge, gf, gg, f));
    }

    public <B> Gen<B> apply(Gen<F<A, B>> gf) {
        return gf.bind(new F<F<A, B>, Gen<B>>() { // from class: fj.test.Gen.4
            @Override // fj.F
            public Gen<B> f(final F<A, B> f) {
                return Gen.this.map(new F<A, B>() { // from class: fj.test.Gen.4.1
                    /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
                    @Override // fj.F
                    public B f(A a) {
                        return f.f(a);
                    }
                });
            }
        });
    }

    public Gen<A> resize(final int s) {
        return new Gen<>(new F<Integer, F<Rand, A>>() { // from class: fj.test.Gen.5
            @Override // fj.F
            public F<Rand, A> f(Integer i) {
                return new F<Rand, A>() { // from class: fj.test.Gen.5.1
                    @Override // fj.F
                    public A f(Rand r) {
                        return (A) ((F) Gen.this.f.f(Integer.valueOf(s))).f(r);
                    }
                };
            }
        });
    }

    public static <A> Gen<A> gen(F<Integer, F<Rand, A>> f) {
        return new Gen<>(f);
    }

    public static <A> Gen<List<A>> sequence(List<Gen<A>> gs) {
        return (Gen) gs.foldRight((F<Gen<A>, F<F, F>>) ((F<Gen<A>, F<Gen<List<A>>, Gen<List<A>>>>) new F<Gen<A>, F<Gen<List<A>>, Gen<List<A>>>>() { // from class: fj.test.Gen.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Gen) ((Gen) obj));
            }

            public F<Gen<List<A>>, Gen<List<A>>> f(final Gen<A> ga) {
                return new F<Gen<List<A>>, Gen<List<A>>>() { // from class: fj.test.Gen.6.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Gen) ((Gen) obj));
                    }

                    public Gen<List<A>> f(Gen<List<A>> gas) {
                        return ga.bind(gas, List.cons());
                    }
                };
            }
        }), (F) value(List.nil()));
    }

    public static <A> Gen<List<A>> sequenceN(int n, Gen<A> g) {
        return sequence(List.replicate(n, g));
    }

    public static <A> Gen<A> parameterised(final F<Integer, F<Rand, Gen<A>>> f) {
        return new Gen<>(Function.curry(new F2<Integer, Rand, A>() { // from class: fj.test.Gen.7
            @Override // fj.F2
            public A f(Integer i, Rand r) {
                return (A) ((Gen) ((F) F.this.f(i)).f(r)).gen(i.intValue(), r);
            }
        }));
    }

    public static <A> Gen<A> sized(F<Integer, Gen<A>> f) {
        return parameterised(Function.flip(Function.constant(f)));
    }

    public static <A> Gen<A> value(final A a) {
        return new Gen<>(new F<Integer, F<Rand, A>>() { // from class: fj.test.Gen.8
            @Override // fj.F
            public F<Rand, A> f(Integer i) {
                return new F<Rand, A>() { // from class: fj.test.Gen.8.1
                    @Override // fj.F
                    public A f(Rand r) {
                        return (A) a;
                    }
                };
            }
        });
    }

    public static Gen<Integer> choose(int from, int to) {
        final int f = Math.min(from, to);
        final int t = Math.max(from, to);
        return parameterised(Function.curry(new F2<Integer, Rand, Gen<Integer>>() { // from class: fj.test.Gen.9
            @Override // fj.F2
            public Gen<Integer> f(Integer i, Rand r) {
                return Gen.value(Integer.valueOf(r.choose(f, t)));
            }
        }));
    }

    public static Gen<Double> choose(double from, double to) {
        final double f = Math.min(from, to);
        final double t = Math.max(from, to);
        return parameterised(new F<Integer, F<Rand, Gen<Double>>>() { // from class: fj.test.Gen.10
            @Override // fj.F
            public F<Rand, Gen<Double>> f(Integer i) {
                return new F<Rand, Gen<Double>>() { // from class: fj.test.Gen.10.1
                    @Override // fj.F
                    public Gen<Double> f(Rand r) {
                        return Gen.value(Double.valueOf(r.choose(f, t)));
                    }
                };
            }
        });
    }

    public static <A> Gen<A> fail() {
        return new Gen<>(new F<Integer, F<Rand, A>>() { // from class: fj.test.Gen.11
            @Override // fj.F
            public F<Rand, A> f(Integer i) {
                return new F<Rand, A>() { // from class: fj.test.Gen.11.1
                    @Override // fj.F
                    public A f(Rand r) {
                        throw Bottom.error("Failing generator");
                    }
                };
            }
        });
    }

    public static <A> Gen<A> join(Gen<Gen<A>> g) {
        return (Gen<A>) g.bind(Function.identity());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <A> Gen<A> frequency(final List<P2<Integer, Gen<A>>> gs) {
        return (Gen<A>) choose(1, ((Integer) Monoid.intAdditionMonoid.sumLeft((List<Object>) gs.map(P2.__1()))).intValue()).bind((F<Integer, Gen<A>>) new F<Integer, Gen<A>>() { // from class: fj.test.Gen.12
            /* JADX WARN: Type inference failed for: r0v0, types: [fj.test.Gen$1Pick] */
            @Override // fj.F
            public Gen<A> f(Integer i) {
                return new Object() { // from class: fj.test.Gen.1Pick
                    <A> Gen<A> pick(int n, List<P2<Integer, Gen<A>>> gs2) {
                        if (gs2.isEmpty()) {
                            return Gen.fail();
                        }
                        int k = gs2.head()._1().intValue();
                        return n <= k ? gs2.head()._2() : pick(n - k, gs2.tail());
                    }
                }.pick(i.intValue(), List.this);
            }
        });
    }

    public static <A> Gen<A> elemFrequency(List<P2<Integer, A>> as) {
        return frequency(as.map((F<P2<Integer, A>, P2<Integer, Gen<A>>>) new F<P2<Integer, A>, P2<Integer, Gen<A>>>() { // from class: fj.test.Gen.13
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P2) ((P2) obj));
            }

            public P2<Integer, Gen<A>> f(P2<Integer, A> p) {
                return (P2<Integer, Gen<A>>) p.map2((F<A, Gen<A>>) new F<A, Gen<A>>() { // from class: fj.test.Gen.13.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public Gen<A> f(A a) {
                        return Gen.value(a);
                    }
                });
            }
        }));
    }

    public static <A> Gen<A> elements(final A... as) {
        return Array.array(as).isEmpty() ? fail() : (Gen<A>) choose(0, as.length - 1).map((F<Integer, A>) new F<Integer, A>() { // from class: fj.test.Gen.14
            @Override // fj.F
            public A f(Integer i) {
                return (A) as[i.intValue()];
            }
        });
    }

    public static <A> Gen<A> oneOf(final List<Gen<A>> gs) {
        return gs.isEmpty() ? fail() : (Gen<A>) choose(0, gs.length() - 1).bind((F<Integer, Gen<A>>) new F<Integer, Gen<A>>() { // from class: fj.test.Gen.15
            @Override // fj.F
            public Gen<A> f(Integer i) {
                return (Gen) List.this.index(i.intValue());
            }
        });
    }

    public static <A> Gen<List<A>> listOf(final Gen<A> g, final int x) {
        return sized(new F<Integer, Gen<List<A>>>() { // from class: fj.test.Gen.16
            @Override // fj.F
            public Gen<List<A>> f(Integer size) {
                return (Gen<List<A>>) Gen.choose(x, size.intValue()).bind((F<Integer, Gen<List<A>>>) new F<Integer, Gen<List<A>>>() { // from class: fj.test.Gen.16.1
                    @Override // fj.F
                    public Gen<List<A>> f(Integer n) {
                        return Gen.sequenceN(n.intValue(), g);
                    }
                });
            }
        });
    }

    public static <A> Gen<List<A>> listOf(Gen<A> g) {
        return listOf(g, 0);
    }

    public static <A> Gen<List<A>> listOf1(Gen<A> g) {
        return listOf(g, 1);
    }

    public static <A> Gen<List<A>> pick(int n, final List<A> as) {
        return (n < 0 || n > as.length()) ? fail() : sequenceN(n, choose(0, as.length() - 1)).map(new F<List<Integer>, List<A>>() { // from class: fj.test.Gen.17
            @Override // fj.F
            public List<A> f(List<Integer> is) {
                List<A> r = List.nil();
                List<Integer> iis = is.sort(Ord.intOrd);
                List<P2<A, Integer>> zipIndex = List.this.zipIndex();
                while (true) {
                    List<P2<A, Integer>> aas = zipIndex;
                    if (!iis.isNotEmpty() || !aas.isNotEmpty()) {
                        break;
                    }
                    if (iis.head().equals(aas.head()._2())) {
                        iis = iis.tail();
                    } else {
                        r = r.snoc(aas.head()._1());
                    }
                    zipIndex = aas.tail();
                }
                return r;
            }
        });
    }

    public static <A> Gen<List<A>> someOf(final List<A> as) {
        return (Gen<List<A>>) choose(0, as.length()).bind((F<Integer, Gen<List<A>>>) new F<Integer, Gen<List<A>>>() { // from class: fj.test.Gen.18
            @Override // fj.F
            public Gen<List<A>> f(Integer i) {
                return Gen.pick(i.intValue(), List.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Gen$19  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Gen$19.class */
    public static class AnonymousClass19 implements F<Integer, F<Rand, F<A, B>>> {
        final /* synthetic */ F val$f;

        AnonymousClass19(F f) {
            this.val$f = f;
        }

        @Override // fj.F
        public F<Rand, F<A, B>> f(final Integer i) {
            return new F<Rand, F<A, B>>() { // from class: fj.test.Gen.19.1
                @Override // fj.F
                public F<A, B> f(final Rand r) {
                    return new F<A, B>() { // from class: fj.test.Gen.19.1.1
                        /* JADX WARN: Type inference failed for: r0v9, types: [B, java.lang.Object] */
                        @Override // fj.F
                        public B f(A a) {
                            return ((F) ((Gen) AnonymousClass19.this.val$f.f(a)).f.f(i)).f(r);
                        }
                    };
                }
            };
        }
    }

    public static <A, B> Gen<F<A, B>> promote(F<A, Gen<B>> f) {
        return new Gen<>(new AnonymousClass19(f));
    }
}
