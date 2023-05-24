package soot.jimple.infoflow.solver.memory;

import soot.Unit;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.FlowDroidMemoryManager;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/memory/DefaultMemoryManagerFactory.class */
public class DefaultMemoryManagerFactory implements IMemoryManagerFactory {
    @Override // soot.jimple.infoflow.solver.memory.IMemoryManagerFactory
    public IMemoryManager<Abstraction, Unit> getMemoryManager(boolean tracingEnabled, FlowDroidMemoryManager.PathDataErasureMode erasePathData) {
        return new FlowDroidMemoryManager(tracingEnabled, erasePathData);
    }
}
