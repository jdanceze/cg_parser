package soot.jbco.bafTransformations;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Unit;
import soot.baf.IdentityInst;
import soot.baf.Inst;
import soot.jbco.IJbcoTransform;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/BafLineNumberer.class */
public class BafLineNumberer extends BodyTransformer implements IJbcoTransform {
    public static String name = "bb.jbco_bln";

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
    }

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return new String[]{name};
    }

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        int idx = 0;
        PatchingChain<Unit> units = b.getUnits();
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Inst i = (Inst) it.next();
            List<Tag> tags = i.getTags();
            int k = 0;
            while (true) {
                if (k >= tags.size()) {
                    break;
                }
                Tag t = tags.get(k);
                if (!(t instanceof LineNumberTag)) {
                    k++;
                } else {
                    tags.remove(k);
                    break;
                }
            }
            if (!(i instanceof IdentityInst)) {
                int i2 = idx;
                idx++;
                i.addTag(new LineNumberTag(i2));
            }
        }
    }
}
