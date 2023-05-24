package soot.jimple.infoflow.aliasing;

import java.util.Set;
import soot.SootMethod;
import soot.Value;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/aliasing/AbstractInteractiveAliasStrategy.class */
public abstract class AbstractInteractiveAliasStrategy extends AbstractAliasStrategy {
    public AbstractInteractiveAliasStrategy(InfoflowManager manager) {
        super(manager);
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public void computeAliasTaints(Abstraction d1, Stmt src, Value targetValue, Set<Abstraction> taintSet, SootMethod method, Abstraction newAbs) {
    }
}
