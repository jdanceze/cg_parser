package soot.jimple.infoflow.codeOptimization;

import java.util.Collection;
import java.util.Iterator;
import soot.JavaMethods;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.entryPointCreators.SimulatedCodeElementTag;
import soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager;
import soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper;
import soot.jimple.toolkits.callgraph.ReachableMethods;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/codeOptimization/AddNopStmt.class */
public class AddNopStmt implements ICodeOptimizer {
    InfoflowConfiguration config;

    @Override // soot.jimple.infoflow.codeOptimization.ICodeOptimizer
    public void initialize(InfoflowConfiguration config) {
        this.config = config;
    }

    @Override // soot.jimple.infoflow.codeOptimization.ICodeOptimizer
    public void run(InfoflowManager manager, Collection<SootMethod> entryPoints, ISourceSinkManager sourcesSinks, ITaintPropagationWrapper taintWrapper) {
        for (SootMethod entryPoint : entryPoints) {
            if (entryPoint.hasActiveBody()) {
                Iterator<Unit> it = entryPoint.getActiveBody().getUnits().iterator();
                while (it.hasNext()) {
                    Unit unit = it.next();
                    if (unit instanceof InvokeStmt) {
                        InvokeExpr iExpr = ((InvokeStmt) unit).getInvokeExpr();
                        if ((iExpr instanceof StaticInvokeExpr) && iExpr.getArgCount() == 0) {
                            Collection<SootMethod> callees = manager.getICFG().getCalleesOfCallAt(unit);
                            for (SootMethod callee : callees) {
                                if (callee.hasActiveBody()) {
                                    UnitPatchingChain units = callee.getActiveBody().getUnits();
                                    if (units.getFirst() instanceof AssignStmt) {
                                        NopStmt nop = Jimple.v().newNopStmt();
                                        nop.addTag(SimulatedCodeElementTag.TAG);
                                        units.addFirst((UnitPatchingChain) nop);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        ReachableMethods rm = Scene.v().getReachableMethods();
        Iterator<MethodOrMethodContext> iter = rm.listener();
        while (iter.hasNext()) {
            SootMethod sm = iter.next().method();
            if (sm.hasActiveBody() && sm.getSubSignature().contains(JavaMethods.SIG_CLINIT)) {
                UnitPatchingChain units2 = sm.getActiveBody().getUnits();
                if (units2.getFirst() instanceof AssignStmt) {
                    NopStmt nop2 = Jimple.v().newNopStmt();
                    nop2.addTag(SimulatedCodeElementTag.TAG);
                    units2.addFirst((UnitPatchingChain) nop2);
                }
            }
        }
    }
}
