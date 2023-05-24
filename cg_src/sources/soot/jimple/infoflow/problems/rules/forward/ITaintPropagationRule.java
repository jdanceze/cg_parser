package soot.jimple.infoflow.problems.rules.forward;

import java.util.Collection;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/forward/ITaintPropagationRule.class */
public interface ITaintPropagationRule {
    Collection<Abstraction> propagateNormalFlow(Abstraction abstraction, Abstraction abstraction2, Stmt stmt, Stmt stmt2, ByReferenceBoolean byReferenceBoolean, ByReferenceBoolean byReferenceBoolean2);

    Collection<Abstraction> propagateCallFlow(Abstraction abstraction, Abstraction abstraction2, Stmt stmt, SootMethod sootMethod, ByReferenceBoolean byReferenceBoolean);

    Collection<Abstraction> propagateCallToReturnFlow(Abstraction abstraction, Abstraction abstraction2, Stmt stmt, ByReferenceBoolean byReferenceBoolean, ByReferenceBoolean byReferenceBoolean2);

    Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> collection, Abstraction abstraction, Abstraction abstraction2, Stmt stmt, Stmt stmt2, Stmt stmt3, ByReferenceBoolean byReferenceBoolean);
}
