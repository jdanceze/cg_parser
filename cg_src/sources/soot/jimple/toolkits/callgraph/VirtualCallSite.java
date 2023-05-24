package soot.jimple.toolkits.callgraph;

import soot.Kind;
import soot.MethodSubSignature;
import soot.SootMethod;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/VirtualCallSite.class */
public class VirtualCallSite extends AbstractCallSite {
    private final InstanceInvokeExpr iie;
    private final MethodSubSignature subSig;
    final Kind kind;

    public VirtualCallSite(Stmt stmt, SootMethod container, InstanceInvokeExpr iie, MethodSubSignature subSig, Kind kind) {
        super(stmt, container);
        this.iie = iie;
        this.subSig = subSig;
        this.kind = kind;
    }

    @Deprecated
    public Stmt stmt() {
        return this.stmt;
    }

    @Deprecated
    public SootMethod container() {
        return this.container;
    }

    public InstanceInvokeExpr iie() {
        return this.iie;
    }

    public MethodSubSignature subSig() {
        return this.subSig;
    }

    public Kind kind() {
        return this.kind;
    }
}
