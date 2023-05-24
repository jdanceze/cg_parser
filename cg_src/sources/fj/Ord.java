package fj;

import fj.data.Array;
import fj.data.Either;
import fj.data.List;
import fj.data.Natural;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Set;
import fj.data.Stream;
import fj.data.Validation;
import java.math.BigDecimal;
import java.math.BigInteger;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Ord.class */
public final class Ord<A> {
    private final F<A, F<A, Ordering>> f;
    public final F<A, F<A, A>> max = Function.curry(new F2<A, A, A>() { // from class: fj.Ord.4
        @Override // fj.F2
        public A f(A a, A a1) {
            return (A) Ord.this.max(a, a1);
        }
    });
    public final F<A, F<A, A>> min = Function.curry(new F2<A, A, A>() { // from class: fj.Ord.5
        @Override // fj.F2
        public A f(A a, A a1) {
            return (A) Ord.this.min(a, a1);
        }
    });
    public static final Ord<Boolean> booleanOrd = new Ord<>(new F<Boolean, F<Boolean, Ordering>>() { // from class: fj.Ord.6
        @Override // fj.F
        public F<Boolean, Ordering> f(final Boolean a1) {
            return new F<Boolean, Ordering>() { // from class: fj.Ord.6.1
                @Override // fj.F
                public Ordering f(Boolean a2) {
                    int x = a1.compareTo(a2);
                    return x < 0 ? Ordering.LT : x == 0 ? Ordering.EQ : Ordering.GT;
                }
            };
        }
    });
    public static final Ord<Byte> byteOrd = new Ord<>(new F<Byte, F<Byte, Ordering>>() { // from class: fj.Ord.7
        @Override // fj.F
        public F<Byte, Ordering> f(final Byte a1) {
            return new F<Byte, Ordering>() { // from class: fj.Ord.7.1
                @Override // fj.F
                public Ordering f(Byte a2) {
                    int x = a1.compareTo(a2);
                    return x < 0 ? Ordering.LT : x == 0 ? Ordering.EQ : Ordering.GT;
                }
            };
        }
    });
    public static final Ord<Character> charOrd = new Ord<>(new F<Character, F<Character, Ordering>>() { // from class: fj.Ord.8
        @Override // fj.F
        public F<Character, Ordering> f(final Character a1) {
            return new F<Character, Ordering>() { // from class: fj.Ord.8.1
                @Override // fj.F
                public Ordering f(Character a2) {
                    int x = a1.compareTo(a2);
                    return x < 0 ? Ordering.LT : x == 0 ? Ordering.EQ : Ordering.GT;
                }
            };
        }
    });
    public static final Ord<Double> doubleOrd = new Ord<>(new F<Double, F<Double, Ordering>>() { // from class: fj.Ord.9
        @Override // fj.F
        public F<Double, Ordering> f(final Double a1) {
            return new F<Double, Ordering>() { // from class: fj.Ord.9.1
                @Override // fj.F
                public Ordering f(Double a2) {
                    int x = a1.compareTo(a2);
                    return x < 0 ? Ordering.LT : x == 0 ? Ordering.EQ : Ordering.GT;
                }
            };
        }
    });
    public static final Ord<Float> floatOrd = new Ord<>(new F<Float, F<Float, Ordering>>() { // from class: fj.Ord.10
        @Override // fj.F
        public F<Float, Ordering> f(final Float a1) {
            return new F<Float, Ordering>() { // from class: fj.Ord.10.1
                @Override // fj.F
                public Ordering f(Float a2) {
                    int x = a1.compareTo(a2);
                    return x < 0 ? Ordering.LT : x == 0 ? Ordering.EQ : Ordering.GT;
                }
            };
        }
    });
    public static final Ord<Integer> intOrd = new Ord<>(new F<Integer, F<Integer, Ordering>>() { // from class: fj.Ord.11
        @Override // fj.F
        public F<Integer, Ordering> f(final Integer a1) {
            return new F<Integer, Ordering>() { // from class: fj.Ord.11.1
                @Override // fj.F
                public Ordering f(Integer a2) {
                    int x = a1.compareTo(a2);
                    return x < 0 ? Ordering.LT : x == 0 ? Ordering.EQ : Ordering.GT;
                }
            };
        }
    });
    public static final Ord<BigInteger> bigintOrd = new Ord<>(new F<BigInteger, F<BigInteger, Ordering>>() { // from class: fj.Ord.12
        @Override // fj.F
        public F<BigInteger, Ordering> f(final BigInteger a1) {
            return new F<BigInteger, Ordering>() { // from class: fj.Ord.12.1
                @Override // fj.F
                public Ordering f(BigInteger a2) {
                    int x = a1.compareTo(a2);
                    return x < 0 ? Ordering.LT : x == 0 ? Ordering.EQ : Ordering.GT;
                }
            };
        }
    });
    public static final Ord<BigDecimal> bigdecimalOrd = new Ord<>(new F<BigDecimal, F<BigDecimal, Ordering>>() { // from class: fj.Ord.13
        @Override // fj.F
        public F<BigDecimal, Ordering> f(final BigDecimal a1) {
            return new F<BigDecimal, Ordering>() { // from class: fj.Ord.13.1
                @Override // fj.F
                public Ordering f(BigDecimal a2) {
                    int x = a1.compareTo(a2);
                    return x < 0 ? Ordering.LT : x == 0 ? Ordering.EQ : Ordering.GT;
                }
            };
        }
    });
    public static final Ord<Long> longOrd = new Ord<>(new F<Long, F<Long, Ordering>>() { // from class: fj.Ord.14
        @Override // fj.F
        public F<Long, Ordering> f(final Long a1) {
            return new F<Long, Ordering>() { // from class: fj.Ord.14.1
                @Override // fj.F
                public Ordering f(Long a2) {
                    int x = a1.compareTo(a2);
                    return x < 0 ? Ordering.LT : x == 0 ? Ordering.EQ : Ordering.GT;
                }
            };
        }
    });
    public static final Ord<Short> shortOrd = new Ord<>(new F<Short, F<Short, Ordering>>() { // from class: fj.Ord.15
        @Override // fj.F
        public F<Short, Ordering> f(final Short a1) {
            return new F<Short, Ordering>() { // from class: fj.Ord.15.1
                @Override // fj.F
                public Ordering f(Short a2) {
                    int x = a1.compareTo(a2);
                    return x < 0 ? Ordering.LT : x == 0 ? Ordering.EQ : Ordering.GT;
                }
            };
        }
    });
    public static final Ord<Ordering> orderingOrd = new Ord<>(Function.curry(new F2<Ordering, Ordering, Ordering>() { // from class: fj.Ord.16
        @Override // fj.F2
        public Ordering f(Ordering o1, Ordering o2) {
            if (o1 == o2) {
                return Ordering.EQ;
            }
            if (o1 == Ordering.LT) {
                return Ordering.LT;
            }
            if (o2 != Ordering.LT && o1 == Ordering.EQ) {
                return Ordering.LT;
            }
            return Ordering.GT;
        }
    }));
    public static final Ord<String> stringOrd = new Ord<>(new F<String, F<String, Ordering>>() { // from class: fj.Ord.17
        @Override // fj.F
        public F<String, Ordering> f(final String a1) {
            return new F<String, Ordering>() { // from class: fj.Ord.17.1
                @Override // fj.F
                public Ordering f(String a2) {
                    int x = a1.compareTo(a2);
                    return x < 0 ? Ordering.LT : x == 0 ? Ordering.EQ : Ordering.GT;
                }
            };
        }
    });
    public static final Ord<StringBuffer> stringBufferOrd = new Ord<>(new F<StringBuffer, F<StringBuffer, Ordering>>() { // from class: fj.Ord.18
        @Override // fj.F
        public F<StringBuffer, Ordering> f(final StringBuffer a1) {
            return new F<StringBuffer, Ordering>() { // from class: fj.Ord.18.1
                @Override // fj.F
                public Ordering f(StringBuffer a2) {
                    return Ord.stringOrd.compare(a1.toString(), a2.toString());
                }
            };
        }
    });
    public static final Ord<StringBuilder> stringBuilderOrd = new Ord<>(new F<StringBuilder, F<StringBuilder, Ordering>>() { // from class: fj.Ord.19
        @Override // fj.F
        public F<StringBuilder, Ordering> f(final StringBuilder a1) {
            return new F<StringBuilder, Ordering>() { // from class: fj.Ord.19.1
                @Override // fj.F
                public Ordering f(StringBuilder a2) {
                    return Ord.stringOrd.compare(a1.toString(), a2.toString());
                }
            };
        }
    });
    public static final Ord<Unit> unitOrd = ord(Function.curry(new F2<Unit, Unit, Ordering>() { // from class: fj.Ord.26
        @Override // fj.F2
        public Ordering f(Unit u1, Unit u2) {
            return Ordering.EQ;
        }
    }));
    public static final Ord<Natural> naturalOrd = bigintOrd.comap(Natural.bigIntegerValue);

    private Ord(F<A, F<A, Ordering>> f) {
        this.f = f;
    }

    public F<A, F<A, Ordering>> compare() {
        return this.f;
    }

    public Ordering compare(A a1, A a2) {
        return this.f.f(a1).f(a2);
    }

    public boolean eq(A a1, A a2) {
        return compare(a1, a2) == Ordering.EQ;
    }

    public Equal<A> equal() {
        return Equal.equal(Function.curry(new F2<A, A, Boolean>() { // from class: fj.Ord.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // fj.F2
            public Boolean f(A a1, A a2) {
                return Boolean.valueOf(Ord.this.eq(a1, a2));
            }
        }));
    }

    public <B> Ord<B> comap(F<B, A> f) {
        return ord(F1Functions.o(F1Functions.o(F1Functions.andThen(f), this.f), f));
    }

    public boolean isLessThan(A a1, A a2) {
        return compare(a1, a2) == Ordering.LT;
    }

    public boolean isGreaterThan(A a1, A a2) {
        return compare(a1, a2) == Ordering.GT;
    }

    public F<A, Boolean> isLessThan(final A a) {
        return new F<A, Boolean>() { // from class: fj.Ord.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                return f((AnonymousClass2) obj);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Boolean f(A a2) {
                return Boolean.valueOf(Ord.this.compare(a2, a) == Ordering.LT);
            }
        };
    }

    public F<A, Boolean> isGreaterThan(final A a) {
        return new F<A, Boolean>() { // from class: fj.Ord.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                return f((AnonymousClass3) obj);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Boolean f(A a2) {
                return Boolean.valueOf(Ord.this.compare(a2, a) == Ordering.GT);
            }
        };
    }

    public A max(A a1, A a2) {
        return isGreaterThan(a1, a2) ? a1 : a2;
    }

    public A min(A a1, A a2) {
        return isLessThan(a1, a2) ? a1 : a2;
    }

    public static <A> Ord<A> ord(F<A, F<A, Ordering>> f) {
        return new Ord<>(f);
    }

    public static <A> Ord<Option<A>> optionOrd(Ord<A> oa) {
        return new Ord<>(new F<Option<A>, F<Option<A>, Ordering>>() { // from class: fj.Ord.20
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Option) ((Option) obj));
            }

            public F<Option<A>, Ordering> f(final Option<A> o1) {
                return new F<Option<A>, Ordering>() { // from class: fj.Ord.20.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Ordering f(Object obj) {
                        return f((Option) ((Option) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public Ordering f(Option<A> o2) {
                        return o1.isNone() ? o2.isNone() ? Ordering.EQ : Ordering.LT : o2.isNone() ? Ordering.GT : (Ordering) ((F) Ord.this.f.f(o1.some())).f(o2.some());
                    }
                };
            }
        });
    }

    public static <A, B> Ord<Either<A, B>> eitherOrd(Ord<A> oa, final Ord<B> ob) {
        return new Ord<>(new F<Either<A, B>, F<Either<A, B>, Ordering>>() { // from class: fj.Ord.21
            @Override // fj.F
            public F<Either<A, B>, Ordering> f(final Either<A, B> e1) {
                return new F<Either<A, B>, Ordering>() { // from class: fj.Ord.21.1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Ordering f(Either<A, B> e2) {
                        return e1.isLeft() ? e2.isLeft() ? (Ordering) ((F) Ord.this.f.f(e1.left().value())).f(e2.left().value()) : Ordering.LT : e2.isLeft() ? Ordering.GT : (Ordering) ((F) ob.f.f(e1.right().value())).f(e2.right().value());
                    }
                };
            }
        });
    }

    public static <A, B> Ord<Validation<A, B>> validationOrd(Ord<A> oa, Ord<B> ob) {
        return eitherOrd(oa, ob).comap(Validation.either());
    }

    public static <A> Ord<List<A>> listOrd(Ord<A> oa) {
        return new Ord<>(new F<List<A>, F<List<A>, Ordering>>() { // from class: fj.Ord.22
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public F<List<A>, Ordering> f(final List<A> l1) {
                return new F<List<A>, Ordering>() { // from class: fj.Ord.22.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Ordering f(Object obj) {
                        return f((List) ((List) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public Ordering f(List<A> l2) {
                        if (l1.isEmpty()) {
                            return l2.isEmpty() ? Ordering.EQ : Ordering.LT;
                        } else if (l2.isEmpty()) {
                            return l1.isEmpty() ? Ordering.EQ : Ordering.GT;
                        } else {
                            Ordering c = Ord.this.compare(l1.head(), l2.head());
                            return c == Ordering.EQ ? (Ordering) ((F) Ord.listOrd(Ord.this).f.f(l1.tail())).f(l2.tail()) : c;
                        }
                    }
                };
            }
        });
    }

    public static <A> Ord<NonEmptyList<A>> nonEmptyListOrd(Ord<A> oa) {
        return listOrd(oa).comap(NonEmptyList.toList_());
    }

    public static <A> Ord<Stream<A>> streamOrd(Ord<A> oa) {
        return new Ord<>(new F<Stream<A>, F<Stream<A>, Ordering>>() { // from class: fj.Ord.23
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Stream) ((Stream) obj));
            }

            public F<Stream<A>, Ordering> f(final Stream<A> s1) {
                return new F<Stream<A>, Ordering>() { // from class: fj.Ord.23.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Ordering f(Object obj) {
                        return f((Stream) ((Stream) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public Ordering f(Stream<A> s2) {
                        if (s1.isEmpty()) {
                            return s2.isEmpty() ? Ordering.EQ : Ordering.LT;
                        } else if (s2.isEmpty()) {
                            return s1.isEmpty() ? Ordering.EQ : Ordering.GT;
                        } else {
                            Ordering c = Ord.this.compare(s1.head(), s2.head());
                            return c == Ordering.EQ ? (Ordering) ((F) Ord.streamOrd(Ord.this).f.f(s1.tail()._1())).f(s2.tail()._1()) : c;
                        }
                    }
                };
            }
        });
    }

    public static <A> Ord<Array<A>> arrayOrd(Ord<A> oa) {
        return new Ord<>(new F<Array<A>, F<Array<A>, Ordering>>() { // from class: fj.Ord.24
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public F<Array<A>, Ordering> f(final Array<A> a1) {
                return new F<Array<A>, Ordering>() { // from class: fj.Ord.24.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Ordering f(Object obj) {
                        return f((Array) ((Array) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public Ordering f(Array<A> a2) {
                        int i = 0;
                        while (i < a1.length() && i < a2.length()) {
                            Ordering c = Ord.this.compare(a1.get(i), a2.get(i));
                            if (c != Ordering.GT && c != Ordering.LT) {
                                i++;
                            } else {
                                return c;
                            }
                        }
                        return i == a1.length() ? i == a2.length() ? Ordering.EQ : Ordering.LT : i == a1.length() ? Ordering.EQ : Ordering.GT;
                    }
                };
            }
        });
    }

    public static <A> Ord<Set<A>> setOrd(Ord<A> oa) {
        return streamOrd(oa).comap(new F<Set<A>, Stream<A>>() { // from class: fj.Ord.25
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Set) ((Set) obj));
            }

            public Stream<A> f(Set<A> as) {
                return as.toStream();
            }
        });
    }

    public static <A> Ord<P1<A>> p1Ord(Ord<A> oa) {
        return (Ord<P1<A>>) oa.comap(P1.__1());
    }

    public static <A, B> Ord<P2<A, B>> p2Ord(Ord<A> oa, final Ord<B> ob) {
        return ord(Function.curry(new F2<P2<A, B>, P2<A, B>, Ordering>() { // from class: fj.Ord.27
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F2
            public Ordering f(P2<A, B> a, P2<A, B> b) {
                return Ord.this.eq(a._1(), b._1()) ? ob.compare(a._2(), b._2()) : Ord.this.compare(a._1(), b._1());
            }
        }));
    }

    public static <A, B, C> Ord<P3<A, B, C>> p3Ord(Ord<A> oa, final Ord<B> ob, final Ord<C> oc) {
        return ord(Function.curry(new F2<P3<A, B, C>, P3<A, B, C>, Ordering>() { // from class: fj.Ord.28
            @Override // fj.F2
            public Ordering f(P3<A, B, C> a, P3<A, B, C> b) {
                if (Ord.this.eq(a._1(), b._1())) {
                    return Ord.p2Ord(ob, oc).compare(P.p(a._2(), a._3()), P.p(b._2(), b._3()));
                }
                return Ord.this.compare(a._1(), b._1());
            }
        }));
    }

    public static <A extends Comparable<A>> Ord<A> comparableOrd() {
        return ord(new F<A, F<A, Ordering>>() { // from class: fj.Ord.29
            /* JADX WARN: Incorrect types in method signature: (TA;)Lfj/F<TA;Lfj/Ordering;>; */
            @Override // fj.F
            public F f(final Comparable comparable) {
                return new F<A, Ordering>() { // from class: fj.Ord.29.1
                    /* JADX WARN: Incorrect types in method signature: (TA;)Lfj/Ordering; */
                    @Override // fj.F
                    public Ordering f(Comparable comparable2) {
                        int x = comparable.compareTo(comparable2);
                        return x < 0 ? Ordering.LT : x == 0 ? Ordering.EQ : Ordering.GT;
                    }
                };
            }
        });
    }

    public static <A> Ord<A> hashOrd() {
        return ord(new F<A, F<A, Ordering>>() { // from class: fj.Ord.30
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass30) obj);
            }

            @Override // fj.F
            public F<A, Ordering> f(final A a) {
                return new F<A, Ordering>() { // from class: fj.Ord.30.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Ordering f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // fj.F
                    public Ordering f(A a2) {
                        int x = a.hashCode() - a2.hashCode();
                        return x < 0 ? Ordering.LT : x == 0 ? Ordering.EQ : Ordering.GT;
                    }
                };
            }
        });
    }

    public static <A> Ord<A> hashEqualsOrd() {
        return ord(new F<A, F<A, Ordering>>() { // from class: fj.Ord.31
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass31) obj);
            }

            @Override // fj.F
            public F<A, Ordering> f(final A a) {
                return new F<A, Ordering>() { // from class: fj.Ord.31.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Ordering f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // fj.F
                    public Ordering f(A a2) {
                        int x = a.hashCode() - a2.hashCode();
                        return x < 0 ? Ordering.LT : (x == 0 && a.equals(a2)) ? Ordering.EQ : Ordering.GT;
                    }
                };
            }
        });
    }
}
