package fj.data;

import fj.F;
import fj.F2;
import fj.F2Functions;
import fj.Function;
import fj.Monoid;
import fj.P;
import fj.P1;
import fj.P2;
import fj.Show;
import java.util.Collection;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Tree.class */
public final class Tree<A> implements Iterable<A> {
    private final A root;
    private final P1<Stream<Tree<A>>> subForest;

    @Override // java.lang.Iterable
    public Iterator<A> iterator() {
        return flatten().iterator();
    }

    private Tree(A root, P1<Stream<Tree<A>>> subForest) {
        this.root = root;
        this.subForest = subForest;
    }

    public static <A> Tree<A> leaf(A root) {
        return node(root, Stream.nil());
    }

    public static <A> Tree<A> node(A root, P1<Stream<Tree<A>>> forest) {
        return new Tree<>(root, forest);
    }

    public static <A> Tree<A> node(A root, Stream<Tree<A>> forest) {
        return new Tree<>(root, P.p(forest));
    }

    public static <A> Tree<A> node(A root, List<Tree<A>> forest) {
        return node(root, forest.toStream());
    }

    public static <A> F<A, F<P1<Stream<Tree<A>>>, Tree<A>>> node() {
        return Function.curry(new F2<A, P1<Stream<Tree<A>>>, Tree<A>>() { // from class: fj.data.Tree.1
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((AnonymousClass1) obj, (P1<Stream<Tree<AnonymousClass1>>>) obj2);
            }

            public Tree<A> f(A a, P1<Stream<Tree<A>>> p1) {
                return Tree.node(a, p1);
            }
        });
    }

    public A root() {
        return this.root;
    }

    public P1<Stream<Tree<A>>> subForest() {
        return this.subForest;
    }

    public static <A> F<Tree<A>, A> root_() {
        return new F<Tree<A>, A>() { // from class: fj.data.Tree.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Tree<Object>) obj);
            }

            public A f(Tree<A> a) {
                return a.root();
            }
        };
    }

    public static <A> F<Tree<A>, P1<Stream<Tree<A>>>> subForest_() {
        return new F<Tree<A>, P1<Stream<Tree<A>>>>() { // from class: fj.data.Tree.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Tree) ((Tree) obj));
            }

            public P1<Stream<Tree<A>>> f(Tree<A> a) {
                return a.subForest();
            }
        };
    }

    public Stream<A> flatten() {
        F2<Tree<A>, P1<Stream<A>>, Stream<A>> squish = new F2<Tree<A>, P1<Stream<A>>, Stream<A>>() { // from class: fj.data.Tree.4
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Tree) ((Tree) obj), (P1) ((P1) obj2));
            }

            public Stream<A> f(Tree<A> t, P1<Stream<A>> xs) {
                return Stream.cons(t.root(), t.subForest().map((F) ((F) Stream.foldRight().f(F2Functions.curry(this))).f(xs._1())));
            }
        };
        return squish.f(this, P.p(Stream.nil()));
    }

    public static <A> F<Tree<A>, Stream<A>> flatten_() {
        return new F<Tree<A>, Stream<A>>() { // from class: fj.data.Tree.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Tree) ((Tree) obj));
            }

            public Stream<A> f(Tree<A> t) {
                return t.flatten();
            }
        };
    }

    public Stream<Stream<A>> levels() {
        F<Stream<Tree<A>>, Stream<Tree<A>>> flatSubForests = (F) Stream.bind_().f(Function.compose(P1.__1(), subForest_()));
        F<Stream<Tree<A>>, Stream<A>> roots = (F) Stream.map_().f(root_());
        return Stream.iterateWhile(flatSubForests, Stream.isNotEmpty_(), Stream.single(this)).map(roots);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B> Tree<B> fmap(F<A, B> f) {
        return node(f.f(root()), (P1<Stream<Tree<B>>>) subForest().map((F) Stream.map_().f(fmap_().f(f))));
    }

    public static <A, B> F<F<A, B>, F<Tree<A>, Tree<B>>> fmap_() {
        return new F<F<A, B>, F<Tree<A>, Tree<B>>>() { // from class: fj.data.Tree.6
            @Override // fj.F
            public F<Tree<A>, Tree<B>> f(final F<A, B> f) {
                return new F<Tree<A>, Tree<B>>() { // from class: fj.data.Tree.6.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Tree) ((Tree) obj));
                    }

                    public Tree<B> f(Tree<A> a) {
                        return a.fmap(f);
                    }
                };
            }
        };
    }

    public <B> B foldMap(F<A, B> f, Monoid<B> m) {
        return m.sum(f.f(root()), m.sumRight(subForest()._1().map(foldMap_(f, m)).toList()));
    }

    public Collection<A> toCollection() {
        return flatten().toCollection();
    }

    public static <A, B> F<Tree<A>, B> foldMap_(final F<A, B> f, final Monoid<B> m) {
        return new F<Tree<A>, B>() { // from class: fj.data.Tree.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Tree) ((Tree) obj));
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            public B f(Tree<A> t) {
                return t.foldMap(F.this, m);
            }
        };
    }

    public static <A, B> F<B, Tree<A>> unfoldTree(final F<B, P2<A, P1<Stream<B>>>> f) {
        return new F<B, Tree<A>>() { // from class: fj.data.Tree.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass8) obj);
            }

            @Override // fj.F
            public Tree<A> f(B b) {
                P2 p2 = (P2) F.this.f(b);
                return Tree.node(p2._1(), ((P1) p2._2()).map((F) Stream.map_().f(Tree.unfoldTree(F.this))));
            }
        };
    }

    public <B> Tree<B> cobind(final F<Tree<A>, B> f) {
        return (Tree) unfoldTree(new F<Tree<A>, P2<B, P1<Stream<Tree<A>>>>>() { // from class: fj.data.Tree.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Tree) ((Tree) obj));
            }

            public P2<B, P1<Stream<Tree<A>>>> f(Tree<A> t) {
                return P.p(f.f(t), t.subForest());
            }
        }).f(this);
    }

    public Tree<Tree<A>> cojoin() {
        return (Tree<Tree<A>>) cobind(Function.identity());
    }

    private static <A> Stream<String> drawSubTrees(Show<A> s, Stream<Tree<A>> ts) {
        return ts.isEmpty() ? Stream.nil() : ts.tail()._1().isEmpty() ? shift("`- ", "   ", ts.head().drawTree(s)).cons("|") : shift("+- ", "|  ", ts.head().drawTree(s)).append(drawSubTrees(s, ts.tail()._1()));
    }

    private static Stream<String> shift(String f, String o, Stream<String> s) {
        return Stream.repeat(o).cons(f).zipWith(s, Monoid.stringMonoid.sum());
    }

    private Stream<String> drawTree(Show<A> s) {
        return drawSubTrees(s, this.subForest._1()).cons(s.showS((Show<A>) this.root));
    }

    public String draw(Show<A> s) {
        return Monoid.stringMonoid.join(drawTree(s), "\n");
    }

    public static <A> Show<Tree<A>> show2D(final Show<A> s) {
        return Show.showS((F) new F<Tree<A>, String>() { // from class: fj.data.Tree.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ String f(Object obj) {
                return f((Tree) ((Tree) obj));
            }

            public String f(Tree<A> tree) {
                return tree.draw(Show.this);
            }
        });
    }

    public <B, C> Tree<C> zipWith(Tree<B> bs, F2<A, B, C> f) {
        return (Tree) F2Functions.zipTreeM(f).f(this, bs);
    }

    public <B, C> Tree<C> zipWith(Tree<B> bs, F<A, F<B, C>> f) {
        return zipWith(bs, Function.uncurryF2(f));
    }

    public static <A, B> Tree<B> bottomUp(Tree<A> t, final F<P2<A, Stream<B>>, B> f) {
        Stream<B> map = t.subForest()._1().map((F<Tree<A>, Tree<B>>) new F<Tree<A>, Tree<B>>() { // from class: fj.data.Tree.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Tree) ((Tree) obj));
            }

            public Tree<B> f(Tree<A> a) {
                return Tree.bottomUp(a, F.this);
            }
        });
        return node(f.f(P.p(t.root(), map.map(getRoot()))), (Stream<Tree<B>>) map);
    }

    private static <A> F<Tree<A>, A> getRoot() {
        return new F<Tree<A>, A>() { // from class: fj.data.Tree.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Tree<Object>) obj);
            }

            public A f(Tree<A> a) {
                return a.root();
            }
        };
    }
}
