package soot.jimple.infoflow.solver.fastSolver;

import soot.SootMethod;
import soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode;
import soot.jimple.infoflow.solver.fastSolver.IFDSSolver;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/fastSolver/DefaultSchedulingStrategy.class */
public class DefaultSchedulingStrategy<N, D extends FastSolverLinkedNode<D, N>, I extends BiDiInterproceduralCFG<N, SootMethod>> {
    protected final IFDSSolver<N, D, I> solver;
    public final ISchedulingStrategy<N, D> EACH_EDGE_INDIVIDUALLY = (ISchedulingStrategy<N, D>) new ISchedulingStrategy<N, D>() { // from class: soot.jimple.infoflow.solver.fastSolver.DefaultSchedulingStrategy.1
        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateInitialSeeds(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.EXECUTOR);
        }

        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateNormalFlow(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.EXECUTOR);
        }

        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateCallFlow(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.EXECUTOR);
        }

        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateCallToReturnFlow(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.EXECUTOR);
        }

        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateReturnFlow(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.EXECUTOR);
        }
    };
    public final ISchedulingStrategy<N, D> EACH_METHOD_INDIVIDUALLY = (ISchedulingStrategy<N, D>) new ISchedulingStrategy<N, D>() { // from class: soot.jimple.infoflow.solver.fastSolver.DefaultSchedulingStrategy.2
        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateInitialSeeds(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.EXECUTOR);
        }

        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateNormalFlow(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.LOCAL);
        }

        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateCallFlow(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.EXECUTOR);
        }

        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateCallToReturnFlow(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.LOCAL);
        }

        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateReturnFlow(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.EXECUTOR);
        }
    };
    public final ISchedulingStrategy<N, D> ALL_EDGES_LOCALLY = (ISchedulingStrategy<N, D>) new ISchedulingStrategy<N, D>() { // from class: soot.jimple.infoflow.solver.fastSolver.DefaultSchedulingStrategy.3
        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateInitialSeeds(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.LOCAL);
        }

        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateNormalFlow(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.LOCAL);
        }

        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateCallFlow(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.LOCAL);
        }

        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateCallToReturnFlow(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.LOCAL);
        }

        @Override // soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy
        public void propagateReturnFlow(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
            DefaultSchedulingStrategy.this.solver.propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, IFDSSolver.ScheduleTarget.LOCAL);
        }
    };

    public DefaultSchedulingStrategy(IFDSSolver<N, D, I> solver) {
        this.solver = solver;
    }
}
