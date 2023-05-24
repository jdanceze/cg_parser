package soot.toolkits.exceptions;

import soot.dexpler.DalvikThrowAnalysis;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/ThrowAnalysisFactory.class */
public class ThrowAnalysisFactory {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ThrowAnalysisFactory.class.desiredAssertionStatus();
    }

    public static ThrowAnalysis checkInitThrowAnalysis() {
        Options opts = Options.v();
        switch (opts.check_init_throw_analysis()) {
            case 1:
                if (!opts.android_jars().isEmpty() || !opts.force_android_jar().isEmpty()) {
                    return DalvikThrowAnalysis.v();
                }
                return PedanticThrowAnalysis.v();
            case 2:
                return PedanticThrowAnalysis.v();
            case 3:
                return UnitThrowAnalysis.v();
            case 4:
                return DalvikThrowAnalysis.v();
            default:
                if ($assertionsDisabled) {
                    return PedanticThrowAnalysis.v();
                }
                throw new AssertionError();
        }
    }

    private ThrowAnalysisFactory() {
    }
}
