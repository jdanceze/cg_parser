package fj.data;

import fj.Effect;
import fj.F;
import fj.P1;
import fj.Try;
import fj.TryEffect;
import fj.Unit;
import fj.function.Effect0;
import fj.function.Effect1;
import fj.function.Try0;
import fj.function.Try1;
import fj.function.TryEffect0;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions.class */
public final class Conversions {
    public static final F<List<Character>, String> List_String = new F<List<Character>, String>() { // from class: fj.data.Conversions.5
        @Override // fj.F
        public String f(List<Character> cs) {
            return List.asString(cs);
        }
    };
    public static final F<List<Character>, StringBuffer> List_StringBuffer = new F<List<Character>, StringBuffer>() { // from class: fj.data.Conversions.6
        @Override // fj.F
        public StringBuffer f(List<Character> cs) {
            return new StringBuffer(List.asString(cs));
        }
    };
    public static final F<List<Character>, StringBuilder> List_StringBuilder = new F<List<Character>, StringBuilder>() { // from class: fj.data.Conversions.7
        @Override // fj.F
        public StringBuilder f(List<Character> cs) {
            return new StringBuilder(List.asString(cs));
        }
    };
    public static final F<Array<Character>, String> Array_String = new F<Array<Character>, String>() { // from class: fj.data.Conversions.12
        @Override // fj.F
        public String f(Array<Character> cs) {
            final StringBuilder sb = new StringBuilder();
            cs.foreachDoEffect(new Effect1<Character>() { // from class: fj.data.Conversions.12.1
                {
                    AnonymousClass12.this = this;
                }

                @Override // fj.function.Effect1
                public void f(Character c) {
                    sb.append(c);
                }
            });
            return sb.toString();
        }
    };
    public static final F<Array<Character>, StringBuffer> Array_StringBuffer = new F<Array<Character>, StringBuffer>() { // from class: fj.data.Conversions.13
        @Override // fj.F
        public StringBuffer f(Array<Character> cs) {
            final StringBuffer sb = new StringBuffer();
            cs.foreachDoEffect(new Effect1<Character>() { // from class: fj.data.Conversions.13.1
                {
                    AnonymousClass13.this = this;
                }

                @Override // fj.function.Effect1
                public void f(Character c) {
                    sb.append(c);
                }
            });
            return sb;
        }
    };
    public static final F<Array<Character>, StringBuilder> Array_StringBuilder = new AnonymousClass14();
    public static final F<Stream<Character>, String> Stream_String = new AnonymousClass19();
    public static final F<Stream<Character>, StringBuffer> Stream_StringBuffer = new AnonymousClass20();
    public static final F<Stream<Character>, StringBuilder> Stream_StringBuilder = new AnonymousClass21();
    public static final F<Option<Character>, String> Option_String = new F<Option<Character>, String>() { // from class: fj.data.Conversions.26
        @Override // fj.F
        public String f(Option<Character> o) {
            return List.asString(o.toList());
        }
    };
    public static final F<Option<Character>, StringBuffer> Option_StringBuffer = new F<Option<Character>, StringBuffer>() { // from class: fj.data.Conversions.27
        @Override // fj.F
        public StringBuffer f(Option<Character> o) {
            return new StringBuffer(List.asString(o.toList()));
        }
    };
    public static final F<Option<Character>, StringBuilder> Option_StringBuilder = new F<Option<Character>, StringBuilder>() { // from class: fj.data.Conversions.28
        @Override // fj.F
        public StringBuilder f(Option<Character> o) {
            return new StringBuilder(List.asString(o.toList()));
        }
    };
    public static final F<String, List<Character>> String_List = new F<String, List<Character>>() { // from class: fj.data.Conversions.43
        @Override // fj.F
        public List<Character> f(String s) {
            return List.fromString(s);
        }
    };
    public static final F<String, Array<Character>> String_Array = new F<String, Array<Character>>() { // from class: fj.data.Conversions.44
        @Override // fj.F
        public Array<Character> f(String s) {
            return List.fromString(s).toArray();
        }
    };
    public static final F<String, Option<Character>> String_Option = new F<String, Option<Character>>() { // from class: fj.data.Conversions.45
        @Override // fj.F
        public Option<Character> f(String s) {
            return List.fromString(s).toOption();
        }
    };
    public static final F<String, Stream<Character>> String_Stream = new F<String, Stream<Character>>() { // from class: fj.data.Conversions.47
        @Override // fj.F
        public Stream<Character> f(String s) {
            return List.fromString(s).toStream();
        }
    };
    public static final F<String, StringBuffer> String_StringBuffer = new F<String, StringBuffer>() { // from class: fj.data.Conversions.48
        @Override // fj.F
        public StringBuffer f(String s) {
            return new StringBuffer(s);
        }
    };
    public static final F<String, StringBuilder> String_StringBuilder = new F<String, StringBuilder>() { // from class: fj.data.Conversions.49
        @Override // fj.F
        public StringBuilder f(String s) {
            return new StringBuilder(s);
        }
    };
    public static final F<StringBuffer, List<Character>> StringBuffer_List = new F<StringBuffer, List<Character>>() { // from class: fj.data.Conversions.50
        @Override // fj.F
        public List<Character> f(StringBuffer s) {
            return List.fromString(s.toString());
        }
    };
    public static final F<StringBuffer, Array<Character>> StringBuffer_Array = new F<StringBuffer, Array<Character>>() { // from class: fj.data.Conversions.51
        @Override // fj.F
        public Array<Character> f(StringBuffer s) {
            return List.fromString(s.toString()).toArray();
        }
    };
    public static final F<StringBuffer, Stream<Character>> StringBuffer_Stream = new F<StringBuffer, Stream<Character>>() { // from class: fj.data.Conversions.52
        @Override // fj.F
        public Stream<Character> f(StringBuffer s) {
            return List.fromString(s.toString()).toStream();
        }
    };
    public static final F<StringBuffer, Option<Character>> StringBuffer_Option = new F<StringBuffer, Option<Character>>() { // from class: fj.data.Conversions.53
        @Override // fj.F
        public Option<Character> f(StringBuffer s) {
            return List.fromString(s.toString()).toOption();
        }
    };
    public static final F<StringBuffer, String> StringBuffer_String = new F<StringBuffer, String>() { // from class: fj.data.Conversions.55
        @Override // fj.F
        public String f(StringBuffer s) {
            return s.toString();
        }
    };
    public static final F<StringBuffer, StringBuilder> StringBuffer_StringBuilder = new F<StringBuffer, StringBuilder>() { // from class: fj.data.Conversions.56
        @Override // fj.F
        public StringBuilder f(StringBuffer s) {
            return new StringBuilder(s);
        }
    };
    public static final F<StringBuilder, List<Character>> StringBuilder_List = new F<StringBuilder, List<Character>>() { // from class: fj.data.Conversions.57
        @Override // fj.F
        public List<Character> f(StringBuilder s) {
            return List.fromString(s.toString());
        }
    };
    public static final F<StringBuilder, Array<Character>> StringBuilder_Array = new F<StringBuilder, Array<Character>>() { // from class: fj.data.Conversions.58
        @Override // fj.F
        public Array<Character> f(StringBuilder s) {
            return List.fromString(s.toString()).toArray();
        }
    };
    public static final F<StringBuilder, Stream<Character>> StringBuilder_Stream = new F<StringBuilder, Stream<Character>>() { // from class: fj.data.Conversions.59
        @Override // fj.F
        public Stream<Character> f(StringBuilder s) {
            return List.fromString(s.toString()).toStream();
        }
    };
    public static final F<StringBuilder, Option<Character>> StringBuilder_Option = new F<StringBuilder, Option<Character>>() { // from class: fj.data.Conversions.60
        @Override // fj.F
        public Option<Character> f(StringBuilder s) {
            return List.fromString(s.toString()).toOption();
        }
    };
    public static final F<StringBuilder, String> StringBuilder_String = new F<StringBuilder, String>() { // from class: fj.data.Conversions.62
        @Override // fj.F
        public String f(StringBuilder s) {
            return s.toString();
        }
    };
    public static final F<StringBuilder, StringBuffer> StringBuilder_StringBuffer = new F<StringBuilder, StringBuffer>() { // from class: fj.data.Conversions.63
        @Override // fj.F
        public StringBuffer f(StringBuilder s) {
            return new StringBuffer(s);
        }
    };

    private Conversions() {
        throw new UnsupportedOperationException();
    }

    public static <A> F<List<A>, Array<A>> List_Array() {
        return new F<List<A>, Array<A>>() { // from class: fj.data.Conversions.1
            @Override // fj.F
            public Array<A> f(List<A> as) {
                return as.toArray();
            }
        };
    }

    public static <A> F<List<A>, Stream<A>> List_Stream() {
        return new F<List<A>, Stream<A>>() { // from class: fj.data.Conversions.2
            @Override // fj.F
            public Stream<A> f(List<A> as) {
                return as.toStream();
            }
        };
    }

    public static <A> F<List<A>, Option<A>> List_Option() {
        return new F<List<A>, Option<A>>() { // from class: fj.data.Conversions.3
            @Override // fj.F
            public Option<A> f(List<A> as) {
                return as.toOption();
            }
        };
    }

    public static <A, B> F<P1<A>, F<List<B>, Either<A, B>>> List_Either() {
        return new F<P1<A>, F<List<B>, Either<A, B>>>() { // from class: fj.data.Conversions.4
            @Override // fj.F
            public F<List<B>, Either<A, B>> f(final P1<A> a) {
                return new F<List<B>, Either<A, B>>() { // from class: fj.data.Conversions.4.1
                    {
                        AnonymousClass4.this = this;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Either<A, B> f(List<B> bs) {
                        return bs.toEither(a);
                    }
                };
            }
        };
    }

    public static <A> F<Array<A>, List<A>> Array_List() {
        return new F<Array<A>, List<A>>() { // from class: fj.data.Conversions.8
            @Override // fj.F
            public List<A> f(Array<A> as) {
                return as.toList();
            }
        };
    }

    public static <A> F<Array<A>, Stream<A>> Array_Stream() {
        return new F<Array<A>, Stream<A>>() { // from class: fj.data.Conversions.9
            @Override // fj.F
            public Stream<A> f(Array<A> as) {
                return as.toStream();
            }
        };
    }

    public static <A> F<Array<A>, Option<A>> Array_Option() {
        return new F<Array<A>, Option<A>>() { // from class: fj.data.Conversions.10
            @Override // fj.F
            public Option<A> f(Array<A> as) {
                return as.toOption();
            }
        };
    }

    public static <A, B> F<P1<A>, F<Array<B>, Either<A, B>>> Array_Either() {
        return new F<P1<A>, F<Array<B>, Either<A, B>>>() { // from class: fj.data.Conversions.11
            @Override // fj.F
            public F<Array<B>, Either<A, B>> f(final P1<A> a) {
                return new F<Array<B>, Either<A, B>>() { // from class: fj.data.Conversions.11.1
                    {
                        AnonymousClass11.this = this;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Either<A, B> f(Array<B> bs) {
                        return bs.toEither(a);
                    }
                };
            }
        };
    }

    /* renamed from: fj.data.Conversions$14 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$14.class */
    static class AnonymousClass14 implements F<Array<Character>, StringBuilder> {
        AnonymousClass14() {
        }

        @Override // fj.F
        public StringBuilder f(Array<Character> cs) {
            StringBuilder sb = new StringBuilder();
            cs.foreachDoEffect(Conversions$14$$Lambda$1.lambdaFactory$(sb));
            return sb;
        }
    }

    public static <A> F<Stream<A>, List<A>> Stream_List() {
        return new F<Stream<A>, List<A>>() { // from class: fj.data.Conversions.15
            @Override // fj.F
            public List<A> f(Stream<A> as) {
                return as.toList();
            }
        };
    }

    public static <A> F<Stream<A>, Array<A>> Stream_Array() {
        return new F<Stream<A>, Array<A>>() { // from class: fj.data.Conversions.16
            @Override // fj.F
            public Array<A> f(Stream<A> as) {
                return as.toArray();
            }
        };
    }

    public static <A> F<Stream<A>, Option<A>> Stream_Option() {
        return new F<Stream<A>, Option<A>>() { // from class: fj.data.Conversions.17
            @Override // fj.F
            public Option<A> f(Stream<A> as) {
                return as.toOption();
            }
        };
    }

    public static <A, B> F<P1<A>, F<Stream<B>, Either<A, B>>> Stream_Either() {
        return new F<P1<A>, F<Stream<B>, Either<A, B>>>() { // from class: fj.data.Conversions.18
            @Override // fj.F
            public F<Stream<B>, Either<A, B>> f(final P1<A> a) {
                return new F<Stream<B>, Either<A, B>>() { // from class: fj.data.Conversions.18.1
                    {
                        AnonymousClass18.this = this;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Either<A, B> f(Stream<B> bs) {
                        return bs.toEither(a);
                    }
                };
            }
        };
    }

    /* renamed from: fj.data.Conversions$19 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$19.class */
    static class AnonymousClass19 implements F<Stream<Character>, String> {
        AnonymousClass19() {
        }

        @Override // fj.F
        public String f(Stream<Character> cs) {
            StringBuilder sb = new StringBuilder();
            cs.foreachDoEffect(Conversions$19$$Lambda$1.lambdaFactory$(sb));
            return sb.toString();
        }
    }

    /* renamed from: fj.data.Conversions$20 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$20.class */
    static class AnonymousClass20 implements F<Stream<Character>, StringBuffer> {
        AnonymousClass20() {
        }

        @Override // fj.F
        public StringBuffer f(Stream<Character> cs) {
            StringBuffer sb = new StringBuffer();
            cs.foreachDoEffect(Conversions$20$$Lambda$1.lambdaFactory$(sb));
            return sb;
        }
    }

    /* renamed from: fj.data.Conversions$21 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Conversions$21.class */
    static class AnonymousClass21 implements F<Stream<Character>, StringBuilder> {
        AnonymousClass21() {
        }

        @Override // fj.F
        public StringBuilder f(Stream<Character> cs) {
            StringBuilder sb = new StringBuilder();
            cs.foreachDoEffect(Conversions$21$$Lambda$1.lambdaFactory$(sb));
            return sb;
        }

        public static /* synthetic */ void lambda$f$91(StringBuilder sb, Character c) {
            sb.append(c);
        }
    }

    public static <A> F<Option<A>, List<A>> Option_List() {
        return new F<Option<A>, List<A>>() { // from class: fj.data.Conversions.22
            @Override // fj.F
            public List<A> f(Option<A> o) {
                return o.toList();
            }
        };
    }

    public static <A> F<Option<A>, Array<A>> Option_Array() {
        return new F<Option<A>, Array<A>>() { // from class: fj.data.Conversions.23
            @Override // fj.F
            public Array<A> f(Option<A> o) {
                return o.toArray();
            }
        };
    }

    public static <A> F<Option<A>, Stream<A>> Option_Stream() {
        return new F<Option<A>, Stream<A>>() { // from class: fj.data.Conversions.24
            @Override // fj.F
            public Stream<A> f(Option<A> o) {
                return o.toStream();
            }
        };
    }

    public static <A, B> F<P1<A>, F<Option<B>, Either<A, B>>> Option_Either() {
        return new F<P1<A>, F<Option<B>, Either<A, B>>>() { // from class: fj.data.Conversions.25
            @Override // fj.F
            public F<Option<B>, Either<A, B>> f(final P1<A> a) {
                return new F<Option<B>, Either<A, B>>() { // from class: fj.data.Conversions.25.1
                    {
                        AnonymousClass25.this = this;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.F
                    public Either<A, B> f(Option<B> o) {
                        return o.toEither(a);
                    }
                };
            }
        };
    }

    public static F<Effect0, P1<Unit>> Effect0_P1() {
        F<Effect0, P1<Unit>> f;
        f = Conversions$$Lambda$1.instance;
        return f;
    }

    public static P1<Unit> Effect0_P1(Effect0 e) {
        return Effect.f(e);
    }

    public static <A> F<A, Unit> Effect1_F(Effect1<A> e) {
        return Effect.f(e);
    }

    public static <A> F<Effect1<A>, F<A, Unit>> Effect1_F() {
        F<Effect1<A>, F<A, Unit>> f;
        f = Conversions$$Lambda$2.instance;
        return f;
    }

    public static IO<Unit> Effect_IO(Effect0 e) {
        return Conversions$$Lambda$3.lambdaFactory$(e);
    }

    public static /* synthetic */ Unit lambda$Effect_IO$94(Effect0 effect0) throws IOException {
        effect0.f();
        return Unit.unit();
    }

    public static F<Effect0, IO<Unit>> Effect_IO() {
        F<Effect0, IO<Unit>> f;
        f = Conversions$$Lambda$4.instance;
        return f;
    }

    public static SafeIO<Unit> Effect_SafeIO(Effect0 e) {
        return Conversions$$Lambda$5.lambdaFactory$(e);
    }

    public static /* synthetic */ Unit lambda$Effect_SafeIO$96(Effect0 effect0) {
        effect0.f();
        return Unit.unit();
    }

    public static F<Effect0, SafeIO<Unit>> Effect_SafeIO() {
        F<Effect0, SafeIO<Unit>> f;
        f = Conversions$$Lambda$6.instance;
        return f;
    }

    public static <A, B> F<Either<A, B>, List<A>> Either_ListA() {
        return new F<Either<A, B>, List<A>>() { // from class: fj.data.Conversions.29
            @Override // fj.F
            public List<A> f(Either<A, B> e) {
                return e.left().toList();
            }
        };
    }

    public static <A, B> F<Either<A, B>, List<B>> Either_ListB() {
        return new F<Either<A, B>, List<B>>() { // from class: fj.data.Conversions.30
            @Override // fj.F
            public List<B> f(Either<A, B> e) {
                return e.right().toList();
            }
        };
    }

    public static <A, B> F<Either<A, B>, Array<A>> Either_ArrayA() {
        return new F<Either<A, B>, Array<A>>() { // from class: fj.data.Conversions.31
            @Override // fj.F
            public Array<A> f(Either<A, B> e) {
                return e.left().toArray();
            }
        };
    }

    public static <A, B> F<Either<A, B>, Array<B>> Either_ArrayB() {
        return new F<Either<A, B>, Array<B>>() { // from class: fj.data.Conversions.32
            @Override // fj.F
            public Array<B> f(Either<A, B> e) {
                return e.right().toArray();
            }
        };
    }

    public static <A, B> F<Either<A, B>, Stream<A>> Either_StreamA() {
        return new F<Either<A, B>, Stream<A>>() { // from class: fj.data.Conversions.33
            @Override // fj.F
            public Stream<A> f(Either<A, B> e) {
                return e.left().toStream();
            }
        };
    }

    public static <A, B> F<Either<A, B>, Stream<B>> Either_StreamB() {
        return new F<Either<A, B>, Stream<B>>() { // from class: fj.data.Conversions.34
            @Override // fj.F
            public Stream<B> f(Either<A, B> e) {
                return e.right().toStream();
            }
        };
    }

    public static <A, B> F<Either<A, B>, Option<A>> Either_OptionA() {
        return new F<Either<A, B>, Option<A>>() { // from class: fj.data.Conversions.35
            @Override // fj.F
            public Option<A> f(Either<A, B> e) {
                return e.left().toOption();
            }
        };
    }

    public static <A, B> F<Either<A, B>, Option<B>> Either_OptionB() {
        return new F<Either<A, B>, Option<B>>() { // from class: fj.data.Conversions.36
            @Override // fj.F
            public Option<B> f(Either<A, B> e) {
                return e.right().toOption();
            }
        };
    }

    public static <B> F<Either<Character, B>, String> Either_StringA() {
        return new F<Either<Character, B>, String>() { // from class: fj.data.Conversions.37
            @Override // fj.F
            public String f(Either<Character, B> e) {
                return List.asString(e.left().toList());
            }
        };
    }

    public static <A> F<Either<A, Character>, String> Either_StringB() {
        return new F<Either<A, Character>, String>() { // from class: fj.data.Conversions.38
            @Override // fj.F
            public String f(Either<A, Character> e) {
                return List.asString(e.right().toList());
            }
        };
    }

    public static <B> F<Either<Character, B>, StringBuffer> Either_StringBufferA() {
        return new F<Either<Character, B>, StringBuffer>() { // from class: fj.data.Conversions.39
            @Override // fj.F
            public StringBuffer f(Either<Character, B> e) {
                return new StringBuffer(List.asString(e.left().toList()));
            }
        };
    }

    public static <A> F<Either<A, Character>, StringBuffer> Either_StringBufferB() {
        return new F<Either<A, Character>, StringBuffer>() { // from class: fj.data.Conversions.40
            @Override // fj.F
            public StringBuffer f(Either<A, Character> e) {
                return new StringBuffer(List.asString(e.right().toList()));
            }
        };
    }

    public static <B> F<Either<Character, B>, StringBuilder> Either_StringBuilderA() {
        return new F<Either<Character, B>, StringBuilder>() { // from class: fj.data.Conversions.41
            @Override // fj.F
            public StringBuilder f(Either<Character, B> e) {
                return new StringBuilder(List.asString(e.left().toList()));
            }
        };
    }

    public static <A> F<Either<A, Character>, StringBuilder> Either_StringBuilderB() {
        return new F<Either<A, Character>, StringBuilder>() { // from class: fj.data.Conversions.42
            @Override // fj.F
            public StringBuilder f(Either<A, Character> e) {
                return new StringBuilder(List.asString(e.right().toList()));
            }
        };
    }

    public static <A> SafeIO<A> F_SafeIO(F<Unit, A> f) {
        return Conversions$$Lambda$7.lambdaFactory$(f);
    }

    public static /* synthetic */ Object lambda$F_SafeIO$98(F f) {
        return f.f(Unit.unit());
    }

    public static <A> F<F<Unit, A>, SafeIO<A>> F_SafeIO() {
        F<F<Unit, A>, SafeIO<A>> f;
        f = Conversions$$Lambda$8.instance;
        return f;
    }

    public static <A> F<P1<A>, F<String, Either<A, Character>>> String_Either() {
        return new F<P1<A>, F<String, Either<A, Character>>>() { // from class: fj.data.Conversions.46
            @Override // fj.F
            public F<String, Either<A, Character>> f(final P1<A> a) {
                return new F<String, Either<A, Character>>() { // from class: fj.data.Conversions.46.1
                    {
                        AnonymousClass46.this = this;
                    }

                    @Override // fj.F
                    public Either<A, Character> f(String s) {
                        return List.fromString(s).toEither(a);
                    }
                };
            }
        };
    }

    public static <A> F<P1<A>, F<StringBuffer, Either<A, Character>>> StringBuffer_Either() {
        return new F<P1<A>, F<StringBuffer, Either<A, Character>>>() { // from class: fj.data.Conversions.54
            @Override // fj.F
            public F<StringBuffer, Either<A, Character>> f(final P1<A> a) {
                return new F<StringBuffer, Either<A, Character>>() { // from class: fj.data.Conversions.54.1
                    {
                        AnonymousClass54.this = this;
                    }

                    @Override // fj.F
                    public Either<A, Character> f(StringBuffer s) {
                        return List.fromString(s.toString()).toEither(a);
                    }
                };
            }
        };
    }

    public static <A> F<P1<A>, F<StringBuilder, Either<A, Character>>> StringBuilder_Either() {
        return new F<P1<A>, F<StringBuilder, Either<A, Character>>>() { // from class: fj.data.Conversions.61
            @Override // fj.F
            public F<StringBuilder, Either<A, Character>> f(final P1<A> a) {
                return new F<StringBuilder, Either<A, Character>>() { // from class: fj.data.Conversions.61.1
                    {
                        AnonymousClass61.this = this;
                    }

                    @Override // fj.F
                    public Either<A, Character> f(StringBuilder s) {
                        return List.fromString(s.toString()).toEither(a);
                    }
                };
            }
        };
    }

    public static <A, B, Z extends Exception> SafeIO<Validation<Z, A>> Try_SafeIO(Try0<A, Z> t) {
        return F_SafeIO(Conversions$$Lambda$9.lambdaFactory$(t));
    }

    public static /* synthetic */ Validation lambda$Try_SafeIO$100(Try0 try0, Unit u) {
        return (Validation) Try.f(try0)._1();
    }

    public static <A, B, Z extends Exception> F<Try0<A, Z>, SafeIO<Validation<Z, A>>> Try_SafeIO() {
        F<Try0<A, Z>, SafeIO<Validation<Z, A>>> f;
        f = Conversions$$Lambda$10.instance;
        return f;
    }

    public static <A, B, Z extends IOException> IO<A> Try_IO(Try0<A, Z> t) {
        return Conversions$$Lambda$11.lambdaFactory$(t);
    }

    public static <A, B, Z extends IOException> F<Try0<A, Z>, IO<A>> Try_IO() {
        F<Try0<A, Z>, IO<A>> f;
        f = Conversions$$Lambda$12.instance;
        return f;
    }

    public static <A, B, Z extends IOException> F<A, Validation<Z, B>> Try_F(Try1<A, B, Z> t) {
        return Try.f(t);
    }

    public static <A, B, Z extends IOException> F<Try1<A, B, Z>, F<A, Validation<Z, B>>> Try_F() {
        F<Try1<A, B, Z>, F<A, Validation<Z, B>>> f;
        f = Conversions$$Lambda$13.instance;
        return f;
    }

    public static <E extends Exception> P1<Validation<E, Unit>> TryEffect_P(TryEffect0<E> t) {
        return TryEffect.f(t);
    }
}
