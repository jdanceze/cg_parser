package soot.jimple.infoflow.data.pathBuilders;

import java.util.Set;
import soot.jimple.infoflow.data.AbstractionAtSink;
import soot.jimple.infoflow.memory.IMemoryBoundedSolver;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.results.ResultSinkInfo;
import soot.jimple.infoflow.results.ResultSourceInfo;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/IAbstractionPathBuilder.class */
public interface IAbstractionPathBuilder extends IMemoryBoundedSolver {

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/IAbstractionPathBuilder$OnPathBuilderResultAvailable.class */
    public interface OnPathBuilderResultAvailable {
        void onResultAvailable(ResultSourceInfo resultSourceInfo, ResultSinkInfo resultSinkInfo);
    }

    void computeTaintPaths(Set<AbstractionAtSink> set);

    InfoflowResults getResults();

    void addResultAvailableHandler(OnPathBuilderResultAvailable onPathBuilderResultAvailable);

    void runIncrementalPathCompuation();
}
