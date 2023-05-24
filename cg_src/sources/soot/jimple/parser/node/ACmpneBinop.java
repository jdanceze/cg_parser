package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ACmpneBinop.class */
public final class ACmpneBinop extends PBinop {
    private TCmpne _cmpne_;

    public ACmpneBinop() {
    }

    public ACmpneBinop(TCmpne _cmpne_) {
        setCmpne(_cmpne_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ACmpneBinop((TCmpne) cloneNode(this._cmpne_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseACmpneBinop(this);
    }

    public TCmpne getCmpne() {
        return this._cmpne_;
    }

    public void setCmpne(TCmpne node) {
        if (this._cmpne_ != null) {
            this._cmpne_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._cmpne_ = node;
    }

    public String toString() {
        return toString(this._cmpne_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._cmpne_ == child) {
            this._cmpne_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._cmpne_ == oldChild) {
            setCmpne((TCmpne) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
