package soot.jimple.infoflow.threading;

import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/threading/IExecutorFactory.class */
public interface IExecutorFactory {
    InterruptableExecutor createExecutor(int i, boolean z, InfoflowConfiguration infoflowConfiguration);
}
