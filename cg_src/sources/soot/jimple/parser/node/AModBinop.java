package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AModBinop.class */
public final class AModBinop extends PBinop {
    private TMod _mod_;

    public AModBinop() {
    }

    public AModBinop(TMod _mod_) {
        setMod(_mod_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AModBinop((TMod) cloneNode(this._mod_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAModBinop(this);
    }

    public TMod getMod() {
        return this._mod_;
    }

    public void setMod(TMod node) {
        if (this._mod_ != null) {
            this._mod_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._mod_ = node;
    }

    public String toString() {
        return toString(this._mod_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._mod_ == child) {
            this._mod_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._mod_ == oldChild) {
            setMod((TMod) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
