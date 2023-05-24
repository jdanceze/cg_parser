package soot.toolkits.exceptions;

import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.baf.ThrowInst;
import soot.jimple.ThrowStmt;
import soot.toolkits.exceptions.ThrowableSet;
/* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/PedanticThrowAnalysis.class */
public class PedanticThrowAnalysis extends AbstractThrowAnalysis {
    public PedanticThrowAnalysis(Singletons.Global g) {
    }

    public static PedanticThrowAnalysis v() {
        return G.v().soot_toolkits_exceptions_PedanticThrowAnalysis();
    }

    @Override // soot.toolkits.exceptions.AbstractThrowAnalysis, soot.toolkits.exceptions.ThrowAnalysis
    public ThrowableSet mightThrow(Unit u) {
        return ThrowableSet.Manager.v().ALL_THROWABLES;
    }

    @Override // soot.toolkits.exceptions.AbstractThrowAnalysis, soot.toolkits.exceptions.ThrowAnalysis
    public ThrowableSet mightThrowImplicitly(ThrowInst t) {
        return ThrowableSet.Manager.v().ALL_THROWABLES;
    }

    @Override // soot.toolkits.exceptions.AbstractThrowAnalysis, soot.toolkits.exceptions.ThrowAnalysis
    public ThrowableSet mightThrowImplicitly(ThrowStmt t) {
        return ThrowableSet.Manager.v().ALL_THROWABLES;
    }
}
