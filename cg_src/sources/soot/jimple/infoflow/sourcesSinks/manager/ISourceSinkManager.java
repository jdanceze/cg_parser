package soot.jimple.infoflow.sourcesSinks.manager;

import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.AccessPath;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/manager/ISourceSinkManager.class */
public interface ISourceSinkManager {
    void initialize();

    SourceInfo getSourceInfo(Stmt stmt, InfoflowManager infoflowManager);

    SinkInfo getSinkInfo(Stmt stmt, InfoflowManager infoflowManager, AccessPath accessPath);
}
