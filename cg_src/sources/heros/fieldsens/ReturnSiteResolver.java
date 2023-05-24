package heros.fieldsens;

import heros.fieldsens.AccessPath;
import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.ReturnEdge;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/ReturnSiteResolver.class */
public class ReturnSiteResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, ReturnEdge<Field, Fact, Stmt, Method>> {
    private Stmt returnSite;
    private boolean propagated;
    private Fact sourceFact;
    private FactMergeHandler<Fact> factMergeHandler;
    static final /* synthetic */ boolean $assertionsDisabled;

    @Override // heros.fieldsens.ResolverTemplate
    protected /* bridge */ /* synthetic */ void processIncomingGuaranteedPrefix(Object obj) {
        processIncomingGuaranteedPrefix((ReturnEdge) ((ReturnEdge) obj));
    }

    @Override // heros.fieldsens.ResolverTemplate
    protected /* bridge */ /* synthetic */ void processIncomingPotentialPrefix(Object obj) {
        processIncomingPotentialPrefix((ReturnEdge) ((ReturnEdge) obj));
    }

    @Override // heros.fieldsens.ResolverTemplate
    protected /* bridge */ /* synthetic */ AccessPath getAccessPathOf(Object obj) {
        return getAccessPathOf((ReturnEdge) ((ReturnEdge) obj));
    }

    static {
        $assertionsDisabled = !ReturnSiteResolver.class.desiredAssertionStatus();
    }

    public ReturnSiteResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt returnSite, Debugger<Field, Fact, Stmt, Method> debugger) {
        this(factMergeHandler, analyzer, returnSite, null, debugger, new AccessPath(), null);
        this.factMergeHandler = factMergeHandler;
        this.propagated = false;
    }

    private ReturnSiteResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt returnSite, Fact sourceFact, Debugger<Field, Fact, Stmt, Method> debugger, AccessPath<Field> resolvedAccPath, ReturnSiteResolver<Field, Fact, Stmt, Method> parent) {
        super(analyzer, resolvedAccPath, parent, debugger);
        this.propagated = false;
        this.factMergeHandler = factMergeHandler;
        this.returnSite = returnSite;
        this.sourceFact = sourceFact;
        this.propagated = true;
    }

    public String toString() {
        return "<" + this.resolvedAccessPath + ":" + this.returnSite + " in " + this.analyzer.getMethod() + ">";
    }

    protected AccessPath<Field> getAccessPathOf(ReturnEdge<Field, Fact, Stmt, Method> inc) {
        return inc.usedAccessPathOfIncResolver.applyTo(inc.incAccessPath);
    }

    public void addIncoming(WrappedFact<Field, Fact, Stmt, Method> fact, Resolver<Field, Fact, Stmt, Method> resolverAtCaller, AccessPath.Delta<Field> callDelta) {
        addIncoming(new ReturnEdge(fact, resolverAtCaller, callDelta));
    }

    protected void processIncomingGuaranteedPrefix(ReturnEdge<Field, Fact, Stmt, Method> retEdge) {
        if (this.propagated) {
            this.factMergeHandler.merge(this.sourceFact, retEdge.incFact);
            return;
        }
        this.propagated = true;
        this.sourceFact = retEdge.incFact;
        this.analyzer.scheduleEdgeTo(new WrappedFactAtStatement<>(this.returnSite, new WrappedFact(retEdge.incFact, new AccessPath(), this)));
    }

    protected void processIncomingPotentialPrefix(ReturnEdge<Field, Fact, Stmt, Method> retEdge) {
        log("Incoming potential prefix:  " + retEdge);
        resolveViaDelta(retEdge);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // heros.fieldsens.Resolver
    public void log(String message) {
        this.analyzer.log("Return Site " + toString() + ": " + message);
    }

    @Override // heros.fieldsens.ResolverTemplate
    protected ResolverTemplate<Field, Fact, Stmt, Method, ReturnEdge<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {
        return new ReturnSiteResolver(this.factMergeHandler, this.analyzer, this.returnSite, this.sourceFact, this.debugger, newAccPath, this);
    }

    public Stmt getReturnSite() {
        return this.returnSite;
    }

    private boolean isNullOrCallEdgeResolver(Resolver<Field, Fact, Stmt, Method> resolver) {
        if (resolver == null) {
            return true;
        }
        return (resolver instanceof CallEdgeResolver) && !(resolver instanceof ZeroCallEdgeResolver);
    }

    private void resolveViaDelta(final ReturnEdge<Field, Fact, Stmt, Method> retEdge) {
        if (isNullOrCallEdgeResolver(retEdge.incResolver)) {
            resolveViaDeltaAndPotentiallyDelegateToCallSite(retEdge);
            return;
        }
        AccessPath.Delta<Field> delta = retEdge.usedAccessPathOfIncResolver.applyTo(retEdge.incAccessPath).getDeltaTo(this.resolvedAccessPath);
        if (!$assertionsDisabled && delta.accesses.length > 1) {
            throw new AssertionError();
        }
        retEdge.incResolver.resolve(new DeltaConstraint(delta), new InterestCallback<Field, Fact, Stmt, Method>() { // from class: heros.fieldsens.ReturnSiteResolver.1
            @Override // heros.fieldsens.InterestCallback
            public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
                if (resolver instanceof ZeroCallEdgeResolver) {
                    ReturnSiteResolver.this.interest(((ZeroCallEdgeResolver) resolver).copyWithAnalyzer(ReturnSiteResolver.this.analyzer));
                    return;
                }
                ReturnSiteResolver.this.incomingEdges.add(retEdge.copyWithIncomingResolver(resolver, retEdge.incAccessPath.getDeltaTo(ReturnSiteResolver.this.resolvedAccessPath)));
                ReturnSiteResolver.this.interest(ReturnSiteResolver.this);
            }

            @Override // heros.fieldsens.InterestCallback
            public void canBeResolvedEmpty() {
                ReturnSiteResolver.this.resolveViaDeltaAndPotentiallyDelegateToCallSite(retEdge);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resolveViaDeltaAndPotentiallyDelegateToCallSite(ReturnEdge<Field, Fact, Stmt, Method> retEdge) {
        AccessPath<Field> inc = retEdge.usedAccessPathOfIncResolver.applyTo(retEdge.incAccessPath);
        if (!retEdge.callDelta.canBeAppliedTo(inc)) {
            return;
        }
        AccessPath<Field> currAccPath = retEdge.callDelta.applyTo(inc);
        if (this.resolvedAccessPath.isPrefixOf(currAccPath) == AccessPath.PrefixTestResult.GUARANTEED_PREFIX) {
            this.incomingEdges.add(retEdge.copyWithIncomingResolver(null, retEdge.usedAccessPathOfIncResolver));
            interest(this);
        } else if (currAccPath.isPrefixOf(this.resolvedAccessPath).atLeast(AccessPath.PrefixTestResult.POTENTIAL_PREFIX)) {
            resolveViaCallSiteResolver(retEdge, currAccPath);
        }
    }

    protected void resolveViaCallSiteResolver(ReturnEdge<Field, Fact, Stmt, Method> retEdge, AccessPath<Field> currAccPath) {
        if (isNullOrCallEdgeResolver(retEdge.resolverAtCaller)) {
            canBeResolvedEmpty();
        } else {
            retEdge.resolverAtCaller.resolve(new DeltaConstraint(currAccPath.getDeltaTo(this.resolvedAccessPath)), new InterestCallback<Field, Fact, Stmt, Method>() { // from class: heros.fieldsens.ReturnSiteResolver.2
                @Override // heros.fieldsens.InterestCallback
                public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
                    ReturnSiteResolver.this.interest(resolver);
                }

                @Override // heros.fieldsens.InterestCallback
                public void canBeResolvedEmpty() {
                    ReturnSiteResolver.this.canBeResolvedEmpty();
                }
            });
        }
    }
}
