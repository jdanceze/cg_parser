package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ACmpltBinop.class */
public final class ACmpltBinop extends PBinop {
    private TCmplt _cmplt_;

    public ACmpltBinop() {
    }

    public ACmpltBinop(TCmplt _cmplt_) {
        setCmplt(_cmplt_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ACmpltBinop((TCmplt) cloneNode(this._cmplt_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseACmpltBinop(this);
    }

    public TCmplt getCmplt() {
        return this._cmplt_;
    }

    public void setCmplt(TCmplt node) {
        if (this._cmplt_ != null) {
            this._cmplt_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._cmplt_ = node;
    }

    public String toString() {
        return toString(this._cmplt_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._cmplt_ == child) {
            this._cmplt_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._cmplt_ == oldChild) {
            setCmplt((TCmplt) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
