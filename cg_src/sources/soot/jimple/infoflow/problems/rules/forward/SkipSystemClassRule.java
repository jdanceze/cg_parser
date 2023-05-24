package soot.jimple.infoflow.problems.rules.forward;

import java.util.Collection;
import java.util.Collections;
import soot.JavaMethods;
import soot.Scene;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/forward/SkipSystemClassRule.class */
public class SkipSystemClassRule extends AbstractTaintPropagationRule {
    private final SootMethod objectCons;
    private final SootMethod objectClinit;
    private final SootMethod objectGetClass;
    private final SootMethod threadCons;

    public SkipSystemClassRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
        this.objectCons = Scene.v().getObjectType().getSootClass().getMethodUnsafe(JavaMethods.SIG_INIT);
        this.objectClinit = Scene.v().getObjectType().getSootClass().getMethodUnsafe(JavaMethods.SIG_CLINIT);
        this.objectGetClass = Scene.v().getObjectType().getSootClass().getMethodUnsafe("java.lang.Class getClass()");
        this.threadCons = Scene.v().grabMethod("<java.lang.Thread: void <init>()>");
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        if (isSystemClassDest(dest)) {
            killAll.value = true;
            return null;
        }
        return null;
    }

    private boolean isSystemClassDest(SootMethod dest) {
        return dest == this.objectCons || dest == this.objectClinit || dest == this.objectGetClass || dest == this.threadCons;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        Collection<SootMethod> callees = getManager().getICFG().getCalleesOfCallAt(stmt);
        if (callees.isEmpty()) {
            return null;
        }
        for (SootMethod callee : getManager().getICFG().getCalleesOfCallAt(stmt)) {
            if (!isSystemClassDest(callee)) {
                return null;
            }
        }
        return Collections.singleton(source);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        return null;
    }
}
