package soot;
/* loaded from: gencallgraphv3.jar:soot/SideEffectTester.class */
public interface SideEffectTester {
    boolean unitCanReadFrom(Unit unit, Value value);

    boolean unitCanWriteTo(Unit unit, Value value);

    void newMethod(SootMethod sootMethod);
}
