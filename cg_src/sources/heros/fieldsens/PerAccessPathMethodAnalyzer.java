package heros.fieldsens;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.structs.FactAtStatement;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/PerAccessPathMethodAnalyzer.class */
public class PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> {
    private static final Logger logger;
    private Fact sourceFact;
    private final AccessPath<Field> accessPath;
    private Map<WrappedFactAtStatement<Field, Fact, Stmt, Method>, WrappedFactAtStatement<Field, Fact, Stmt, Method>> reachableStatements;
    private List<WrappedFactAtStatement<Field, Fact, Stmt, Method>> summaries;
    private Context<Field, Fact, Stmt, Method> context;
    private Method method;
    private DefaultValueMap<FactAtStatement<Fact, Stmt>, ReturnSiteResolver<Field, Fact, Stmt, Method>> returnSiteResolvers;
    private DefaultValueMap<FactAtStatement<Fact, Stmt>, ControlFlowJoinResolver<Field, Fact, Stmt, Method>> ctrFlowJoinResolvers;
    private CallEdgeResolver<Field, Fact, Stmt, Method> callEdgeResolver;
    private PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> parent;
    private Debugger<Field, Fact, Stmt, Method> debugger;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PerAccessPathMethodAnalyzer.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(PerAccessPathMethodAnalyzer.class);
    }

    public PerAccessPathMethodAnalyzer(Method method, Fact sourceFact, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {
        this(method, sourceFact, context, debugger, new AccessPath(), null);
    }

    private PerAccessPathMethodAnalyzer(Method method, Fact sourceFact, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger, AccessPath<Field> accPath, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> parent) {
        this.reachableStatements = Maps.newHashMap();
        this.summaries = Lists.newLinkedList();
        this.returnSiteResolvers = new DefaultValueMap<FactAtStatement<Fact, Stmt>, ReturnSiteResolver<Field, Fact, Stmt, Method>>() { // from class: heros.fieldsens.PerAccessPathMethodAnalyzer.1
            @Override // heros.utilities.DefaultValueMap
            protected /* bridge */ /* synthetic */ Object createItem(Object obj) {
                return createItem((FactAtStatement) ((FactAtStatement) obj));
            }

            protected ReturnSiteResolver<Field, Fact, Stmt, Method> createItem(FactAtStatement<Fact, Stmt> key) {
                return new ReturnSiteResolver<>(PerAccessPathMethodAnalyzer.this.context.factHandler, PerAccessPathMethodAnalyzer.this, key.stmt, PerAccessPathMethodAnalyzer.this.debugger);
            }
        };
        this.ctrFlowJoinResolvers = new DefaultValueMap<FactAtStatement<Fact, Stmt>, ControlFlowJoinResolver<Field, Fact, Stmt, Method>>() { // from class: heros.fieldsens.PerAccessPathMethodAnalyzer.2
            @Override // heros.utilities.DefaultValueMap
            protected /* bridge */ /* synthetic */ Object createItem(Object obj) {
                return createItem((FactAtStatement) ((FactAtStatement) obj));
            }

            protected ControlFlowJoinResolver<Field, Fact, Stmt, Method> createItem(FactAtStatement<Fact, Stmt> key) {
                return new ControlFlowJoinResolver<>(PerAccessPathMethodAnalyzer.this.context.factHandler, PerAccessPathMethodAnalyzer.this, key.stmt, PerAccessPathMethodAnalyzer.this.debugger);
            }
        };
        this.debugger = debugger;
        if (method == null) {
            throw new IllegalArgumentException("Method must be not null");
        }
        this.parent = parent;
        this.method = method;
        this.sourceFact = sourceFact;
        this.accessPath = accPath;
        this.context = context;
        if (parent == null) {
            this.callEdgeResolver = isZeroSource() ? new ZeroCallEdgeResolver<>(this, context.zeroHandler, debugger) : new CallEdgeResolver<>(this, debugger);
        } else {
            this.callEdgeResolver = isZeroSource() ? parent.callEdgeResolver : new CallEdgeResolver<>(this, debugger, parent.callEdgeResolver);
        }
        log("initialized");
    }

    public PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createWithAccessPath(AccessPath<Field> accPath) {
        return new PerAccessPathMethodAnalyzer<>(this.method, this.sourceFact, this.context, this.debugger, accPath, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WrappedFact<Field, Fact, Stmt, Method> wrappedSource() {
        return new WrappedFact<>(this.sourceFact, this.accessPath, this.callEdgeResolver);
    }

    public AccessPath<Field> getAccessPath() {
        return this.accessPath;
    }

    private boolean isBootStrapped() {
        return this.callEdgeResolver.hasIncomingEdges() || !this.accessPath.isEmpty();
    }

    private void bootstrapAtMethodStartPoints() {
        this.callEdgeResolver.interest(this.callEdgeResolver);
        for (Stmt startPoint : this.context.icfg.getStartPointsOf(this.method)) {
            WrappedFactAtStatement<Field, Fact, Stmt, Method> target = new WrappedFactAtStatement<>(startPoint, wrappedSource());
            if (!this.reachableStatements.containsKey(target)) {
                scheduleEdgeTo(target);
            }
        }
    }

    public void addInitialSeed(Stmt stmt) {
        scheduleEdgeTo(new WrappedFactAtStatement<>(stmt, wrappedSource()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleEdgeTo(Collection<Stmt> successors, WrappedFact<Field, Fact, Stmt, Method> fact) {
        for (Stmt stmt : successors) {
            scheduleEdgeTo(new WrappedFactAtStatement<>(stmt, fact));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleEdgeTo(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
        if (!$assertionsDisabled && !this.context.icfg.getMethodOf(factAtStmt.getStatement()).equals(this.method)) {
            throw new AssertionError();
        }
        if (this.reachableStatements.containsKey(factAtStmt)) {
            log("Merging " + factAtStmt);
            this.context.factHandler.merge(this.reachableStatements.get(factAtStmt).getWrappedFact().getFact(), factAtStmt.getWrappedFact().getFact());
            return;
        }
        log("Edge to " + factAtStmt);
        this.reachableStatements.put(factAtStmt, factAtStmt);
        this.context.scheduler.schedule(new Job(factAtStmt));
        this.debugger.edgeTo(this, factAtStmt);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void log(String message) {
        logger.trace("[{}; {}{}: " + message + "]", this.method, this.sourceFact, this.accessPath);
    }

    public String toString() {
        return this.method + "; " + this.sourceFact + this.accessPath;
    }

    void processCall(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
        Collection<Method> calledMethods = this.context.icfg.getCalleesOfCallAt(factAtStmt.getStatement());
        for (Method calledMethod : calledMethods) {
            FlowFunction<Field, Fact, Stmt, Method> flowFunction = this.context.flowFunctions.getCallFlowFunction(factAtStmt.getStatement(), calledMethod);
            Collection<FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method>> targetFacts = flowFunction.computeTargets(factAtStmt.getFact(), new AccessPathHandler<>(factAtStmt.getAccessPath(), factAtStmt.getResolver(), this.debugger));
            for (FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> targetFact : targetFacts) {
                MethodAnalyzer<Field, Fact, Stmt, Method> analyzer = this.context.getAnalyzer(calledMethod);
                analyzer.addIncomingEdge(new CallEdge<>(this, factAtStmt, targetFact.getFact()));
            }
        }
        processCallToReturnEdge(factAtStmt);
    }

    void processExit(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
        log("New Summary: " + factAtStmt);
        if (!this.summaries.add(factAtStmt)) {
            throw new AssertionError();
        }
        this.callEdgeResolver.applySummaries(factAtStmt);
        if (this.context.followReturnsPastSeeds && isZeroSource()) {
            Collection<Stmt> callSites = this.context.icfg.getCallersOf(this.method);
            for (Stmt callSite : callSites) {
                Collection<Stmt> returnSites = this.context.icfg.getReturnSitesOfCallAt(callSite);
                for (Stmt returnSite : returnSites) {
                    FlowFunction<Field, Fact, Stmt, Method> flowFunction = this.context.flowFunctions.getReturnFlowFunction(callSite, this.method, factAtStmt.getStatement(), returnSite);
                    Collection<FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method>> targetFacts = flowFunction.computeTargets(factAtStmt.getFact(), new AccessPathHandler<>(factAtStmt.getAccessPath(), factAtStmt.getResolver(), this.debugger));
                    for (FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> targetFact : targetFacts) {
                        this.context.getAnalyzer(this.context.icfg.getMethodOf(callSite)).addUnbalancedReturnFlow(new WrappedFactAtStatement<>(returnSite, targetFact.getFact()), callSite);
                    }
                }
            }
            if (callSites.isEmpty()) {
                FlowFunction<Field, Fact, Stmt, Method> flowFunction2 = this.context.flowFunctions.getReturnFlowFunction(null, this.method, factAtStmt.getStatement(), null);
                flowFunction2.computeTargets(factAtStmt.getFact(), new AccessPathHandler<>(factAtStmt.getAccessPath(), factAtStmt.getResolver(), this.debugger));
            }
        }
    }

    private void processCallToReturnEdge(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
        if (isLoopStart(factAtStmt.getStatement())) {
            this.ctrFlowJoinResolvers.getOrCreate(factAtStmt.getAsFactAtStatement()).addIncoming(factAtStmt.getWrappedFact());
        } else {
            processNonJoiningCallToReturnFlow(factAtStmt);
        }
    }

    private void processNonJoiningCallToReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
        Collection<Stmt> returnSites = this.context.icfg.getReturnSitesOfCallAt(factAtStmt.getStatement());
        for (Stmt returnSite : returnSites) {
            FlowFunction<Field, Fact, Stmt, Method> flowFunction = this.context.flowFunctions.getCallToReturnFlowFunction(factAtStmt.getStatement(), returnSite);
            Collection<FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method>> targetFacts = flowFunction.computeTargets(factAtStmt.getFact(), new AccessPathHandler<>(factAtStmt.getAccessPath(), factAtStmt.getResolver(), this.debugger));
            for (FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> targetFact : targetFacts) {
                scheduleEdgeTo(new WrappedFactAtStatement<>(returnSite, targetFact.getFact()));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNormalFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
        if (isLoopStart(factAtStmt.getStatement())) {
            this.ctrFlowJoinResolvers.getOrCreate(factAtStmt.getAsFactAtStatement()).addIncoming(factAtStmt.getWrappedFact());
        } else {
            processNormalNonJoiningFlow(factAtStmt);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean isLoopStart(Stmt stmt) {
        int numberOfPredecessors = this.context.icfg.getPredsOf(stmt).size();
        if ((numberOfPredecessors > 1 && !this.context.icfg.isExitStmt(stmt)) || (this.context.icfg.isStartPoint(stmt) && numberOfPredecessors > 0)) {
            HashSet newHashSet = Sets.newHashSet();
            List<Stmt> worklist = Lists.newLinkedList();
            worklist.addAll(this.context.icfg.getPredsOf(stmt));
            while (!worklist.isEmpty()) {
                Stmt current = worklist.remove(0);
                if (current.equals(stmt)) {
                    return true;
                }
                if (newHashSet.add(current)) {
                    worklist.addAll(this.context.icfg.getPredsOf(current));
                }
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void processFlowFromJoinStmt(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
        if (this.context.icfg.isCallStmt(factAtStmt.getStatement())) {
            processNonJoiningCallToReturnFlow(factAtStmt);
        } else {
            processNormalNonJoiningFlow(factAtStmt);
        }
    }

    private void processNormalNonJoiningFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
        final List<Stmt> successors = this.context.icfg.getSuccsOf(factAtStmt.getStatement());
        FlowFunction<Field, Fact, Stmt, Method> flowFunction = this.context.flowFunctions.getNormalFlowFunction(factAtStmt.getStatement());
        Collection<FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method>> targetFacts = flowFunction.computeTargets(factAtStmt.getFact(), new AccessPathHandler<>(factAtStmt.getAccessPath(), factAtStmt.getResolver(), this.debugger));
        for (final FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> targetFact : targetFacts) {
            if (targetFact.getConstraint() == null) {
                scheduleEdgeTo(successors, targetFact.getFact());
            } else {
                targetFact.getFact().getResolver().resolve(targetFact.getConstraint(), new InterestCallback<Field, Fact, Stmt, Method>() { // from class: heros.fieldsens.PerAccessPathMethodAnalyzer.3
                    @Override // heros.fieldsens.InterestCallback
                    public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
                        analyzer.scheduleEdgeTo(successors, new WrappedFact(targetFact.getFact().getFact(), targetFact.getFact().getAccessPath(), resolver));
                    }

                    @Override // heros.fieldsens.InterestCallback
                    public void canBeResolvedEmpty() {
                        PerAccessPathMethodAnalyzer.this.callEdgeResolver.resolve(targetFact.getConstraint(), this);
                    }
                });
            }
        }
    }

    public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {
        if (isBootStrapped()) {
            this.context.factHandler.merge(this.sourceFact, incEdge.getCalleeSourceFact().getFact());
        } else {
            bootstrapAtMethodStartPoints();
        }
        this.callEdgeResolver.addIncoming(incEdge);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applySummary(CallEdge<Field, Fact, Stmt, Method> incEdge, WrappedFactAtStatement<Field, Fact, Stmt, Method> exitFact) {
        Collection<Stmt> returnSites = this.context.icfg.getReturnSitesOfCallAt(incEdge.getCallSite());
        for (Stmt returnSite : returnSites) {
            FlowFunction<Field, Fact, Stmt, Method> flowFunction = this.context.flowFunctions.getReturnFlowFunction(incEdge.getCallSite(), this.method, exitFact.getStatement(), returnSite);
            Set<FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method>> targets = flowFunction.computeTargets(exitFact.getFact(), new AccessPathHandler<>(exitFact.getAccessPath(), exitFact.getResolver(), this.debugger));
            for (FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> targetFact : targets) {
                this.context.factHandler.restoreCallingContext(targetFact.getFact().getFact(), incEdge.getCallerCallSiteFact().getFact());
                scheduleReturnEdge(incEdge, targetFact.getFact(), returnSite);
            }
        }
    }

    public void scheduleUnbalancedReturnEdgeTo(WrappedFactAtStatement<Field, Fact, Stmt, Method> fact) {
        ReturnSiteResolver<Field, Fact, Stmt, Method> resolver = this.returnSiteResolvers.getOrCreate(fact.getAsFactAtStatement());
        resolver.addIncoming(new WrappedFact<>(fact.getWrappedFact().getFact(), fact.getWrappedFact().getAccessPath(), fact.getWrappedFact().getResolver()), null, AccessPath.Delta.empty());
    }

    private void scheduleReturnEdge(CallEdge<Field, Fact, Stmt, Method> incEdge, WrappedFact<Field, Fact, Stmt, Method> fact, Stmt returnSite) {
        AccessPath.Delta<Field> delta = this.accessPath.getDeltaTo(incEdge.getCalleeSourceFact().getAccessPath());
        ReturnSiteResolver<Field, Fact, Stmt, Method> returnSiteResolver = incEdge.getCallerAnalyzer().returnSiteResolvers.getOrCreate(new FactAtStatement<>(fact.getFact(), returnSite));
        returnSiteResolver.addIncoming(fact, incEdge.getCalleeSourceFact().getResolver(), delta);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applySummaries(CallEdge<Field, Fact, Stmt, Method> incEdge) {
        for (WrappedFactAtStatement<Field, Fact, Stmt, Method> summary : this.summaries) {
            applySummary(incEdge, summary);
        }
    }

    public boolean isZeroSource() {
        return this.sourceFact.equals(this.context.zeroValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/PerAccessPathMethodAnalyzer$Job.class */
    public class Job implements Runnable {
        private WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt;

        public Job(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
            this.factAtStmt = factAtStmt;
            PerAccessPathMethodAnalyzer.this.debugger.newJob(PerAccessPathMethodAnalyzer.this, factAtStmt);
        }

        @Override // java.lang.Runnable
        public void run() {
            PerAccessPathMethodAnalyzer.this.debugger.jobStarted(PerAccessPathMethodAnalyzer.this, this.factAtStmt);
            if (!PerAccessPathMethodAnalyzer.this.context.icfg.isCallStmt(this.factAtStmt.getStatement())) {
                if (PerAccessPathMethodAnalyzer.this.context.icfg.isExitStmt(this.factAtStmt.getStatement())) {
                    PerAccessPathMethodAnalyzer.this.processExit(this.factAtStmt);
                }
                if (!PerAccessPathMethodAnalyzer.this.context.icfg.getSuccsOf(this.factAtStmt.getStatement()).isEmpty()) {
                    PerAccessPathMethodAnalyzer.this.processNormalFlow(this.factAtStmt);
                }
            } else {
                PerAccessPathMethodAnalyzer.this.processCall(this.factAtStmt);
            }
            PerAccessPathMethodAnalyzer.this.debugger.jobFinished(PerAccessPathMethodAnalyzer.this, this.factAtStmt);
        }

        public String toString() {
            return "Job: " + this.factAtStmt;
        }
    }

    public CallEdgeResolver<Field, Fact, Stmt, Method> getCallEdgeResolver() {
        return this.callEdgeResolver;
    }

    public Method getMethod() {
        return this.method;
    }
}
