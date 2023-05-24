package soot.jimple.infoflow.results;

import heros.solver.Pair;
import java.util.Collections;
import java.util.List;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/BackwardsInfoflowResults.class */
public class BackwardsInfoflowResults extends InfoflowResults {
    public BackwardsInfoflowResults() {
    }

    public BackwardsInfoflowResults(boolean pathAgnosticResults) {
        super(pathAgnosticResults);
    }

    @Override // soot.jimple.infoflow.results.InfoflowResults
    public Pair<ResultSourceInfo, ResultSinkInfo> addResult(ISourceSinkDefinition sinkDefinition, AccessPath sink, Stmt sinkStmt, ISourceSinkDefinition sourceDefinition, AccessPath source, Stmt sourceStmt, Object userData, List<Stmt> propagationPath, List<AccessPath> propagationAccessPath, List<Stmt> propagationCallSites, InfoflowManager manager) {
        ResultSinkInfo sourceObj = new ResultSinkInfo(sourceDefinition, source, sourceStmt);
        if (propagationCallSites != null) {
            Collections.reverse(propagationCallSites);
        }
        if (propagationPath != null) {
            Collections.reverse(propagationPath);
            if (!manager.getConfig().getPathAgnosticResults() && propagationCallSites != null && manager != null) {
                for (int i = 0; i < propagationPath.size(); i++) {
                    if (manager.getICFG().isExitStmt(propagationPath.get(i))) {
                        propagationPath.set(i, propagationCallSites.get(i));
                    }
                }
            }
        }
        if (propagationAccessPath != null) {
            Collections.reverse(propagationAccessPath);
        }
        ResultSourceInfo sinkObj = new ResultSourceInfo(sinkDefinition, sink, sinkStmt, userData, propagationPath, propagationAccessPath, propagationCallSites, this.pathAgnosticResults);
        addResult(sourceObj, sinkObj);
        return new Pair<>(sinkObj, sourceObj);
    }

    @Override // soot.jimple.infoflow.results.InfoflowResults
    public void addResult(ISourceSinkDefinition sinkDefinition, AccessPath sink, Stmt sinkStmt, ISourceSinkDefinition sourceDefinition, AccessPath source, Stmt sourceStmt) {
        addResult(new ResultSinkInfo(sourceDefinition, sink, sinkStmt), new ResultSourceInfo(sinkDefinition, source, sourceStmt, this.pathAgnosticResults));
    }
}
