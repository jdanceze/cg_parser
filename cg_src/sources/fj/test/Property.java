package fj.test;

import fj.F;
import fj.F2;
import fj.F3;
import fj.F4;
import fj.F5;
import fj.F6;
import fj.F7;
import fj.F8;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.data.List;
import fj.data.Option;
import fj.data.Stream;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property.class */
public final class Property {
    private final F<Integer, F<Rand, Result>> f;

    private Property(F<Integer, F<Rand, Result>> f) {
        this.f = f;
    }

    public Result prop(int i, Rand r) {
        return this.f.f(Integer.valueOf(i)).f(r);
    }

    public Gen<Result> gen() {
        return Gen.gen(new F<Integer, F<Rand, Result>>() { // from class: fj.test.Property.1
            @Override // fj.F
            public F<Rand, Result> f(final Integer i) {
                return new F<Rand, Result>() { // from class: fj.test.Property.1.1
                    @Override // fj.F
                    public Result f(Rand r) {
                        return (Result) ((F) Property.this.f.f(i)).f(r);
                    }
                };
            }
        });
    }

    public Property and(Property p) {
        return fromGen(gen().bind(p.gen(), new F<Result, F<Result, Result>>() { // from class: fj.test.Property.2
            @Override // fj.F
            public F<Result, Result> f(final Result res1) {
                return new F<Result, Result>() { // from class: fj.test.Property.2.1
                    @Override // fj.F
                    public Result f(Result res2) {
                        return (res1.isException() || res1.isFalsified()) ? res1 : (res2.isException() || res2.isFalsified()) ? res2 : (res1.isProven() || res1.isUnfalsified()) ? res2 : (res2.isProven() || res2.isUnfalsified()) ? res1 : Result.noResult();
                    }
                };
            }
        }));
    }

    public Property or(Property p) {
        return fromGen(gen().bind(p.gen(), new F<Result, F<Result, Result>>() { // from class: fj.test.Property.3
            @Override // fj.F
            public F<Result, Result> f(final Result res1) {
                return new F<Result, Result>() { // from class: fj.test.Property.3.1
                    @Override // fj.F
                    public Result f(Result res2) {
                        return (res1.isException() || res1.isFalsified()) ? res1 : (res2.isException() || res2.isFalsified()) ? res2 : (res1.isProven() || res1.isUnfalsified()) ? res1 : (res2.isProven() || res2.isUnfalsified()) ? res2 : Result.noResult();
                    }
                };
            }
        }));
    }

    public Property sequence(Property p) {
        return fromGen(gen().bind(p.gen(), new F<Result, F<Result, Result>>() { // from class: fj.test.Property.4
            @Override // fj.F
            public F<Result, Result> f(final Result res1) {
                return new F<Result, Result>() { // from class: fj.test.Property.4.1
                    @Override // fj.F
                    public Result f(Result res2) {
                        return (res1.isException() || res1.isProven() || res1.isUnfalsified()) ? res1 : (res2.isException() || res2.isProven() || res2.isUnfalsified()) ? res2 : res1.isFalsified() ? res2 : res2.isFalsified() ? res1 : Result.noResult();
                    }
                };
            }
        }));
    }

    public CheckResult check(Rand r, int minSuccessful, int maxDiscarded, int minSize, int maxSize) {
        CheckResult res;
        int s = 0;
        int d = 0;
        float sz = minSize;
        while (true) {
            float size = (s == 0 && d == 0) ? minSize : sz + ((maxSize - sz) / (minSuccessful - s));
            try {
                Result x = this.f.f(Integer.valueOf(Math.round(size))).f(r);
                if (x.isNoResult()) {
                    if (d + 1 >= maxDiscarded) {
                        res = CheckResult.exhausted(s, d + 1);
                        break;
                    }
                    sz = size;
                    d++;
                } else if (x.isProven()) {
                    res = CheckResult.proven(x.args().some(), s + 1, d);
                    break;
                } else if (x.isUnfalsified()) {
                    if (s + 1 >= minSuccessful) {
                        res = CheckResult.passed(s + 1, d);
                        break;
                    }
                    sz = size;
                    s++;
                } else if (x.isFalsified()) {
                    res = CheckResult.falsified(x.args().some(), s, d);
                    break;
                } else if (x.isException()) {
                    res = CheckResult.propException(x.args().some(), x.exception().some(), s, d);
                    break;
                }
            } catch (Throwable t) {
                res = CheckResult.genException(t, s, d);
            }
        }
        return res;
    }

    public CheckResult check(int minSuccessful, int maxDiscarded, int minSize, int maxSize) {
        return check(Rand.standard, minSuccessful, maxDiscarded, minSize, maxSize);
    }

    public CheckResult check(Rand r) {
        return check(r, 100, 500, 0, 100);
    }

    public CheckResult check(Rand r, int minSize, int maxSize) {
        return check(r, 100, 500, minSize, maxSize);
    }

    public CheckResult check(int minSize, int maxSize) {
        return check(100, 500, minSize, maxSize);
    }

    public CheckResult check() {
        return check(0, 100);
    }

    public CheckResult minSuccessful(int minSuccessful) {
        return check(minSuccessful, 500, 0, 100);
    }

    public CheckResult minSuccessful(Rand r, int minSuccessful) {
        return check(r, minSuccessful, 500, 0, 100);
    }

    public CheckResult maxDiscarded(int maxDiscarded) {
        return check(100, maxDiscarded, 0, 100);
    }

    public CheckResult maxDiscarded(Rand r, int maxDiscarded) {
        return check(r, 100, maxDiscarded, 0, 100);
    }

    public CheckResult minSize(int minSize) {
        return check(100, 500, minSize, 100);
    }

    public CheckResult minSize(Rand r, int minSize) {
        return check(r, 100, 500, minSize, 100);
    }

    public CheckResult maxSize(int maxSize) {
        return check(100, 500, 0, maxSize);
    }

    public CheckResult maxSize(Rand r, int maxSize) {
        return check(r, 100, 500, 0, maxSize);
    }

    public static Property implies(boolean b, P1<Property> p) {
        return b ? p._1() : new Property(new F<Integer, F<Rand, Result>>() { // from class: fj.test.Property.5
            @Override // fj.F
            public F<Rand, Result> f(Integer i) {
                return new F<Rand, Result>() { // from class: fj.test.Property.5.1
                    @Override // fj.F
                    public Result f(Rand r) {
                        return Result.noResult();
                    }
                };
            }
        });
    }

    public static Property prop(F<Integer, F<Rand, Result>> f) {
        return new Property(f);
    }

    public static Property prop(final Result r) {
        return new Property(new F<Integer, F<Rand, Result>>() { // from class: fj.test.Property.6
            @Override // fj.F
            public F<Rand, Result> f(Integer integer) {
                return new F<Rand, Result>() { // from class: fj.test.Property.6.1
                    @Override // fj.F
                    public Result f(Rand x) {
                        return Result.this;
                    }
                };
            }
        });
    }

    public static Property prop(boolean b) {
        return b ? prop(Result.proven(List.nil())) : prop(Result.falsified(List.nil()));
    }

    public static Property fromGen(final Gen<Result> g) {
        return prop(new F<Integer, F<Rand, Result>>() { // from class: fj.test.Property.7
            @Override // fj.F
            public F<Rand, Result> f(final Integer i) {
                return new F<Rand, Result>() { // from class: fj.test.Property.7.1
                    @Override // fj.F
                    public Result f(Rand r) {
                        return (Result) Gen.this.gen(i.intValue(), r);
                    }
                };
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Property$8  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$8.class */
    public static class AnonymousClass8 implements F<Integer, F<Rand, Result>> {
        final /* synthetic */ F val$f;
        final /* synthetic */ Gen val$g;
        final /* synthetic */ Shrink val$shrink;

        AnonymousClass8(F f, Gen gen, Shrink shrink) {
            this.val$f = f;
            this.val$g = gen;
            this.val$shrink = shrink;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.test.Property$8$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$8$1.class */
        public class AnonymousClass1 implements F<Rand, Result> {
            final /* synthetic */ Integer val$i;

            AnonymousClass1(Integer num) {
                this.val$i = num;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.test.Property$8$1$1Util  reason: invalid class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$8$1$1Util.class */
            public final class C1Util {
                final /* synthetic */ Rand val$r;

                C1Util(Rand rand) {
                    this.val$r = rand;
                }

                Option<P2<A, Result>> first(Stream<A> as, final int shrinks) {
                    final Stream map = as.map(new F<A, Option<P2<A, Result>>>() { // from class: fj.test.Property.8.1.1Util.1
                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C01661) obj);
                        }

                        @Override // fj.F
                        public Option<P2<A, Result>> f(final A a) {
                            Result result = Property.exception((P1) AnonymousClass8.this.val$f.f(a)).prop(AnonymousClass1.this.val$i.intValue(), C1Util.this.val$r);
                            return result.toOption().map(new F<Result, P2<A, Result>>() { // from class: fj.test.Property.8.1.1Util.1.1
                                @Override // fj.F
                                public P2<A, Result> f(Result result2) {
                                    return P.p(a, result2.provenAsUnfalsified().addArg(Arg.arg(a, shrinks)));
                                }
                            });
                        }
                    });
                    if (map.isEmpty()) {
                        return Option.none();
                    }
                    return (Option) map.find(new F<Option<P2<A, Result>>, Boolean>() { // from class: fj.test.Property.8.1.1Util.3
                        @Override // fj.F
                        public Boolean f(Option<P2<A, Result>> o) {
                            return Boolean.valueOf(C1Util.this.failed(o));
                        }
                    }).orSome((P1<Object>) new P1<Option<P2<A, Result>>>() { // from class: fj.test.Property.8.1.1Util.2
                        @Override // fj.P1
                        public Option<P2<A, Result>> _1() {
                            return (Option) map.head();
                        }
                    });
                }

                public boolean failed(Option<P2<A, Result>> o) {
                    return o.isSome() && ((Result) ((P2) o.some())._2()).failed();
                }
            }

            @Override // fj.F
            public Result f(Rand r) {
                Option<Result> or;
                C1Util u = new C1Util(r);
                Option first = u.first(Stream.single(AnonymousClass8.this.val$g.gen(this.val$i.intValue(), r)), 0);
                F __2 = P2.__2();
                if (u.failed(first)) {
                    int shrinks = 0;
                    do {
                        shrinks++;
                        or = first.map(__2);
                        first = u.first(AnonymousClass8.this.val$shrink.shrink((Shrink) ((P2) first.some())._1()), shrinks);
                    } while (u.failed(first));
                    return Result.noResult(or);
                }
                return Result.noResult(first.map(__2));
            }
        }

        @Override // fj.F
        public F<Rand, Result> f(Integer i) {
            return new AnonymousClass1(i);
        }
    }

    public static <A> Property forall(Gen<A> g, Shrink<A> shrink, F<A, P1<Property>> f) {
        return prop(new AnonymousClass8(f, g, shrink));
    }

    public static <A> Property propertyP(Arbitrary<A> aa, Shrink<A> sa, F<A, P1<Property>> f) {
        return forall(aa.gen, sa, f);
    }

    public static <A> Property property(Arbitrary<A> aa, Shrink<A> sa, F<A, Property> f) {
        return propertyP(aa, sa, P1.curry(f));
    }

    public static <A> Property propertyP(Arbitrary<A> aa, F<A, P1<Property>> f) {
        return propertyP(aa, Shrink.empty(), f);
    }

    public static <A> Property property(Arbitrary<A> aa, F<A, Property> f) {
        return propertyP(aa, P1.curry(f));
    }

    public static <A, B> Property propertyP(Arbitrary<A> aa, final Arbitrary<B> ab, Shrink<A> sa, final Shrink<B> sb, final F<A, F<B, P1<Property>>> f) {
        return property(aa, sa, new F<A, Property>() { // from class: fj.test.Property.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Property f(Object obj) {
                return f((AnonymousClass9) obj);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // fj.F
            public Property f(final A a) {
                return Property.propertyP(Arbitrary.this, sb, new F<B, P1<Property>>() { // from class: fj.test.Property.9.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ P1<Property> f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // fj.F
                    public P1<Property> f(B b) {
                        return (P1) ((F) f.f(a)).f(b);
                    }
                });
            }
        });
    }

    public static <A, B> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Shrink<A> sa, Shrink<B> sb, F<A, F<B, Property>> f) {
        return propertyP(aa, ab, sa, sb, Function.compose2(P.p1(), f));
    }

    public static <A, B> Property propertyP(Arbitrary<A> aa, final Arbitrary<B> ab, final F<A, F<B, P1<Property>>> f) {
        return property(aa, new F<A, Property>() { // from class: fj.test.Property.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Property f(Object obj) {
                return f((AnonymousClass10) obj);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // fj.F
            public Property f(final A a) {
                return Property.propertyP(Arbitrary.this, new F<B, P1<Property>>() { // from class: fj.test.Property.10.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ P1<Property> f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // fj.F
                    public P1<Property> f(B b) {
                        return (P1) ((F) f.f(a)).f(b);
                    }
                });
            }
        });
    }

    public static <A, B> Property property(Arbitrary<A> aa, Arbitrary<B> ab, F<A, F<B, Property>> f) {
        return propertyP(aa, ab, Function.compose2(P.p1(), f));
    }

    public static <A, B> Property propertyP(Arbitrary<A> aa, Arbitrary<B> ab, Shrink<A> sa, Shrink<B> sb, F2<A, B, P1<Property>> f) {
        return propertyP(aa, ab, sa, sb, Function.curry(f));
    }

    public static <A, B> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Shrink<A> sa, Shrink<B> sb, F2<A, B, Property> f) {
        return propertyP(aa, ab, sa, sb, Function.compose2(P.p1(), Function.curry(f)));
    }

    public static <A, B> Property propertyP(Arbitrary<A> aa, Arbitrary<B> ab, F2<A, B, P1<Property>> f) {
        return propertyP(aa, ab, Function.curry(f));
    }

    public static <A, B> Property property(Arbitrary<A> aa, Arbitrary<B> ab, F2<A, B, Property> f) {
        return propertyP(aa, ab, Function.compose2(P.p1(), Function.curry(f)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Property$11  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$11.class */
    public static class AnonymousClass11 implements F<A, F<B, Property>> {
        final /* synthetic */ Arbitrary val$ac;
        final /* synthetic */ Shrink val$sc;
        final /* synthetic */ F val$f;

        AnonymousClass11(Arbitrary arbitrary, Shrink shrink, F f) {
            this.val$ac = arbitrary;
            this.val$sc = shrink;
            this.val$f = f;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass11) obj);
        }

        @Override // fj.F
        public F<B, Property> f(final A a) {
            return new F<B, Property>() { // from class: fj.test.Property.11.1
                @Override // fj.F
                public /* bridge */ /* synthetic */ Property f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // fj.F
                public Property f(final B b) {
                    return Property.property(AnonymousClass11.this.val$ac, AnonymousClass11.this.val$sc, new F<C, Property>() { // from class: fj.test.Property.11.1.1
                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Property f(Object obj) {
                            return f((C01241) obj);
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // fj.F
                        public Property f(C c) {
                            return (Property) ((F) ((F) AnonymousClass11.this.val$f.f(a)).f(b)).f(c);
                        }
                    });
                }
            };
        }
    }

    public static <A, B, C> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, F<A, F<B, F<C, Property>>> f) {
        return property(aa, ab, sa, sb, new AnonymousClass11(ac, sc, f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Property$12  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$12.class */
    public static class AnonymousClass12 implements F<A, F<B, Property>> {
        final /* synthetic */ Arbitrary val$ac;
        final /* synthetic */ F val$f;

        AnonymousClass12(Arbitrary arbitrary, F f) {
            this.val$ac = arbitrary;
            this.val$f = f;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass12) obj);
        }

        @Override // fj.F
        public F<B, Property> f(final A a) {
            return new F<B, Property>() { // from class: fj.test.Property.12.1
                @Override // fj.F
                public /* bridge */ /* synthetic */ Property f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // fj.F
                public Property f(final B b) {
                    return Property.property(AnonymousClass12.this.val$ac, new F<C, Property>() { // from class: fj.test.Property.12.1.1
                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Property f(Object obj) {
                            return f((C01251) obj);
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // fj.F
                        public Property f(C c) {
                            return (Property) ((F) ((F) AnonymousClass12.this.val$f.f(a)).f(b)).f(c);
                        }
                    });
                }
            };
        }
    }

    public static <A, B, C> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, F<A, F<B, F<C, Property>>> f) {
        return property(aa, ab, new AnonymousClass12(ac, f));
    }

    public static <A, B, C> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, F3<A, B, C, Property> f) {
        return property(aa, ab, ac, sa, sb, sc, Function.curry(f));
    }

    public static <A, B, C> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, F3<A, B, C, Property> f) {
        return property(aa, ab, ac, Function.curry(f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Property$13  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$13.class */
    public static class AnonymousClass13 implements F<A, F<B, F<C, Property>>> {
        final /* synthetic */ Arbitrary val$ad;
        final /* synthetic */ Shrink val$sd;
        final /* synthetic */ F val$f;

        AnonymousClass13(Arbitrary arbitrary, Shrink shrink, F f) {
            this.val$ad = arbitrary;
            this.val$sd = shrink;
            this.val$f = f;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass13) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.test.Property$13$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$13$1.class */
        public class AnonymousClass1 implements F<B, F<C, Property>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            @Override // fj.F
            public F<C, Property> f(final B b) {
                return new F<C, Property>() { // from class: fj.test.Property.13.1.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Property f(Object obj) {
                        return f((C01261) obj);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // fj.F
                    public Property f(final C c) {
                        return Property.property(AnonymousClass13.this.val$ad, AnonymousClass13.this.val$sd, new F<D, Property>() { // from class: fj.test.Property.13.1.1.1
                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Property f(Object obj) {
                                return f((C01271) obj);
                            }

                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // fj.F
                            public Property f(D d) {
                                return (Property) ((F) ((F) ((F) AnonymousClass13.this.val$f.f(AnonymousClass1.this.val$a)).f(b)).f(c)).f(d);
                            }
                        });
                    }
                };
            }
        }

        @Override // fj.F
        public F<B, F<C, Property>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, F<A, F<B, F<C, F<D, Property>>>> f) {
        return property(aa, ab, ac, sa, sb, sc, new AnonymousClass13(ad, sd, f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Property$14  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$14.class */
    public static class AnonymousClass14 implements F<A, F<B, F<C, Property>>> {
        final /* synthetic */ Arbitrary val$ad;
        final /* synthetic */ F val$f;

        AnonymousClass14(Arbitrary arbitrary, F f) {
            this.val$ad = arbitrary;
            this.val$f = f;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass14) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.test.Property$14$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$14$1.class */
        public class AnonymousClass1 implements F<B, F<C, Property>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            @Override // fj.F
            public F<C, Property> f(final B b) {
                return new F<C, Property>() { // from class: fj.test.Property.14.1.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Property f(Object obj) {
                        return f((C01281) obj);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // fj.F
                    public Property f(final C c) {
                        return Property.property(AnonymousClass14.this.val$ad, new F<D, Property>() { // from class: fj.test.Property.14.1.1.1
                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Property f(Object obj) {
                                return f((C01291) obj);
                            }

                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // fj.F
                            public Property f(D d) {
                                return (Property) ((F) ((F) ((F) AnonymousClass14.this.val$f.f(AnonymousClass1.this.val$a)).f(b)).f(c)).f(d);
                            }
                        });
                    }
                };
            }
        }

        @Override // fj.F
        public F<B, F<C, Property>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, F<A, F<B, F<C, F<D, Property>>>> f) {
        return property(aa, ab, ac, new AnonymousClass14(ad, f));
    }

    public static <A, B, C, D> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, F4<A, B, C, D, Property> f) {
        return property(aa, ab, ac, ad, sa, sb, sc, sd, Function.curry(f));
    }

    public static <A, B, C, D> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, F4<A, B, C, D, Property> f) {
        return property(aa, ab, ac, ad, Function.curry(f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Property$15  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$15.class */
    public static class AnonymousClass15 implements F<A, F<B, F<C, F<D, Property>>>> {
        final /* synthetic */ Arbitrary val$ae;
        final /* synthetic */ Shrink val$se;
        final /* synthetic */ F val$f;

        AnonymousClass15(Arbitrary arbitrary, Shrink shrink, F f) {
            this.val$ae = arbitrary;
            this.val$se = shrink;
            this.val$f = f;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass15) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.test.Property$15$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$15$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, Property>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.test.Property$15$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$15$1$1.class */
            public class C01301 implements F<C, F<D, Property>> {
                final /* synthetic */ Object val$b;

                C01301(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C01301) obj);
                }

                @Override // fj.F
                public F<D, Property> f(final C c) {
                    return new F<D, Property>() { // from class: fj.test.Property.15.1.1.1
                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Property f(Object obj) {
                            return f((C01311) obj);
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // fj.F
                        public Property f(final D d) {
                            return Property.property(AnonymousClass15.this.val$ae, AnonymousClass15.this.val$se, new F<E, Property>() { // from class: fj.test.Property.15.1.1.1.1
                                @Override // fj.F
                                public /* bridge */ /* synthetic */ Property f(Object obj) {
                                    return f((C01321) obj);
                                }

                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // fj.F
                                public Property f(E e) {
                                    return (Property) ((F) ((F) ((F) ((F) AnonymousClass15.this.val$f.f(AnonymousClass1.this.val$a)).f(C01301.this.val$b)).f(c)).f(d)).f(e);
                                }
                            });
                        }
                    };
                }
            }

            @Override // fj.F
            public F<C, F<D, Property>> f(B b) {
                return new C01301(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, Property>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, F<A, F<B, F<C, F<D, F<E, Property>>>>> f) {
        return property(aa, ab, ac, ad, sa, sb, sc, sd, new AnonymousClass15(ae, se, f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Property$16  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$16.class */
    public static class AnonymousClass16 implements F<A, F<B, F<C, F<D, Property>>>> {
        final /* synthetic */ Arbitrary val$ae;
        final /* synthetic */ F val$f;

        AnonymousClass16(Arbitrary arbitrary, F f) {
            this.val$ae = arbitrary;
            this.val$f = f;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass16) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.test.Property$16$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$16$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, Property>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.test.Property$16$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$16$1$1.class */
            public class C01331 implements F<C, F<D, Property>> {
                final /* synthetic */ Object val$b;

                C01331(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C01331) obj);
                }

                @Override // fj.F
                public F<D, Property> f(final C c) {
                    return new F<D, Property>() { // from class: fj.test.Property.16.1.1.1
                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Property f(Object obj) {
                            return f((C01341) obj);
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // fj.F
                        public Property f(final D d) {
                            return Property.property(AnonymousClass16.this.val$ae, new F<E, Property>() { // from class: fj.test.Property.16.1.1.1.1
                                @Override // fj.F
                                public /* bridge */ /* synthetic */ Property f(Object obj) {
                                    return f((C01351) obj);
                                }

                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // fj.F
                                public Property f(E e) {
                                    return (Property) ((F) ((F) ((F) ((F) AnonymousClass16.this.val$f.f(AnonymousClass1.this.val$a)).f(C01331.this.val$b)).f(c)).f(d)).f(e);
                                }
                            });
                        }
                    };
                }
            }

            @Override // fj.F
            public F<C, F<D, Property>> f(B b) {
                return new C01331(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, Property>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, F<A, F<B, F<C, F<D, F<E, Property>>>>> f) {
        return property(aa, ab, ac, ad, new AnonymousClass16(ae, f));
    }

    public static <A, B, C, D, E> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, F5<A, B, C, D, E, Property> f) {
        return property(aa, ab, ac, ad, ae, sa, sb, sc, sd, se, Function.curry(f));
    }

    public static <A, B, C, D, E> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, F5<A, B, C, D, E, Property> f) {
        return property(aa, ab, ac, ad, ae, Function.curry(f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Property$17  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$17.class */
    public static class AnonymousClass17 implements F<A, F<B, F<C, F<D, F<E, Property>>>>> {
        final /* synthetic */ Arbitrary val$af;
        final /* synthetic */ Shrink val$sf;
        final /* synthetic */ F val$f;

        AnonymousClass17(Arbitrary arbitrary, Shrink shrink, F f) {
            this.val$af = arbitrary;
            this.val$sf = shrink;
            this.val$f = f;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass17) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.test.Property$17$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$17$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, F<E, Property>>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.test.Property$17$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$17$1$1.class */
            public class C01361 implements F<C, F<D, F<E, Property>>> {
                final /* synthetic */ Object val$b;

                C01361(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C01361) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.test.Property$17$1$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$17$1$1$1.class */
                public class C01371 implements F<D, F<E, Property>> {
                    final /* synthetic */ Object val$c;

                    C01371(Object obj) {
                        this.val$c = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C01371) obj);
                    }

                    @Override // fj.F
                    public F<E, Property> f(final D d) {
                        return new F<E, Property>() { // from class: fj.test.Property.17.1.1.1.1
                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Property f(Object obj) {
                                return f((C01381) obj);
                            }

                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // fj.F
                            public Property f(final E e) {
                                return Property.property(AnonymousClass17.this.val$af, AnonymousClass17.this.val$sf, new F<F$, Property>() { // from class: fj.test.Property.17.1.1.1.1.1
                                    @Override // fj.F
                                    public /* bridge */ /* synthetic */ Property f(Object obj) {
                                        return f((C01391) obj);
                                    }

                                    /* JADX WARN: Can't rename method to resolve collision */
                                    @Override // fj.F
                                    public Property f(F$ f$) {
                                        return (Property) ((F) ((F) ((F) ((F) ((F) AnonymousClass17.this.val$f.f(AnonymousClass1.this.val$a)).f(C01361.this.val$b)).f(C01371.this.val$c)).f(d)).f(e)).f(f$);
                                    }
                                });
                            }
                        };
                    }
                }

                @Override // fj.F
                public F<D, F<E, Property>> f(C c) {
                    return new C01371(c);
                }
            }

            @Override // fj.F
            public F<C, F<D, F<E, Property>>> f(B b) {
                return new C01361(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, F<E, Property>>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E, F$> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, Shrink<F$> sf, F<A, F<B, F<C, F<D, F<E, F<F$, Property>>>>>> f) {
        return property(aa, ab, ac, ad, ae, sa, sb, sc, sd, se, new AnonymousClass17(af, sf, f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Property$18  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$18.class */
    public static class AnonymousClass18 implements F<A, F<B, F<C, F<D, F<E, Property>>>>> {
        final /* synthetic */ Arbitrary val$af;
        final /* synthetic */ F val$f;

        AnonymousClass18(Arbitrary arbitrary, F f) {
            this.val$af = arbitrary;
            this.val$f = f;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass18) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.test.Property$18$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$18$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, F<E, Property>>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.test.Property$18$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$18$1$1.class */
            public class C01401 implements F<C, F<D, F<E, Property>>> {
                final /* synthetic */ Object val$b;

                C01401(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C01401) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.test.Property$18$1$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$18$1$1$1.class */
                public class C01411 implements F<D, F<E, Property>> {
                    final /* synthetic */ Object val$c;

                    C01411(Object obj) {
                        this.val$c = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C01411) obj);
                    }

                    @Override // fj.F
                    public F<E, Property> f(final D d) {
                        return new F<E, Property>() { // from class: fj.test.Property.18.1.1.1.1
                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Property f(Object obj) {
                                return f((C01421) obj);
                            }

                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // fj.F
                            public Property f(final E e) {
                                return Property.property(AnonymousClass18.this.val$af, new F<F$, Property>() { // from class: fj.test.Property.18.1.1.1.1.1
                                    @Override // fj.F
                                    public /* bridge */ /* synthetic */ Property f(Object obj) {
                                        return f((C01431) obj);
                                    }

                                    /* JADX WARN: Can't rename method to resolve collision */
                                    @Override // fj.F
                                    public Property f(F$ f$) {
                                        return (Property) ((F) ((F) ((F) ((F) ((F) AnonymousClass18.this.val$f.f(AnonymousClass1.this.val$a)).f(C01401.this.val$b)).f(C01411.this.val$c)).f(d)).f(e)).f(f$);
                                    }
                                });
                            }
                        };
                    }
                }

                @Override // fj.F
                public F<D, F<E, Property>> f(C c) {
                    return new C01411(c);
                }
            }

            @Override // fj.F
            public F<C, F<D, F<E, Property>>> f(B b) {
                return new C01401(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, F<E, Property>>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E, F$> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, F<A, F<B, F<C, F<D, F<E, F<F$, Property>>>>>> f) {
        return property(aa, ab, ac, ad, ae, new AnonymousClass18(af, f));
    }

    public static <A, B, C, D, E, F$> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, Shrink<F$> sf, F6<A, B, C, D, E, F$, Property> f) {
        return property(aa, ab, ac, ad, ae, af, sa, sb, sc, sd, se, sf, Function.curry(f));
    }

    public static <A, B, C, D, E, F$> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, F6<A, B, C, D, E, F$, Property> f) {
        return property(aa, ab, ac, ad, ae, af, Function.curry(f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Property$19  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$19.class */
    public static class AnonymousClass19 implements F<A, F<B, F<C, F<D, F<E, F<F$, Property>>>>>> {
        final /* synthetic */ Arbitrary val$ag;
        final /* synthetic */ Shrink val$sg;
        final /* synthetic */ F val$f;

        AnonymousClass19(Arbitrary arbitrary, Shrink shrink, F f) {
            this.val$ag = arbitrary;
            this.val$sg = shrink;
            this.val$f = f;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass19) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.test.Property$19$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$19$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, Property>>>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.test.Property$19$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$19$1$1.class */
            public class C01441 implements F<C, F<D, F<E, F<F$, Property>>>> {
                final /* synthetic */ Object val$b;

                C01441(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C01441) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.test.Property$19$1$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$19$1$1$1.class */
                public class C01451 implements F<D, F<E, F<F$, Property>>> {
                    final /* synthetic */ Object val$c;

                    C01451(Object obj) {
                        this.val$c = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C01451) obj);
                    }

                    /* JADX INFO: Access modifiers changed from: package-private */
                    /* renamed from: fj.test.Property$19$1$1$1$1  reason: invalid class name and collision with other inner class name */
                    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$19$1$1$1$1.class */
                    public class C01461 implements F<E, F<F$, Property>> {
                        final /* synthetic */ Object val$d;

                        C01461(Object obj) {
                            this.val$d = obj;
                        }

                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C01461) obj);
                        }

                        @Override // fj.F
                        public F<F$, Property> f(final E e) {
                            return new F<F$, Property>() { // from class: fj.test.Property.19.1.1.1.1.1
                                @Override // fj.F
                                public /* bridge */ /* synthetic */ Property f(Object obj) {
                                    return f((C01471) obj);
                                }

                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // fj.F
                                public Property f(final F$ f$) {
                                    return Property.property(AnonymousClass19.this.val$ag, AnonymousClass19.this.val$sg, new F<G, Property>() { // from class: fj.test.Property.19.1.1.1.1.1.1
                                        @Override // fj.F
                                        public /* bridge */ /* synthetic */ Property f(Object obj) {
                                            return f((C01481) obj);
                                        }

                                        /* JADX WARN: Can't rename method to resolve collision */
                                        @Override // fj.F
                                        public Property f(G g) {
                                            return (Property) ((F) ((F) ((F) ((F) ((F) ((F) AnonymousClass19.this.val$f.f(AnonymousClass1.this.val$a)).f(C01441.this.val$b)).f(C01451.this.val$c)).f(C01461.this.val$d)).f(e)).f(f$)).f(g);
                                        }
                                    });
                                }
                            };
                        }
                    }

                    @Override // fj.F
                    public F<E, F<F$, Property>> f(D d) {
                        return new C01461(d);
                    }
                }

                @Override // fj.F
                public F<D, F<E, F<F$, Property>>> f(C c) {
                    return new C01451(c);
                }
            }

            @Override // fj.F
            public F<C, F<D, F<E, F<F$, Property>>>> f(B b) {
                return new C01441(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, F<E, F<F$, Property>>>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E, F$, G> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, Shrink<F$> sf, Shrink<G> sg, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>>> f) {
        return property(aa, ab, ac, ad, ae, af, sa, sb, sc, sd, se, sf, new AnonymousClass19(ag, sg, f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Property$20  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$20.class */
    public static class AnonymousClass20 implements F<A, F<B, F<C, F<D, F<E, F<F$, Property>>>>>> {
        final /* synthetic */ Arbitrary val$ag;
        final /* synthetic */ F val$f;

        AnonymousClass20(Arbitrary arbitrary, F f) {
            this.val$ag = arbitrary;
            this.val$f = f;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass20) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.test.Property$20$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$20$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, Property>>>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.test.Property$20$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$20$1$1.class */
            public class C01491 implements F<C, F<D, F<E, F<F$, Property>>>> {
                final /* synthetic */ Object val$b;

                C01491(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C01491) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.test.Property$20$1$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$20$1$1$1.class */
                public class C01501 implements F<D, F<E, F<F$, Property>>> {
                    final /* synthetic */ Object val$c;

                    C01501(Object obj) {
                        this.val$c = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C01501) obj);
                    }

                    /* JADX INFO: Access modifiers changed from: package-private */
                    /* renamed from: fj.test.Property$20$1$1$1$1  reason: invalid class name and collision with other inner class name */
                    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$20$1$1$1$1.class */
                    public class C01511 implements F<E, F<F$, Property>> {
                        final /* synthetic */ Object val$d;

                        C01511(Object obj) {
                            this.val$d = obj;
                        }

                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C01511) obj);
                        }

                        @Override // fj.F
                        public F<F$, Property> f(final E e) {
                            return new F<F$, Property>() { // from class: fj.test.Property.20.1.1.1.1.1
                                @Override // fj.F
                                public /* bridge */ /* synthetic */ Property f(Object obj) {
                                    return f((C01521) obj);
                                }

                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // fj.F
                                public Property f(final F$ f$) {
                                    return Property.property(AnonymousClass20.this.val$ag, new F<G, Property>() { // from class: fj.test.Property.20.1.1.1.1.1.1
                                        @Override // fj.F
                                        public /* bridge */ /* synthetic */ Property f(Object obj) {
                                            return f((C01531) obj);
                                        }

                                        /* JADX WARN: Can't rename method to resolve collision */
                                        @Override // fj.F
                                        public Property f(G g) {
                                            return (Property) ((F) ((F) ((F) ((F) ((F) ((F) AnonymousClass20.this.val$f.f(AnonymousClass1.this.val$a)).f(C01491.this.val$b)).f(C01501.this.val$c)).f(C01511.this.val$d)).f(e)).f(f$)).f(g);
                                        }
                                    });
                                }
                            };
                        }
                    }

                    @Override // fj.F
                    public F<E, F<F$, Property>> f(D d) {
                        return new C01511(d);
                    }
                }

                @Override // fj.F
                public F<D, F<E, F<F$, Property>>> f(C c) {
                    return new C01501(c);
                }
            }

            @Override // fj.F
            public F<C, F<D, F<E, F<F$, Property>>>> f(B b) {
                return new C01491(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, F<E, F<F$, Property>>>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E, F$, G> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>>> f) {
        return property(aa, ab, ac, ad, ae, af, new AnonymousClass20(ag, f));
    }

    public static <A, B, C, D, E, F$, G> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, Shrink<F$> sf, Shrink<G> sg, F7<A, B, C, D, E, F$, G, Property> f) {
        return property(aa, ab, ac, ad, ae, af, ag, sa, sb, sc, sd, se, sf, sg, Function.curry(f));
    }

    public static <A, B, C, D, E, F$, G> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, F7<A, B, C, D, E, F$, G, Property> f) {
        return property(aa, ab, ac, ad, ae, af, ag, Function.curry(f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Property$21  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$21.class */
    public static class AnonymousClass21 implements F<A, F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>>> {
        final /* synthetic */ Arbitrary val$ah;
        final /* synthetic */ Shrink val$sh;
        final /* synthetic */ F val$f;

        AnonymousClass21(Arbitrary arbitrary, Shrink shrink, F f) {
            this.val$ah = arbitrary;
            this.val$sh = shrink;
            this.val$f = f;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass21) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.test.Property$21$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$21$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.test.Property$21$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$21$1$1.class */
            public class C01541 implements F<C, F<D, F<E, F<F$, F<G, Property>>>>> {
                final /* synthetic */ Object val$b;

                C01541(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C01541) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.test.Property$21$1$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$21$1$1$1.class */
                public class C01551 implements F<D, F<E, F<F$, F<G, Property>>>> {
                    final /* synthetic */ Object val$c;

                    C01551(Object obj) {
                        this.val$c = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C01551) obj);
                    }

                    /* JADX INFO: Access modifiers changed from: package-private */
                    /* renamed from: fj.test.Property$21$1$1$1$1  reason: invalid class name and collision with other inner class name */
                    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$21$1$1$1$1.class */
                    public class C01561 implements F<E, F<F$, F<G, Property>>> {
                        final /* synthetic */ Object val$d;

                        C01561(Object obj) {
                            this.val$d = obj;
                        }

                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C01561) obj);
                        }

                        /* JADX INFO: Access modifiers changed from: package-private */
                        /* renamed from: fj.test.Property$21$1$1$1$1$1  reason: invalid class name and collision with other inner class name */
                        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$21$1$1$1$1$1.class */
                        public class C01571 implements F<F$, F<G, Property>> {
                            final /* synthetic */ Object val$e;

                            C01571(Object obj) {
                                this.val$e = obj;
                            }

                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((C01571) obj);
                            }

                            @Override // fj.F
                            public F<G, Property> f(final F$ f$) {
                                return new F<G, Property>() { // from class: fj.test.Property.21.1.1.1.1.1.1
                                    @Override // fj.F
                                    public /* bridge */ /* synthetic */ Property f(Object obj) {
                                        return f((C01581) obj);
                                    }

                                    /* JADX WARN: Can't rename method to resolve collision */
                                    @Override // fj.F
                                    public Property f(final G g) {
                                        return Property.property(AnonymousClass21.this.val$ah, AnonymousClass21.this.val$sh, new F<H, Property>() { // from class: fj.test.Property.21.1.1.1.1.1.1.1
                                            @Override // fj.F
                                            public /* bridge */ /* synthetic */ Property f(Object obj) {
                                                return f((C01591) obj);
                                            }

                                            /* JADX WARN: Can't rename method to resolve collision */
                                            @Override // fj.F
                                            public Property f(H h) {
                                                return (Property) ((F) ((F) ((F) ((F) ((F) ((F) ((F) AnonymousClass21.this.val$f.f(AnonymousClass1.this.val$a)).f(C01541.this.val$b)).f(C01551.this.val$c)).f(C01561.this.val$d)).f(C01571.this.val$e)).f(f$)).f(g)).f(h);
                                            }
                                        });
                                    }
                                };
                            }
                        }

                        @Override // fj.F
                        public F<F$, F<G, Property>> f(E e) {
                            return new C01571(e);
                        }
                    }

                    @Override // fj.F
                    public F<E, F<F$, F<G, Property>>> f(D d) {
                        return new C01561(d);
                    }
                }

                @Override // fj.F
                public F<D, F<E, F<F$, F<G, Property>>>> f(C c) {
                    return new C01551(c);
                }
            }

            @Override // fj.F
            public F<C, F<D, F<E, F<F$, F<G, Property>>>>> f(B b) {
                return new C01541(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E, F$, G, H> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, Arbitrary<H> ah, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, Shrink<F$> sf, Shrink<G> sg, Shrink<H> sh, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, Property>>>>>>>> f) {
        return property(aa, ab, ac, ad, ae, af, ag, sa, sb, sc, sd, se, sf, sg, new AnonymousClass21(ah, sh, f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.test.Property$22  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$22.class */
    public static class AnonymousClass22 implements F<A, F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>>> {
        final /* synthetic */ Arbitrary val$ah;
        final /* synthetic */ F val$f;

        AnonymousClass22(Arbitrary arbitrary, F f) {
            this.val$ah = arbitrary;
            this.val$f = f;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass22) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.test.Property$22$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$22$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.test.Property$22$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$22$1$1.class */
            public class C01601 implements F<C, F<D, F<E, F<F$, F<G, Property>>>>> {
                final /* synthetic */ Object val$b;

                C01601(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C01601) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.test.Property$22$1$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$22$1$1$1.class */
                public class C01611 implements F<D, F<E, F<F$, F<G, Property>>>> {
                    final /* synthetic */ Object val$c;

                    C01611(Object obj) {
                        this.val$c = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C01611) obj);
                    }

                    /* JADX INFO: Access modifiers changed from: package-private */
                    /* renamed from: fj.test.Property$22$1$1$1$1  reason: invalid class name and collision with other inner class name */
                    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$22$1$1$1$1.class */
                    public class C01621 implements F<E, F<F$, F<G, Property>>> {
                        final /* synthetic */ Object val$d;

                        C01621(Object obj) {
                            this.val$d = obj;
                        }

                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C01621) obj);
                        }

                        /* JADX INFO: Access modifiers changed from: package-private */
                        /* renamed from: fj.test.Property$22$1$1$1$1$1  reason: invalid class name and collision with other inner class name */
                        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Property$22$1$1$1$1$1.class */
                        public class C01631 implements F<F$, F<G, Property>> {
                            final /* synthetic */ Object val$e;

                            C01631(Object obj) {
                                this.val$e = obj;
                            }

                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((C01631) obj);
                            }

                            @Override // fj.F
                            public F<G, Property> f(final F$ f$) {
                                return new F<G, Property>() { // from class: fj.test.Property.22.1.1.1.1.1.1
                                    @Override // fj.F
                                    public /* bridge */ /* synthetic */ Property f(Object obj) {
                                        return f((C01641) obj);
                                    }

                                    /* JADX WARN: Can't rename method to resolve collision */
                                    @Override // fj.F
                                    public Property f(final G g) {
                                        return Property.property(AnonymousClass22.this.val$ah, new F<H, Property>() { // from class: fj.test.Property.22.1.1.1.1.1.1.1
                                            @Override // fj.F
                                            public /* bridge */ /* synthetic */ Property f(Object obj) {
                                                return f((C01651) obj);
                                            }

                                            /* JADX WARN: Can't rename method to resolve collision */
                                            @Override // fj.F
                                            public Property f(H h) {
                                                return (Property) ((F) ((F) ((F) ((F) ((F) ((F) ((F) AnonymousClass22.this.val$f.f(AnonymousClass1.this.val$a)).f(C01601.this.val$b)).f(C01611.this.val$c)).f(C01621.this.val$d)).f(C01631.this.val$e)).f(f$)).f(g)).f(h);
                                            }
                                        });
                                    }
                                };
                            }
                        }

                        @Override // fj.F
                        public F<F$, F<G, Property>> f(E e) {
                            return new C01631(e);
                        }
                    }

                    @Override // fj.F
                    public F<E, F<F$, F<G, Property>>> f(D d) {
                        return new C01621(d);
                    }
                }

                @Override // fj.F
                public F<D, F<E, F<F$, F<G, Property>>>> f(C c) {
                    return new C01611(c);
                }
            }

            @Override // fj.F
            public F<C, F<D, F<E, F<F$, F<G, Property>>>>> f(B b) {
                return new C01601(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E, F$, G, H> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, Arbitrary<H> ah, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, Property>>>>>>>> f) {
        return property(aa, ab, ac, ad, ae, af, ag, new AnonymousClass22(ah, f));
    }

    public static <A, B, C, D, E, F$, G, H> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, Arbitrary<H> ah, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, Shrink<F$> sf, Shrink<G> sg, Shrink<H> sh, F8<A, B, C, D, E, F$, G, H, Property> f) {
        return property(aa, ab, ac, ad, ae, af, ag, ah, sa, sb, sc, sd, se, sf, sg, sh, Function.curry(f));
    }

    public static <A, B, C, D, E, F$, G, H> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, Arbitrary<H> ah, F8<A, B, C, D, E, F$, G, H, Property> f) {
        return property(aa, ab, ac, ad, ae, af, ag, ah, Function.curry(f));
    }

    public static Property exception(P1<Property> p) {
        try {
            return p._1();
        } catch (Throwable t) {
            return new Property(new F<Integer, F<Rand, Result>>() { // from class: fj.test.Property.23
                @Override // fj.F
                public F<Rand, Result> f(Integer i) {
                    return new F<Rand, Result>() { // from class: fj.test.Property.23.1
                        @Override // fj.F
                        public Result f(Rand r) {
                            return Result.exception(List.nil(), t);
                        }
                    };
                }
            });
        }
    }
}
