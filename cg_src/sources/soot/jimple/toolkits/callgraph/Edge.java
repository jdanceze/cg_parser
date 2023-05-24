package soot.jimple.toolkits.callgraph;

import soot.Context;
import soot.Kind;
import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.util.Invalidable;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/Edge.class */
public final class Edge implements Invalidable {
    private MethodOrMethodContext src;
    private MethodOrMethodContext tgt;
    private Unit srcUnit;
    private final Kind kind;
    private boolean invalid = false;
    private Edge nextByUnit = this;
    private Edge prevByUnit = this;
    private Edge nextBySrc = this;
    private Edge prevBySrc = this;
    private Edge nextByTgt = this;
    private Edge prevByTgt = this;

    public Edge(MethodOrMethodContext src, Unit srcUnit, MethodOrMethodContext tgt, Kind kind) {
        this.src = src;
        this.srcUnit = srcUnit;
        this.tgt = tgt;
        this.kind = kind;
    }

    public Edge(MethodOrMethodContext src, Stmt srcUnit, MethodOrMethodContext tgt) {
        this.kind = ieToKind(srcUnit.getInvokeExpr());
        this.src = src;
        this.srcUnit = srcUnit;
        this.tgt = tgt;
    }

    public SootMethod src() {
        if (this.src == null) {
            return null;
        }
        return this.src.method();
    }

    public Context srcCtxt() {
        if (this.src == null) {
            return null;
        }
        return this.src.context();
    }

    public MethodOrMethodContext getSrc() {
        return this.src;
    }

    public Unit srcUnit() {
        return this.srcUnit;
    }

    public Stmt srcStmt() {
        return (Stmt) this.srcUnit;
    }

    public SootMethod tgt() {
        if (this.tgt == null) {
            return null;
        }
        return this.tgt.method();
    }

    public Context tgtCtxt() {
        if (this.tgt == null) {
            return null;
        }
        return this.tgt.context();
    }

    public MethodOrMethodContext getTgt() {
        return this.tgt;
    }

    public Kind kind() {
        return this.kind;
    }

    public static Kind ieToKind(InvokeExpr ie) {
        if (ie instanceof VirtualInvokeExpr) {
            return Kind.VIRTUAL;
        }
        if (ie instanceof SpecialInvokeExpr) {
            return Kind.SPECIAL;
        }
        if (ie instanceof InterfaceInvokeExpr) {
            return Kind.INTERFACE;
        }
        if (ie instanceof StaticInvokeExpr) {
            return Kind.STATIC;
        }
        throw new RuntimeException();
    }

    public boolean isExplicit() {
        return Kind.isExplicit(this.kind);
    }

    public boolean isInstance() {
        return Kind.isInstance(this.kind);
    }

    public boolean isVirtual() {
        return Kind.isVirtual(this.kind);
    }

    public boolean isSpecial() {
        return Kind.isSpecial(this.kind);
    }

    public boolean isClinit() {
        return Kind.isClinit(this.kind);
    }

    public boolean isStatic() {
        return Kind.isStatic(this.kind);
    }

    public boolean isThreadRunCall() {
        return Kind.isThread(this.kind);
    }

    public boolean passesParameters() {
        return Kind.passesParameters(this.kind);
    }

    @Override // soot.util.Invalidable
    public boolean isInvalid() {
        return this.invalid;
    }

    @Override // soot.util.Invalidable
    public void invalidate() {
        this.src = null;
        this.srcUnit = null;
        this.tgt = null;
        this.invalid = true;
    }

    public int hashCode() {
        if (this.invalid) {
            return 0;
        }
        int ret = this.tgt.hashCode() + 20 + (this.kind == null ? 0 : this.kind.getNumber());
        if (this.src != null) {
            ret = (ret * 32) + this.src.hashCode();
        }
        if (this.srcUnit != null) {
            ret = (ret * 32) + this.srcUnit.hashCode();
        }
        return ret;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Edge)) {
            return false;
        }
        Edge o = (Edge) other;
        return o.src == this.src && o.srcUnit == this.srcUnit && o.tgt == this.tgt && o.kind == this.kind;
    }

    public String toString() {
        return String.valueOf(String.valueOf(this.kind)) + " edge: " + this.srcUnit + " in " + this.src + " ==> " + this.tgt;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void insertAfterByUnit(Edge other) {
        this.nextByUnit = other.nextByUnit;
        this.nextByUnit.prevByUnit = this;
        other.nextByUnit = this;
        this.prevByUnit = other;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void insertAfterBySrc(Edge other) {
        this.nextBySrc = other.nextBySrc;
        this.nextBySrc.prevBySrc = this;
        other.nextBySrc = this;
        this.prevBySrc = other;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void insertAfterByTgt(Edge other) {
        this.nextByTgt = other.nextByTgt;
        this.nextByTgt.prevByTgt = this;
        other.nextByTgt = this;
        this.prevByTgt = other;
    }

    void insertBeforeByUnit(Edge other) {
        this.prevByUnit = other.prevByUnit;
        this.prevByUnit.nextByUnit = this;
        other.prevByUnit = this;
        this.nextByUnit = other;
    }

    void insertBeforeBySrc(Edge other) {
        this.prevBySrc = other.prevBySrc;
        this.prevBySrc.nextBySrc = this;
        other.prevBySrc = this;
        this.nextBySrc = other;
    }

    void insertBeforeByTgt(Edge other) {
        this.prevByTgt = other.prevByTgt;
        this.prevByTgt.nextByTgt = this;
        other.prevByTgt = this;
        this.nextByTgt = other;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remove() {
        this.invalid = true;
        this.nextByUnit.prevByUnit = this.prevByUnit;
        this.prevByUnit.nextByUnit = this.nextByUnit;
        this.nextBySrc.prevBySrc = this.prevBySrc;
        this.prevBySrc.nextBySrc = this.nextBySrc;
        this.nextByTgt.prevByTgt = this.prevByTgt;
        this.prevByTgt.nextByTgt = this.nextByTgt;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Edge nextByUnit() {
        return this.nextByUnit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Edge nextBySrc() {
        return this.nextBySrc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Edge nextByTgt() {
        return this.nextByTgt;
    }

    Edge prevByUnit() {
        return this.prevByUnit;
    }

    Edge prevBySrc() {
        return this.prevBySrc;
    }

    Edge prevByTgt() {
        return this.prevByTgt;
    }
}
