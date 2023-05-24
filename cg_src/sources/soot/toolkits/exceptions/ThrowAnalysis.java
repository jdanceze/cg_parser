package soot.toolkits.exceptions;

import soot.Unit;
import soot.baf.ThrowInst;
import soot.jimple.ThrowStmt;
/* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/ThrowAnalysis.class */
public interface ThrowAnalysis {
    ThrowableSet mightThrow(Unit unit);

    ThrowableSet mightThrowExplicitly(ThrowInst throwInst);

    ThrowableSet mightThrowExplicitly(ThrowStmt throwStmt);

    ThrowableSet mightThrowImplicitly(ThrowInst throwInst);

    ThrowableSet mightThrowImplicitly(ThrowStmt throwStmt);
}
