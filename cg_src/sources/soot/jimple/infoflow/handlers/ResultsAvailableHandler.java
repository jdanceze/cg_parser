package soot.jimple.infoflow.handlers;

import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/handlers/ResultsAvailableHandler.class */
public interface ResultsAvailableHandler {
    void onResultsAvailable(IInfoflowCFG iInfoflowCFG, InfoflowResults infoflowResults);
}
