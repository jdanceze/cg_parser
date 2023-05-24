package soot.jimple.infoflow.solver.memory;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/memory/IMemoryManager.class */
public interface IMemoryManager<D, N> {
    D handleMemoryObject(D d);

    D handleGeneratedMemoryObject(D d, D d2);

    boolean isEssentialJoinPoint(D d, N n);
}
