package soot.jimple.infoflow.solver.memory;

import soot.Unit;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.FlowDroidMemoryManager;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/memory/IMemoryManagerFactory.class */
public interface IMemoryManagerFactory {
    IMemoryManager<Abstraction, Unit> getMemoryManager(boolean z, FlowDroidMemoryManager.PathDataErasureMode pathDataErasureMode);
}
