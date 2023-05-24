package soot.jimple.toolkits.scalar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/DeadAssignmentEliminator.class */
public class DeadAssignmentEliminator extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(DeadAssignmentEliminator.class);

    public DeadAssignmentEliminator(Singletons.Global g) {
    }

    public static DeadAssignmentEliminator v() {
        return G.v().soot_jimple_toolkits_scalar_DeadAssignmentEliminator();
    }

    /* JADX WARN: Removed duplicated region for block: B:122:0x02f8  */
    @Override // soot.BodyTransformer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void internalTransform(soot.Body r6, java.lang.String r7, java.util.Map<java.lang.String, java.lang.String> r8) {
        /*
            Method dump skipped, instructions count: 1242
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.toolkits.scalar.DeadAssignmentEliminator.internalTransform(soot.Body, java.lang.String, java.util.Map):void");
    }
}
