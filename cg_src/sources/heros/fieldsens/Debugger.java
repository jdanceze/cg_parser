package heros.fieldsens;

import heros.InterproceduralCFG;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.structs.WrappedFactAtStatement;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/Debugger.class */
public interface Debugger<Field, Fact, Stmt, Method> {
    void setICFG(InterproceduralCFG<Stmt, Method> interproceduralCFG);

    void initialSeed(Stmt stmt);

    void edgeTo(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> perAccessPathMethodAnalyzer, WrappedFactAtStatement<Field, Fact, Stmt, Method> wrappedFactAtStatement);

    void newResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> perAccessPathMethodAnalyzer, Resolver<Field, Fact, Stmt, Method> resolver);

    void newJob(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> perAccessPathMethodAnalyzer, WrappedFactAtStatement<Field, Fact, Stmt, Method> wrappedFactAtStatement);

    void jobStarted(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> perAccessPathMethodAnalyzer, WrappedFactAtStatement<Field, Fact, Stmt, Method> wrappedFactAtStatement);

    void jobFinished(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> perAccessPathMethodAnalyzer, WrappedFactAtStatement<Field, Fact, Stmt, Method> wrappedFactAtStatement);

    void askedToResolve(Resolver<Field, Fact, Stmt, Method> resolver, FlowFunction.Constraint<Field> constraint);

    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/Debugger$NullDebugger.class */
    public static class NullDebugger<Field, Fact, Stmt, Method> implements Debugger<Field, Fact, Stmt, Method> {
        @Override // heros.fieldsens.Debugger
        public void setICFG(InterproceduralCFG<Stmt, Method> icfg) {
        }

        @Override // heros.fieldsens.Debugger
        public void initialSeed(Stmt stmt) {
        }

        @Override // heros.fieldsens.Debugger
        public void edgeTo(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
        }

        @Override // heros.fieldsens.Debugger
        public void newResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
        }

        @Override // heros.fieldsens.Debugger
        public void newJob(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
        }

        @Override // heros.fieldsens.Debugger
        public void jobStarted(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
        }

        @Override // heros.fieldsens.Debugger
        public void jobFinished(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
        }

        @Override // heros.fieldsens.Debugger
        public void askedToResolve(Resolver<Field, Fact, Stmt, Method> resolver, FlowFunction.Constraint<Field> constraint) {
        }
    }
}
