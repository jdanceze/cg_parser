package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ANewExpression.class */
public final class ANewExpression extends PExpression {
    private PNewExpr _newExpr_;

    public ANewExpression() {
    }

    public ANewExpression(PNewExpr _newExpr_) {
        setNewExpr(_newExpr_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ANewExpression((PNewExpr) cloneNode(this._newExpr_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseANewExpression(this);
    }

    public PNewExpr getNewExpr() {
        return this._newExpr_;
    }

    public void setNewExpr(PNewExpr node) {
        if (this._newExpr_ != null) {
            this._newExpr_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._newExpr_ = node;
    }

    public String toString() {
        return toString(this._newExpr_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._newExpr_ == child) {
            this._newExpr_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._newExpr_ == oldChild) {
            setNewExpr((PNewExpr) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
