package soot.shimple;

import soot.Body;
import soot.MethodSource;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/shimple/ShimpleMethodSource.class */
public class ShimpleMethodSource implements MethodSource {
    MethodSource ms;

    public ShimpleMethodSource(MethodSource ms) {
        this.ms = ms;
    }

    @Override // soot.MethodSource
    public Body getBody(SootMethod m, String phaseName) {
        Body b = this.ms.getBody(m, phaseName);
        if (b == null) {
            return null;
        }
        return Shimple.v().newBody(b);
    }
}
