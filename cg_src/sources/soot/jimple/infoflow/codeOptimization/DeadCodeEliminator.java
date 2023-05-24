package soot.jimple.infoflow.codeOptimization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.entryPointCreators.SimulatedCodeElementTag;
import soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager;
import soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper;
import soot.jimple.infoflow.util.SystemClassHandler;
import soot.jimple.toolkits.scalar.ConditionalBranchFolder;
import soot.jimple.toolkits.scalar.ConstantPropagatorAndFolder;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/codeOptimization/DeadCodeEliminator.class */
public class DeadCodeEliminator implements ICodeOptimizer {
    private InfoflowConfiguration config;

    @Override // soot.jimple.infoflow.codeOptimization.ICodeOptimizer
    public void initialize(InfoflowConfiguration config) {
        this.config = config;
    }

    @Override // soot.jimple.infoflow.codeOptimization.ICodeOptimizer
    public void run(InfoflowManager manager, Collection<SootMethod> entryPoints, ISourceSinkManager sourcesSinks, ITaintPropagationWrapper taintWrapper) {
        QueueReader<MethodOrMethodContext> rdr = Scene.v().getReachableMethods().listener();
        while (rdr.hasNext()) {
            SootMethod method = rdr.next().method();
            if (method != null && method.hasActiveBody() && method.getTag(SimulatedCodeElementTag.TAG_NAME) == null) {
                List<Unit> callSites = getCallsInMethod(method);
                ConstantPropagatorAndFolder.v().transform(method.getActiveBody());
                DeadAssignmentEliminator.v().transform(method.getActiveBody());
                removeDeadCallgraphEdges(method, callSites);
            }
        }
        InterproceduralConstantValuePropagator ipcvp = new InterproceduralConstantValuePropagator(manager, entryPoints, sourcesSinks, taintWrapper);
        ipcvp.setRemoveSideEffectFreeMethods(this.config.getCodeEliminationMode() == InfoflowConfiguration.CodeEliminationMode.RemoveSideEffectFreeCode && this.config.getImplicitFlowMode() != InfoflowConfiguration.ImplicitFlowMode.AllImplicitFlows);
        ipcvp.setExcludeSystemClasses(this.config.getIgnoreFlowsInSystemPackages());
        ipcvp.transform();
        QueueReader<MethodOrMethodContext> rdr2 = Scene.v().getReachableMethods().listener();
        while (rdr2.hasNext()) {
            MethodOrMethodContext sm = rdr2.next();
            SootMethod method2 = sm.method();
            if (method2 != null && method2.hasActiveBody() && (!this.config.getIgnoreFlowsInSystemPackages() || !SystemClassHandler.v().isClassInSystemPackage(sm.method().getDeclaringClass().getName()))) {
                ConditionalBranchFolder.v().transform(method2.getActiveBody());
                List<Unit> callSites2 = getCallsInMethod(method2);
                UnreachableCodeEliminator.v().transform(method2.getActiveBody());
                removeDeadCallgraphEdges(method2, callSites2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void removeDeadCallgraphEdges(SootMethod method, List<Unit> oldCallSites) {
        List<Unit> newCallSites = getCallsInMethod(method);
        if (oldCallSites != null) {
            for (Unit u : oldCallSites) {
                if (newCallSites == null || !newCallSites.contains(u)) {
                    Scene.v().getCallGraph().removeAllEdgesOutOf(u);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<Unit> getCallsInMethod(SootMethod method) {
        List<Unit> callSites = null;
        Iterator<Unit> it = method.getActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (((Stmt) u).containsInvokeExpr()) {
                if (callSites == null) {
                    callSites = new ArrayList<>();
                }
                callSites.add(u);
            }
        }
        return callSites;
    }
}
