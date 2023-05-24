package soot.jimple.infoflow.codeOptimization;

import java.util.Collection;
import soot.SootMethod;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager;
import soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/codeOptimization/ICodeOptimizer.class */
public interface ICodeOptimizer {
    void initialize(InfoflowConfiguration infoflowConfiguration);

    void run(InfoflowManager infoflowManager, Collection<SootMethod> collection, ISourceSinkManager iSourceSinkManager, ITaintPropagationWrapper iTaintPropagationWrapper);
}
