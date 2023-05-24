package soot.jimple.infoflow.aliasing;

import java.util.Set;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.Stmt;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.solver.IInfoflowSolver;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/aliasing/IAliasingStrategy.class */
public interface IAliasingStrategy {
    void computeAliasTaints(Abstraction abstraction, Stmt stmt, Value value, Set<Abstraction> set, SootMethod sootMethod, Abstraction abstraction2);

    boolean isInteractive();

    boolean mayAlias(AccessPath accessPath, AccessPath accessPath2);

    void injectCallingContext(Abstraction abstraction, IInfoflowSolver iInfoflowSolver, SootMethod sootMethod, Unit unit, Abstraction abstraction2, Abstraction abstraction3);

    boolean isFlowSensitive();

    boolean requiresAnalysisOnReturn();

    boolean hasProcessedMethod(SootMethod sootMethod);

    boolean isLazyAnalysis();

    IInfoflowSolver getSolver();

    void cleanup();
}
