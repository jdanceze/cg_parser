package fj;

import fj.data.Option;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function.class */
public final class Function {
    private Function() {
        throw new UnsupportedOperationException();
    }

    public static <A, B> F<F<A, B>, B> apply(final A a) {
        return new F<F<A, B>, B>() { // from class: fj.Function.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((F<A, Object>) obj);
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            public B f(F<A, B> k) {
                return k.f(a);
            }
        };
    }

    public static <A, B, C> F<F<B, C>, F<F<A, B>, F<A, C>>> compose() {
        return new F<F<B, C>, F<F<A, B>, F<A, C>>>() { // from class: fj.Function.2
            @Override // fj.F
            public F<F<A, B>, F<A, C>> f(final F<B, C> f) {
                return new F<F<A, B>, F<A, C>>() { // from class: fj.Function.2.1
                    @Override // fj.F
                    public F<A, C> f(F<A, B> g) {
                        return Function.compose(f, g);
                    }
                };
            }
        };
    }

    public static <A, B, C> F<A, C> compose(final F<B, C> f, final F<A, B> g) {
        return new F<A, C>() { // from class: fj.Function.3
            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.F
            public C f(A a) {
                return F.this.f(g.f(a));
            }
        };
    }

    public static <A, B, C, D> F<A, F<B, D>> compose2(final F<C, D> f, final F<A, F<B, C>> g) {
        return new F<A, F<B, D>>() { // from class: fj.Function.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass4) obj);
            }

            @Override // fj.F
            public F<B, D> f(final A a) {
                return new F<B, D>() { // from class: fj.Function.4.1
                    /* JADX WARN: Type inference failed for: r0v3, types: [D, java.lang.Object] */
                    @Override // fj.F
                    public D f(B b) {
                        return F.this.f(((F) g.f(a)).f(b));
                    }
                };
            }
        };
    }

    public static <A, B, C> F<F<A, B>, F<F<B, C>, F<A, C>>> andThen() {
        return new F<F<A, B>, F<F<B, C>, F<A, C>>>() { // from class: fj.Function.5
            @Override // fj.F
            public F<F<B, C>, F<A, C>> f(final F<A, B> g) {
                return new F<F<B, C>, F<A, C>>() { // from class: fj.Function.5.1
                    @Override // fj.F
                    public F<A, C> f(F<B, C> f) {
                        return Function.andThen(g, f);
                    }
                };
            }
        };
    }

    public static <A, B, C> F<A, C> andThen(final F<A, B> g, final F<B, C> f) {
        return new F<A, C>() { // from class: fj.Function.6
            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.F
            public C f(A a) {
                return F.this.f(g.f(a));
            }
        };
    }

    public static <A> F<A, A> identity() {
        return new F<A, A>() { // from class: fj.Function.7
            @Override // fj.F
            public A f(A a) {
                return a;
            }
        };
    }

    public static <A, B> F<B, F<A, B>> constant() {
        return new F<B, F<A, B>>() { // from class: fj.Function.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass8) obj);
            }

            @Override // fj.F
            public F<A, B> f(B b) {
                return Function.constant(b);
            }
        };
    }

    public static <A, B> F<A, B> constant(final B b) {
        return new F<A, B>() { // from class: fj.Function.9
            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            @Override // fj.F
            public B f(A a) {
                return b;
            }
        };
    }

    public static <A, B> F<A, B> vary(final F<? super A, ? extends B> f) {
        return new F<A, B>() { // from class: fj.Function.10
            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.F
            public B f(A a) {
                return F.this.f(a);
            }
        };
    }

    public static <C, A extends C, B, D extends B> F<F<C, D>, F<A, B>> vary() {
        return (F<F<C, D>, F<A, B>>) new F<F<C, D>, F<A, B>>() { // from class: fj.Function.11
            @Override // fj.F
            public F<A, B> f(F<C, D> f) {
                return Function.vary(f);
            }
        };
    }

    public static <A, B, C> F<F<A, F<B, C>>, F<B, F<A, C>>> flip() {
        return new F<F<A, F<B, C>>, F<B, F<A, C>>>() { // from class: fj.Function.12
            @Override // fj.F
            public F<B, F<A, C>> f(F<A, F<B, C>> f) {
                return Function.flip(f);
            }
        };
    }

    public static <A, B, C> F<B, F<A, C>> flip(final F<A, F<B, C>> f) {
        return new F<B, F<A, C>>() { // from class: fj.Function.13
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass13) obj);
            }

            @Override // fj.F
            public F<A, C> f(final B b) {
                return new F<A, C>() { // from class: fj.Function.13.1
                    /* JADX WARN: Type inference failed for: r0v5, types: [C, java.lang.Object] */
                    @Override // fj.F
                    public C f(A a) {
                        return ((F) F.this.f(a)).f(b);
                    }
                };
            }
        };
    }

    public static <A, B, C> F2<B, A, C> flip(final F2<A, B, C> f) {
        return new F2<B, A, C>() { // from class: fj.Function.14
            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.F2
            public C f(B b, A a) {
                return F2.this.f(a, b);
            }
        };
    }

    public static <A, B, C> F<F2<A, B, C>, F2<B, A, C>> flip2() {
        return new F<F2<A, B, C>, F2<B, A, C>>() { // from class: fj.Function.15
            @Override // fj.F
            public F2<B, A, C> f(F2<A, B, C> f) {
                return Function.flip(f);
            }
        };
    }

    public static <A, B> F<A, Option<B>> nullable(final F<A, B> f) {
        return new F<A, Option<B>>() { // from class: fj.Function.16
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass16) obj);
            }

            @Override // fj.F
            public Option<B> f(A a) {
                return a == 0 ? Option.none() : Option.some(F.this.f(a));
            }
        };
    }

    public static <A, B, C> F<A, F<B, C>> curry(final F2<A, B, C> f) {
        return new F<A, F<B, C>>() { // from class: fj.Function.17
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass17) obj);
            }

            @Override // fj.F
            public F<B, C> f(final A a) {
                return new F<B, C>() { // from class: fj.Function.17.1
                    /* JADX WARN: Type inference failed for: r0v3, types: [C, java.lang.Object] */
                    @Override // fj.F
                    public C f(B b) {
                        return F2.this.f(a, b);
                    }
                };
            }
        };
    }

    public static <A, B, C> F<B, C> curry(F2<A, B, C> f, A a) {
        return (F) curry(f).f(a);
    }

    public static <A, B, C> F<F<A, F<B, C>>, F2<A, B, C>> uncurryF2() {
        return new F<F<A, F<B, C>>, F2<A, B, C>>() { // from class: fj.Function.18
            @Override // fj.F
            public F2<A, B, C> f(F<A, F<B, C>> f) {
                return Function.uncurryF2(f);
            }
        };
    }

    public static <A, B, C> F2<A, B, C> uncurryF2(final F<A, F<B, C>> f) {
        return new F2<A, B, C>() { // from class: fj.Function.19
            /* JADX WARN: Type inference failed for: r0v4, types: [C, java.lang.Object] */
            @Override // fj.F2
            public C f(A a, B b) {
                return ((F) F.this.f(a)).f(b);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.Function$20  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$20.class */
    public static class AnonymousClass20 implements F<A, F<B, F<C, D>>> {
        final /* synthetic */ F3 val$f;

        AnonymousClass20(F3 f3) {
            this.val$f = f3;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass20) obj);
        }

        @Override // fj.F
        public F<B, F<C, D>> f(final A a) {
            return new F<B, F<C, D>>() { // from class: fj.Function.20.1
                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                @Override // fj.F
                public F<C, D> f(final B b) {
                    return new F<C, D>() { // from class: fj.Function.20.1.1
                        /* JADX WARN: Type inference failed for: r0v4, types: [D, java.lang.Object] */
                        @Override // fj.F
                        public D f(C c) {
                            return AnonymousClass20.this.val$f.f(a, b, c);
                        }
                    };
                }
            };
        }
    }

    public static <A, B, C, D> F<A, F<B, F<C, D>>> curry(F3<A, B, C, D> f) {
        return new AnonymousClass20(f);
    }

    public static <A, B, C, D> F<B, F<C, D>> curry(F3<A, B, C, D> f, A a) {
        return (F) curry(f).f(a);
    }

    public static <A, B, C, D> F<C, D> curry(F3<A, B, C, D> f, A a, B b) {
        return (F) curry(f, a).f(b);
    }

    public static <A, B, C, D> F<F<A, F<B, F<C, D>>>, F3<A, B, C, D>> uncurryF3() {
        return new F<F<A, F<B, F<C, D>>>, F3<A, B, C, D>>() { // from class: fj.Function.21
            @Override // fj.F
            public F3<A, B, C, D> f(F<A, F<B, F<C, D>>> f) {
                return Function.uncurryF3(f);
            }
        };
    }

    public static <A, B, C, D> F3<A, B, C, D> uncurryF3(final F<A, F<B, F<C, D>>> f) {
        return new F3<A, B, C, D>() { // from class: fj.Function.22
            /* JADX WARN: Type inference failed for: r0v6, types: [D, java.lang.Object] */
            @Override // fj.F3
            public D f(A a, B b, C c) {
                return ((F) ((F) F.this.f(a)).f(b)).f(c);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.Function$23  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$23.class */
    public static class AnonymousClass23 implements F<A, F<B, F<C, F<D, E>>>> {
        final /* synthetic */ F4 val$f;

        AnonymousClass23(F4 f4) {
            this.val$f = f4;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass23) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.Function$23$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$23$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, E>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            @Override // fj.F
            public F<C, F<D, E>> f(final B b) {
                return new F<C, F<D, E>>() { // from class: fj.Function.23.1.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C00241) obj);
                    }

                    @Override // fj.F
                    public F<D, E> f(final C c) {
                        return new F<D, E>() { // from class: fj.Function.23.1.1.1
                            /* JADX WARN: Type inference failed for: r0v5, types: [E, java.lang.Object] */
                            @Override // fj.F
                            public E f(D d) {
                                return AnonymousClass23.this.val$f.f(AnonymousClass1.this.val$a, b, c, d);
                            }
                        };
                    }
                };
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, E>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E> F<A, F<B, F<C, F<D, E>>>> curry(F4<A, B, C, D, E> f) {
        return new AnonymousClass23(f);
    }

    public static <A, B, C, D, E> F<B, F<C, F<D, E>>> curry(F4<A, B, C, D, E> f, A a) {
        return (F) curry(f).f(a);
    }

    public static <A, B, C, D, E> F<C, F<D, E>> curry(F4<A, B, C, D, E> f, A a, B b) {
        return (F) ((F) curry(f).f(a)).f(b);
    }

    public static <A, B, C, D, E> F<D, E> curry(F4<A, B, C, D, E> f, A a, B b, C c) {
        return (F) ((F) ((F) curry(f).f(a)).f(b)).f(c);
    }

    public static <A, B, C, D, E> F<F<A, F<B, F<C, F<D, E>>>>, F4<A, B, C, D, E>> uncurryF4() {
        return new F<F<A, F<B, F<C, F<D, E>>>>, F4<A, B, C, D, E>>() { // from class: fj.Function.24
            @Override // fj.F
            public F4<A, B, C, D, E> f(F<A, F<B, F<C, F<D, E>>>> f) {
                return Function.uncurryF4(f);
            }
        };
    }

    public static <A, B, C, D, E> F4<A, B, C, D, E> uncurryF4(final F<A, F<B, F<C, F<D, E>>>> f) {
        return new F4<A, B, C, D, E>() { // from class: fj.Function.25
            /* JADX WARN: Type inference failed for: r0v8, types: [E, java.lang.Object] */
            @Override // fj.F4
            public E f(A a, B b, C c, D d) {
                return ((F) ((F) ((F) F.this.f(a)).f(b)).f(c)).f(d);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.Function$26  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$26.class */
    public static class AnonymousClass26 implements F<A, F<B, F<C, F<D, F<E, F$>>>>> {
        final /* synthetic */ F5 val$f;

        AnonymousClass26(F5 f5) {
            this.val$f = f5;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass26) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.Function$26$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$26$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F$>>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.Function$26$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$26$1$1.class */
            public class C00261 implements F<C, F<D, F<E, F$>>> {
                final /* synthetic */ Object val$b;

                C00261(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C00261) obj);
                }

                @Override // fj.F
                public F<D, F<E, F$>> f(final C c) {
                    return new F<D, F<E, F$>>() { // from class: fj.Function.26.1.1.1
                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C00271) obj);
                        }

                        @Override // fj.F
                        public F<E, F$> f(final D d) {
                            return new F<E, F$>() { // from class: fj.Function.26.1.1.1.1
                                /* JADX WARN: Type inference failed for: r0v6, types: [java.lang.Object, F$] */
                                @Override // fj.F
                                public F$ f(E e) {
                                    return AnonymousClass26.this.val$f.f(AnonymousClass1.this.val$a, C00261.this.val$b, c, d, e);
                                }
                            };
                        }
                    };
                }
            }

            @Override // fj.F
            public F<C, F<D, F<E, F$>>> f(B b) {
                return new C00261(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, F<E, F$>>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E, F$> F<A, F<B, F<C, F<D, F<E, F$>>>>> curry(F5<A, B, C, D, E, F$> f) {
        return new AnonymousClass26(f);
    }

    public static <A, B, C, D, E, F$> F<B, F<C, F<D, F<E, F$>>>> curry(F5<A, B, C, D, E, F$> f, A a) {
        return (F) curry(f).f(a);
    }

    public static <A, B, C, D, E, F$> F<C, F<D, F<E, F$>>> curry(F5<A, B, C, D, E, F$> f, A a, B b) {
        return (F) ((F) curry(f).f(a)).f(b);
    }

    public static <A, B, C, D, E, F$> F<D, F<E, F$>> curry(F5<A, B, C, D, E, F$> f, A a, B b, C c) {
        return (F) ((F) ((F) curry(f).f(a)).f(b)).f(c);
    }

    public static <A, B, C, D, E, F$> F<E, F$> curry(F5<A, B, C, D, E, F$> f, A a, B b, C c, D d) {
        return (F) ((F) ((F) ((F) curry(f).f(a)).f(b)).f(c)).f(d);
    }

    public static <A, B, C, D, E, F$> F<F<A, F<B, F<C, F<D, F<E, F$>>>>>, F5<A, B, C, D, E, F$>> uncurryF5() {
        return new F<F<A, F<B, F<C, F<D, F<E, F$>>>>>, F5<A, B, C, D, E, F$>>() { // from class: fj.Function.27
            @Override // fj.F
            public F5<A, B, C, D, E, F$> f(F<A, F<B, F<C, F<D, F<E, F$>>>>> f) {
                return Function.uncurryF5(f);
            }
        };
    }

    public static <A, B, C, D, E, F$> F5<A, B, C, D, E, F$> uncurryF5(final F<A, F<B, F<C, F<D, F<E, F$>>>>> f) {
        return new F5<A, B, C, D, E, F$>() { // from class: fj.Function.28
            /* JADX WARN: Type inference failed for: r0v10, types: [java.lang.Object, F$] */
            @Override // fj.F5
            public F$ f(A a, B b, C c, D d, E e) {
                return ((F) ((F) ((F) ((F) F.this.f(a)).f(b)).f(c)).f(d)).f(e);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.Function$29  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$29.class */
    public static class AnonymousClass29 implements F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> {
        final /* synthetic */ F6 val$f;

        AnonymousClass29(F6 f6) {
            this.val$f = f6;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass29) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.Function$29$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$29$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, G>>>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.Function$29$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$29$1$1.class */
            public class C00291 implements F<C, F<D, F<E, F<F$, G>>>> {
                final /* synthetic */ Object val$b;

                C00291(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C00291) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.Function$29$1$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$29$1$1$1.class */
                public class C00301 implements F<D, F<E, F<F$, G>>> {
                    final /* synthetic */ Object val$c;

                    C00301(Object obj) {
                        this.val$c = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C00301) obj);
                    }

                    @Override // fj.F
                    public F<E, F<F$, G>> f(final D d) {
                        return new F<E, F<F$, G>>() { // from class: fj.Function.29.1.1.1.1
                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((C00311) obj);
                            }

                            @Override // fj.F
                            public F<F$, G> f(final E e) {
                                return new F<F$, G>() { // from class: fj.Function.29.1.1.1.1.1
                                    /* JADX WARN: Type inference failed for: r0v7, types: [G, java.lang.Object] */
                                    @Override // fj.F
                                    public G f(F$ f$) {
                                        return AnonymousClass29.this.val$f.f(AnonymousClass1.this.val$a, C00291.this.val$b, C00301.this.val$c, d, e, f$);
                                    }
                                };
                            }
                        };
                    }
                }

                @Override // fj.F
                public F<D, F<E, F<F$, G>>> f(C c) {
                    return new C00301(c);
                }
            }

            @Override // fj.F
            public F<C, F<D, F<E, F<F$, G>>>> f(B b) {
                return new C00291(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, F<E, F<F$, G>>>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E, F$, G> F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> curry(F6<A, B, C, D, E, F$, G> f) {
        return new AnonymousClass29(f);
    }

    public static <A, B, C, D, E, F$, G> F<F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>>, F6<A, B, C, D, E, F$, G>> uncurryF6() {
        return new F<F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>>, F6<A, B, C, D, E, F$, G>>() { // from class: fj.Function.30
            @Override // fj.F
            public F6<A, B, C, D, E, F$, G> f(F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f) {
                return Function.uncurryF6(f);
            }
        };
    }

    public static <A, B, C, D, E, F$, G> F6<A, B, C, D, E, F$, G> uncurryF6(final F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f) {
        return new F6<A, B, C, D, E, F$, G>() { // from class: fj.Function.31
            /* JADX WARN: Type inference failed for: r0v12, types: [G, java.lang.Object] */
            @Override // fj.F6
            public G f(A a, B b, C c, D d, E e, F$ f$) {
                return ((F) ((F) ((F) ((F) ((F) F.this.f(a)).f(b)).f(c)).f(d)).f(e)).f(f$);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.Function$32  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$32.class */
    public static class AnonymousClass32 implements F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> {
        final /* synthetic */ F7 val$f;

        AnonymousClass32(F7 f7) {
            this.val$f = f7;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass32) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.Function$32$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$32$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.Function$32$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$32$1$1.class */
            public class C00331 implements F<C, F<D, F<E, F<F$, F<G, H>>>>> {
                final /* synthetic */ Object val$b;

                C00331(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C00331) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.Function$32$1$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$32$1$1$1.class */
                public class C00341 implements F<D, F<E, F<F$, F<G, H>>>> {
                    final /* synthetic */ Object val$c;

                    C00341(Object obj) {
                        this.val$c = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C00341) obj);
                    }

                    /* JADX INFO: Access modifiers changed from: package-private */
                    /* renamed from: fj.Function$32$1$1$1$1  reason: invalid class name and collision with other inner class name */
                    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$32$1$1$1$1.class */
                    public class C00351 implements F<E, F<F$, F<G, H>>> {
                        final /* synthetic */ Object val$d;

                        C00351(Object obj) {
                            this.val$d = obj;
                        }

                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C00351) obj);
                        }

                        @Override // fj.F
                        public F<F$, F<G, H>> f(final E e) {
                            return new F<F$, F<G, H>>() { // from class: fj.Function.32.1.1.1.1.1
                                @Override // fj.F
                                public /* bridge */ /* synthetic */ Object f(Object obj) {
                                    return f((C00361) obj);
                                }

                                @Override // fj.F
                                public F<G, H> f(final F$ f$) {
                                    return new F<G, H>() { // from class: fj.Function.32.1.1.1.1.1.1
                                        /* JADX WARN: Type inference failed for: r0v8, types: [H, java.lang.Object] */
                                        @Override // fj.F
                                        public H f(G g) {
                                            return AnonymousClass32.this.val$f.f(AnonymousClass1.this.val$a, C00331.this.val$b, C00341.this.val$c, C00351.this.val$d, e, f$, g);
                                        }
                                    };
                                }
                            };
                        }
                    }

                    @Override // fj.F
                    public F<E, F<F$, F<G, H>>> f(D d) {
                        return new C00351(d);
                    }
                }

                @Override // fj.F
                public F<D, F<E, F<F$, F<G, H>>>> f(C c) {
                    return new C00341(c);
                }
            }

            @Override // fj.F
            public F<C, F<D, F<E, F<F$, F<G, H>>>>> f(B b) {
                return new C00331(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E, F$, G, H> F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> curry(F7<A, B, C, D, E, F$, G, H> f) {
        return new AnonymousClass32(f);
    }

    public static <A, B, C, D, E, F$, G, H> F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>> curry(F7<A, B, C, D, E, F$, G, H> f, A a) {
        return (F) curry(f).f(a);
    }

    public static <A, B, C, D, E, F$, G, H> F<C, F<D, F<E, F<F$, F<G, H>>>>> curry(F7<A, B, C, D, E, F$, G, H> f, A a, B b) {
        return (F) ((F) curry(f).f(a)).f(b);
    }

    public static <A, B, C, D, E, F$, G, H> F<D, F<E, F<F$, F<G, H>>>> curry(F7<A, B, C, D, E, F$, G, H> f, A a, B b, C c) {
        return (F) ((F) ((F) curry(f).f(a)).f(b)).f(c);
    }

    public static <A, B, C, D, E, F$, G, H> F<E, F<F$, F<G, H>>> curry(F7<A, B, C, D, E, F$, G, H> f, A a, B b, C c, D d) {
        return (F) ((F) ((F) ((F) curry(f).f(a)).f(b)).f(c)).f(d);
    }

    public static <A, B, C, D, E, F$, G, H> F<F$, F<G, H>> curry(F7<A, B, C, D, E, F$, G, H> f, A a, B b, C c, D d, E e) {
        return (F) ((F) ((F) ((F) ((F) curry(f).f(a)).f(b)).f(c)).f(d)).f(e);
    }

    public static <A, B, C, D, E, F$, G, H> F<G, H> curry(F7<A, B, C, D, E, F$, G, H> f, A a, B b, C c, D d, E e, F$ f$) {
        return (F) ((F) ((F) ((F) ((F) ((F) curry(f).f(a)).f(b)).f(c)).f(d)).f(e)).f(f$);
    }

    public static <A, B, C, D, E, F$, G, H> F<F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>>, F7<A, B, C, D, E, F$, G, H>> uncurryF7() {
        return new F<F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>>, F7<A, B, C, D, E, F$, G, H>>() { // from class: fj.Function.33
            @Override // fj.F
            public F7<A, B, C, D, E, F$, G, H> f(F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f) {
                return Function.uncurryF7(f);
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H> F7<A, B, C, D, E, F$, G, H> uncurryF7(final F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f) {
        return new F7<A, B, C, D, E, F$, G, H>() { // from class: fj.Function.34
            /* JADX WARN: Type inference failed for: r0v14, types: [H, java.lang.Object] */
            @Override // fj.F7
            public H f(A a, B b, C c, D d, E e, F$ f$, G g) {
                return ((F) ((F) ((F) ((F) ((F) ((F) F.this.f(a)).f(b)).f(c)).f(d)).f(e)).f(f$)).f(g);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.Function$35  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$35.class */
    public static class AnonymousClass35 implements F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> {
        final /* synthetic */ F8 val$f;

        AnonymousClass35(F8 f8) {
            this.val$f = f8;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass35) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.Function$35$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$35$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.Function$35$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$35$1$1.class */
            public class C00381 implements F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>> {
                final /* synthetic */ Object val$b;

                C00381(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C00381) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.Function$35$1$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$35$1$1$1.class */
                public class C00391 implements F<D, F<E, F<F$, F<G, F<H, I>>>>> {
                    final /* synthetic */ Object val$c;

                    C00391(Object obj) {
                        this.val$c = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C00391) obj);
                    }

                    /* JADX INFO: Access modifiers changed from: package-private */
                    /* renamed from: fj.Function$35$1$1$1$1  reason: invalid class name and collision with other inner class name */
                    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$35$1$1$1$1.class */
                    public class C00401 implements F<E, F<F$, F<G, F<H, I>>>> {
                        final /* synthetic */ Object val$d;

                        C00401(Object obj) {
                            this.val$d = obj;
                        }

                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C00401) obj);
                        }

                        /* JADX INFO: Access modifiers changed from: package-private */
                        /* renamed from: fj.Function$35$1$1$1$1$1  reason: invalid class name and collision with other inner class name */
                        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$35$1$1$1$1$1.class */
                        public class C00411 implements F<F$, F<G, F<H, I>>> {
                            final /* synthetic */ Object val$e;

                            C00411(Object obj) {
                                this.val$e = obj;
                            }

                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((C00411) obj);
                            }

                            @Override // fj.F
                            public F<G, F<H, I>> f(final F$ f$) {
                                return new F<G, F<H, I>>() { // from class: fj.Function.35.1.1.1.1.1.1
                                    @Override // fj.F
                                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                                        return f((C00421) obj);
                                    }

                                    @Override // fj.F
                                    public F<H, I> f(final G g) {
                                        return new F<H, I>() { // from class: fj.Function.35.1.1.1.1.1.1.1
                                            /* JADX WARN: Type inference failed for: r0v9, types: [I, java.lang.Object] */
                                            @Override // fj.F
                                            public I f(H h) {
                                                return AnonymousClass35.this.val$f.f(AnonymousClass1.this.val$a, C00381.this.val$b, C00391.this.val$c, C00401.this.val$d, C00411.this.val$e, f$, g, h);
                                            }
                                        };
                                    }
                                };
                            }
                        }

                        @Override // fj.F
                        public F<F$, F<G, F<H, I>>> f(E e) {
                            return new C00411(e);
                        }
                    }

                    @Override // fj.F
                    public F<E, F<F$, F<G, F<H, I>>>> f(D d) {
                        return new C00401(d);
                    }
                }

                @Override // fj.F
                public F<D, F<E, F<F$, F<G, F<H, I>>>>> f(C c) {
                    return new C00391(c);
                }
            }

            @Override // fj.F
            public F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>> f(B b) {
                return new C00381(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E, F$, G, H, I> F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> curry(F8<A, B, C, D, E, F$, G, H, I> f) {
        return new AnonymousClass35(f);
    }

    public static <A, B, C, D, E, F$, G, H, I> F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>> curry(F8<A, B, C, D, E, F$, G, H, I> f, A a) {
        return (F) curry(f).f(a);
    }

    public static <A, B, C, D, E, F$, G, H, I> F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>> curry(F8<A, B, C, D, E, F$, G, H, I> f, A a, B b) {
        return (F) ((F) curry(f).f(a)).f(b);
    }

    public static <A, B, C, D, E, F$, G, H, I> F<D, F<E, F<F$, F<G, F<H, I>>>>> curry(F8<A, B, C, D, E, F$, G, H, I> f, A a, B b, C c) {
        return (F) ((F) ((F) curry(f).f(a)).f(b)).f(c);
    }

    public static <A, B, C, D, E, F$, G, H, I> F<E, F<F$, F<G, F<H, I>>>> curry(F8<A, B, C, D, E, F$, G, H, I> f, A a, B b, C c, D d) {
        return (F) ((F) ((F) ((F) curry(f).f(a)).f(b)).f(c)).f(d);
    }

    public static <A, B, C, D, E, F$, G, H, I> F<F$, F<G, F<H, I>>> curry(F8<A, B, C, D, E, F$, G, H, I> f, A a, B b, C c, D d, E e) {
        return (F) ((F) ((F) ((F) ((F) curry(f).f(a)).f(b)).f(c)).f(d)).f(e);
    }

    public static <A, B, C, D, E, F$, G, H, I> F<G, F<H, I>> curry(F8<A, B, C, D, E, F$, G, H, I> f, A a, B b, C c, D d, E e, F$ f$) {
        return (F) ((F) ((F) ((F) ((F) ((F) curry(f).f(a)).f(b)).f(c)).f(d)).f(e)).f(f$);
    }

    public static <A, B, C, D, E, F$, G, H, I> F<H, I> curry(F8<A, B, C, D, E, F$, G, H, I> f, A a, B b, C c, D d, E e, F$ f$, G g) {
        return (F) ((F) ((F) ((F) ((F) ((F) ((F) curry(f).f(a)).f(b)).f(c)).f(d)).f(e)).f(f$)).f(g);
    }

    public static <A, B, C, D, E, F$, G, H, I> F<F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>>, F8<A, B, C, D, E, F$, G, H, I>> uncurryF8() {
        return new F<F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>>, F8<A, B, C, D, E, F$, G, H, I>>() { // from class: fj.Function.36
            @Override // fj.F
            public F8<A, B, C, D, E, F$, G, H, I> f(F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f) {
                return Function.uncurryF8(f);
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H, I> F8<A, B, C, D, E, F$, G, H, I> uncurryF8(final F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f) {
        return new F8<A, B, C, D, E, F$, G, H, I>() { // from class: fj.Function.37
            /* JADX WARN: Type inference failed for: r0v16, types: [I, java.lang.Object] */
            @Override // fj.F8
            public I f(A a, B b, C c, D d, E e, F$ f$, G g, H h) {
                return ((F) ((F) ((F) ((F) ((F) ((F) ((F) F.this.f(a)).f(b)).f(c)).f(d)).f(e)).f(f$)).f(g)).f(h);
            }
        };
    }

    public static <A, B, C> F<C, B> bind(final F<C, A> ma, final F<A, F<C, B>> f) {
        return new F<C, B>() { // from class: fj.Function.38
            /* JADX WARN: Type inference failed for: r0v4, types: [B, java.lang.Object] */
            @Override // fj.F
            public B f(C m) {
                return ((F) F.this.f(ma.f(m))).f(m);
            }
        };
    }

    public static <A, B, C> F<C, B> apply(F<C, F<A, B>> cab, final F<C, A> ca) {
        return bind(cab, new F<F<A, B>, F<C, B>>() { // from class: fj.Function.39
            @Override // fj.F
            public F<C, B> f(final F<A, B> f) {
                return Function.compose(new F<A, B>() { // from class: fj.Function.39.1
                    /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
                    @Override // fj.F
                    public B f(A a) {
                        return f.f(a);
                    }
                }, F.this);
            }
        });
    }

    public static <A, B, C, D> F<D, C> bind(F<D, A> ca, F<D, B> cb, F<A, F<B, C>> f) {
        return apply(compose(f, ca), cb);
    }

    public static <A, B, C> F<B, F<B, C>> on(F<A, F<A, C>> a, F<B, A> f) {
        return compose(compose((F) andThen().f(f), a), f);
    }

    public static <A, B, C, D> F<F<D, A>, F<F<D, B>, F<D, C>>> lift(final F<A, F<B, C>> f) {
        return curry(new F2<F<D, A>, F<D, B>, F<D, C>>() { // from class: fj.Function.40
            @Override // fj.F2
            public F<D, C> f(F<D, A> ca, F<D, B> cb) {
                return Function.bind(ca, cb, F.this);
            }
        });
    }

    public static <A, B> F<B, A> join(F<B, F<B, A>> f) {
        return bind(f, identity());
    }

    public static <A, B, C> F<A, C> partialApply2(final F<A, F<B, C>> f, final B b) {
        return new F<A, C>() { // from class: fj.Function.41
            /* JADX WARN: Type inference failed for: r0v3, types: [C, java.lang.Object] */
            @Override // fj.F
            public C f(A a) {
                return Function.uncurryF2(F.this).f(a, b);
            }
        };
    }

    public static <A, B, C, D> F<A, F<B, D>> partialApply3(final F<A, F<B, F<C, D>>> f, final C c) {
        return new F<A, F<B, D>>() { // from class: fj.Function.42
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass42) obj);
            }

            @Override // fj.F
            public F<B, D> f(final A a) {
                return new F<B, D>() { // from class: fj.Function.42.1
                    /* JADX WARN: Type inference failed for: r0v4, types: [D, java.lang.Object] */
                    @Override // fj.F
                    public D f(B b) {
                        return Function.uncurryF3(F.this).f(a, b, c);
                    }
                };
            }
        };
    }

    /* renamed from: fj.Function$43  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$43.class */
    static class AnonymousClass43 implements F<A, F<B, F<C, E>>> {
        final /* synthetic */ F val$f;
        final /* synthetic */ Object val$d;

        AnonymousClass43(F f, Object obj) {
            this.val$f = f;
            this.val$d = obj;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass43) obj);
        }

        @Override // fj.F
        public F<B, F<C, E>> f(final A a) {
            return new F<B, F<C, E>>() { // from class: fj.Function.43.1
                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                @Override // fj.F
                public F<C, E> f(final B b) {
                    return new F<C, E>() { // from class: fj.Function.43.1.1
                        /* JADX WARN: Type inference failed for: r0v5, types: [E, java.lang.Object] */
                        @Override // fj.F
                        public E f(C c) {
                            return Function.uncurryF4(AnonymousClass43.this.val$f).f(a, b, c, AnonymousClass43.this.val$d);
                        }
                    };
                }
            };
        }
    }

    public static <A, B, C, D, E> F<A, F<B, F<C, E>>> partialApply4(F<A, F<B, F<C, F<D, E>>>> f, D d) {
        return new AnonymousClass43(f, d);
    }

    /* renamed from: fj.Function$44  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$44.class */
    static class AnonymousClass44 implements F<A, F<B, F<C, F<D, F$>>>> {
        final /* synthetic */ F val$f;
        final /* synthetic */ Object val$e;

        AnonymousClass44(F f, Object obj) {
            this.val$f = f;
            this.val$e = obj;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass44) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.Function$44$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$44$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, F$>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            @Override // fj.F
            public F<C, F<D, F$>> f(final B b) {
                return new F<C, F<D, F$>>() { // from class: fj.Function.44.1.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C00451) obj);
                    }

                    @Override // fj.F
                    public F<D, F$> f(final C c) {
                        return new F<D, F$>() { // from class: fj.Function.44.1.1.1
                            /* JADX WARN: Type inference failed for: r0v6, types: [java.lang.Object, F$] */
                            @Override // fj.F
                            public F$ f(D d) {
                                return Function.uncurryF5(AnonymousClass44.this.val$f).f(AnonymousClass1.this.val$a, b, c, d, AnonymousClass44.this.val$e);
                            }
                        };
                    }
                };
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, F$>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E, F$> F<A, F<B, F<C, F<D, F$>>>> partialApply5(F<A, F<B, F<C, F<D, F<E, F$>>>>> f, E e) {
        return new AnonymousClass44(f, e);
    }

    /* renamed from: fj.Function$45  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$45.class */
    static class AnonymousClass45 implements F<A, F<B, F<C, F<D, F<E, G>>>>> {
        final /* synthetic */ F val$f;
        final /* synthetic */ Object val$f$;

        AnonymousClass45(F f, Object obj) {
            this.val$f = f;
            this.val$f$ = obj;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass45) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.Function$45$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$45$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, F<E, G>>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.Function$45$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$45$1$1.class */
            public class C00471 implements F<C, F<D, F<E, G>>> {
                final /* synthetic */ Object val$b;

                C00471(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C00471) obj);
                }

                @Override // fj.F
                public F<D, F<E, G>> f(final C c) {
                    return new F<D, F<E, G>>() { // from class: fj.Function.45.1.1.1
                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C00481) obj);
                        }

                        @Override // fj.F
                        public F<E, G> f(final D d) {
                            return new F<E, G>() { // from class: fj.Function.45.1.1.1.1
                                /* JADX WARN: Type inference failed for: r0v7, types: [G, java.lang.Object] */
                                @Override // fj.F
                                public G f(E e) {
                                    return Function.uncurryF6(AnonymousClass45.this.val$f).f(AnonymousClass1.this.val$a, C00471.this.val$b, c, d, e, AnonymousClass45.this.val$f$);
                                }
                            };
                        }
                    };
                }
            }

            @Override // fj.F
            public F<C, F<D, F<E, G>>> f(B b) {
                return new C00471(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, F<E, G>>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E, F$, G> F<A, F<B, F<C, F<D, F<E, G>>>>> partialApply6(F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f, F$ f$) {
        return new AnonymousClass45(f, f$);
    }

    /* renamed from: fj.Function$46  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$46.class */
    static class AnonymousClass46 implements F<A, F<B, F<C, F<D, F<E, F<F$, H>>>>>> {
        final /* synthetic */ F val$f;
        final /* synthetic */ Object val$g;

        AnonymousClass46(F f, Object obj) {
            this.val$f = f;
            this.val$g = obj;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass46) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.Function$46$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$46$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, H>>>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.Function$46$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$46$1$1.class */
            public class C00501 implements F<C, F<D, F<E, F<F$, H>>>> {
                final /* synthetic */ Object val$b;

                C00501(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C00501) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.Function$46$1$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$46$1$1$1.class */
                public class C00511 implements F<D, F<E, F<F$, H>>> {
                    final /* synthetic */ Object val$c;

                    C00511(Object obj) {
                        this.val$c = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C00511) obj);
                    }

                    @Override // fj.F
                    public F<E, F<F$, H>> f(final D d) {
                        return new F<E, F<F$, H>>() { // from class: fj.Function.46.1.1.1.1
                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((C00521) obj);
                            }

                            @Override // fj.F
                            public F<F$, H> f(final E e) {
                                return new F<F$, H>() { // from class: fj.Function.46.1.1.1.1.1
                                    /* JADX WARN: Type inference failed for: r0v8, types: [H, java.lang.Object] */
                                    @Override // fj.F
                                    public H f(F$ f$) {
                                        return Function.uncurryF7(AnonymousClass46.this.val$f).f(AnonymousClass1.this.val$a, C00501.this.val$b, C00511.this.val$c, d, e, f$, AnonymousClass46.this.val$g);
                                    }
                                };
                            }
                        };
                    }
                }

                @Override // fj.F
                public F<D, F<E, F<F$, H>>> f(C c) {
                    return new C00511(c);
                }
            }

            @Override // fj.F
            public F<C, F<D, F<E, F<F$, H>>>> f(B b) {
                return new C00501(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, F<E, F<F$, H>>>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E, F$, G, H> F<A, F<B, F<C, F<D, F<E, F<F$, H>>>>>> partialApply7(F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f, G g) {
        return new AnonymousClass46(f, g);
    }

    /* renamed from: fj.Function$47  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$47.class */
    static class AnonymousClass47 implements F<A, F<B, F<C, F<D, F<E, F<F$, F<G, I>>>>>>> {
        final /* synthetic */ F val$f;
        final /* synthetic */ Object val$h;

        AnonymousClass47(F f, Object obj) {
            this.val$f = f;
            this.val$h = obj;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((AnonymousClass47) obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.Function$47$1  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$47$1.class */
        public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, F<G, I>>>>>> {
            final /* synthetic */ Object val$a;

            AnonymousClass1(Object obj) {
                this.val$a = obj;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.Function$47$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$47$1$1.class */
            public class C00541 implements F<C, F<D, F<E, F<F$, F<G, I>>>>> {
                final /* synthetic */ Object val$b;

                C00541(Object obj) {
                    this.val$b = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((C00541) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.Function$47$1$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$47$1$1$1.class */
                public class C00551 implements F<D, F<E, F<F$, F<G, I>>>> {
                    final /* synthetic */ Object val$c;

                    C00551(Object obj) {
                        this.val$c = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C00551) obj);
                    }

                    /* JADX INFO: Access modifiers changed from: package-private */
                    /* renamed from: fj.Function$47$1$1$1$1  reason: invalid class name and collision with other inner class name */
                    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Function$47$1$1$1$1.class */
                    public class C00561 implements F<E, F<F$, F<G, I>>> {
                        final /* synthetic */ Object val$d;

                        C00561(Object obj) {
                            this.val$d = obj;
                        }

                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C00561) obj);
                        }

                        @Override // fj.F
                        public F<F$, F<G, I>> f(final E e) {
                            return new F<F$, F<G, I>>() { // from class: fj.Function.47.1.1.1.1.1
                                @Override // fj.F
                                public /* bridge */ /* synthetic */ Object f(Object obj) {
                                    return f((C00571) obj);
                                }

                                @Override // fj.F
                                public F<G, I> f(final F$ f$) {
                                    return new F<G, I>() { // from class: fj.Function.47.1.1.1.1.1.1
                                        /* JADX WARN: Type inference failed for: r0v9, types: [I, java.lang.Object] */
                                        @Override // fj.F
                                        public I f(G g) {
                                            return Function.uncurryF8(AnonymousClass47.this.val$f).f(AnonymousClass1.this.val$a, C00541.this.val$b, C00551.this.val$c, C00561.this.val$d, e, f$, g, AnonymousClass47.this.val$h);
                                        }
                                    };
                                }
                            };
                        }
                    }

                    @Override // fj.F
                    public F<E, F<F$, F<G, I>>> f(D d) {
                        return new C00561(d);
                    }
                }

                @Override // fj.F
                public F<D, F<E, F<F$, F<G, I>>>> f(C c) {
                    return new C00551(c);
                }
            }

            @Override // fj.F
            public F<C, F<D, F<E, F<F$, F<G, I>>>>> f(B b) {
                return new C00541(b);
            }
        }

        @Override // fj.F
        public F<B, F<C, F<D, F<E, F<F$, F<G, I>>>>>> f(A a) {
            return new AnonymousClass1(a);
        }
    }

    public static <A, B, C, D, E, F$, G, H, I> F<A, F<B, F<C, F<D, F<E, F<F$, F<G, I>>>>>>> partialApply8(F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f, H h) {
        return new AnonymousClass47(f, h);
    }
}
