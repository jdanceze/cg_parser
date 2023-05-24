package fj.parser;

import fj.Digit;
import fj.F;
import fj.P;
import fj.P1;
import fj.Semigroup;
import fj.Unit;
import fj.data.List;
import fj.data.Stream;
import fj.data.Validation;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/parser/Parser.class */
public final class Parser<I, A, E> {
    private final F<I, Validation<E, Result<I, A>>> f;

    private Parser(F<I, Validation<E, Result<I, A>>> f) {
        this.f = f;
    }

    public Validation<E, Result<I, A>> parse(I i) {
        return this.f.f(i);
    }

    public <Z> Parser<Z, A, E> xmap(final F<I, Z> f, final F<Z, I> g) {
        return parser(new F<Z, Validation<E, Result<Z, A>>>() { // from class: fj.parser.Parser.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Validation<E, Result<Z, A>> f(Z z) {
                return Parser.this.parse(g.f(z)).map(new F<Result<I, A>, Result<Z, A>>() { // from class: fj.parser.Parser.1.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Result) ((Result) obj));
                    }

                    public Result<Z, A> f(Result<I, A> r) {
                        return r.mapRest(f);
                    }
                });
            }
        });
    }

    public <B> Parser<I, B, E> map(final F<A, B> f) {
        return parser(new F<I, Validation<E, Result<I, B>>>() { // from class: fj.parser.Parser.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass2) obj);
            }

            @Override // fj.F
            public Validation<E, Result<I, B>> f(I i) {
                return (Validation<E, A>) Parser.this.parse(i).map((F<Result<I, A>, A>) new F<Result<I, A>, Result<I, B>>() { // from class: fj.parser.Parser.2.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Result) ((Result) obj));
                    }

                    public Result<I, B> f(Result<I, A> r) {
                        return r.mapValue(f);
                    }
                });
            }
        });
    }

    public Parser<I, A, E> filter(final F<A, Boolean> f, final E e) {
        return parser(new F<I, Validation<E, Result<I, A>>>() { // from class: fj.parser.Parser.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass3) obj);
            }

            @Override // fj.F
            public Validation<E, Result<I, A>> f(I i) {
                return (Validation<E, A>) Parser.this.parse(i).bind((F<Result<I, A>, Validation<E, Result<I, A>>>) new F<Result<I, A>, Validation<E, Result<I, A>>>() { // from class: fj.parser.Parser.3.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Result) ((Result) obj));
                    }

                    public Validation<E, Result<I, A>> f(Result<I, A> r) {
                        A v = r.value();
                        if (((Boolean) f.f(v)).booleanValue()) {
                            return Validation.success(Result.result(r.rest(), v));
                        }
                        return Validation.fail(e);
                    }
                });
            }
        });
    }

    public <B> Parser<I, B, E> bind(final F<A, Parser<I, B, E>> f) {
        return parser(new F<I, Validation<E, Result<I, B>>>() { // from class: fj.parser.Parser.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass4) obj);
            }

            @Override // fj.F
            public Validation<E, Result<I, B>> f(I i) {
                return (Validation<E, A>) Parser.this.parse(i).bind((F<Result<I, A>, Validation<E, A>>) new F<Result<I, A>, Validation<E, Result<I, B>>>() { // from class: fj.parser.Parser.4.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Result) ((Result) obj));
                    }

                    public Validation<E, Result<I, B>> f(Result<I, A> r) {
                        return ((Parser) f.f(r.value())).parse(r.rest());
                    }
                });
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C> Parser<I, C, E> bind(Parser<I, B, E> pb, F<A, F<B, C>> f) {
        return (Parser<I, B, E>) pb.apply(map(f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C, D> Parser<I, D, E> bind(Parser<I, B, E> pb, Parser<I, C, E> pc, F<A, F<B, F<C, D>>> f) {
        return (Parser<I, B, E>) pc.apply(bind(pb, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C, D, E$> Parser<I, E$, E> bind(Parser<I, B, E> pb, Parser<I, C, E> pc, Parser<I, D, E> pd, F<A, F<B, F<C, F<D, E$>>>> f) {
        return (Parser<I, B, E>) pd.apply(bind(pb, pc, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C, D, E$, F$> Parser<I, F$, E> bind(Parser<I, B, E> pb, Parser<I, C, E> pc, Parser<I, D, E> pd, Parser<I, E$, E> pe, F<A, F<B, F<C, F<D, F<E$, F$>>>>> f) {
        return (Parser<I, B, E>) pe.apply(bind(pb, pc, pd, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C, D, E$, F$, G> Parser<I, G, E> bind(Parser<I, B, E> pb, Parser<I, C, E> pc, Parser<I, D, E> pd, Parser<I, E$, E> pe, Parser<I, F$, E> pf, F<A, F<B, F<C, F<D, F<E$, F<F$, G>>>>>> f) {
        return (Parser<I, B, E>) pf.apply(bind(pb, pc, pd, pe, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C, D, E$, F$, G, H> Parser<I, H, E> bind(Parser<I, B, E> pb, Parser<I, C, E> pc, Parser<I, D, E> pd, Parser<I, E$, E> pe, Parser<I, F$, E> pf, Parser<I, G, E> pg, F<A, F<B, F<C, F<D, F<E$, F<F$, F<G, H>>>>>>> f) {
        return (Parser<I, B, E>) pg.apply(bind(pb, pc, pd, pe, pf, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C, D, E$, F$, G, H, I$> Parser<I, I$, E> bind(Parser<I, B, E> pb, Parser<I, C, E> pc, Parser<I, D, E> pd, Parser<I, E$, E> pe, Parser<I, F$, E> pf, Parser<I, G, E> pg, Parser<I, H, E> ph, F<A, F<B, F<C, F<D, F<E$, F<F$, F<G, F<H, I$>>>>>>>> f) {
        return (Parser<I, B, E>) ph.apply(bind(pb, pc, pd, pe, pf, pg, f));
    }

    public <B> Parser<I, B, E> sequence(final Parser<I, B, E> p) {
        return bind(new F<A, Parser<I, B, E>>() { // from class: fj.parser.Parser.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass5) obj);
            }

            @Override // fj.F
            public Parser<I, B, E> f(A a) {
                return p;
            }
        });
    }

    public <B> Parser<I, B, E> apply(Parser<I, F<A, B>, E> p) {
        return p.bind(new F<F<A, B>, Parser<I, B, E>>() { // from class: fj.parser.Parser.6
            @Override // fj.F
            public Parser<I, B, E> f(F<A, B> f) {
                return Parser.this.map(f);
            }
        });
    }

    public Parser<I, A, E> or(final P1<Parser<I, A, E>> alt) {
        return parser(new F<I, Validation<E, Result<I, A>>>() { // from class: fj.parser.Parser.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass7) obj);
            }

            @Override // fj.F
            public Validation<E, Result<I, A>> f(I i) {
                return (Validation<A, Result<I, A>>) Parser.this.parse(i).f().sequence(((Parser) alt._1()).parse(i));
            }
        });
    }

    public Parser<I, A, E> or(Parser<I, A, E> alt) {
        return or(P.p(alt));
    }

    public Parser<I, A, E> or(final P1<Parser<I, A, E>> alt, final Semigroup<E> s) {
        return parser(new F<I, Validation<E, Result<I, A>>>() { // from class: fj.parser.Parser.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass8) obj);
            }

            @Override // fj.F
            public Validation<E, Result<I, A>> f(final I i) {
                return (Validation<A, Result<I, A>>) Parser.this.parse(i).f().bind((F<E, Validation<E, Result<I, A>>>) new F<E, Validation<E, Result<I, A>>>() { // from class: fj.parser.Parser.8.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Validation<E, Result<I, A>> f(E e) {
                        return ((Parser) alt._1()).parse(i).f().map(s.sum(e));
                    }
                });
            }
        });
    }

    public Parser<I, A, E> or(Parser<I, A, E> alt, Semigroup<E> s) {
        return or(P.p(alt), s);
    }

    public Parser<I, Unit, E> not(final P1<E> e) {
        return parser(new F<I, Validation<E, Result<I, Unit>>>() { // from class: fj.parser.Parser.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass9) obj);
            }

            @Override // fj.F
            public Validation<E, Result<I, Unit>> f(I i) {
                if (Parser.this.parse(i).isFail()) {
                    return Validation.success(Result.result(i, Unit.unit()));
                }
                return Validation.fail(e._1());
            }
        });
    }

    public Parser<I, Unit, E> not(E e) {
        return not((P1) P.p(e));
    }

    public Parser<I, Stream<A>, E> repeat() {
        return repeat1().or(new P1<Parser<I, Stream<A>, E>>() { // from class: fj.parser.Parser.10
            @Override // fj.P1
            public Parser<I, Stream<A>, E> _1() {
                return Parser.value(Stream.nil());
            }
        });
    }

    public Parser<I, Stream<A>, E> repeat1() {
        return (Parser<I, Stream<A>, E>) bind((F<A, Parser<I, Stream<A>, E>>) new F<A, Parser<I, Stream<A>, E>>() { // from class: fj.parser.Parser.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass11) obj);
            }

            @Override // fj.F
            public Parser<I, Stream<A>, E> f(final A a) {
                return (Parser<I, Stream<A>, E>) Parser.this.repeat().map((F<Stream<A>, Stream<A>>) new F<Stream<A>, Stream<A>>() { // from class: fj.parser.Parser.11.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Stream) ((Stream) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public Stream<A> f(Stream<A> as) {
                        return as.cons(a);
                    }
                });
            }
        });
    }

    public <K> Parser<I, A, K> mapError(final F<E, K> f) {
        return parser(new F<I, Validation<K, Result<I, A>>>() { // from class: fj.parser.Parser.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass12) obj);
            }

            @Override // fj.F
            public Validation<K, Result<I, A>> f(I i) {
                return ((Validation) Parser.this.f.f(i)).f().map(f);
            }
        });
    }

    public static <I, A, E> Parser<I, A, E> parser(F<I, Validation<E, Result<I, A>>> f) {
        return new Parser<>(f);
    }

    public static <I, A, E> Parser<I, A, E> value(final A a) {
        return parser(new F<I, Validation<E, Result<I, A>>>() { // from class: fj.parser.Parser.13
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass13) obj);
            }

            @Override // fj.F
            public Validation<E, Result<I, A>> f(I i) {
                return Validation.success(Result.result(i, a));
            }
        });
    }

    public static <I, A, E> Parser<I, A, E> fail(final E e) {
        return parser(new F<I, Validation<E, Result<I, A>>>() { // from class: fj.parser.Parser.14
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass14) obj);
            }

            @Override // fj.F
            public Validation<E, Result<I, A>> f(I i) {
                return Validation.fail(e);
            }
        });
    }

    public static <I, A, E> Parser<I, List<A>, E> sequence(final List<Parser<I, A, E>> ps) {
        if (ps.isEmpty()) {
            return value(List.nil());
        }
        return (Parser<I, List<A>, E>) ps.head().bind((F<A, Parser<I, List<A>, E>>) new F<A, Parser<I, List<A>, E>>() { // from class: fj.parser.Parser.15
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass15) obj);
            }

            @Override // fj.F
            public Parser<I, List<A>, E> f(A a) {
                return Parser.sequence(List.this.tail()).map(List.cons_(a));
            }
        });
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/parser/Parser$StreamParser.class */
    public static final class StreamParser {
        private StreamParser() {
        }

        public static <I, E> Parser<Stream<I>, I, E> element(final P1<E> e) {
            return Parser.parser(new F<Stream<I>, Validation<E, Result<Stream<I>, I>>>() { // from class: fj.parser.Parser.StreamParser.1
                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((Stream) ((Stream) obj));
                }

                public Validation<E, Result<Stream<I>, I>> f(Stream<I> is) {
                    if (is.isEmpty()) {
                        return Validation.fail(P1.this._1());
                    }
                    return Validation.success(Result.result(is.tail()._1(), is.head()));
                }
            });
        }

        public static <I, E> Parser<Stream<I>, I, E> element(E e) {
            return element(P.p(e));
        }

        public static <I, E> Parser<Stream<I>, I, E> satisfy(P1<E> missing, final F<I, E> sat, final F<I, Boolean> f) {
            return element((P1) missing).bind(new F<I, Parser<Stream<I>, I, E>>() { // from class: fj.parser.Parser.StreamParser.2
                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass2) obj);
                }

                @Override // fj.F
                public Parser<Stream<I>, I, E> f(I x) {
                    if (((Boolean) F.this.f(x)).booleanValue()) {
                        return Parser.value(x);
                    }
                    return Parser.fail(sat.f(x));
                }
            });
        }

        public static <I, E> Parser<Stream<I>, I, E> satisfy(E missing, F<I, E> sat, F<I, Boolean> f) {
            return satisfy(P.p(missing), (F) sat, (F) f);
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/parser/Parser$CharsParser.class */
    public static final class CharsParser {
        private CharsParser() {
        }

        public static <E> Parser<Stream<Character>, Character, E> character(P1<E> e) {
            return StreamParser.element((P1) e);
        }

        public static <E> Parser<Stream<Character>, Character, E> character(E e) {
            return character(P.p(e));
        }

        public static <E> Parser<Stream<Character>, Character, E> character(P1<E> missing, F<Character, E> sat, final char c) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.1
                @Override // fj.F
                public Boolean f(Character x) {
                    return Boolean.valueOf(x.charValue() == c);
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> character(E missing, F<Character, E> sat, char c) {
            return character(P.p(missing), (F) sat, c);
        }

        public static <E> Parser<Stream<Character>, Stream<Character>, E> characters(P1<E> missing, int n) {
            if (n <= 0) {
                return Parser.value(Stream.nil());
            }
            return character((P1) missing).bind(characters((P1) missing, n - 1), Stream.cons_());
        }

        public static <E> Parser<Stream<Character>, Stream<Character>, E> characters(E missing, int n) {
            return characters(P.p(missing), n);
        }

        public static <E> Parser<Stream<Character>, Stream<Character>, E> characters(P1<E> missing, F<Character, E> sat, Stream<Character> cs) {
            if (cs.isEmpty()) {
                return Parser.value(Stream.nil());
            }
            return character((P1) missing, (F) sat, cs.head().charValue()).bind(characters((P1) missing, (F) sat, cs.tail()._1()), Stream.cons_());
        }

        public static <E> Parser<Stream<Character>, Stream<Character>, E> characters(E missing, F<Character, E> sat, Stream<Character> cs) {
            return characters(P.p(missing), (F) sat, cs);
        }

        public static <E> Parser<Stream<Character>, String, E> string(P1<E> missing, F<Character, E> sat, String s) {
            return characters((P1) missing, (F) sat, List.fromString(s).toStream()).map(new F<Stream<Character>, String>() { // from class: fj.parser.Parser.CharsParser.2
                @Override // fj.F
                public String f(Stream<Character> cs) {
                    return List.asString(cs.toList());
                }
            });
        }

        public static <E> Parser<Stream<Character>, String, E> string(E missing, F<Character, E> sat, String s) {
            return string(P.p(missing), (F) sat, s);
        }

        public static <E> Parser<Stream<Character>, Digit, E> digit(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.4
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isDigit(c.charValue()));
                }
            }).map(new F<Character, Digit>() { // from class: fj.parser.Parser.CharsParser.3
                @Override // fj.F
                public Digit f(Character c) {
                    return Digit.fromChar(c.charValue()).some();
                }
            });
        }

        public static <E> Parser<Stream<Character>, Digit, E> digit(E missing, F<Character, E> sat) {
            return digit(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> lower(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.5
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isLowerCase(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> lower(E missing, F<Character, E> sat) {
            return lower(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> upper(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.6
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isUpperCase(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> upper(E missing, F<Character, E> sat) {
            return upper(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> defined(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.7
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isDefined(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> defined(E missing, F<Character, E> sat) {
            return defined(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> highSurrogate(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.8
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isHighSurrogate(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> highSurrogate(E missing, F<Character, E> sat) {
            return highSurrogate(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> identifierIgnorable(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.9
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isIdentifierIgnorable(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> identifierIgnorable(E missing, F<Character, E> sat) {
            return identifierIgnorable(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> isoControl(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.10
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isISOControl(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> isoControl(E missing, F<Character, E> sat) {
            return isoControl(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> javaIdentifierPart(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.11
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isJavaIdentifierPart(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> javaIdentifierPart(E missing, F<Character, E> sat) {
            return javaIdentifierPart(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> javaIdentifierStart(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.12
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isJavaIdentifierStart(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> javaIdentifierStart(E missing, F<Character, E> sat) {
            return javaIdentifierStart(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> alpha(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.13
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isLetter(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> alpha(E missing, F<Character, E> sat) {
            return alpha(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> alphaNum(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.14
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isLetterOrDigit(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> alphaNum(E missing, F<Character, E> sat) {
            return alphaNum(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> lowSurrogate(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.15
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isLowSurrogate(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> lowSurrogate(E missing, F<Character, E> sat) {
            return lowSurrogate(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> mirrored(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.16
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isMirrored(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> mirrored(E missing, F<Character, E> sat) {
            return mirrored(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> space(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.17
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isSpaceChar(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> space(E missing, F<Character, E> sat) {
            return space(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> titleCase(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.18
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isTitleCase(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> titleCase(E missing, F<Character, E> sat) {
            return titleCase(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> unicodeIdentiferPart(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.19
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isUnicodeIdentifierPart(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> unicodeIdentiferPart(E missing, F<Character, E> sat) {
            return unicodeIdentiferPart(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> unicodeIdentiferStart(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.20
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isUnicodeIdentifierStart(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> unicodeIdentiferStart(E missing, F<Character, E> sat) {
            return unicodeIdentiferStart(P.p(missing), (F) sat);
        }

        public static <E> Parser<Stream<Character>, Character, E> whitespace(P1<E> missing, F<Character, E> sat) {
            return StreamParser.satisfy((P1) missing, (F) sat, (F) new F<Character, Boolean>() { // from class: fj.parser.Parser.CharsParser.21
                @Override // fj.F
                public Boolean f(Character c) {
                    return Boolean.valueOf(Character.isWhitespace(c.charValue()));
                }
            });
        }

        public static <E> Parser<Stream<Character>, Character, E> whitespace(E missing, F<Character, E> sat) {
            return whitespace(P.p(missing), (F) sat);
        }
    }
}
