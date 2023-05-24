package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ACmpeqBinop.class */
public final class ACmpeqBinop extends PBinop {
    private TCmpeq _cmpeq_;

    public ACmpeqBinop() {
    }

    public ACmpeqBinop(TCmpeq _cmpeq_) {
        setCmpeq(_cmpeq_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ACmpeqBinop((TCmpeq) cloneNode(this._cmpeq_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseACmpeqBinop(this);
    }

    public TCmpeq getCmpeq() {
        return this._cmpeq_;
    }

    public void setCmpeq(TCmpeq node) {
        if (this._cmpeq_ != null) {
            this._cmpeq_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._cmpeq_ = node;
    }

    public String toString() {
        return toString(this._cmpeq_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._cmpeq_ == child) {
            this._cmpeq_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._cmpeq_ == oldChild) {
            setCmpeq((TCmpeq) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
