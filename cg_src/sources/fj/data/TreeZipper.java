package fj.data;

import fj.Equal;
import fj.F;
import fj.F2;
import fj.F2Functions;
import fj.F4;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.P4;
import fj.Show;
import fj.function.Booleans;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/TreeZipper.class */
public final class TreeZipper<A> implements Iterable<TreeZipper<A>> {
    private final Tree<A> tree;
    private final Stream<Tree<A>> lefts;
    private final Stream<Tree<A>> rights;
    private final Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>> parents;

    @Override // java.lang.Iterable
    public Iterator<TreeZipper<A>> iterator() {
        return positions().toTree().iterator();
    }

    private TreeZipper(Tree<A> tree, Stream<Tree<A>> lefts, Stream<Tree<A>> rights, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>> parents) {
        this.tree = tree;
        this.lefts = lefts;
        this.rights = rights;
        this.parents = parents;
    }

    public static <A> TreeZipper<A> treeZipper(Tree<A> tree, Stream<Tree<A>> lefts, Stream<Tree<A>> rights, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>> parents) {
        return new TreeZipper<>(tree, lefts, rights, parents);
    }

    public static <A> F<Tree<A>, F<Stream<Tree<A>>, F<Stream<Tree<A>>, F<Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>, TreeZipper<A>>>>> treeZipper() {
        return Function.curry(new F4<Tree<A>, Stream<Tree<A>>, Stream<Tree<A>>, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>, TreeZipper<A>>() { // from class: fj.data.TreeZipper.1
            @Override // fj.F4
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2, Object obj3, Object obj4) {
                return f((Tree) ((Tree) obj), (Stream) ((Stream) obj2), (Stream) ((Stream) obj3), (Stream) ((Stream) obj4));
            }

            public TreeZipper<A> f(Tree<A> tree, Stream<Tree<A>> lefts, Stream<Tree<A>> rights, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>> parents) {
                return TreeZipper.treeZipper(tree, lefts, rights, parents);
            }
        });
    }

    public P4<Tree<A>, Stream<Tree<A>>, Stream<Tree<A>>, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>> p() {
        return P.p(this.tree, this.lefts, this.rights, this.parents);
    }

    public static <A> F<TreeZipper<A>, P4<Tree<A>, Stream<Tree<A>>, Stream<Tree<A>>, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>>> p_() {
        return new F<TreeZipper<A>, P4<Tree<A>, Stream<Tree<A>>, Stream<Tree<A>>, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>>>() { // from class: fj.data.TreeZipper.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((TreeZipper) ((TreeZipper) obj));
            }

            public P4<Tree<A>, Stream<Tree<A>>, Stream<Tree<A>>, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>> f(TreeZipper<A> a) {
                return a.p();
            }
        };
    }

    public static <A> Equal<TreeZipper<A>> eq(Equal<A> e) {
        return Equal.p4Equal(Equal.treeEqual(e), Equal.streamEqual(Equal.treeEqual(e)), Equal.streamEqual(Equal.treeEqual(e)), Equal.streamEqual(Equal.p3Equal(Equal.streamEqual(Equal.treeEqual(e)), e, Equal.streamEqual(Equal.treeEqual(e))))).comap(p_());
    }

    public static <A> Show<TreeZipper<A>> show(Show<A> s) {
        return Show.p4Show(Show.treeShow(s), Show.streamShow(Show.treeShow(s)), Show.streamShow(Show.treeShow(s)), Show.streamShow(Show.p3Show(Show.streamShow(Show.treeShow(s)), s, Show.streamShow(Show.treeShow(s))))).comap(p_());
    }

    private static <A> Stream<Tree<A>> combChildren(Stream<Tree<A>> ls, Tree<A> t, Stream<Tree<A>> rs) {
        return (Stream) ls.foldLeft((F<F<B, F<Tree<A>, B>>, F<Tree<A>, F<B, F<Tree<A>, B>>>>) Function.compose(Function.flip(Stream.cons()), P.p1()), (F<B, F<Tree<A>, B>>) Stream.cons(t, P.p(rs)));
    }

    public Option<TreeZipper<A>> parent() {
        if (this.parents.isEmpty()) {
            return Option.none();
        }
        P3<Stream<Tree<A>>, A, Stream<Tree<A>>> p = this.parents.head();
        return Option.some(treeZipper(Tree.node(p._2(), combChildren(this.lefts, this.tree, this.rights)), p._1(), p._3(), this.parents.tail()._1()));
    }

    public TreeZipper<A> root() {
        return (TreeZipper) parent().option((Option<TreeZipper<A>>) this, (F<TreeZipper<A>, Option<TreeZipper<A>>>) root_());
    }

    public static <A> F<TreeZipper<A>, TreeZipper<A>> root_() {
        return new F<TreeZipper<A>, TreeZipper<A>>() { // from class: fj.data.TreeZipper.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((TreeZipper) ((TreeZipper) obj));
            }

            public TreeZipper<A> f(TreeZipper<A> a) {
                return a.root();
            }
        };
    }

    public Option<TreeZipper<A>> left() {
        return this.lefts.isEmpty() ? Option.none() : Option.some(treeZipper(this.lefts.head(), this.lefts.tail()._1(), this.rights.cons(this.tree), this.parents));
    }

    public Option<TreeZipper<A>> right() {
        return this.rights.isEmpty() ? Option.none() : Option.some(treeZipper(this.rights.head(), this.lefts.cons(this.tree), this.rights.tail()._1(), this.parents));
    }

    public Option<TreeZipper<A>> firstChild() {
        Stream<Tree<A>> ts = this.tree.subForest()._1();
        return ts.isEmpty() ? Option.none() : Option.some(treeZipper(ts.head(), Stream.nil(), ts.tail()._1(), downParents()));
    }

    public Option<TreeZipper<A>> lastChild() {
        Stream<Tree<A>> ts = this.tree.subForest()._1().reverse();
        return ts.isEmpty() ? Option.none() : Option.some(treeZipper(ts.head(), ts.tail()._1(), Stream.nil(), downParents()));
    }

    public Option<TreeZipper<A>> getChild(int n) {
        Option<TreeZipper<A>> r = Option.none();
        Iterator<A> it = splitChildren(Stream.nil(), this.tree.subForest()._1(), n).iterator();
        while (it.hasNext()) {
            P2<Stream<Tree<A>>, Stream<Tree<A>>> lr = (P2) it.next();
            r = Option.some(treeZipper(lr._1().head(), lr._1().tail()._1(), lr._2(), downParents()));
        }
        return r;
    }

    public Option<TreeZipper<A>> findChild(final F<Tree<A>, Boolean> p) {
        Option<TreeZipper<A>> r = Option.none();
        F2<Stream<Tree<A>>, Stream<Tree<A>>, Option<P3<Stream<Tree<A>>, Tree<A>, Stream<Tree<A>>>>> split = new F2<Stream<Tree<A>>, Stream<Tree<A>>, Option<P3<Stream<Tree<A>>, Tree<A>, Stream<Tree<A>>>>>() { // from class: fj.data.TreeZipper.4
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Stream) ((Stream) obj), (Stream) ((Stream) obj2));
            }

            public Option<P3<Stream<Tree<A>>, Tree<A>, Stream<Tree<A>>>> f(Stream<Tree<A>> acc, Stream<Tree<A>> xs) {
                if (xs.isNotEmpty()) {
                    return ((Boolean) p.f(xs.head())).booleanValue() ? Option.some(P.p(acc, xs.head(), xs.tail()._1())) : f((Stream) acc.cons(xs.head()), (Stream) xs.tail()._1());
                }
                return Option.none();
            }
        };
        Stream<Tree<A>> subforest = this.tree.subForest()._1();
        if (subforest.isNotEmpty()) {
            Iterator<P3<Stream<Tree<A>>, Tree<A>, Stream<Tree<A>>>> it = split.f(Stream.nil(), subforest).iterator();
            while (it.hasNext()) {
                P3<Stream<Tree<A>>, Tree<A>, Stream<Tree<A>>> ltr = it.next();
                r = Option.some(treeZipper(ltr._2(), ltr._1(), ltr._3(), downParents()));
            }
        }
        return r;
    }

    private Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>> downParents() {
        return this.parents.cons(P.p(this.lefts, this.tree.root(), this.rights));
    }

    private static <A> Option<P2<Stream<A>, Stream<A>>> splitChildren(Stream<A> acc, Stream<A> xs, int n) {
        return n == 0 ? Option.some(P.p(acc, xs)) : xs.isNotEmpty() ? splitChildren(acc.cons(xs.head()), xs.tail()._1(), n - 1) : Option.none();
    }

    private static <A> Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>> lp3nil() {
        return Stream.nil();
    }

    public static <A> TreeZipper<A> fromTree(Tree<A> t) {
        return treeZipper(t, Stream.nil(), Stream.nil(), lp3nil());
    }

    public static <A> Option<TreeZipper<A>> fromForest(Stream<Tree<A>> ts) {
        if (ts.isNotEmpty()) {
            return Option.some(treeZipper(ts.head(), Stream.nil(), ts.tail()._1(), lp3nil()));
        }
        return Option.none();
    }

    public Tree<A> toTree() {
        return root().tree;
    }

    public Stream<Tree<A>> toForest() {
        TreeZipper<A> r = root();
        return combChildren(r.lefts, r.tree, r.rights);
    }

    public Tree<A> focus() {
        return this.tree;
    }

    public Stream<Tree<A>> lefts() {
        return this.lefts;
    }

    public Stream<Tree<A>> rights() {
        return this.rights;
    }

    public boolean isRoot() {
        return this.parents.isEmpty();
    }

    public boolean isFirst() {
        return this.lefts.isEmpty();
    }

    public boolean isLast() {
        return this.rights.isEmpty();
    }

    public boolean isLeaf() {
        return this.tree.subForest()._1().isEmpty();
    }

    public boolean isChild() {
        return !isRoot();
    }

    public boolean hasChildren() {
        return !isLeaf();
    }

    public TreeZipper<A> setTree(Tree<A> t) {
        return treeZipper(t, this.lefts, this.rights, this.parents);
    }

    public TreeZipper<A> modifyTree(F<Tree<A>, Tree<A>> f) {
        return setTree(f.f(this.tree));
    }

    public TreeZipper<A> modifyLabel(F<A, A> f) {
        return setLabel(f.f(getLabel()));
    }

    public TreeZipper<A> setLabel(final A v) {
        return modifyTree(new F<Tree<A>, Tree<A>>() { // from class: fj.data.TreeZipper.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Tree) ((Tree) obj));
            }

            public Tree<A> f(Tree<A> t) {
                return Tree.node(v, t.subForest());
            }
        });
    }

    public A getLabel() {
        return this.tree.root();
    }

    public TreeZipper<A> insertLeft(Tree<A> t) {
        return treeZipper(t, this.lefts, this.rights.cons(this.tree), this.parents);
    }

    public TreeZipper<A> insertRight(Tree<A> t) {
        return treeZipper(t, this.lefts.cons(this.tree), this.rights, this.parents);
    }

    public TreeZipper<A> insertDownFirst(Tree<A> t) {
        return treeZipper(t, Stream.nil(), this.tree.subForest()._1(), downParents());
    }

    public TreeZipper<A> insertDownLast(Tree<A> t) {
        return treeZipper(t, this.tree.subForest()._1().reverse(), Stream.nil(), downParents());
    }

    public Option<TreeZipper<A>> insertDownAt(int n, Tree<A> t) {
        Option<TreeZipper<A>> r = Option.none();
        Iterator<A> it = splitChildren(Stream.nil(), this.tree.subForest()._1(), n).iterator();
        while (it.hasNext()) {
            P2<Stream<Tree<A>>, Stream<Tree<A>>> lr = (P2) it.next();
            r = Option.some(treeZipper(t, lr._1(), lr._2(), downParents()));
        }
        return r;
    }

    public Option<TreeZipper<A>> delete() {
        Option<TreeZipper<A>> r = Option.none();
        if (this.rights.isNotEmpty()) {
            r = Option.some(treeZipper(this.rights.head(), this.lefts, this.rights.tail()._1(), this.parents));
        } else if (this.lefts.isNotEmpty()) {
            r = Option.some(treeZipper(this.lefts.head(), this.lefts.tail()._1(), this.rights, this.parents));
        } else {
            Iterator<TreeZipper<A>> it = parent().iterator();
            while (it.hasNext()) {
                TreeZipper<A> loc = it.next();
                r = Option.some(loc.modifyTree(new F<Tree<A>, Tree<A>>() { // from class: fj.data.TreeZipper.6
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Tree) ((Tree) obj));
                    }

                    public Tree<A> f(Tree<A> t) {
                        return Tree.node(t.root(), Stream.nil());
                    }
                }));
            }
        }
        return r;
    }

    public TreeZipper<P2<A, Boolean>> zipWithFocus() {
        return map((F) Function.flip(P.p2()).f(false)).modifyLabel(P2.map2_(Booleans.not));
    }

    public <B> TreeZipper<B> map(final F<A, B> f) {
        F<Tree<A>, B> f2 = (F) Tree.fmap_().f(f);
        final F<Stream<Tree<A>>, Stream<Tree<B>>> h = (F) Stream.map_().f(f2);
        return treeZipper(this.tree.fmap(f), this.lefts.map(f2), this.rights.map(f2), this.parents.map(new F<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>, P3<Stream<Tree<B>>, B, Stream<Tree<B>>>>() { // from class: fj.data.TreeZipper.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P3) ((P3) obj));
            }

            public P3<Stream<Tree<B>>, B, Stream<Tree<B>>> f(P3<Stream<Tree<A>>, A, Stream<Tree<A>>> p) {
                return p.map1(h).map2(f).map3(h);
            }
        }));
    }

    public static <A> F<Tree<A>, TreeZipper<A>> fromTree() {
        return new F<Tree<A>, TreeZipper<A>>() { // from class: fj.data.TreeZipper.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Tree) ((Tree) obj));
            }

            public TreeZipper<A> f(Tree<A> t) {
                return TreeZipper.fromTree(t);
            }
        };
    }

    public static <A> F<TreeZipper<A>, Option<TreeZipper<A>>> left_() {
        return new F<TreeZipper<A>, Option<TreeZipper<A>>>() { // from class: fj.data.TreeZipper.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((TreeZipper) ((TreeZipper) obj));
            }

            public Option<TreeZipper<A>> f(TreeZipper<A> z) {
                return z.left();
            }
        };
    }

    public static <A> F<TreeZipper<A>, Option<TreeZipper<A>>> right_() {
        return new F<TreeZipper<A>, Option<TreeZipper<A>>>() { // from class: fj.data.TreeZipper.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((TreeZipper) ((TreeZipper) obj));
            }

            public Option<TreeZipper<A>> f(TreeZipper<A> z) {
                return z.right();
            }
        };
    }

    public TreeZipper<TreeZipper<A>> positions() {
        Tree<TreeZipper<A>> t = (Tree) Tree.unfoldTree(dwn()).f(this);
        Stream<Tree<TreeZipper<A>>> l = uf(left_());
        Stream<Tree<TreeZipper<A>>> r = uf(right_());
        Stream<P3<Stream<Tree<TreeZipper<A>>>, TreeZipper<A>, Stream<Tree<TreeZipper<A>>>>> p = Stream.unfold(new F<Option<TreeZipper<A>>, Option<P2<P3<Stream<Tree<TreeZipper<A>>>, TreeZipper<A>, Stream<Tree<TreeZipper<A>>>>, Option<TreeZipper<A>>>>>() { // from class: fj.data.TreeZipper.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Option) ((Option) obj));
            }

            public Option<P2<P3<Stream<Tree<TreeZipper<A>>>, TreeZipper<A>, Stream<Tree<TreeZipper<A>>>>, Option<TreeZipper<A>>>> f(Option<TreeZipper<A>> o) {
                Option<P2<P3<Stream<Tree<TreeZipper<A>>>, TreeZipper<A>, Stream<Tree<TreeZipper<A>>>>, Option<TreeZipper<A>>>> r2 = Option.none();
                Iterator<TreeZipper<A>> it = o.iterator();
                while (it.hasNext()) {
                    TreeZipper<A> z = it.next();
                    r2 = Option.some(P.p(P.p(z.uf(TreeZipper.left_()), z, z.uf(TreeZipper.right_())), z.parent()));
                }
                return r2;
            }
        }, parent());
        return treeZipper(t, l, r, p);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Stream<Tree<TreeZipper<A>>> uf(final F<TreeZipper<A>, Option<TreeZipper<A>>> f) {
        return Stream.unfold(new F<Option<TreeZipper<A>>, Option<P2<Tree<TreeZipper<A>>, Option<TreeZipper<A>>>>>() { // from class: fj.data.TreeZipper.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Option) ((Option) obj));
            }

            public Option<P2<Tree<TreeZipper<A>>, Option<TreeZipper<A>>>> f(Option<TreeZipper<A>> o) {
                Option<P2<Tree<TreeZipper<A>>, Option<TreeZipper<A>>>> r = Option.none();
                Iterator<TreeZipper<A>> it = o.iterator();
                while (it.hasNext()) {
                    TreeZipper<A> c = it.next();
                    r = Option.some(P.p(Tree.unfoldTree(TreeZipper.dwn()).f(c), f.f(c)));
                }
                return r;
            }
        }, f.f(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <A> F<TreeZipper<A>, P2<TreeZipper<A>, P1<Stream<TreeZipper<A>>>>> dwn() {
        return new F<TreeZipper<A>, P2<TreeZipper<A>, P1<Stream<TreeZipper<A>>>>>() { // from class: fj.data.TreeZipper.13
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((TreeZipper) ((TreeZipper) obj));
            }

            public P2<TreeZipper<A>, P1<Stream<TreeZipper<A>>>> f(final TreeZipper<A> tz) {
                return P.p(tz, new P1<Stream<TreeZipper<A>>>() { // from class: fj.data.TreeZipper.13.1
                    private F<Option<TreeZipper<A>>, Option<P2<TreeZipper<A>, Option<TreeZipper<A>>>>> fwd() {
                        return new F<Option<TreeZipper<A>>, Option<P2<TreeZipper<A>, Option<TreeZipper<A>>>>>() { // from class: fj.data.TreeZipper.13.1.1
                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((Option) ((Option) obj));
                            }

                            public Option<P2<TreeZipper<A>, Option<TreeZipper<A>>>> f(Option<TreeZipper<A>> o) {
                                Option<P2<TreeZipper<A>, Option<TreeZipper<A>>>> r = Option.none();
                                Iterator<TreeZipper<A>> it = o.iterator();
                                while (it.hasNext()) {
                                    TreeZipper<A> c = it.next();
                                    r = Option.some(P.p(c, c.right()));
                                }
                                return r;
                            }
                        };
                    }

                    @Override // fj.P1
                    public Stream<TreeZipper<A>> _1() {
                        return Stream.unfold(fwd(), tz.firstChild());
                    }
                });
            }
        };
    }

    public <B> TreeZipper<B> cobind(F<TreeZipper<A>, B> f) {
        return positions().map(f);
    }

    public static <A> F2<F<Tree<A>, Boolean>, TreeZipper<A>, Option<TreeZipper<A>>> findChild() {
        return new F2<F<Tree<A>, Boolean>, TreeZipper<A>, Option<TreeZipper<A>>>() { // from class: fj.data.TreeZipper.14
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((F) ((F) obj), (TreeZipper) ((TreeZipper) obj2));
            }

            public Option<TreeZipper<A>> f(F<Tree<A>, Boolean> f, TreeZipper<A> az) {
                return az.findChild(f);
            }
        };
    }

    public <B, C> TreeZipper<C> zipWith(TreeZipper<B> bs, F2<A, B, C> f) {
        return (TreeZipper) F2Functions.zipTreeZipperM(f).f(this, bs);
    }

    public <B, C> TreeZipper<C> zipWith(TreeZipper<B> bs, F<A, F<B, C>> f) {
        return zipWith(bs, Function.uncurryF2(f));
    }
}
