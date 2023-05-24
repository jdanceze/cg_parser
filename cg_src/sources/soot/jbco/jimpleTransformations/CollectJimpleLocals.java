package soot.jbco.jimpleTransformations;

import java.util.ArrayList;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
/* loaded from: gencallgraphv3.jar:soot/jbco/jimpleTransformations/CollectJimpleLocals.class */
public class CollectJimpleLocals extends BodyTransformer implements IJbcoTransform {
    public static String[] dependancies = {"jtp.jbco_jl"};
    public static String name = "jtp.jbco_jl";

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
    }

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return dependancies;
    }

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        Main.methods2JLocals.put(body.getMethod(), new ArrayList(body.getLocals()));
    }
}
