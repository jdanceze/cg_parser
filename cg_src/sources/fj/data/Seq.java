package fj.data;

import fj.Bottom;
import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.data.fingertrees.FingerTree;
import fj.data.fingertrees.MakeTree;
import fj.data.fingertrees.Measured;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Seq.class */
public final class Seq<A> {
    private final FingerTree<Integer, A> ftree;

    private static <A> MakeTree<Integer, A> mkTree() {
        return FingerTree.mkTree(elemMeasured());
    }

    private Seq(FingerTree<Integer, A> ftree) {
        this.ftree = ftree;
    }

    private static <A> Measured<Integer, A> elemMeasured() {
        return FingerTree.measured(Monoid.intAdditionMonoid, Function.constant(1));
    }

    public static <A> Seq<A> empty() {
        return new Seq<>(mkTree().empty());
    }

    public static <A> Seq<A> single(A a) {
        return new Seq<>(mkTree().single(a));
    }

    public Seq<A> cons(A a) {
        return new Seq<>(this.ftree.cons(a));
    }

    public Seq<A> snoc(A a) {
        return new Seq<>(this.ftree.snoc(a));
    }

    public Seq<A> append(Seq<A> as) {
        return new Seq<>(this.ftree.append(as.ftree));
    }

    public boolean isEmpty() {
        return this.ftree.isEmpty();
    }

    public int length() {
        return this.ftree.measure().intValue();
    }

    public A index(int i) {
        if (i < 0 || i >= length()) {
            throw Bottom.error("Index " + i + "out of bounds.");
        }
        return this.ftree.lookup(Function.identity(), i)._2();
    }

    public <B> B foldLeft(F2<B, A, B> f, B z) {
        return (B) this.ftree.foldLeft((F2<F2<B, A, B>, A, F2<B, A, B>>) f, (F2<B, A, B>) z);
    }

    public <B> B foldRight(F2<A, B, B> f, B z) {
        return (B) this.ftree.foldRight((F2<A, F2<A, B, B>, F2<A, B, B>>) f, (F2<A, B, B>) z);
    }

    public <B> Seq<B> map(F<A, B> f) {
        return new Seq<>(this.ftree.map(f, elemMeasured()));
    }
}
