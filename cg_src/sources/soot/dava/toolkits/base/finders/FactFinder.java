package soot.dava.toolkits.base.finders;

import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.asg.AugmentedStmtGraph;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/finders/FactFinder.class */
public interface FactFinder {
    void find(DavaBody davaBody, AugmentedStmtGraph augmentedStmtGraph, SETNode sETNode) throws RetriggerAnalysisException;
}
