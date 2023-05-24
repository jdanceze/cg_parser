package soot.jbco.bafTransformations;

import java.util.List;
import soot.BodyTransformer;
import soot.DoubleType;
import soot.Local;
import soot.LongType;
import soot.Type;
import soot.Unit;
import soot.baf.internal.BLoadInst;
import soot.jbco.IJbcoTransform;
import soot.toolkits.graph.BriefUnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/MoveLoadsAboveIfs.class */
public class MoveLoadsAboveIfs extends BodyTransformer implements IJbcoTransform {
    int movedloads = 0;
    public static String[] dependancies = {"bb.jbco_rlaii", "bb.jbco_ful", "bb.lp"};
    public static String name = "bb.jbco_rlaii";

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return dependancies;
    }

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
        out.println("Moved Loads Above Ifs: " + this.movedloads);
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x0229  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x02bc  */
    @Override // soot.BodyTransformer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void internalTransform(soot.Body r7, java.lang.String r8, java.util.Map<java.lang.String, java.lang.String> r9) {
        /*
            Method dump skipped, instructions count: 792
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jbco.bafTransformations.MoveLoadsAboveIfs.internalTransform(soot.Body, java.lang.String, java.util.Map):void");
    }

    private boolean checkCandidate(List<Unit> succs, BriefUnitGraph bug) {
        if (succs.size() < 2) {
            return false;
        }
        Object o = succs.get(0);
        for (int i = 1; i < succs.size(); i++) {
            if (succs.get(i).getClass() != o.getClass()) {
                return false;
            }
        }
        if (o instanceof BLoadInst) {
            BLoadInst bl = (BLoadInst) o;
            Local l = bl.getLocal();
            for (int i2 = 1; i2 < succs.size(); i2++) {
                BLoadInst bld = (BLoadInst) succs.get(i2);
                if (bld.getLocal() != l || bug.getPredsOf((Unit) bld).size() > 1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private int category(Type t) {
        return ((t instanceof LongType) || (t instanceof DoubleType)) ? 2 : 1;
    }
}
