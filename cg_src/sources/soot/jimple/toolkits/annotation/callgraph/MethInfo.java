package soot.jimple.toolkits.annotation.callgraph;

import soot.Kind;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/callgraph/MethInfo.class */
public class MethInfo {
    private SootMethod method;
    private boolean canExpandCollapse;
    private Kind edgeKind;

    public MethInfo(SootMethod meth, boolean b, Kind kind) {
        method(meth);
        canExpandCollapse(b);
        edgeKind(kind);
    }

    public Kind edgeKind() {
        return this.edgeKind;
    }

    public void edgeKind(Kind kind) {
        this.edgeKind = kind;
    }

    public boolean canExpandCollapse() {
        return this.canExpandCollapse;
    }

    public void canExpandCollapse(boolean b) {
        this.canExpandCollapse = b;
    }

    public SootMethod method() {
        return this.method;
    }

    public void method(SootMethod m) {
        this.method = m;
    }
}
