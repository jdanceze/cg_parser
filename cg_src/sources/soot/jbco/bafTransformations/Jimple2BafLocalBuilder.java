package soot.jbco.bafTransformations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/Jimple2BafLocalBuilder.class */
public class Jimple2BafLocalBuilder extends BodyTransformer implements IJbcoTransform {
    public static String[] dependancies = {"jtp.jbco_jl", "bb.jbco_j2bl", "bb.lp"};
    public static String name = "bb.jbco_j2bl";
    private static boolean runOnce = false;

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
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        if (Main.methods2JLocals.size() == 0) {
            if (!runOnce) {
                runOnce = true;
                out.println("[Jimple2BafLocalBuilder]:: Jimple Local Lists have not been built");
                out.println("                           Skipping Jimple To Baf Builder\n");
                return;
            }
            return;
        }
        Collection<Local> bLocals = b.getLocals();
        HashMap<Local, Local> bafToJLocals = new HashMap<>();
        for (Local jl : Main.methods2JLocals.get(b.getMethod())) {
            Iterator<Local> blocIt = bLocals.iterator();
            while (true) {
                if (!blocIt.hasNext()) {
                    break;
                }
                Local bl = blocIt.next();
                if (bl.getName().equals(jl.getName())) {
                    bafToJLocals.put(bl, jl);
                    break;
                }
            }
        }
        Main.methods2Baf2JLocals.put(b.getMethod(), bafToJLocals);
    }
}
