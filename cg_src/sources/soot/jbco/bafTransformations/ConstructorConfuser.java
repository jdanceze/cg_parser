package soot.jbco.bafTransformations;

import soot.BodyTransformer;
import soot.jbco.IJbcoTransform;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/ConstructorConfuser.class */
public class ConstructorConfuser extends BodyTransformer implements IJbcoTransform {
    static int count = 0;
    static int[] instances = new int[4];
    public static String[] dependancies = {"bb.jbco_dcc", "bb.jbco_ful", "bb.lp"};
    public static String name = "bb.jbco_dcc";

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
        out.println("Constructor methods have been jumbled: " + count);
    }

    /* JADX WARN: Code restructure failed: missing block: B:79:0x0356, code lost:
        if (r21 != false) goto L88;
     */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.BodyTransformer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void internalTransform(soot.Body r8, java.lang.String r9, java.util.Map<java.lang.String, java.lang.String> r10) {
        /*
            Method dump skipped, instructions count: 933
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jbco.bafTransformations.ConstructorConfuser.internalTransform(soot.Body, java.lang.String, java.util.Map):void");
    }
}
