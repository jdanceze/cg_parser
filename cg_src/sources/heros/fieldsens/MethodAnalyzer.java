package heros.fieldsens;

import heros.fieldsens.structs.WrappedFactAtStatement;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/MethodAnalyzer.class */
public interface MethodAnalyzer<Field, Fact, Stmt, Method> {
    void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> callEdge);

    void addInitialSeed(Stmt stmt, Fact fact);

    void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> wrappedFactAtStatement, Stmt stmt);
}
