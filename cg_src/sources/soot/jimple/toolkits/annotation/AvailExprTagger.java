package soot.jimple.toolkits.annotation;

import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.PhaseOptions;
import soot.Scene;
import soot.SideEffectTester;
import soot.Singletons;
import soot.jimple.NaiveSideEffectTester;
import soot.jimple.toolkits.pointer.PASideEffectTester;
import soot.jimple.toolkits.scalar.PessimisticAvailableExpressionsAnalysis;
import soot.jimple.toolkits.scalar.SlowAvailableExpressionsAnalysis;
import soot.options.AETOptions;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/AvailExprTagger.class */
public class AvailExprTagger extends BodyTransformer {
    public AvailExprTagger(Singletons.Global g) {
    }

    public static AvailExprTagger v() {
        return G.v().soot_jimple_toolkits_annotation_AvailExprTagger();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map opts) {
        SideEffectTester sideEffect;
        if (Scene.v().hasCallGraph() && !PhaseOptions.getBoolean(opts, "naive-side-effect")) {
            sideEffect = new PASideEffectTester();
        } else {
            sideEffect = new NaiveSideEffectTester();
        }
        sideEffect.newMethod(b.getMethod());
        AETOptions options = new AETOptions(opts);
        if (options.kind() == 1) {
            new SlowAvailableExpressionsAnalysis(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b));
        } else {
            new PessimisticAvailableExpressionsAnalysis(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b), b.getMethod(), sideEffect);
        }
    }
}
