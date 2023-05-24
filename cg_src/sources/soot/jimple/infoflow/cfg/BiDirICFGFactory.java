package soot.jimple.infoflow.cfg;

import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/cfg/BiDirICFGFactory.class */
public interface BiDirICFGFactory {
    IInfoflowCFG buildBiDirICFG(InfoflowConfiguration.CallgraphAlgorithm callgraphAlgorithm, boolean z);
}
