package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ACmpgeBinop.class */
public final class ACmpgeBinop extends PBinop {
    private TCmpge _cmpge_;

    public ACmpgeBinop() {
    }

    public ACmpgeBinop(TCmpge _cmpge_) {
        setCmpge(_cmpge_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ACmpgeBinop((TCmpge) cloneNode(this._cmpge_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseACmpgeBinop(this);
    }

    public TCmpge getCmpge() {
        return this._cmpge_;
    }

    public void setCmpge(TCmpge node) {
        if (this._cmpge_ != null) {
            this._cmpge_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._cmpge_ = node;
    }

    public String toString() {
        return toString(this._cmpge_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._cmpge_ == child) {
            this._cmpge_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._cmpge_ == oldChild) {
            setCmpge((TCmpge) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
