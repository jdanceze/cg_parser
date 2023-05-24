package soot.jimple.infoflow.data.pathBuilders;

import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/IPathBuilderFactory.class */
public interface IPathBuilderFactory {
    IAbstractionPathBuilder createPathBuilder(InfoflowManager infoflowManager, int i);

    IAbstractionPathBuilder createPathBuilder(InfoflowManager infoflowManager, InterruptableExecutor interruptableExecutor);

    boolean supportsPathReconstruction();

    boolean isContextSensitive();
}
