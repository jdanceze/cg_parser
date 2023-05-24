package soot.jimple.toolkits.annotation.nullcheck;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.RefLikeType;
import soot.Singletons;
import soot.SootClass;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.toolkits.annotation.tags.NullCheckTag;
import soot.tagkit.ColorTag;
import soot.tagkit.KeyTag;
import soot.tagkit.StringTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/nullcheck/NullPointerColorer.class */
public class NullPointerColorer extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(NullPointerColorer.class);

    public NullPointerColorer(Singletons.Global g) {
    }

    public static NullPointerColorer v() {
        return G.v().soot_jimple_toolkits_annotation_nullcheck_NullPointerColorer();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        BranchedRefVarsAnalysis analysis = new BranchedRefVarsAnalysis(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b));
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit s = it.next();
            FlowSet<RefIntPair> beforeSet = analysis.getFlowBefore(s);
            for (ValueBox vBox : s.getUseBoxes()) {
                addColorTags(vBox, beforeSet, s, analysis);
            }
            FlowSet<RefIntPair> afterSet = analysis.getFallFlowAfter(s);
            for (ValueBox vBox2 : s.getDefBoxes()) {
                addColorTags(vBox2, afterSet, s, analysis);
            }
        }
        boolean keysAdded = false;
        SootClass declaringClass = b.getMethod().getDeclaringClass();
        for (Tag next : declaringClass.getTags()) {
            if ((next instanceof KeyTag) && NullCheckTag.NAME.equals(((KeyTag) next).analysisType())) {
                keysAdded = true;
            }
        }
        if (!keysAdded) {
            declaringClass.addTag(new KeyTag(0, "Nullness: Null", NullCheckTag.NAME));
            declaringClass.addTag(new KeyTag(1, "Nullness: Not Null", NullCheckTag.NAME));
            declaringClass.addTag(new KeyTag(3, "Nullness: Nullness Unknown", NullCheckTag.NAME));
        }
    }

    private void addColorTags(ValueBox vBox, FlowSet<RefIntPair> set, Unit u, BranchedRefVarsAnalysis analysis) {
        Value val = vBox.getValue();
        if (val.getType() instanceof RefLikeType) {
            switch (analysis.anyRefInfo(val, set)) {
                case 0:
                    u.addTag(new StringTag(val + ": Nullness Unknown", NullCheckTag.NAME));
                    vBox.addTag(new ColorTag(3, NullCheckTag.NAME));
                    return;
                case 1:
                    u.addTag(new StringTag(val + ": Null", NullCheckTag.NAME));
                    vBox.addTag(new ColorTag(0, NullCheckTag.NAME));
                    return;
                case 2:
                    u.addTag(new StringTag(val + ": NonNull", NullCheckTag.NAME));
                    vBox.addTag(new ColorTag(1, NullCheckTag.NAME));
                    return;
                case 99:
                    u.addTag(new StringTag(val + ": Nullness Unknown", NullCheckTag.NAME));
                    vBox.addTag(new ColorTag(3, NullCheckTag.NAME));
                    return;
                default:
                    return;
            }
        }
    }
}
