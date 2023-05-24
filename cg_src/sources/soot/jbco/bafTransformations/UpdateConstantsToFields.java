package soot.jbco.bafTransformations;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.SootField;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.PushInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.jimpleTransformations.CollectConstants;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/UpdateConstantsToFields.class */
public class UpdateConstantsToFields extends BodyTransformer implements IJbcoTransform {
    public static String[] dependancies = {CollectConstants.name, "bb.jbco_ecvf", "bb.jbco_ful", "bb.lp"};
    public static String name = "bb.jbco_ecvf";
    static int updated = 0;

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
        out.println("Updated constant references: " + updated);
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        int weight;
        SootField f;
        if (b.getMethod().getName().indexOf("<clinit>") >= 0 || (weight = Main.getWeight(phaseName, b.getMethod().getSignature())) == 0) {
            return;
        }
        PatchingChain<Unit> units = b.getUnits();
        Iterator<Unit> iter = units.snapshotIterator();
        while (iter.hasNext()) {
            Unit u = iter.next();
            if ((u instanceof PushInst) && (f = CollectConstants.constantsToFields.get(((PushInst) u).getConstant())) != null && Rand.getInt(10) <= weight) {
                Unit get = Baf.v().newStaticGetInst(f.makeRef());
                units.insertBefore(get, u);
                BodyBuilder.updateTraps(get, u, b.getTraps());
                units.remove(u);
                updated++;
            }
        }
    }
}
