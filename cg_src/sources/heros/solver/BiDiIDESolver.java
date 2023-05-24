package heros.solver;

import com.google.common.collect.Maps;
import heros.EdgeFunction;
import heros.EdgeFunctions;
import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.InterproceduralCFG;
import heros.MeetLattice;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/BiDiIDESolver.class */
public class BiDiIDESolver<N, D, M, V, I extends InterproceduralCFG<N, M>> {
    private final IDETabulationProblem<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V, I> forwardProblem;
    private final IDETabulationProblem<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V, I> backwardProblem;
    private final CountingThreadPoolExecutor sharedExecutor;
    protected BiDiIDESolver<N, D, M, V, I>.SingleDirectionSolver fwSolver;
    protected BiDiIDESolver<N, D, M, V, I>.SingleDirectionSolver bwSolver;

    public BiDiIDESolver(IDETabulationProblem<N, D, M, V, I> forwardProblem, IDETabulationProblem<N, D, M, V, I> backwardProblem) {
        if (!forwardProblem.followReturnsPastSeeds() || !backwardProblem.followReturnsPastSeeds()) {
            throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true.");
        }
        this.forwardProblem = new AugmentedTabulationProblem(forwardProblem);
        this.backwardProblem = new AugmentedTabulationProblem(backwardProblem);
        this.sharedExecutor = new CountingThreadPoolExecutor(Math.max(1, forwardProblem.numThreads()), Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());
    }

    public void solve() {
        this.fwSolver = createSingleDirectionSolver(this.forwardProblem, "FW");
        this.bwSolver = createSingleDirectionSolver(this.backwardProblem, "BW");
        ((SingleDirectionSolver) this.fwSolver).otherSolver = this.bwSolver;
        ((SingleDirectionSolver) this.bwSolver).otherSolver = this.fwSolver;
        this.bwSolver.submitInitialSeeds();
        this.fwSolver.solve();
    }

    protected BiDiIDESolver<N, D, M, V, I>.SingleDirectionSolver createSingleDirectionSolver(IDETabulationProblem<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V, I> problem, String debugName) {
        return new SingleDirectionSolver(problem, debugName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/BiDiIDESolver$PausedEdge.class */
    public class PausedEdge {
        private N retSiteC;
        private BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt targetVal;
        private EdgeFunction<V> edgeFunction;
        private N relatedCallSite;

        public PausedEdge(N retSiteC, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt targetVal, EdgeFunction<V> edgeFunction, N relatedCallSite) {
            this.retSiteC = retSiteC;
            this.targetVal = targetVal;
            this.edgeFunction = edgeFunction;
            this.relatedCallSite = relatedCallSite;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/BiDiIDESolver$LeakKey.class */
    public static class LeakKey<N> {
        private N sourceStmt;
        private N relatedCallSite;

        public LeakKey(N sourceStmt, N relatedCallSite) {
            this.sourceStmt = sourceStmt;
            this.relatedCallSite = relatedCallSite;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.relatedCallSite == null ? 0 : this.relatedCallSite.hashCode());
            return (31 * result) + (this.sourceStmt == null ? 0 : this.sourceStmt.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof LeakKey)) {
                return false;
            }
            LeakKey other = (LeakKey) obj;
            if (this.relatedCallSite == null) {
                if (other.relatedCallSite != null) {
                    return false;
                }
            } else if (!this.relatedCallSite.equals(other.relatedCallSite)) {
                return false;
            }
            if (this.sourceStmt == null) {
                if (other.sourceStmt != null) {
                    return false;
                }
                return true;
            } else if (!this.sourceStmt.equals(other.sourceStmt)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/BiDiIDESolver$SingleDirectionSolver.class */
    public class SingleDirectionSolver extends IDESolver<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V, I> {
        private final String debugName;
        private BiDiIDESolver<N, D, M, V, I>.SingleDirectionSolver otherSolver;
        private Set<LeakKey<N>> leakedSources;
        private ConcurrentMap<LeakKey<N>, Set<BiDiIDESolver<N, D, M, V, I>.PausedEdge>> pausedPathEdges;
        static final /* synthetic */ boolean $assertionsDisabled;

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // heros.solver.IDESolver
        public /* bridge */ /* synthetic */ void propagate(Object obj, Object obj2, Object obj3, EdgeFunction edgeFunction, Object obj4, boolean z) {
            propagate((BiDiIDESolver<EdgeFunction, D, M, V, I>.AbstractionWithSourceStmt) obj, (EdgeFunction) obj2, (BiDiIDESolver<EdgeFunction, D, M, V, I>.AbstractionWithSourceStmt) obj3, edgeFunction, (EdgeFunction) obj4, z);
        }

        @Override // heros.solver.IDESolver
        protected /* bridge */ /* synthetic */ Object restoreContextOnReturnedFact(Object obj, Object obj2, Object obj3) {
            return restoreContextOnReturnedFact((SingleDirectionSolver) obj, (BiDiIDESolver<SingleDirectionSolver, D, M, V, I>.AbstractionWithSourceStmt) obj2, (BiDiIDESolver<SingleDirectionSolver, D, M, V, I>.AbstractionWithSourceStmt) obj3);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // heros.solver.IDESolver
        public /* bridge */ /* synthetic */ void propagateUnbalancedReturnFlow(Object obj, Object obj2, EdgeFunction edgeFunction, Object obj3) {
            propagateUnbalancedReturnFlow((EdgeFunction) obj, (BiDiIDESolver<EdgeFunction, D, M, V, I>.AbstractionWithSourceStmt) obj2, edgeFunction, (EdgeFunction) obj3);
        }

        static {
            $assertionsDisabled = !BiDiIDESolver.class.desiredAssertionStatus();
        }

        public SingleDirectionSolver(IDETabulationProblem<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V, I> ifdsProblem, String debugName) {
            super(ifdsProblem);
            this.leakedSources = Collections.newSetFromMap(Maps.newConcurrentMap());
            this.pausedPathEdges = Maps.newConcurrentMap();
            this.debugName = debugName;
        }

        protected void propagateUnbalancedReturnFlow(N retSiteC, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt targetVal, EdgeFunction<V> edgeFunction, N relatedCallSite) {
            N sourceStmt = targetVal.getSourceStmt();
            LeakKey<N> leakKey = new LeakKey<>(sourceStmt, relatedCallSite);
            this.leakedSources.add(leakKey);
            if (this.otherSolver.hasLeaked(leakKey)) {
                this.otherSolver.unpausePathEdgesForSource(leakKey);
                super.propagateUnbalancedReturnFlow((EdgeFunction<V>) retSiteC, (N) targetVal, (EdgeFunction) edgeFunction, (EdgeFunction<V>) relatedCallSite);
                return;
            }
            Set<BiDiIDESolver<N, D, M, V, I>.PausedEdge> newPausedEdges = Collections.newSetFromMap(Maps.newConcurrentMap());
            Set<BiDiIDESolver<N, D, M, V, I>.PausedEdge> existingPausedEdges = this.pausedPathEdges.putIfAbsent(leakKey, newPausedEdges);
            if (existingPausedEdges == null) {
                existingPausedEdges = newPausedEdges;
            }
            BiDiIDESolver<N, D, M, V, I>.PausedEdge edge = new PausedEdge(retSiteC, targetVal, edgeFunction, relatedCallSite);
            existingPausedEdges.add(edge);
            if (this.otherSolver.hasLeaked(leakKey) && existingPausedEdges.remove(edge)) {
                super.propagateUnbalancedReturnFlow((EdgeFunction<V>) retSiteC, (N) targetVal, (EdgeFunction) edgeFunction, (EdgeFunction<V>) relatedCallSite);
            }
            logger.debug(" ++ PAUSE {}: {}", this.debugName, edge);
        }

        protected void propagate(BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt sourceVal, N target, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt targetVal, EdgeFunction<V> f, N relatedCallSite, boolean isUnbalancedReturn) {
            if (isUnbalancedReturn) {
                if (!$assertionsDisabled && sourceVal.getSourceStmt() != null) {
                    throw new AssertionError("source value should have no statement attached");
                }
                super.propagate((N) sourceVal, (EdgeFunction<V>) target, (N) new AbstractionWithSourceStmt(targetVal.getAbstraction(), relatedCallSite), (EdgeFunction) f, (EdgeFunction<V>) relatedCallSite, isUnbalancedReturn);
                return;
            }
            super.propagate((N) sourceVal, (EdgeFunction<V>) target, (N) targetVal, (EdgeFunction) f, (EdgeFunction<V>) relatedCallSite, isUnbalancedReturn);
        }

        protected BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt restoreContextOnReturnedFact(N callSite, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt d4, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt d5) {
            return new AbstractionWithSourceStmt(d5.getAbstraction(), d4.getSourceStmt());
        }

        private boolean hasLeaked(LeakKey<N> leakKey) {
            return this.leakedSources.contains(leakKey);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void unpausePathEdgesForSource(LeakKey<N> leakKey) {
            Set<BiDiIDESolver<N, D, M, V, I>.PausedEdge> pausedEdges = this.pausedPathEdges.get(leakKey);
            if (pausedEdges != null) {
                for (BiDiIDESolver<N, D, M, V, I>.PausedEdge edge : pausedEdges) {
                    if (pausedEdges.remove(edge)) {
                        if (DEBUG) {
                            logger.debug("-- UNPAUSE {}: {}", this.debugName, edge);
                        }
                        super.propagateUnbalancedReturnFlow((EdgeFunction) ((PausedEdge) edge).retSiteC, (Object) ((PausedEdge) edge).targetVal, ((PausedEdge) edge).edgeFunction, (EdgeFunction) ((PausedEdge) edge).relatedCallSite);
                    }
                }
            }
        }

        @Override // heros.solver.IDESolver
        protected CountingThreadPoolExecutor getExecutor() {
            return BiDiIDESolver.this.sharedExecutor;
        }

        @Override // heros.solver.IDESolver
        protected String getDebugName() {
            return this.debugName;
        }
    }

    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/BiDiIDESolver$AbstractionWithSourceStmt.class */
    public class AbstractionWithSourceStmt {
        protected final D abstraction;
        protected final N source;

        private AbstractionWithSourceStmt(D abstraction, N source) {
            this.abstraction = abstraction;
            this.source = source;
        }

        public D getAbstraction() {
            return this.abstraction;
        }

        public N getSourceStmt() {
            return this.source;
        }

        public String toString() {
            if (this.source != null) {
                return "" + this.abstraction + "-@-" + this.source + "";
            }
            return this.abstraction.toString();
        }

        public int hashCode() {
            int result = (31 * 1) + (this.abstraction == null ? 0 : this.abstraction.hashCode());
            return (31 * result) + (this.source == null ? 0 : this.source.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt other = (AbstractionWithSourceStmt) obj;
            if (this.abstraction == null) {
                if (other.abstraction != null) {
                    return false;
                }
            } else if (!this.abstraction.equals(other.abstraction)) {
                return false;
            }
            if (this.source == null) {
                if (other.source != null) {
                    return false;
                }
                return true;
            } else if (!this.source.equals(other.source)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/BiDiIDESolver$AugmentedTabulationProblem.class */
    public class AugmentedTabulationProblem implements IDETabulationProblem<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V, I> {
        private final IDETabulationProblem<N, D, M, V, I> delegate;
        private final BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt ZERO;
        private final FlowFunctions<N, D, M> originalFunctions;

        public AugmentedTabulationProblem(IDETabulationProblem<N, D, M, V, I> delegate) {
            this.delegate = delegate;
            this.originalFunctions = this.delegate.flowFunctions();
            this.ZERO = new AbstractionWithSourceStmt(delegate.zeroValue(), null);
        }

        @Override // heros.IFDSTabulationProblem
        public FlowFunctions<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M> flowFunctions() {
            return new FlowFunctions<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M>() { // from class: heros.solver.BiDiIDESolver.AugmentedTabulationProblem.1
                @Override // heros.FlowFunctions
                public FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> getNormalFlowFunction(final N curr, final N succ) {
                    return new FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt>() { // from class: heros.solver.BiDiIDESolver.AugmentedTabulationProblem.1.1
                        @Override // heros.FlowFunction
                        public /* bridge */ /* synthetic */ Set computeTargets(Object obj) {
                            return computeTargets((AbstractionWithSourceStmt) ((AbstractionWithSourceStmt) obj));
                        }

                        /* JADX WARN: Multi-variable type inference failed */
                        public Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> computeTargets(BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt source) {
                            return copyOverSourceStmts(source, AugmentedTabulationProblem.this.originalFunctions.getNormalFlowFunction(curr, succ));
                        }
                    };
                }

                @Override // heros.FlowFunctions
                public FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> getCallFlowFunction(final N callStmt, final M destinationMethod) {
                    return new FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt>() { // from class: heros.solver.BiDiIDESolver.AugmentedTabulationProblem.1.2
                        @Override // heros.FlowFunction
                        public /* bridge */ /* synthetic */ Set computeTargets(Object obj) {
                            return computeTargets((AbstractionWithSourceStmt) ((AbstractionWithSourceStmt) obj));
                        }

                        /* JADX WARN: Multi-variable type inference failed */
                        public Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> computeTargets(BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt source) {
                            Set<D> origTargets = AugmentedTabulationProblem.this.originalFunctions.getCallFlowFunction(callStmt, destinationMethod).computeTargets(source.getAbstraction());
                            Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> res = new HashSet<>();
                            for (D d : origTargets) {
                                res.add(new AbstractionWithSourceStmt(d, null));
                            }
                            return res;
                        }
                    };
                }

                @Override // heros.FlowFunctions
                public FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
                    return new FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt>() { // from class: heros.solver.BiDiIDESolver.AugmentedTabulationProblem.1.3
                        @Override // heros.FlowFunction
                        public /* bridge */ /* synthetic */ Set computeTargets(Object obj) {
                            return computeTargets((AbstractionWithSourceStmt) ((AbstractionWithSourceStmt) obj));
                        }

                        /* JADX WARN: Multi-variable type inference failed */
                        public Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> computeTargets(BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt source) {
                            return copyOverSourceStmts(source, AugmentedTabulationProblem.this.originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
                        }
                    };
                }

                @Override // heros.FlowFunctions
                public FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
                    return new FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt>() { // from class: heros.solver.BiDiIDESolver.AugmentedTabulationProblem.1.4
                        @Override // heros.FlowFunction
                        public /* bridge */ /* synthetic */ Set computeTargets(Object obj) {
                            return computeTargets((AbstractionWithSourceStmt) ((AbstractionWithSourceStmt) obj));
                        }

                        /* JADX WARN: Multi-variable type inference failed */
                        public Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> computeTargets(BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt source) {
                            return copyOverSourceStmts(source, AugmentedTabulationProblem.this.originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
                        }
                    };
                }

                /* JADX INFO: Access modifiers changed from: private */
                public Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> copyOverSourceStmts(BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt source, FlowFunction<D> originalFunction) {
                    D originalAbstraction = source.getAbstraction();
                    Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);
                    Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> res = new HashSet<>();
                    for (D d : origTargets) {
                        res.add(new AbstractionWithSourceStmt(d, source.getSourceStmt()));
                    }
                    return res;
                }
            };
        }

        @Override // heros.SolverConfiguration
        public boolean followReturnsPastSeeds() {
            return this.delegate.followReturnsPastSeeds();
        }

        @Override // heros.SolverConfiguration
        public boolean autoAddZero() {
            return this.delegate.autoAddZero();
        }

        @Override // heros.SolverConfiguration
        public int numThreads() {
            return this.delegate.numThreads();
        }

        @Override // heros.SolverConfiguration
        public boolean computeValues() {
            return this.delegate.computeValues();
        }

        @Override // heros.IFDSTabulationProblem
        public I interproceduralCFG() {
            return this.delegate.interproceduralCFG();
        }

        @Override // heros.IFDSTabulationProblem
        public Map<N, Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt>> initialSeeds() {
            Map<N, Set<D>> originalSeeds = this.delegate.initialSeeds();
            HashMap res = new HashMap();
            for (Map.Entry<N, Set<D>> entry : originalSeeds.entrySet()) {
                N stmt = entry.getKey();
                Set<D> seeds = entry.getValue();
                Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> resSet = new HashSet<>();
                for (D d : seeds) {
                    resSet.add(new AbstractionWithSourceStmt(d, stmt));
                }
                res.put(stmt, resSet);
            }
            return res;
        }

        @Override // heros.IFDSTabulationProblem
        public BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt zeroValue() {
            return this.ZERO;
        }

        @Override // heros.IDETabulationProblem
        public EdgeFunctions<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V> edgeFunctions() {
            return new EdgeFunctions<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V>() { // from class: heros.solver.BiDiIDESolver.AugmentedTabulationProblem.2
                @Override // heros.EdgeFunctions
                public /* bridge */ /* synthetic */ EdgeFunction getCallToReturnEdgeFunction(Object obj, Object obj2, Object obj3, Object obj4) {
                    return getCallToReturnEdgeFunction((BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt) obj, (BiDiIDESolver<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, D, M, V, I>.AbstractionWithSourceStmt) obj2, (BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt) obj3, (BiDiIDESolver<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, D, M, V, I>.AbstractionWithSourceStmt) obj4);
                }

                @Override // heros.EdgeFunctions
                public /* bridge */ /* synthetic */ EdgeFunction getReturnEdgeFunction(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
                    return getReturnEdgeFunction((BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt) obj, obj2, (BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt) obj3, (BiDiIDESolver<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, D, Object, V, I>.AbstractionWithSourceStmt) obj4, (BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt) obj5, (BiDiIDESolver<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, D, Object, V, I>.AbstractionWithSourceStmt) obj6);
                }

                @Override // heros.EdgeFunctions
                public /* bridge */ /* synthetic */ EdgeFunction getCallEdgeFunction(Object obj, Object obj2, Object obj3, Object obj4) {
                    return getCallEdgeFunction((AnonymousClass2) obj, (BiDiIDESolver<AnonymousClass2, D, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, V, I>.AbstractionWithSourceStmt) obj2, (BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt) obj3, (BiDiIDESolver<AnonymousClass2, D, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, V, I>.AbstractionWithSourceStmt) obj4);
                }

                @Override // heros.EdgeFunctions
                public /* bridge */ /* synthetic */ EdgeFunction getNormalEdgeFunction(Object obj, Object obj2, Object obj3, Object obj4) {
                    return getNormalEdgeFunction((BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt) obj, (BiDiIDESolver<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, D, M, V, I>.AbstractionWithSourceStmt) obj2, (BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt) obj3, (BiDiIDESolver<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, D, M, V, I>.AbstractionWithSourceStmt) obj4);
                }

                public EdgeFunction<V> getNormalEdgeFunction(N curr, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt currNode, N succ, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt succNode) {
                    return AugmentedTabulationProblem.this.delegate.edgeFunctions().getNormalEdgeFunction(curr, currNode.getAbstraction(), succ, succNode.getAbstraction());
                }

                public EdgeFunction<V> getCallEdgeFunction(N callStmt, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt srcNode, M destinationMethod, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt destNode) {
                    return AugmentedTabulationProblem.this.delegate.edgeFunctions().getCallEdgeFunction(callStmt, srcNode.getAbstraction(), destinationMethod, destNode.getAbstraction());
                }

                public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt exitNode, N returnSite, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt retNode) {
                    return AugmentedTabulationProblem.this.delegate.edgeFunctions().getReturnEdgeFunction(callSite, calleeMethod, exitStmt, exitNode.getAbstraction(), returnSite, retNode.getAbstraction());
                }

                public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt callNode, N returnSite, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt returnSideNode) {
                    return AugmentedTabulationProblem.this.delegate.edgeFunctions().getCallToReturnEdgeFunction(callSite, callNode.getAbstraction(), returnSite, returnSideNode.getAbstraction());
                }
            };
        }

        @Override // heros.IDETabulationProblem
        public MeetLattice<V> meetLattice() {
            return this.delegate.meetLattice();
        }

        @Override // heros.IDETabulationProblem
        public EdgeFunction<V> allTopFunction() {
            return this.delegate.allTopFunction();
        }

        @Override // heros.SolverConfiguration
        public boolean recordEdges() {
            return this.delegate.recordEdges();
        }
    }
}
