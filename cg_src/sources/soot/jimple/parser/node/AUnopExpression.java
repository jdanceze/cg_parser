package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AUnopExpression.class */
public final class AUnopExpression extends PExpression {
    private PUnopExpr _unopExpr_;

    public AUnopExpression() {
    }

    public AUnopExpression(PUnopExpr _unopExpr_) {
        setUnopExpr(_unopExpr_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AUnopExpression((PUnopExpr) cloneNode(this._unopExpr_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAUnopExpression(this);
    }

    public PUnopExpr getUnopExpr() {
        return this._unopExpr_;
    }

    public void setUnopExpr(PUnopExpr node) {
        if (this._unopExpr_ != null) {
            this._unopExpr_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._unopExpr_ = node;
    }

    public String toString() {
        return toString(this._unopExpr_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._unopExpr_ == child) {
            this._unopExpr_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._unopExpr_ == oldChild) {
            setUnopExpr((PUnopExpr) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
