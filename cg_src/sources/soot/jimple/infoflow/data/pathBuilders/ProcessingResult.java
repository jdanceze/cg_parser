package soot.jimple.infoflow.data.pathBuilders;

import soot.jimple.infoflow.data.SourceContextAndPath;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/ProcessingResult.class */
public class ProcessingResult {
    private static final ProcessingResult NEW_INSTANCE;
    private static final ProcessingResult INFEASIBLE_INSTANCE;
    private final ResultStatus result;
    private final SourceContextAndPath scap;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ProcessingResult.class.desiredAssertionStatus();
        NEW_INSTANCE = new ProcessingResult(ResultStatus.NEW, null);
        INFEASIBLE_INSTANCE = new ProcessingResult(ResultStatus.INFEASIBLE_OR_MAX_PATHS_REACHED, null);
    }

    private ProcessingResult(ResultStatus result, SourceContextAndPath scap) {
        this.result = result;
        this.scap = scap;
    }

    public static ProcessingResult NEW() {
        return NEW_INSTANCE;
    }

    public static ProcessingResult INFEASIBLE_OR_MAX_PATHS_REACHED() {
        return INFEASIBLE_INSTANCE;
    }

    public static ProcessingResult CACHED(SourceContextAndPath scap) {
        return new ProcessingResult(ResultStatus.CACHED, scap);
    }

    public ResultStatus getResult() {
        return this.result;
    }

    public SourceContextAndPath getScap() {
        if ($assertionsDisabled || this.result == ResultStatus.CACHED) {
            return this.scap;
        }
        throw new AssertionError();
    }
}
