package soot.jimple.infoflow.solver;

import soot.Unit;
import soot.jimple.infoflow.data.Abstraction;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/IFollowReturnsPastSeedsHandler.class */
public interface IFollowReturnsPastSeedsHandler {
    void handleFollowReturnsPastSeeds(Abstraction abstraction, Unit unit, Abstraction abstraction2);
}
