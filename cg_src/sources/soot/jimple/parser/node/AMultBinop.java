package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AMultBinop.class */
public final class AMultBinop extends PBinop {
    private TMult _mult_;

    public AMultBinop() {
    }

    public AMultBinop(TMult _mult_) {
        setMult(_mult_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AMultBinop((TMult) cloneNode(this._mult_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAMultBinop(this);
    }

    public TMult getMult() {
        return this._mult_;
    }

    public void setMult(TMult node) {
        if (this._mult_ != null) {
            this._mult_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._mult_ = node;
    }

    public String toString() {
        return toString(this._mult_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._mult_ == child) {
            this._mult_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._mult_ == oldChild) {
            setMult((TMult) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
