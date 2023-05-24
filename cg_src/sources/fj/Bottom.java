package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Bottom.class */
public final class Bottom {
    private Bottom() {
        throw new UnsupportedOperationException();
    }

    public static Error undefined() {
        return error("undefined");
    }

    public static Error error(String s) {
        throw new Error(s);
    }

    public static <A> P1<A> error_(final String s) {
        return new P1<A>() { // from class: fj.Bottom.1
            @Override // fj.P1
            public A _1() {
                throw new Error(s);
            }
        };
    }

    public static <A, B> F<A, B> errorF(final String s) {
        return new F<A, B>() { // from class: fj.Bottom.2
            @Override // fj.F
            public B f(A a) {
                throw new Error(s);
            }
        };
    }

    public static <A> Error decons(A a, Show<A> sa) {
        return error("Deconstruction failure on type " + a.getClass() + " with value " + sa.show((Show<A>) a).toString());
    }

    public static <A> Error decons(java.lang.Class<A> c) {
        return error("Deconstruction failure on type " + c);
    }

    public static <T extends Throwable> F<T, String> eToString() {
        return (F<T, String>) new F<T, String>() { // from class: fj.Bottom.3
            @Override // fj.F
            public String f(Throwable t) {
                return t.toString();
            }
        };
    }

    public static <T extends Throwable> F<T, String> eMessage() {
        return (F<T, String>) new F<T, String>() { // from class: fj.Bottom.4
            @Override // fj.F
            public String f(Throwable t) {
                return t.getMessage();
            }
        };
    }
}
