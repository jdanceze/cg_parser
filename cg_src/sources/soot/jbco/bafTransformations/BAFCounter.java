package soot.jbco.bafTransformations;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.baf.GotoInst;
import soot.baf.JSRInst;
import soot.baf.TargetArgInst;
import soot.jbco.IJbcoTransform;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/BAFCounter.class */
public class BAFCounter extends BodyTransformer implements IJbcoTransform {
    static int count = 0;
    public static String[] dependancies = {"bb.jbco_counter"};
    public static String name = "bb.jbco_counter";

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
        out.println("Count: " + count);
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if ((u instanceof TargetArgInst) && !(u instanceof GotoInst) && !(u instanceof JSRInst)) {
                count++;
            }
        }
    }
}
