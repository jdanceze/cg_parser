package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P.class */
public final class P {
    private P() {
        throw new UnsupportedOperationException();
    }

    public static <A> F<A, P1<A>> p1() {
        return new F<A, P1<A>>() { // from class: fj.P.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            @Override // fj.F
            public P1<A> f(A a) {
                return P.p(a);
            }
        };
    }

    public static <A> P1<A> p(final A a) {
        return new P1<A>() { // from class: fj.P.2
            /* JADX WARN: Type inference failed for: r0v1, types: [A, java.lang.Object] */
            @Override // fj.P1
            public A _1() {
                return a;
            }
        };
    }

    public static <A> P1<A> lazy(P1<A> pa) {
        return pa;
    }

    public static <A, B> P2<A, B> lazy(final P1<A> pa, final P1<B> pb) {
        return new P2<A, B>() { // from class: fj.P.3
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P2
            public A _1() {
                return P1.this._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P2
            public B _2() {
                return pb._1();
            }
        };
    }

    public static <A, B, C> P3<A, B, C> lazy(final P1<A> pa, final P1<B> pb, final P1<C> pc) {
        return new P3<A, B, C>() { // from class: fj.P.4
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P3
            public A _1() {
                return P1.this._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P3
            public B _2() {
                return pb._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.P3
            public C _3() {
                return pc._1();
            }
        };
    }

    public static <A, B, C, D> P4<A, B, C, D> lazy(final P1<A> pa, final P1<B> pb, final P1<C> pc, final P1<D> pd) {
        return new P4<A, B, C, D>() { // from class: fj.P.5
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P4
            public A _1() {
                return P1.this._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P4
            public B _2() {
                return pb._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.P4
            public C _3() {
                return pc._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [D, java.lang.Object] */
            @Override // fj.P4
            public D _4() {
                return pd._1();
            }
        };
    }

    public static <A, B, C, D, E> P5<A, B, C, D, E> lazy(final P1<A> pa, final P1<B> pb, final P1<C> pc, final P1<D> pd, final P1<E> pe) {
        return new P5<A, B, C, D, E>() { // from class: fj.P.6
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P5
            public A _1() {
                return P1.this._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P5
            public B _2() {
                return pb._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.P5
            public C _3() {
                return pc._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [D, java.lang.Object] */
            @Override // fj.P5
            public D _4() {
                return pd._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [E, java.lang.Object] */
            @Override // fj.P5
            public E _5() {
                return pe._1();
            }
        };
    }

    public static <A, B, C, D, E, F> P6<A, B, C, D, E, F> lazy(final P1<A> pa, final P1<B> pb, final P1<C> pc, final P1<D> pd, final P1<E> pe, final P1<F> pf) {
        return new P6<A, B, C, D, E, F>() { // from class: fj.P.7
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P6
            public A _1() {
                return P1.this._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P6
            public B _2() {
                return pb._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.P6
            public C _3() {
                return pc._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [D, java.lang.Object] */
            @Override // fj.P6
            public D _4() {
                return pd._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [E, java.lang.Object] */
            @Override // fj.P6
            public E _5() {
                return pe._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [F, java.lang.Object] */
            @Override // fj.P6
            public F _6() {
                return pf._1();
            }
        };
    }

    public static <A, B, C, D, E, F, G> P7<A, B, C, D, E, F, G> lazy(final P1<A> pa, final P1<B> pb, final P1<C> pc, final P1<D> pd, final P1<E> pe, final P1<F> pf, final P1<G> pg) {
        return new P7<A, B, C, D, E, F, G>() { // from class: fj.P.8
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P7
            public A _1() {
                return P1.this._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P7
            public B _2() {
                return pb._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.P7
            public C _3() {
                return pc._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [D, java.lang.Object] */
            @Override // fj.P7
            public D _4() {
                return pd._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [E, java.lang.Object] */
            @Override // fj.P7
            public E _5() {
                return pe._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [F, java.lang.Object] */
            @Override // fj.P7
            public F _6() {
                return pf._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [G, java.lang.Object] */
            @Override // fj.P7
            public G _7() {
                return pg._1();
            }
        };
    }

    public static <A, B, C, D, E, F, G, H> P8<A, B, C, D, E, F, G, H> lazy(final P1<A> pa, final P1<B> pb, final P1<C> pc, final P1<D> pd, final P1<E> pe, final P1<F> pf, final P1<G> pg, final P1<H> ph) {
        return new P8<A, B, C, D, E, F, G, H>() { // from class: fj.P.9
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P8
            public A _1() {
                return P1.this._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P8
            public B _2() {
                return pb._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.P8
            public C _3() {
                return pc._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [D, java.lang.Object] */
            @Override // fj.P8
            public D _4() {
                return pd._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [E, java.lang.Object] */
            @Override // fj.P8
            public E _5() {
                return pe._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [F, java.lang.Object] */
            @Override // fj.P8
            public F _6() {
                return pf._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [G, java.lang.Object] */
            @Override // fj.P8
            public G _7() {
                return pg._1();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [H, java.lang.Object] */
            @Override // fj.P8
            public H _8() {
                return ph._1();
            }
        };
    }

    public static <A, B> F<A, F<B, P2<A, B>>> p2() {
        return new F<A, F<B, P2<A, B>>>() { // from class: fj.P.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass10) obj);
            }

            @Override // fj.F
            public F<B, P2<A, B>> f(final A a) {
                return new F<B, P2<A, B>>() { // from class: fj.P.10.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public P2<A, B> f(B b) {
                        return P.p(a, b);
                    }
                };
            }
        };
    }

    public static <A, B> P2<A, B> p(final A a, final B b) {
        return new P2<A, B>() { // from class: fj.P.11
            /* JADX WARN: Type inference failed for: r0v1, types: [A, java.lang.Object] */
            @Override // fj.P2
            public A _1() {
                return a;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            @Override // fj.P2
            public B _2() {
                return b;
            }
        };
    }

    public static <A, B, C> F<A, F<B, F<C, P3<A, B, C>>>> p3() {
        return new F<A, F<B, F<C, P3<A, B, C>>>>() { // from class: fj.P.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass12) obj);
            }

            @Override // fj.F
            public F<B, F<C, P3<A, B, C>>> f(final A a) {
                return new F<B, F<C, P3<A, B, C>>>() { // from class: fj.P.12.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public F<C, P3<A, B, C>> f(final B b) {
                        return new F<C, P3<A, B, C>>() { // from class: fj.P.12.1.1
                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((C00591) obj);
                            }

                            @Override // fj.F
                            public P3<A, B, C> f(C c) {
                                return P.p(a, b, c);
                            }
                        };
                    }
                };
            }
        };
    }

    public static <A, B, C> P3<A, B, C> p(final A a, final B b, final C c) {
        return new P3<A, B, C>() { // from class: fj.P.13
            /* JADX WARN: Type inference failed for: r0v1, types: [A, java.lang.Object] */
            @Override // fj.P3
            public A _1() {
                return a;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            @Override // fj.P3
            public B _2() {
                return b;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [C, java.lang.Object] */
            @Override // fj.P3
            public C _3() {
                return c;
            }
        };
    }

    public static <A, B, C, D> F<A, F<B, F<C, F<D, P4<A, B, C, D>>>>> p4() {
        return new F<A, F<B, F<C, F<D, P4<A, B, C, D>>>>>() { // from class: fj.P.14
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass14) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.P$14$1  reason: invalid class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$14$1.class */
            public class AnonymousClass1 implements F<B, F<C, F<D, P4<A, B, C, D>>>> {
                final /* synthetic */ Object val$a;

                AnonymousClass1(Object obj) {
                    this.val$a = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                @Override // fj.F
                public F<C, F<D, P4<A, B, C, D>>> f(final B b) {
                    return new F<C, F<D, P4<A, B, C, D>>>() { // from class: fj.P.14.1.1
                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C00601) obj);
                        }

                        @Override // fj.F
                        public F<D, P4<A, B, C, D>> f(final C c) {
                            return new F<D, P4<A, B, C, D>>() { // from class: fj.P.14.1.1.1
                                @Override // fj.F
                                public /* bridge */ /* synthetic */ Object f(Object obj) {
                                    return f((C00611) obj);
                                }

                                @Override // fj.F
                                public P4<A, B, C, D> f(D d) {
                                    return P.p(AnonymousClass1.this.val$a, b, c, d);
                                }
                            };
                        }
                    };
                }
            }

            @Override // fj.F
            public F<B, F<C, F<D, P4<A, B, C, D>>>> f(A a) {
                return new AnonymousClass1(a);
            }
        };
    }

    public static <A, B, C, D> P4<A, B, C, D> p(final A a, final B b, final C c, final D d) {
        return new P4<A, B, C, D>() { // from class: fj.P.15
            /* JADX WARN: Type inference failed for: r0v1, types: [A, java.lang.Object] */
            @Override // fj.P4
            public A _1() {
                return a;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            @Override // fj.P4
            public B _2() {
                return b;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [C, java.lang.Object] */
            @Override // fj.P4
            public C _3() {
                return c;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [D, java.lang.Object] */
            @Override // fj.P4
            public D _4() {
                return d;
            }
        };
    }

    public static <A, B, C, D, E> F<A, F<B, F<C, F<D, F<E, P5<A, B, C, D, E>>>>>> p5() {
        return new F<A, F<B, F<C, F<D, F<E, P5<A, B, C, D, E>>>>>>() { // from class: fj.P.16
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass16) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.P$16$1  reason: invalid class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$16$1.class */
            public class AnonymousClass1 implements F<B, F<C, F<D, F<E, P5<A, B, C, D, E>>>>> {
                final /* synthetic */ Object val$a;

                AnonymousClass1(Object obj) {
                    this.val$a = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.P$16$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$16$1$1.class */
                public class C00621 implements F<C, F<D, F<E, P5<A, B, C, D, E>>>> {
                    final /* synthetic */ Object val$b;

                    C00621(Object obj) {
                        this.val$b = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C00621) obj);
                    }

                    @Override // fj.F
                    public F<D, F<E, P5<A, B, C, D, E>>> f(final C c) {
                        return new F<D, F<E, P5<A, B, C, D, E>>>() { // from class: fj.P.16.1.1.1
                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((C00631) obj);
                            }

                            @Override // fj.F
                            public F<E, P5<A, B, C, D, E>> f(final D d) {
                                return new F<E, P5<A, B, C, D, E>>() { // from class: fj.P.16.1.1.1.1
                                    @Override // fj.F
                                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                                        return f((C00641) obj);
                                    }

                                    @Override // fj.F
                                    public P5<A, B, C, D, E> f(E e) {
                                        return P.p(AnonymousClass1.this.val$a, C00621.this.val$b, c, d, e);
                                    }
                                };
                            }
                        };
                    }
                }

                @Override // fj.F
                public F<C, F<D, F<E, P5<A, B, C, D, E>>>> f(B b) {
                    return new C00621(b);
                }
            }

            @Override // fj.F
            public F<B, F<C, F<D, F<E, P5<A, B, C, D, E>>>>> f(A a) {
                return new AnonymousClass1(a);
            }
        };
    }

    public static <A, B, C, D, E> P5<A, B, C, D, E> p(final A a, final B b, final C c, final D d, final E e) {
        return new P5<A, B, C, D, E>() { // from class: fj.P.17
            /* JADX WARN: Type inference failed for: r0v1, types: [A, java.lang.Object] */
            @Override // fj.P5
            public A _1() {
                return a;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            @Override // fj.P5
            public B _2() {
                return b;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [C, java.lang.Object] */
            @Override // fj.P5
            public C _3() {
                return c;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [D, java.lang.Object] */
            @Override // fj.P5
            public D _4() {
                return d;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [E, java.lang.Object] */
            @Override // fj.P5
            public E _5() {
                return e;
            }
        };
    }

    public static <A, B, C, D, E, F$> F<A, F<B, F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>>> p6() {
        return new F<A, F<B, F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>>>() { // from class: fj.P.18
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass18) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.P$18$1  reason: invalid class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$18$1.class */
            public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>> {
                final /* synthetic */ Object val$a;

                AnonymousClass1(Object obj) {
                    this.val$a = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.P$18$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$18$1$1.class */
                public class C00651 implements F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>> {
                    final /* synthetic */ Object val$b;

                    C00651(Object obj) {
                        this.val$b = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C00651) obj);
                    }

                    /* JADX INFO: Access modifiers changed from: package-private */
                    /* renamed from: fj.P$18$1$1$1  reason: invalid class name and collision with other inner class name */
                    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$18$1$1$1.class */
                    public class C00661 implements F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>> {
                        final /* synthetic */ Object val$c;

                        C00661(Object obj) {
                            this.val$c = obj;
                        }

                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C00661) obj);
                        }

                        @Override // fj.F
                        public F<E, F<F$, P6<A, B, C, D, E, F$>>> f(final D d) {
                            return new F<E, F<F$, P6<A, B, C, D, E, F$>>>() { // from class: fj.P.18.1.1.1.1
                                @Override // fj.F
                                public /* bridge */ /* synthetic */ Object f(Object obj) {
                                    return f((C00671) obj);
                                }

                                @Override // fj.F
                                public F<F$, P6<A, B, C, D, E, F$>> f(final E e) {
                                    return new F<F$, P6<A, B, C, D, E, F$>>() { // from class: fj.P.18.1.1.1.1.1
                                        @Override // fj.F
                                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                                            return f((C00681) obj);
                                        }

                                        @Override // fj.F
                                        public P6<A, B, C, D, E, F$> f(F$ f) {
                                            return P.p(AnonymousClass1.this.val$a, C00651.this.val$b, C00661.this.val$c, d, e, f);
                                        }
                                    };
                                }
                            };
                        }
                    }

                    @Override // fj.F
                    public F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>> f(C c) {
                        return new C00661(c);
                    }
                }

                @Override // fj.F
                public F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>> f(B b) {
                    return new C00651(b);
                }
            }

            @Override // fj.F
            public F<B, F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>> f(A a) {
                return new AnonymousClass1(a);
            }
        };
    }

    public static <A, B, C, D, E, F$> P6<A, B, C, D, E, F$> p(final A a, final B b, final C c, final D d, final E e, final F$ f) {
        return new P6<A, B, C, D, E, F$>() { // from class: fj.P.19
            /* JADX WARN: Type inference failed for: r0v1, types: [A, java.lang.Object] */
            @Override // fj.P6
            public A _1() {
                return a;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            @Override // fj.P6
            public B _2() {
                return b;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [C, java.lang.Object] */
            @Override // fj.P6
            public C _3() {
                return c;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [D, java.lang.Object] */
            @Override // fj.P6
            public D _4() {
                return d;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [E, java.lang.Object] */
            @Override // fj.P6
            public E _5() {
                return e;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object, F$] */
            @Override // fj.P6
            public F$ _6() {
                return f;
            }
        };
    }

    public static <A, B, C, D, E, F$, G> F<A, F<B, F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>>> p7() {
        return new F<A, F<B, F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>>>() { // from class: fj.P.20
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass20) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.P$20$1  reason: invalid class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$20$1.class */
            public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>> {
                final /* synthetic */ Object val$a;

                AnonymousClass1(Object obj) {
                    this.val$a = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.P$20$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$20$1$1.class */
                public class C00691 implements F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>> {
                    final /* synthetic */ Object val$b;

                    C00691(Object obj) {
                        this.val$b = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C00691) obj);
                    }

                    /* JADX INFO: Access modifiers changed from: package-private */
                    /* renamed from: fj.P$20$1$1$1  reason: invalid class name and collision with other inner class name */
                    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$20$1$1$1.class */
                    public class C00701 implements F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>> {
                        final /* synthetic */ Object val$c;

                        C00701(Object obj) {
                            this.val$c = obj;
                        }

                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C00701) obj);
                        }

                        /* JADX INFO: Access modifiers changed from: package-private */
                        /* renamed from: fj.P$20$1$1$1$1  reason: invalid class name and collision with other inner class name */
                        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$20$1$1$1$1.class */
                        public class C00711 implements F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>> {
                            final /* synthetic */ Object val$d;

                            C00711(Object obj) {
                                this.val$d = obj;
                            }

                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((C00711) obj);
                            }

                            @Override // fj.F
                            public F<F$, F<G, P7<A, B, C, D, E, F$, G>>> f(final E e) {
                                return new F<F$, F<G, P7<A, B, C, D, E, F$, G>>>() { // from class: fj.P.20.1.1.1.1.1
                                    @Override // fj.F
                                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                                        return f((C00721) obj);
                                    }

                                    @Override // fj.F
                                    public F<G, P7<A, B, C, D, E, F$, G>> f(final F$ f) {
                                        return new F<G, P7<A, B, C, D, E, F$, G>>() { // from class: fj.P.20.1.1.1.1.1.1
                                            @Override // fj.F
                                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                                return f((C00731) obj);
                                            }

                                            @Override // fj.F
                                            public P7<A, B, C, D, E, F$, G> f(G g) {
                                                return P.p(AnonymousClass1.this.val$a, C00691.this.val$b, C00701.this.val$c, C00711.this.val$d, e, f, g);
                                            }
                                        };
                                    }
                                };
                            }
                        }

                        @Override // fj.F
                        public F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>> f(D d) {
                            return new C00711(d);
                        }
                    }

                    @Override // fj.F
                    public F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>> f(C c) {
                        return new C00701(c);
                    }
                }

                @Override // fj.F
                public F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>> f(B b) {
                    return new C00691(b);
                }
            }

            @Override // fj.F
            public F<B, F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>> f(A a) {
                return new AnonymousClass1(a);
            }
        };
    }

    public static <A, B, C, D, E, F$, G> P7<A, B, C, D, E, F$, G> p(final A a, final B b, final C c, final D d, final E e, final F$ f, final G g) {
        return new P7<A, B, C, D, E, F$, G>() { // from class: fj.P.21
            /* JADX WARN: Type inference failed for: r0v1, types: [A, java.lang.Object] */
            @Override // fj.P7
            public A _1() {
                return a;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            @Override // fj.P7
            public B _2() {
                return b;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [C, java.lang.Object] */
            @Override // fj.P7
            public C _3() {
                return c;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [D, java.lang.Object] */
            @Override // fj.P7
            public D _4() {
                return d;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [E, java.lang.Object] */
            @Override // fj.P7
            public E _5() {
                return e;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object, F$] */
            @Override // fj.P7
            public F$ _6() {
                return f;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [G, java.lang.Object] */
            @Override // fj.P7
            public G _7() {
                return g;
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H> F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>>> p8() {
        return new F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>>>() { // from class: fj.P.22
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass22) obj);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.P$22$1  reason: invalid class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$22$1.class */
            public class AnonymousClass1 implements F<B, F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>> {
                final /* synthetic */ Object val$a;

                AnonymousClass1(Object obj) {
                    this.val$a = obj;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: fj.P$22$1$1  reason: invalid class name and collision with other inner class name */
                /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$22$1$1.class */
                public class C00741 implements F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>> {
                    final /* synthetic */ Object val$b;

                    C00741(Object obj) {
                        this.val$b = obj;
                    }

                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((C00741) obj);
                    }

                    /* JADX INFO: Access modifiers changed from: package-private */
                    /* renamed from: fj.P$22$1$1$1  reason: invalid class name and collision with other inner class name */
                    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$22$1$1$1.class */
                    public class C00751 implements F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>> {
                        final /* synthetic */ Object val$c;

                        C00751(Object obj) {
                            this.val$c = obj;
                        }

                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((C00751) obj);
                        }

                        /* JADX INFO: Access modifiers changed from: package-private */
                        /* renamed from: fj.P$22$1$1$1$1  reason: invalid class name and collision with other inner class name */
                        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$22$1$1$1$1.class */
                        public class C00761 implements F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>> {
                            final /* synthetic */ Object val$d;

                            C00761(Object obj) {
                                this.val$d = obj;
                            }

                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((C00761) obj);
                            }

                            /* JADX INFO: Access modifiers changed from: package-private */
                            /* renamed from: fj.P$22$1$1$1$1$1  reason: invalid class name and collision with other inner class name */
                            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P$22$1$1$1$1$1.class */
                            public class C00771 implements F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>> {
                                final /* synthetic */ Object val$e;

                                C00771(Object obj) {
                                    this.val$e = obj;
                                }

                                @Override // fj.F
                                public /* bridge */ /* synthetic */ Object f(Object obj) {
                                    return f((C00771) obj);
                                }

                                @Override // fj.F
                                public F<G, F<H, P8<A, B, C, D, E, F$, G, H>>> f(final F$ f) {
                                    return new F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>() { // from class: fj.P.22.1.1.1.1.1.1
                                        @Override // fj.F
                                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                                            return f((C00781) obj);
                                        }

                                        @Override // fj.F
                                        public F<H, P8<A, B, C, D, E, F$, G, H>> f(final G g) {
                                            return new F<H, P8<A, B, C, D, E, F$, G, H>>() { // from class: fj.P.22.1.1.1.1.1.1.1
                                                @Override // fj.F
                                                public /* bridge */ /* synthetic */ Object f(Object obj) {
                                                    return f((C00791) obj);
                                                }

                                                @Override // fj.F
                                                public P8<A, B, C, D, E, F$, G, H> f(H h) {
                                                    return P.p(AnonymousClass1.this.val$a, C00741.this.val$b, C00751.this.val$c, C00761.this.val$d, C00771.this.val$e, f, g, h);
                                                }
                                            };
                                        }
                                    };
                                }
                            }

                            @Override // fj.F
                            public F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>> f(E e) {
                                return new C00771(e);
                            }
                        }

                        @Override // fj.F
                        public F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>> f(D d) {
                            return new C00761(d);
                        }
                    }

                    @Override // fj.F
                    public F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>> f(C c) {
                        return new C00751(c);
                    }
                }

                @Override // fj.F
                public F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>> f(B b) {
                    return new C00741(b);
                }
            }

            @Override // fj.F
            public F<B, F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>> f(A a) {
                return new AnonymousClass1(a);
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H> P8<A, B, C, D, E, F$, G, H> p(final A a, final B b, final C c, final D d, final E e, final F$ f, final G g, final H h) {
        return new P8<A, B, C, D, E, F$, G, H>() { // from class: fj.P.23
            /* JADX WARN: Type inference failed for: r0v1, types: [A, java.lang.Object] */
            @Override // fj.P8
            public A _1() {
                return a;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            @Override // fj.P8
            public B _2() {
                return b;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [C, java.lang.Object] */
            @Override // fj.P8
            public C _3() {
                return c;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [D, java.lang.Object] */
            @Override // fj.P8
            public D _4() {
                return d;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [E, java.lang.Object] */
            @Override // fj.P8
            public E _5() {
                return e;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object, F$] */
            @Override // fj.P8
            public F$ _6() {
                return f;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [G, java.lang.Object] */
            @Override // fj.P8
            public G _7() {
                return g;
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [H, java.lang.Object] */
            @Override // fj.P8
            public H _8() {
                return h;
            }
        };
    }

    public static <A> P1<A> lazy(final F<Unit, A> f) {
        return new P1<A>() { // from class: fj.P.24
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P1
            public A _1() {
                return F.this.f(Unit.unit());
            }
        };
    }

    public static <A, B> P2<A, B> lazy(final F<Unit, A> fa, final F<Unit, B> fb) {
        return new P2<A, B>() { // from class: fj.P.25
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P2
            public A _1() {
                return F.this.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P2
            public B _2() {
                return fb.f(Unit.unit());
            }
        };
    }

    public static <A, B, C> P3<A, B, C> lazy(final F<Unit, A> fa, final F<Unit, B> fb, final F<Unit, C> fc) {
        return new P3<A, B, C>() { // from class: fj.P.26
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P3
            public A _1() {
                return F.this.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P3
            public B _2() {
                return fb.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.P3
            public C _3() {
                return fc.f(Unit.unit());
            }
        };
    }

    public static <A, B, C, D> P4<A, B, C, D> lazy(final F<Unit, A> fa, final F<Unit, B> fb, final F<Unit, C> fc, final F<Unit, D> fd) {
        return new P4<A, B, C, D>() { // from class: fj.P.27
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P4
            public A _1() {
                return F.this.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P4
            public B _2() {
                return fb.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.P4
            public C _3() {
                return fc.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [D, java.lang.Object] */
            @Override // fj.P4
            public D _4() {
                return fd.f(Unit.unit());
            }
        };
    }

    public static <A, B, C, D, E> P5<A, B, C, D, E> lazy(final F<Unit, A> fa, final F<Unit, B> fb, final F<Unit, C> fc, final F<Unit, D> fd, final F<Unit, E> fe) {
        return new P5<A, B, C, D, E>() { // from class: fj.P.28
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P5
            public A _1() {
                return F.this.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P5
            public B _2() {
                return fb.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.P5
            public C _3() {
                return fc.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [D, java.lang.Object] */
            @Override // fj.P5
            public D _4() {
                return fd.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [E, java.lang.Object] */
            @Override // fj.P5
            public E _5() {
                return fe.f(Unit.unit());
            }
        };
    }

    public static <A, B, C, D, E, F$> P6<A, B, C, D, E, F$> lazy(final F<Unit, A> fa, final F<Unit, B> fb, final F<Unit, C> fc, final F<Unit, D> fd, final F<Unit, E> fe, final F<Unit, F$> ff) {
        return new P6<A, B, C, D, E, F$>() { // from class: fj.P.29
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P6
            public A _1() {
                return F.this.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P6
            public B _2() {
                return fb.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.P6
            public C _3() {
                return fc.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [D, java.lang.Object] */
            @Override // fj.P6
            public D _4() {
                return fd.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [E, java.lang.Object] */
            @Override // fj.P6
            public E _5() {
                return fe.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Object, F$] */
            @Override // fj.P6
            public F$ _6() {
                return ff.f(Unit.unit());
            }
        };
    }

    public static <A, B, C, D, E, F$, G> P7<A, B, C, D, E, F$, G> lazy(final F<Unit, A> fa, final F<Unit, B> fb, final F<Unit, C> fc, final F<Unit, D> fd, final F<Unit, E> fe, final F<Unit, F$> ff, final F<Unit, G> fg) {
        return new P7<A, B, C, D, E, F$, G>() { // from class: fj.P.30
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P7
            public A _1() {
                return F.this.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P7
            public B _2() {
                return fb.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.P7
            public C _3() {
                return fc.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [D, java.lang.Object] */
            @Override // fj.P7
            public D _4() {
                return fd.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [E, java.lang.Object] */
            @Override // fj.P7
            public E _5() {
                return fe.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Object, F$] */
            @Override // fj.P7
            public F$ _6() {
                return ff.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [G, java.lang.Object] */
            @Override // fj.P7
            public G _7() {
                return fg.f(Unit.unit());
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H> P8<A, B, C, D, E, F$, G, H> lazy(final F<Unit, A> fa, final F<Unit, B> fb, final F<Unit, C> fc, final F<Unit, D> fd, final F<Unit, E> fe, final F<Unit, F$> ff, final F<Unit, G> fg, final F<Unit, H> fh) {
        return new P8<A, B, C, D, E, F$, G, H>() { // from class: fj.P.31
            /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
            @Override // fj.P8
            public A _1() {
                return F.this.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P8
            public B _2() {
                return fb.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.P8
            public C _3() {
                return fc.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [D, java.lang.Object] */
            @Override // fj.P8
            public D _4() {
                return fd.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [E, java.lang.Object] */
            @Override // fj.P8
            public E _5() {
                return fe.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Object, F$] */
            @Override // fj.P8
            public F$ _6() {
                return ff.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [G, java.lang.Object] */
            @Override // fj.P8
            public G _7() {
                return fg.f(Unit.unit());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [H, java.lang.Object] */
            @Override // fj.P8
            public H _8() {
                return fh.f(Unit.unit());
            }
        };
    }
}
