package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ACmpgBinop.class */
public final class ACmpgBinop extends PBinop {
    private TCmpg _cmpg_;

    public ACmpgBinop() {
    }

    public ACmpgBinop(TCmpg _cmpg_) {
        setCmpg(_cmpg_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ACmpgBinop((TCmpg) cloneNode(this._cmpg_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseACmpgBinop(this);
    }

    public TCmpg getCmpg() {
        return this._cmpg_;
    }

    public void setCmpg(TCmpg node) {
        if (this._cmpg_ != null) {
            this._cmpg_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._cmpg_ = node;
    }

    public String toString() {
        return toString(this._cmpg_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._cmpg_ == child) {
            this._cmpg_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._cmpg_ == oldChild) {
            setCmpg((TCmpg) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
