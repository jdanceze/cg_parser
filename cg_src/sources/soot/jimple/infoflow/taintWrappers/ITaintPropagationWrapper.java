package soot.jimple.infoflow.taintWrappers;

import java.util.Set;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/taintWrappers/ITaintPropagationWrapper.class */
public interface ITaintPropagationWrapper {
    void initialize(InfoflowManager infoflowManager);

    Set<Abstraction> getTaintsForMethod(Stmt stmt, Abstraction abstraction, Abstraction abstraction2);

    boolean isExclusive(Stmt stmt, Abstraction abstraction);

    Set<Abstraction> getAliasesForMethod(Stmt stmt, Abstraction abstraction, Abstraction abstraction2);

    boolean supportsCallee(SootMethod sootMethod);

    boolean supportsCallee(Stmt stmt);

    int getWrapperHits();

    int getWrapperMisses();
}
