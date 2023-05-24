package heros.fieldsens;

import com.google.common.collect.Lists;
import heros.fieldsens.structs.WrappedFactAtStatement;
import java.util.Iterator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/CallEdgeResolver.class */
public class CallEdgeResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, CallEdge<Field, Fact, Stmt, Method>> {
    @Override // heros.fieldsens.ResolverTemplate
    protected /* bridge */ /* synthetic */ void processIncomingGuaranteedPrefix(Object obj) {
        processIncomingGuaranteedPrefix((CallEdge) ((CallEdge) obj));
    }

    @Override // heros.fieldsens.ResolverTemplate
    protected /* bridge */ /* synthetic */ void processIncomingPotentialPrefix(Object obj) {
        processIncomingPotentialPrefix((CallEdge) ((CallEdge) obj));
    }

    @Override // heros.fieldsens.ResolverTemplate
    protected /* bridge */ /* synthetic */ AccessPath getAccessPathOf(Object obj) {
        return getAccessPathOf((CallEdge) ((CallEdge) obj));
    }

    public CallEdgeResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Debugger<Field, Fact, Stmt, Method> debugger) {
        this(analyzer, debugger, null);
    }

    public CallEdgeResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Debugger<Field, Fact, Stmt, Method> debugger, CallEdgeResolver<Field, Fact, Stmt, Method> parent) {
        super(analyzer, analyzer.getAccessPath(), parent, debugger);
    }

    protected AccessPath<Field> getAccessPathOf(CallEdge<Field, Fact, Stmt, Method> inc) {
        return inc.getCalleeSourceFact().getAccessPath();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void processIncomingGuaranteedPrefix(CallEdge<Field, Fact, Stmt, Method> inc) {
        this.analyzer.applySummaries(inc);
    }

    protected void processIncomingPotentialPrefix(CallEdge<Field, Fact, Stmt, Method> inc) {
        lock();
        inc.registerInterestCallback(this.analyzer);
        unlock();
    }

    @Override // heros.fieldsens.ResolverTemplate
    protected ResolverTemplate<Field, Fact, Stmt, Method, CallEdge<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {
        return this.analyzer.createWithAccessPath(newAccPath).getCallEdgeResolver();
    }

    public void applySummaries(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
        Iterator it = Lists.newLinkedList(this.incomingEdges).iterator();
        while (it.hasNext()) {
            CallEdge<Field, Fact, Stmt, Method> incEdge = (CallEdge) it.next();
            this.analyzer.applySummary(incEdge, factAtStmt);
        }
    }

    public String toString() {
        return "<" + this.analyzer.getAccessPath() + ":" + this.analyzer.getMethod() + ">";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // heros.fieldsens.Resolver
    public void log(String message) {
        this.analyzer.log(message);
    }

    public boolean hasIncomingEdges() {
        return !this.incomingEdges.isEmpty();
    }
}
