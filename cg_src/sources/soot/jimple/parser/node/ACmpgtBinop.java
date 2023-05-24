package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ACmpgtBinop.class */
public final class ACmpgtBinop extends PBinop {
    private TCmpgt _cmpgt_;

    public ACmpgtBinop() {
    }

    public ACmpgtBinop(TCmpgt _cmpgt_) {
        setCmpgt(_cmpgt_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ACmpgtBinop((TCmpgt) cloneNode(this._cmpgt_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseACmpgtBinop(this);
    }

    public TCmpgt getCmpgt() {
        return this._cmpgt_;
    }

    public void setCmpgt(TCmpgt node) {
        if (this._cmpgt_ != null) {
            this._cmpgt_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._cmpgt_ = node;
    }

    public String toString() {
        return toString(this._cmpgt_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._cmpgt_ == child) {
            this._cmpgt_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._cmpgt_ == oldChild) {
            setCmpgt((TCmpgt) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
