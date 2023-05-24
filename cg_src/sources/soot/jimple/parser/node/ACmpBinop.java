package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ACmpBinop.class */
public final class ACmpBinop extends PBinop {
    private TCmp _cmp_;

    public ACmpBinop() {
    }

    public ACmpBinop(TCmp _cmp_) {
        setCmp(_cmp_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ACmpBinop((TCmp) cloneNode(this._cmp_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseACmpBinop(this);
    }

    public TCmp getCmp() {
        return this._cmp_;
    }

    public void setCmp(TCmp node) {
        if (this._cmp_ != null) {
            this._cmp_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._cmp_ = node;
    }

    public String toString() {
        return toString(this._cmp_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._cmp_ == child) {
            this._cmp_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._cmp_ == oldChild) {
            setCmp((TCmp) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
