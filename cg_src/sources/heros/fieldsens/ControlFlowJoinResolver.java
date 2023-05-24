package heros.fieldsens;

import heros.fieldsens.AccessPath;
import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/ControlFlowJoinResolver.class */
public class ControlFlowJoinResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> {
    private Stmt joinStmt;
    private boolean propagated;
    private Fact sourceFact;
    private FactMergeHandler<Fact> factMergeHandler;

    @Override // heros.fieldsens.ResolverTemplate
    protected /* bridge */ /* synthetic */ void processIncomingGuaranteedPrefix(Object obj) {
        processIncomingGuaranteedPrefix((WrappedFact) ((WrappedFact) obj));
    }

    @Override // heros.fieldsens.ResolverTemplate
    protected /* bridge */ /* synthetic */ void processIncomingPotentialPrefix(Object obj) {
        processIncomingPotentialPrefix((WrappedFact) ((WrappedFact) obj));
    }

    @Override // heros.fieldsens.ResolverTemplate
    protected /* bridge */ /* synthetic */ AccessPath getAccessPathOf(Object obj) {
        return getAccessPathOf((WrappedFact) ((WrappedFact) obj));
    }

    public ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt, Debugger<Field, Fact, Stmt, Method> debugger) {
        this(factMergeHandler, analyzer, joinStmt, null, new AccessPath(), debugger, null);
        this.factMergeHandler = factMergeHandler;
        this.propagated = false;
    }

    private ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt, Fact sourceFact, AccessPath<Field> resolvedAccPath, Debugger<Field, Fact, Stmt, Method> debugger, ControlFlowJoinResolver<Field, Fact, Stmt, Method> parent) {
        super(analyzer, resolvedAccPath, parent, debugger);
        this.propagated = false;
        this.factMergeHandler = factMergeHandler;
        this.joinStmt = joinStmt;
        this.sourceFact = sourceFact;
        this.propagated = true;
    }

    protected AccessPath<Field> getAccessPathOf(WrappedFact<Field, Fact, Stmt, Method> inc) {
        return inc.getAccessPath();
    }

    protected void processIncomingGuaranteedPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {
        if (this.propagated) {
            this.factMergeHandler.merge(this.sourceFact, fact.getFact());
            return;
        }
        this.propagated = true;
        this.sourceFact = fact.getFact();
        this.analyzer.processFlowFromJoinStmt(new WrappedFactAtStatement<>(this.joinStmt, new WrappedFact(fact.getFact(), new AccessPath(), this)));
    }

    private boolean isNullOrCallEdgeResolver(Resolver<Field, Fact, Stmt, Method> resolver) {
        if (resolver == null) {
            return true;
        }
        return (resolver instanceof CallEdgeResolver) && !(resolver instanceof ZeroCallEdgeResolver);
    }

    protected void processIncomingPotentialPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {
        if (isNullOrCallEdgeResolver(fact.getResolver())) {
            canBeResolvedEmpty();
            return;
        }
        lock();
        AccessPath.Delta<Field> delta = fact.getAccessPath().getDeltaTo(this.resolvedAccessPath);
        fact.getResolver().resolve(new DeltaConstraint(delta), new InterestCallback<Field, Fact, Stmt, Method>() { // from class: heros.fieldsens.ControlFlowJoinResolver.1
            @Override // heros.fieldsens.InterestCallback
            public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
                ControlFlowJoinResolver.this.interest(resolver);
            }

            @Override // heros.fieldsens.InterestCallback
            public void canBeResolvedEmpty() {
                ControlFlowJoinResolver.this.canBeResolvedEmpty();
            }
        });
        unlock();
    }

    @Override // heros.fieldsens.ResolverTemplate
    protected ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {
        return new ControlFlowJoinResolver(this.factMergeHandler, this.analyzer, this.joinStmt, this.sourceFact, newAccPath, this.debugger, this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // heros.fieldsens.Resolver
    public void log(String message) {
        this.analyzer.log("Join Stmt " + toString() + ": " + message);
    }

    public String toString() {
        return "<" + this.resolvedAccessPath + ":" + this.joinStmt + " in " + this.analyzer.getMethod() + ">";
    }

    public Stmt getJoinStmt() {
        return this.joinStmt;
    }
}
