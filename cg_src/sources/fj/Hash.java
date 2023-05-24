package fj;

import fj.data.Array;
import fj.data.Either;
import fj.data.List;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Stream;
import fj.data.Tree;
import fj.data.Validation;
import fj.data.vector.V2;
import fj.data.vector.V3;
import fj.data.vector.V4;
import fj.data.vector.V5;
import fj.data.vector.V6;
import fj.data.vector.V7;
import fj.data.vector.V8;
import org.apache.http.HttpStatus;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Hash.class */
public final class Hash<A> {
    private final F<A, Integer> f;
    public static final Hash<Boolean> booleanHash = anyHash();
    public static final Hash<Byte> byteHash = anyHash();
    public static final Hash<Character> charHash = anyHash();
    public static final Hash<Double> doubleHash = anyHash();
    public static final Hash<Float> floatHash = anyHash();
    public static final Hash<Integer> intHash = anyHash();
    public static final Hash<Long> longHash = anyHash();
    public static final Hash<Short> shortHash = anyHash();
    public static final Hash<String> stringHash = anyHash();
    public static final Hash<StringBuffer> stringBufferHash = new Hash<>(new F<StringBuffer, Integer>() { // from class: fj.Hash.2
        @Override // fj.F
        public Integer f(StringBuffer sb) {
            int r = 239;
            for (int i = 0; i < sb.length(); i++) {
                r = (HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * r) + sb.charAt(i);
            }
            return Integer.valueOf(r);
        }
    });
    public static final Hash<StringBuilder> stringBuilderHash = new Hash<>(new F<StringBuilder, Integer>() { // from class: fj.Hash.3
        @Override // fj.F
        public Integer f(StringBuilder sb) {
            int r = 239;
            for (int i = 0; i < sb.length(); i++) {
                r = (HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * r) + sb.charAt(i);
            }
            return Integer.valueOf(r);
        }
    });

    private Hash(F<A, Integer> f) {
        this.f = f;
    }

    public int hash(A a) {
        return this.f.f(a).intValue();
    }

    public <B> Hash<B> comap(F<B, A> g) {
        return new Hash<>(Function.compose(this.f, g));
    }

    public static <A> Hash<A> hash(F<A, Integer> f) {
        return new Hash<>(f);
    }

    public static <A> Hash<A> anyHash() {
        return new Hash<>(new F<A, Integer>() { // from class: fj.Hash.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Integer f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // fj.F
            public Integer f(A a) {
                return Integer.valueOf(a.hashCode());
            }
        });
    }

    public static <A, B> Hash<Either<A, B>> eitherHash(Hash<A> ha, final Hash<B> hb) {
        return new Hash<>(new F<Either<A, B>, Integer>() { // from class: fj.Hash.4
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Integer f(Either<A, B> e) {
                return Integer.valueOf(e.isLeft() ? Hash.this.hash((Hash) e.left().value()) : hb.hash((Hash) e.right().value()));
            }
        });
    }

    public static <A, B> Hash<Validation<A, B>> validationHash(Hash<A> ha, Hash<B> hb) {
        return eitherHash(ha, hb).comap(Validation.either());
    }

    public static <A> Hash<List<A>> listHash(Hash<A> ha) {
        return new Hash<>(new F<List<A>, Integer>() { // from class: fj.Hash.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Integer f(Object obj) {
                return f((List) ((List) obj));
            }

            public Integer f(List<A> as) {
                int r = 239;
                List<A> list = as;
                while (true) {
                    List<A> aas = list;
                    if (!aas.isEmpty()) {
                        r = (HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * r) + Hash.this.hash((Hash) aas.head());
                        list = aas.tail();
                    } else {
                        return Integer.valueOf(r);
                    }
                }
            }
        });
    }

    public static <A> Hash<NonEmptyList<A>> nonEmptyListHash(Hash<A> ha) {
        return listHash(ha).comap(NonEmptyList.toList_());
    }

    public static <A> Hash<Option<A>> optionHash(Hash<A> ha) {
        return new Hash<>(new F<Option<A>, Integer>() { // from class: fj.Hash.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Integer f(Object obj) {
                return f((Option) ((Option) obj));
            }

            public Integer f(Option<A> o) {
                return Integer.valueOf(o.isNone() ? 0 : Hash.this.hash((Hash) o.some()));
            }
        });
    }

    public static <A> Hash<Stream<A>> streamHash(Hash<A> ha) {
        return new Hash<>(new F<Stream<A>, Integer>() { // from class: fj.Hash.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Integer f(Object obj) {
                return f((Stream) ((Stream) obj));
            }

            public Integer f(Stream<A> as) {
                int r = 239;
                Stream<A> stream = as;
                while (true) {
                    Stream<A> aas = stream;
                    if (!aas.isEmpty()) {
                        r = (HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * r) + Hash.this.hash((Hash) aas.head());
                        stream = aas.tail()._1();
                    } else {
                        return Integer.valueOf(r);
                    }
                }
            }
        });
    }

    public static <A> Hash<Array<A>> arrayHash(Hash<A> ha) {
        return new Hash<>(new F<Array<A>, Integer>() { // from class: fj.Hash.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Integer f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public Integer f(Array<A> as) {
                int r = 239;
                for (int i = 0; i < as.length(); i++) {
                    r = (HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * r) + Hash.this.hash((Hash) as.get(i));
                }
                return Integer.valueOf(r);
            }
        });
    }

    public static <A> Hash<Tree<A>> treeHash(Hash<A> ha) {
        return streamHash(ha).comap(Tree.flatten_());
    }

    public static <A> Hash<P1<A>> p1Hash(Hash<A> ha) {
        return (Hash<P1<A>>) ha.comap(P1.__1());
    }

    public static <A, B> Hash<P2<A, B>> p2Hash(Hash<A> ha, final Hash<B> hb) {
        return new Hash<>(new F<P2<A, B>, Integer>() { // from class: fj.Hash.9
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Integer f(P2<A, B> p2) {
                int r = (HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * 239) + Hash.this.hash((Hash) p2._1());
                return Integer.valueOf((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * r) + hb.hash((Hash) p2._2()));
            }
        });
    }

    public static <A, B, C> Hash<P3<A, B, C>> p3Hash(Hash<A> ha, final Hash<B> hb, final Hash<C> hc) {
        return new Hash<>(new F<P3<A, B, C>, Integer>() { // from class: fj.Hash.10
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Integer f(P3<A, B, C> p3) {
                int r = (HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * 239) + Hash.this.hash((Hash) p3._1());
                return Integer.valueOf((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * r) + hb.hash((Hash) p3._2()))) + hc.hash((Hash) p3._3()));
            }
        });
    }

    public static <A, B, C, D> Hash<P4<A, B, C, D>> p4Hash(Hash<A> ha, final Hash<B> hb, final Hash<C> hc, final Hash<D> hd) {
        return new Hash<>(new F<P4<A, B, C, D>, Integer>() { // from class: fj.Hash.11
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Integer f(P4<A, B, C, D> p4) {
                int r = (HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * 239) + Hash.this.hash((Hash) p4._1());
                return Integer.valueOf((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * r) + hb.hash((Hash) p4._2()))) + hc.hash((Hash) p4._3()))) + hd.hash((Hash) p4._4()));
            }
        });
    }

    public static <A, B, C, D, E> Hash<P5<A, B, C, D, E>> p5Hash(Hash<A> ha, final Hash<B> hb, final Hash<C> hc, final Hash<D> hd, final Hash<E> he) {
        return new Hash<>(new F<P5<A, B, C, D, E>, Integer>() { // from class: fj.Hash.12
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Integer f(P5<A, B, C, D, E> p5) {
                int r = (HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * 239) + Hash.this.hash((Hash) p5._1());
                return Integer.valueOf((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * r) + hb.hash((Hash) p5._2()))) + hc.hash((Hash) p5._3()))) + hd.hash((Hash) p5._4()))) + he.hash((Hash) p5._5()));
            }
        });
    }

    public static <A, B, C, D, E, F$> Hash<P6<A, B, C, D, E, F$>> p6Hash(Hash<A> ha, final Hash<B> hb, final Hash<C> hc, final Hash<D> hd, final Hash<E> he, final Hash<F$> hf) {
        return new Hash<>(new F<P6<A, B, C, D, E, F$>, Integer>() { // from class: fj.Hash.13
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Integer f(P6<A, B, C, D, E, F$> p6) {
                int r = (HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * 239) + Hash.this.hash((Hash) p6._1());
                return Integer.valueOf((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * r) + hb.hash((Hash) p6._2()))) + hc.hash((Hash) p6._3()))) + hd.hash((Hash) p6._4()))) + he.hash((Hash) p6._5()))) + hf.hash((Hash) p6._6()));
            }
        });
    }

    public static <A, B, C, D, E, F$, G> Hash<P7<A, B, C, D, E, F$, G>> p7Hash(Hash<A> ha, final Hash<B> hb, final Hash<C> hc, final Hash<D> hd, final Hash<E> he, final Hash<F$> hf, final Hash<G> hg) {
        return new Hash<>(new F<P7<A, B, C, D, E, F$, G>, Integer>() { // from class: fj.Hash.14
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Integer f(P7<A, B, C, D, E, F$, G> p7) {
                int r = (HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * 239) + Hash.this.hash((Hash) p7._1());
                return Integer.valueOf((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * r) + hb.hash((Hash) p7._2()))) + hc.hash((Hash) p7._3()))) + hd.hash((Hash) p7._4()))) + he.hash((Hash) p7._5()))) + hf.hash((Hash) p7._6()))) + hg.hash((Hash) p7._7()));
            }
        });
    }

    public static <A, B, C, D, E, F$, G, H> Hash<P8<A, B, C, D, E, F$, G, H>> p8Hash(Hash<A> ha, final Hash<B> hb, final Hash<C> hc, final Hash<D> hd, final Hash<E> he, final Hash<F$> hf, final Hash<G> hg, final Hash<H> hh) {
        return new Hash<>(new F<P8<A, B, C, D, E, F$, G, H>, Integer>() { // from class: fj.Hash.15
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public Integer f(P8<A, B, C, D, E, F$, G, H> p8) {
                int r = (HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * 239) + Hash.this.hash((Hash) p8._1());
                return Integer.valueOf((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * ((HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE * r) + hb.hash((Hash) p8._2()))) + hc.hash((Hash) p8._3()))) + hd.hash((Hash) p8._4()))) + he.hash((Hash) p8._5()))) + hf.hash((Hash) p8._6()))) + hg.hash((Hash) p8._7()))) + hh.hash((Hash) p8._8()));
            }
        });
    }

    public static <A> Hash<V2<A>> v2Hash(Hash<A> ea) {
        return streamHash(ea).comap(V2.toStream_());
    }

    public static <A> Hash<V3<A>> v3Hash(Hash<A> ea) {
        return streamHash(ea).comap(V3.toStream_());
    }

    public static <A> Hash<V4<A>> v4Hash(Hash<A> ea) {
        return streamHash(ea).comap(V4.toStream_());
    }

    public static <A> Hash<V5<A>> v5Hash(Hash<A> ea) {
        return streamHash(ea).comap(V5.toStream_());
    }

    public static <A> Hash<V6<A>> v6Hash(Hash<A> ea) {
        return streamHash(ea).comap(V6.toStream_());
    }

    public static <A> Hash<V7<A>> v7Hash(Hash<A> ea) {
        return streamHash(ea).comap(V7.toStream_());
    }

    public static <A> Hash<V8<A>> v8Hash(Hash<A> ea) {
        return streamHash(ea).comap(V8.toStream_());
    }
}
