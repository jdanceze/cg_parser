package heros.solver;

import heros.EdgeFunction;
import heros.EdgeFunctions;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.MeetLattice;
import heros.edgefunc.AllBottom;
import heros.edgefunc.AllTop;
import heros.edgefunc.EdgeIdentity;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/IFDSSolver.class */
public class IFDSSolver<N, D, M, I extends InterproceduralCFG<N, M>> extends IDESolver<N, D, M, BinaryDomain, I> {
    private static final EdgeFunction<BinaryDomain> ALL_BOTTOM = new AllBottom(BinaryDomain.BOTTOM);

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/IFDSSolver$BinaryDomain.class */
    public enum BinaryDomain {
        TOP,
        BOTTOM
    }

    public IFDSSolver(IFDSTabulationProblem<N, D, M, I> ifdsProblem) {
        super(createIDETabulationProblem(ifdsProblem));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <N, D, M, I extends InterproceduralCFG<N, M>> IDETabulationProblem<N, D, M, BinaryDomain, I> createIDETabulationProblem(final IFDSTabulationProblem<N, D, M, I> ifdsProblem) {
        return (IDETabulationProblem<N, D, M, BinaryDomain, I>) new IDETabulationProblem<N, D, M, BinaryDomain, I>() { // from class: heros.solver.IFDSSolver.1
            @Override // heros.IFDSTabulationProblem
            public FlowFunctions<N, D, M> flowFunctions() {
                return IFDSTabulationProblem.this.flowFunctions();
            }

            @Override // heros.IFDSTabulationProblem
            public I interproceduralCFG() {
                return (I) IFDSTabulationProblem.this.interproceduralCFG();
            }

            @Override // heros.IFDSTabulationProblem
            public Map<N, Set<D>> initialSeeds() {
                return IFDSTabulationProblem.this.initialSeeds();
            }

            @Override // heros.IFDSTabulationProblem
            public D zeroValue() {
                return (D) IFDSTabulationProblem.this.zeroValue();
            }

            @Override // heros.IDETabulationProblem
            public EdgeFunctions<N, D, M, BinaryDomain> edgeFunctions() {
                return new IFDSEdgeFunctions();
            }

            @Override // heros.IDETabulationProblem
            public MeetLattice<BinaryDomain> meetLattice() {
                return new MeetLattice<BinaryDomain>() { // from class: heros.solver.IFDSSolver.1.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // heros.MeetLattice
                    public BinaryDomain topElement() {
                        return BinaryDomain.TOP;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // heros.MeetLattice
                    public BinaryDomain bottomElement() {
                        return BinaryDomain.BOTTOM;
                    }

                    @Override // heros.MeetLattice
                    public BinaryDomain meet(BinaryDomain left, BinaryDomain right) {
                        if (left == BinaryDomain.TOP && right == BinaryDomain.TOP) {
                            return BinaryDomain.TOP;
                        }
                        return BinaryDomain.BOTTOM;
                    }
                };
            }

            @Override // heros.IDETabulationProblem
            public EdgeFunction<BinaryDomain> allTopFunction() {
                return new AllTop(BinaryDomain.TOP);
            }

            @Override // heros.SolverConfiguration
            public boolean followReturnsPastSeeds() {
                return IFDSTabulationProblem.this.followReturnsPastSeeds();
            }

            @Override // heros.SolverConfiguration
            public boolean autoAddZero() {
                return IFDSTabulationProblem.this.autoAddZero();
            }

            @Override // heros.SolverConfiguration
            public int numThreads() {
                return IFDSTabulationProblem.this.numThreads();
            }

            @Override // heros.SolverConfiguration
            public boolean computeValues() {
                return IFDSTabulationProblem.this.computeValues();
            }

            /* renamed from: heros.solver.IFDSSolver$1$IFDSEdgeFunctions */
            /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/IFDSSolver$1$IFDSEdgeFunctions.class */
            class IFDSEdgeFunctions implements EdgeFunctions<N, D, M, BinaryDomain> {
                IFDSEdgeFunctions() {
                }

                @Override // heros.EdgeFunctions
                public EdgeFunction<BinaryDomain> getNormalEdgeFunction(N src, D srcNode, N tgt, D tgtNode) {
                    if (srcNode == IFDSTabulationProblem.this.zeroValue()) {
                        return IFDSSolver.ALL_BOTTOM;
                    }
                    return EdgeIdentity.v();
                }

                @Override // heros.EdgeFunctions
                public EdgeFunction<BinaryDomain> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {
                    if (srcNode == IFDSTabulationProblem.this.zeroValue()) {
                        return IFDSSolver.ALL_BOTTOM;
                    }
                    return EdgeIdentity.v();
                }

                @Override // heros.EdgeFunctions
                public EdgeFunction<BinaryDomain> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
                    if (exitNode == IFDSTabulationProblem.this.zeroValue()) {
                        return IFDSSolver.ALL_BOTTOM;
                    }
                    return EdgeIdentity.v();
                }

                @Override // heros.EdgeFunctions
                public EdgeFunction<BinaryDomain> getCallToReturnEdgeFunction(N callStmt, D callNode, N returnSite, D returnSideNode) {
                    if (callNode == IFDSTabulationProblem.this.zeroValue()) {
                        return IFDSSolver.ALL_BOTTOM;
                    }
                    return EdgeIdentity.v();
                }
            }

            @Override // heros.SolverConfiguration
            public boolean recordEdges() {
                return IFDSTabulationProblem.this.recordEdges();
            }
        };
    }

    public Set<D> ifdsResultsAt(N statement) {
        return resultsAt(statement).keySet();
    }
}
