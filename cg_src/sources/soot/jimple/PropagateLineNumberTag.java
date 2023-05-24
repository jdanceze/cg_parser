package soot.jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/PropagateLineNumberTag.class */
public class PropagateLineNumberTag {

    /* loaded from: gencallgraphv3.jar:soot/jimple/PropagateLineNumberTag$A.class */
    public static class A {
    }

    public void nullAssignment() {
        new A();
        foo(null);
    }

    public void transitiveNullAssignment() {
        new A();
        foo(null);
        foo(null);
    }

    private static A foo(A param) {
        return param;
    }
}
