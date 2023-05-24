package net.bytebuddy.matcher;

import net.bytebuddy.build.HashCodeAndEqualsPlugin;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/matcher/ElementMatcher.class */
public interface ElementMatcher<T> {
    boolean matches(T t);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/matcher/ElementMatcher$Junction.class */
    public interface Junction<S> extends ElementMatcher<S> {
        <U extends S> Junction<U> and(ElementMatcher<? super U> elementMatcher);

        <U extends S> Junction<U> or(ElementMatcher<? super U> elementMatcher);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/matcher/ElementMatcher$Junction$AbstractBase.class */
        public static abstract class AbstractBase<V> implements Junction<V> {
            @Override // net.bytebuddy.matcher.ElementMatcher.Junction
            public <U extends V> Junction<U> and(ElementMatcher<? super U> other) {
                return new Conjunction(this, other);
            }

            @Override // net.bytebuddy.matcher.ElementMatcher.Junction
            public <U extends V> Junction<U> or(ElementMatcher<? super U> other) {
                return new Disjunction(this, other);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/matcher/ElementMatcher$Junction$Conjunction.class */
        public static class Conjunction<W> extends AbstractBase<W> {
            private final ElementMatcher<? super W> left;
            private final ElementMatcher<? super W> right;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.left.equals(((Conjunction) obj).left) && this.right.equals(((Conjunction) obj).right);
            }

            public int hashCode() {
                return (((17 * 31) + this.left.hashCode()) * 31) + this.right.hashCode();
            }

            public Conjunction(ElementMatcher<? super W> left, ElementMatcher<? super W> right) {
                this.left = left;
                this.right = right;
            }

            @Override // net.bytebuddy.matcher.ElementMatcher
            public boolean matches(W target) {
                return this.left.matches(target) && this.right.matches(target);
            }

            public String toString() {
                return "(" + this.left + " and " + this.right + ')';
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/matcher/ElementMatcher$Junction$Disjunction.class */
        public static class Disjunction<W> extends AbstractBase<W> {
            private final ElementMatcher<? super W> left;
            private final ElementMatcher<? super W> right;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.left.equals(((Disjunction) obj).left) && this.right.equals(((Disjunction) obj).right);
            }

            public int hashCode() {
                return (((17 * 31) + this.left.hashCode()) * 31) + this.right.hashCode();
            }

            public Disjunction(ElementMatcher<? super W> left, ElementMatcher<? super W> right) {
                this.left = left;
                this.right = right;
            }

            @Override // net.bytebuddy.matcher.ElementMatcher
            public boolean matches(W target) {
                return this.left.matches(target) || this.right.matches(target);
            }

            public String toString() {
                return "(" + this.left + " or " + this.right + ')';
            }
        }
    }
}
