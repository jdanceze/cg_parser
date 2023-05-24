package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ABinopBoolExpr.class */
public final class ABinopBoolExpr extends PBoolExpr {
    private PBinopExpr _binopExpr_;

    public ABinopBoolExpr() {
    }

    public ABinopBoolExpr(PBinopExpr _binopExpr_) {
        setBinopExpr(_binopExpr_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ABinopBoolExpr((PBinopExpr) cloneNode(this._binopExpr_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseABinopBoolExpr(this);
    }

    public PBinopExpr getBinopExpr() {
        return this._binopExpr_;
    }

    public void setBinopExpr(PBinopExpr node) {
        if (this._binopExpr_ != null) {
            this._binopExpr_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._binopExpr_ = node;
    }

    public String toString() {
        return toString(this._binopExpr_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._binopExpr_ == child) {
            this._binopExpr_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._binopExpr_ == oldChild) {
            setBinopExpr((PBinopExpr) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
