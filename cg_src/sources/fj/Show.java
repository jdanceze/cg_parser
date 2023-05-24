package fj;

import fj.data.Array;
import fj.data.Either;
import fj.data.LazyString;
import fj.data.List;
import fj.data.Natural;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Stream;
import fj.data.Tree;
import fj.data.Validation;
import fj.data.hlist.HList;
import fj.data.vector.V2;
import fj.data.vector.V3;
import fj.data.vector.V4;
import fj.data.vector.V5;
import fj.data.vector.V6;
import fj.data.vector.V7;
import fj.data.vector.V8;
import java.math.BigDecimal;
import java.math.BigInteger;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Show.class */
public final class Show<A> {
    private final F<A, Stream<Character>> f;
    public static final Show<Boolean> booleanShow = anyShow();
    public static final Show<Byte> byteShow = anyShow();
    public static final Show<Character> charShow = anyShow();
    public static final Show<Double> doubleShow = anyShow();
    public static final Show<Float> floatShow = anyShow();
    public static final Show<Integer> intShow = anyShow();
    public static final Show<BigInteger> bigintShow = anyShow();
    public static final Show<BigDecimal> bigdecimalShow = anyShow();
    public static final Show<Long> longShow = anyShow();
    public static final Show<Short> shortShow = anyShow();
    public static final Show<String> stringShow = anyShow();
    public static final Show<StringBuffer> stringBufferShow = anyShow();
    public static final Show<StringBuilder> stringBuilderShow = anyShow();
    public static final Show<Natural> naturalShow = bigintShow.comap(new F<Natural, BigInteger>() { // from class: fj.Show.20
        @Override // fj.F
        public BigInteger f(Natural natural) {
            return natural.bigIntegerValue();
        }
    });
    public static final Show<LazyString> lazyStringShow = show((F) new F<LazyString, Stream<Character>>() { // from class: fj.Show.22
        @Override // fj.F
        public Stream<Character> f(LazyString string) {
            return string.toStream();
        }
    });
    public static final Show<HList.HNil> HListShow = showS(Function.constant("Nil"));

    private Show(F<A, Stream<Character>> f) {
        this.f = f;
    }

    public <B> Show<B> comap(F<B, A> f) {
        return show(Function.compose(this.f, f));
    }

    public Stream<Character> show(A a) {
        return this.f.f(a);
    }

    public List<Character> showl(A a) {
        return show((Show<A>) a).toList();
    }

    public String showS(A a) {
        return Stream.asString(show((Show<A>) a));
    }

    public F<A, String> showS_() {
        return new F<A, String>() { // from class: fj.Show.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ String f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // fj.F
            public String f(A a) {
                return Show.this.showS((Show) a);
            }
        };
    }

    public F<A, Stream<Character>> show_() {
        return this.f;
    }

    public Unit println(A a) {
        print(a);
        System.out.println();
        return Unit.unit();
    }

    public Unit print(A a) {
        char[] buffer = new char[8192];
        int c = 0;
        Stream<Character> show = show((Show<A>) a);
        while (true) {
            Stream<Character> cs = show;
            if (cs.isNotEmpty()) {
                buffer[c] = cs.head().charValue();
                c++;
                if (c == 8192) {
                    System.out.print(buffer);
                    c = 0;
                }
                show = cs.tail()._1();
            } else {
                System.out.print(Array.copyOfRange(buffer, 0, c));
                return Unit.unit();
            }
        }
    }

    public void printlnE(A a) {
        System.err.println(showS((Show<A>) a));
    }

    public static <A> Show<A> show(F<A, Stream<Character>> f) {
        return new Show<>(f);
    }

    public static <A> Show<A> showS(final F<A, String> f) {
        return new Show<>(new F<A, Stream<Character>>() { // from class: fj.Show.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Stream<Character> f(Object obj) {
                return f((AnonymousClass2) obj);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // fj.F
            public Stream<Character> f(A a) {
                return Stream.fromString((String) F.this.f(a));
            }
        });
    }

    public static <A> Show<A> anyShow() {
        return new Show<>(new F<A, Stream<Character>>() { // from class: fj.Show.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Stream<Character> f(Object obj) {
                return f((AnonymousClass3) obj);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // fj.F
            public Stream<Character> f(A a) {
                return Stream.fromString(a == null ? Jimple.NULL : a.toString());
            }
        });
    }

    public static <A> Show<Option<A>> optionShow(Show<A> sa) {
        return new Show<>(new F<Option<A>, Stream<Character>>() { // from class: fj.Show.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Stream<Character> f(Object obj) {
                return f((Option) ((Option) obj));
            }

            public Stream<Character> f(Option<A> o) {
                if (!o.isNone()) {
                    return Stream.fromString("Some(").append((Stream) Show.this.f.f(o.some())).append(Stream.single(')'));
                }
                return Stream.fromString("None");
            }
        });
    }

    public static <A, B> Show<Either<A, B>> eitherShow(Show<A> sa, final Show<B> sb) {
        return new Show<>(new F<Either<A, B>, Stream<Character>>() { // from class: fj.Show.5
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<Character> f(Either<A, B> e) {
                return e.isLeft() ? Stream.fromString("Left(").append((Stream) Show.this.f.f(e.left().value())).append(Stream.single(')')) : Stream.fromString("Right(").append((Stream) sb.f.f(e.right().value())).append(Stream.single(')'));
            }
        });
    }

    public static <A, B> Show<Validation<A, B>> validationShow(Show<A> sa, final Show<B> sb) {
        return new Show<>(new F<Validation<A, B>, Stream<Character>>() { // from class: fj.Show.6
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<Character> f(Validation<A, B> v) {
                return v.isFail() ? Stream.fromString("Fail(").append((Stream) Show.this.f.f(v.fail())).append(Stream.single(')')) : Stream.fromString("Success(").append((Stream) sb.f.f(v.success())).append(Stream.single(')'));
            }
        });
    }

    public static <A> Show<List<A>> listShow(Show<A> sa) {
        return new Show<>(new F<List<A>, Stream<Character>>() { // from class: fj.Show.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Stream<Character> f(Object obj) {
                return f((List) ((List) obj));
            }

            public Stream<Character> f(List<A> as) {
                return Show.streamShow(Show.this).show((Show) as.toStream());
            }
        });
    }

    public static <A> Show<NonEmptyList<A>> nonEmptyListShow(Show<A> sa) {
        return listShow(sa).comap(NonEmptyList.toList_());
    }

    public static <A> Show<Tree<A>> treeShow(Show<A> sa) {
        return new Show<>(new F<Tree<A>, Stream<Character>>() { // from class: fj.Show.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Stream<Character> f(Object obj) {
                return f((Tree) ((Tree) obj));
            }

            public Stream<Character> f(Tree<A> a) {
                return Stream.cons('(', P.p(((Stream) Show.this.f.f(a.root())).append((Stream) Show.p1Show(Show.streamShow(Show.treeShow(Show.this))).f.f(a.subForest())).snoc((Stream<A>) ((A) ')'))));
            }
        });
    }

    public static <A> Show<Stream<A>> streamShow(Show<A> sa) {
        return new Show<>(new F<Stream<A>, Stream<Character>>() { // from class: fj.Show.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Stream<Character> f(Object obj) {
                return f((Stream) ((Stream) obj));
            }

            public Stream<Character> f(Stream<A> as) {
                return Stream.join(as.map(Show.this.show_()).intersperse(Stream.fromString(",")).cons(Stream.fromString("<")).snoc((P1) P.p(Stream.fromString(">"))));
            }
        });
    }

    public static <A> Show<Array<A>> arrayShow(Show<A> sa) {
        return new Show<>(new F<Array<A>, Stream<Character>>() { // from class: fj.Show.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Stream<Character> f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public Stream<Character> f(Array<A> as) {
                Stream<Character> b = Stream.nil();
                for (int i = 0; i < as.length(); i++) {
                    b = b.append((Stream) Show.this.f.f(as.get(i)));
                    if (i != as.length() - 1) {
                        b = b.snoc((Stream<Character>) ',');
                    }
                }
                return Stream.cons('{', P.p(b.snoc((Stream<Character>) '}')));
            }
        });
    }

    public static <A> Show<Class<A>> classShow() {
        return new Show<>(new F<Class<A>, Stream<Character>>() { // from class: fj.Show.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Stream<Character> f(Object obj) {
                return f((Class) ((Class) obj));
            }

            public Stream<Character> f(Class<A> c) {
                return Show.anyShow().show((Show) c.clas());
            }
        });
    }

    public static <A> Show<P1<A>> p1Show(Show<A> sa) {
        return new Show<>(new F<P1<A>, Stream<Character>>() { // from class: fj.Show.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Stream<Character> f(Object obj) {
                return f((P1) ((P1) obj));
            }

            public Stream<Character> f(P1<A> p) {
                return Stream.cons('(', P.p(Show.this.show((Show) p._1()))).snoc((Stream) ')');
            }
        });
    }

    public static <A, B> Show<P2<A, B>> p2Show(Show<A> sa, final Show<B> sb) {
        return new Show<>(new F<P2<A, B>, Stream<Character>>() { // from class: fj.Show.13
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<Character> f(P2<A, B> p) {
                return Stream.cons('(', P.p(Show.this.show((Show) p._1()))).snoc((Stream) ',').append(sb.show((Show) p._2())).snoc((Stream<A>) ')');
            }
        });
    }

    public static <A, B, C> Show<P3<A, B, C>> p3Show(Show<A> sa, final Show<B> sb, final Show<C> sc) {
        return new Show<>(new F<P3<A, B, C>, Stream<Character>>() { // from class: fj.Show.14
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<Character> f(P3<A, B, C> p) {
                return Stream.cons('(', P.p(Show.this.show((Show) p._1()))).snoc((Stream) ',').append(sb.show((Show) p._2())).snoc((Stream<A>) ',').append(sc.show((Show) p._3())).snoc((Stream<A>) ')');
            }
        });
    }

    public static <A, B, C, D> Show<P4<A, B, C, D>> p4Show(Show<A> sa, final Show<B> sb, final Show<C> sc, final Show<D> sd) {
        return new Show<>(new F<P4<A, B, C, D>, Stream<Character>>() { // from class: fj.Show.15
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<Character> f(P4<A, B, C, D> p) {
                return Stream.cons('(', P.p(Show.this.show((Show) p._1()))).snoc((Stream) ',').append(sb.show((Show) p._2())).snoc((Stream<A>) ',').append(sc.show((Show) p._3())).snoc((Stream<A>) ',').append(sd.show((Show) p._4())).snoc((Stream<A>) ')');
            }
        });
    }

    public static <A, B, C, D, E> Show<P5<A, B, C, D, E>> p5Show(Show<A> sa, final Show<B> sb, final Show<C> sc, final Show<D> sd, final Show<E> se) {
        return new Show<>(new F<P5<A, B, C, D, E>, Stream<Character>>() { // from class: fj.Show.16
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<Character> f(P5<A, B, C, D, E> p) {
                return Stream.cons('(', P.p(Show.this.show((Show) p._1()))).snoc((Stream) ',').append(sb.show((Show) p._2())).snoc((Stream<A>) ',').append(sc.show((Show) p._3())).snoc((Stream<A>) ',').append(sd.show((Show) p._4())).snoc((Stream<A>) ',').append(se.show((Show) p._5())).snoc((Stream<A>) ')');
            }
        });
    }

    public static <A, B, C, D, E, F$> Show<P6<A, B, C, D, E, F$>> p6Show(Show<A> sa, final Show<B> sb, final Show<C> sc, final Show<D> sd, final Show<E> se, final Show<F$> sf) {
        return new Show<>(new F<P6<A, B, C, D, E, F$>, Stream<Character>>() { // from class: fj.Show.17
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<Character> f(P6<A, B, C, D, E, F$> p) {
                return Stream.cons('(', P.p(Show.this.show((Show) p._1()))).snoc((Stream) ',').append(sb.show((Show) p._2())).snoc((Stream<A>) ',').append(sc.show((Show) p._3())).snoc((Stream<A>) ',').append(sd.show((Show) p._4())).snoc((Stream<A>) ',').append(se.show((Show) p._5())).snoc((Stream<A>) ',').append(sf.show((Show) p._6())).snoc((Stream<A>) ')');
            }
        });
    }

    public static <A, B, C, D, E, F$, G> Show<P7<A, B, C, D, E, F$, G>> p7Show(Show<A> sa, final Show<B> sb, final Show<C> sc, final Show<D> sd, final Show<E> se, final Show<F$> sf, final Show<G> sg) {
        return new Show<>(new F<P7<A, B, C, D, E, F$, G>, Stream<Character>>() { // from class: fj.Show.18
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<Character> f(P7<A, B, C, D, E, F$, G> p) {
                return Stream.cons('(', P.p(Show.this.show((Show) p._1()))).snoc((Stream) ',').append(sb.show((Show) p._2())).snoc((Stream<A>) ',').append(sc.show((Show) p._3())).snoc((Stream<A>) ',').append(sd.show((Show) p._4())).snoc((Stream<A>) ',').append(se.show((Show) p._5())).snoc((Stream<A>) ',').append(sf.show((Show) p._6())).snoc((Stream<A>) ',').append(sg.show((Show) p._7())).snoc((Stream<A>) ')');
            }
        });
    }

    public static <A, B, C, D, E, F$, G, H> Show<P8<A, B, C, D, E, F$, G, H>> p8Show(Show<A> sa, final Show<B> sb, final Show<C> sc, final Show<D> sd, final Show<E> se, final Show<F$> sf, final Show<G> sg, final Show<H> sh) {
        return new Show<>(new F<P8<A, B, C, D, E, F$, G, H>, Stream<Character>>() { // from class: fj.Show.19
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<Character> f(P8<A, B, C, D, E, F$, G, H> p) {
                return Stream.cons('(', P.p(Show.this.show((Show) p._1()))).snoc((Stream) ',').append(sb.show((Show) p._2())).snoc((Stream<A>) ',').append(sc.show((Show) p._3())).snoc((Stream<A>) ',').append(sd.show((Show) p._4())).snoc((Stream<A>) ',').append(se.show((Show) p._5())).snoc((Stream<A>) ',').append(sf.show((Show) p._6())).snoc((Stream<A>) ',').append(sg.show((Show) p._7())).snoc((Stream<A>) ',').append(sh.show((Show) p._8())).snoc((Stream<A>) ')');
            }
        });
    }

    public static <A> Show<V2<A>> v2Show(Show<A> ea) {
        return streamShow(ea).comap(V2.toStream_());
    }

    public static <A> Show<V3<A>> v3Show(Show<A> ea) {
        return streamShow(ea).comap(V3.toStream_());
    }

    public static <A> Show<V4<A>> v4Show(Show<A> ea) {
        return streamShow(ea).comap(V4.toStream_());
    }

    public static <A> Show<V5<A>> v5Show(Show<A> ea) {
        return streamShow(ea).comap(V5.toStream_());
    }

    public static <A> Show<V6<A>> v6Show(Show<A> ea) {
        return streamShow(ea).comap(V6.toStream_());
    }

    public static <A> Show<V7<A>> v7Show(Show<A> ea) {
        return streamShow(ea).comap(V7.toStream_());
    }

    public static <A> Show<V8<A>> v8Show(Show<A> ea) {
        return streamShow(ea).comap(V8.toStream_());
    }

    public static <A> Show<Stream<A>> unlineShow(Show<A> sa) {
        return new Show<>(new F<Stream<A>, Stream<Character>>() { // from class: fj.Show.21
            @Override // fj.F
            public /* bridge */ /* synthetic */ Stream<Character> f(Object obj) {
                return f((Stream) ((Stream) obj));
            }

            public Stream<Character> f(Stream<A> as) {
                return Stream.join(as.map(Show.this.show_()).intersperse(Stream.fromString("\n")));
            }
        });
    }

    public static <E, L extends HList<L>> Show<HList.HCons<E, L>> HListShow(Show<E> e, final Show<L> l) {
        return show((F) new F<HList.HCons<E, L>, Stream<Character>>() { // from class: fj.Show.23
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Stream<Character> f(HList.HCons<E, L> c) {
                return Show.this.show((Show) c.head()).cons('[').append(l.show((Show) c.tail())).snoc((Stream<Character>) ']');
            }
        });
    }
}
