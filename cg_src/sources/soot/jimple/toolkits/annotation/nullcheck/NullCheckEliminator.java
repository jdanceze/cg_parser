package soot.jimple.toolkits.annotation.nullcheck;

import soot.BodyTransformer;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/nullcheck/NullCheckEliminator.class */
public class NullCheckEliminator extends BodyTransformer {
    private AnalysisFactory analysisFactory;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NullCheckEliminator.class.desiredAssertionStatus();
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/nullcheck/NullCheckEliminator$AnalysisFactory.class */
    public static class AnalysisFactory {
        public NullnessAnalysis newAnalysis(UnitGraph g) {
            return new NullnessAnalysis(g);
        }
    }

    public NullCheckEliminator() {
        this(new AnalysisFactory());
    }

    public NullCheckEliminator(AnalysisFactory f) {
        this.analysisFactory = f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:58:0x0131, code lost:
        continue;
     */
    @Override // soot.BodyTransformer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void internalTransform(soot.Body r5, java.lang.String r6, java.util.Map<java.lang.String, java.lang.String> r7) {
        /*
            Method dump skipped, instructions count: 330
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.toolkits.annotation.nullcheck.NullCheckEliminator.internalTransform(soot.Body, java.lang.String, java.util.Map):void");
    }
}
