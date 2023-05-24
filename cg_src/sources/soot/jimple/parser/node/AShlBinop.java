package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AShlBinop.class */
public final class AShlBinop extends PBinop {
    private TShl _shl_;

    public AShlBinop() {
    }

    public AShlBinop(TShl _shl_) {
        setShl(_shl_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AShlBinop((TShl) cloneNode(this._shl_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAShlBinop(this);
    }

    public TShl getShl() {
        return this._shl_;
    }

    public void setShl(TShl node) {
        if (this._shl_ != null) {
            this._shl_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._shl_ = node;
    }

    public String toString() {
        return toString(this._shl_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._shl_ == child) {
            this._shl_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._shl_ == oldChild) {
            setShl((TShl) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
