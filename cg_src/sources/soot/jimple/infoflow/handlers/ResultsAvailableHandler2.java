package soot.jimple.infoflow.handlers;

import soot.jimple.infoflow.results.ResultSinkInfo;
import soot.jimple.infoflow.results.ResultSourceInfo;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/handlers/ResultsAvailableHandler2.class */
public interface ResultsAvailableHandler2 extends ResultsAvailableHandler {
    boolean onSingleResultAvailable(ResultSourceInfo resultSourceInfo, ResultSinkInfo resultSinkInfo);
}
