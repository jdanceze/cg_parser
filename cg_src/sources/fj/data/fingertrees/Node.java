package fj.data.fingertrees;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/fingertrees/Node.class */
public abstract class Node<V, A> {
    private final Measured<V, A> m;
    private final V measure;

    public abstract <B> B foldRight(F<A, F<B, B>> f, B b);

    public abstract <B> B foldLeft(F<B, F<A, B>> f, B b);

    public abstract Digit<V, A> toDigit();

    public abstract P2<Integer, A> lookup(F<V, Integer> f, int i);

    public abstract <B> B match(F<Node2<V, A>, B> f, F<Node3<V, A>, B> f2);

    public static <V, A, B> F<B, F<Node<V, A>, B>> foldLeft_(final F<B, F<A, B>> bff) {
        return Function.curry(new F2<B, Node<V, A>, B>() { // from class: fj.data.fingertrees.Node.1
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((AnonymousClass1) obj, (Node) ((Node) obj2));
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            public B f(B b, Node<V, A> node) {
                return node.foldLeft(F.this, b);
            }
        });
    }

    public static <V, A, B> F<B, F<Node<V, A>, B>> foldRight_(final F<A, F<B, B>> aff) {
        return Function.curry(new F2<B, Node<V, A>, B>() { // from class: fj.data.fingertrees.Node.2
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((AnonymousClass2) obj, (Node) ((Node) obj2));
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            public B f(B b, Node<V, A> node) {
                return node.foldRight(F.this, b);
            }
        });
    }

    public final <B> Node<V, B> map(final F<A, B> f, final Measured<V, B> m) {
        return (Node) match(new F<Node2<V, A>, Node<V, B>>() { // from class: fj.data.fingertrees.Node.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Node2) ((Node2) obj));
            }

            public Node<V, B> f(Node2<V, A> node2) {
                return new Node2(m, node2.toVector().map(f));
            }
        }, new F<Node3<V, A>, Node<V, B>>() { // from class: fj.data.fingertrees.Node.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Node3) ((Node3) obj));
            }

            public Node<V, B> f(Node3<V, A> node3) {
                return new Node3(m, node3.toVector().map(f));
            }
        });
    }

    public static <V, A, B> F<Node<V, A>, Node<V, B>> liftM(final F<A, B> f, final Measured<V, B> m) {
        return new F<Node<V, A>, Node<V, B>>() { // from class: fj.data.fingertrees.Node.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Node) ((Node) obj));
            }

            public Node<V, B> f(Node<V, A> node) {
                return node.map(F.this, m);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Node(Measured<V, A> m, V measure) {
        this.m = m;
        this.measure = measure;
    }

    public final V measure() {
        return this.measure;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Measured<V, A> measured() {
        return this.m;
    }
}
