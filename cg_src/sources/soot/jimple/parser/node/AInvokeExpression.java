package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AInvokeExpression.class */
public final class AInvokeExpression extends PExpression {
    private PInvokeExpr _invokeExpr_;

    public AInvokeExpression() {
    }

    public AInvokeExpression(PInvokeExpr _invokeExpr_) {
        setInvokeExpr(_invokeExpr_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AInvokeExpression((PInvokeExpr) cloneNode(this._invokeExpr_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAInvokeExpression(this);
    }

    public PInvokeExpr getInvokeExpr() {
        return this._invokeExpr_;
    }

    public void setInvokeExpr(PInvokeExpr node) {
        if (this._invokeExpr_ != null) {
            this._invokeExpr_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._invokeExpr_ = node;
    }

    public String toString() {
        return toString(this._invokeExpr_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._invokeExpr_ == child) {
            this._invokeExpr_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._invokeExpr_ == oldChild) {
            setInvokeExpr((PInvokeExpr) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
