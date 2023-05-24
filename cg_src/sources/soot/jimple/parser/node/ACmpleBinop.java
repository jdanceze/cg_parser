package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ACmpleBinop.class */
public final class ACmpleBinop extends PBinop {
    private TCmple _cmple_;

    public ACmpleBinop() {
    }

    public ACmpleBinop(TCmple _cmple_) {
        setCmple(_cmple_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ACmpleBinop((TCmple) cloneNode(this._cmple_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseACmpleBinop(this);
    }

    public TCmple getCmple() {
        return this._cmple_;
    }

    public void setCmple(TCmple node) {
        if (this._cmple_ != null) {
            this._cmple_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._cmple_ = node;
    }

    public String toString() {
        return toString(this._cmple_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._cmple_ == child) {
            this._cmple_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._cmple_ == oldChild) {
            setCmple((TCmple) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
