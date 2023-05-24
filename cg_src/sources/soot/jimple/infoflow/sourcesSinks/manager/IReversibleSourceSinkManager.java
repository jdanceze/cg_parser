package soot.jimple.infoflow.sourcesSinks.manager;

import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.AccessPath;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/manager/IReversibleSourceSinkManager.class */
public interface IReversibleSourceSinkManager extends ISourceSinkManager {
    SinkInfo getInverseSourceInfo(Stmt stmt, InfoflowManager infoflowManager, AccessPath accessPath);

    SourceInfo getInverseSinkInfo(Stmt stmt, InfoflowManager infoflowManager);
}
