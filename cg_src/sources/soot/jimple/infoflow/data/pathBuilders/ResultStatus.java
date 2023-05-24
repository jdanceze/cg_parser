package soot.jimple.infoflow.data.pathBuilders;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/ResultStatus.class */
public enum ResultStatus {
    NEW,
    CACHED,
    INFEASIBLE_OR_MAX_PATHS_REACHED;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static ResultStatus[] valuesCustom() {
        ResultStatus[] valuesCustom = values();
        int length = valuesCustom.length;
        ResultStatus[] resultStatusArr = new ResultStatus[length];
        System.arraycopy(valuesCustom, 0, resultStatusArr, 0, length);
        return resultStatusArr;
    }
}
