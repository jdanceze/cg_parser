package soot.jimple.infoflow.solver;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/PredecessorShorteningMode.class */
public enum PredecessorShorteningMode {
    NeverShorten,
    ShortenIfEqual,
    AlwaysShorten;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static PredecessorShorteningMode[] valuesCustom() {
        PredecessorShorteningMode[] valuesCustom = values();
        int length = valuesCustom.length;
        PredecessorShorteningMode[] predecessorShorteningModeArr = new PredecessorShorteningMode[length];
        System.arraycopy(valuesCustom, 0, predecessorShorteningModeArr, 0, length);
        return predecessorShorteningModeArr;
    }
}
