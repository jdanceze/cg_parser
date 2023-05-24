package soot.jimple.infoflow.solver.gcSolver;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/GarbageCollectionTrigger.class */
public enum GarbageCollectionTrigger {
    Immediate,
    MethodThreshold,
    EdgeThreshold,
    Never;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static GarbageCollectionTrigger[] valuesCustom() {
        GarbageCollectionTrigger[] valuesCustom = values();
        int length = valuesCustom.length;
        GarbageCollectionTrigger[] garbageCollectionTriggerArr = new GarbageCollectionTrigger[length];
        System.arraycopy(valuesCustom, 0, garbageCollectionTriggerArr, 0, length);
        return garbageCollectionTriggerArr;
    }
}
