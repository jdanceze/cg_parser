package soot.jimple.infoflow.taintWrappers;

import java.util.Set;
import soot.jimple.Stmt;
import soot.jimple.infoflow.data.Abstraction;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/taintWrappers/IReversibleTaintWrapper.class */
public interface IReversibleTaintWrapper extends ITaintPropagationWrapper {
    Set<Abstraction> getInverseTaintsForMethod(Stmt stmt, Abstraction abstraction, Abstraction abstraction2);
}
