package soot;
/* loaded from: gencallgraphv3.jar:soot/UnknownMethodSource.class */
public class UnknownMethodSource implements MethodSource {
    UnknownMethodSource() {
    }

    @Override // soot.MethodSource
    public Body getBody(SootMethod m, String phaseName) {
        throw new RuntimeException("Can't get body for unknown source!");
    }
}
