package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ACmplBinop.class */
public final class ACmplBinop extends PBinop {
    private TCmpl _cmpl_;

    public ACmplBinop() {
    }

    public ACmplBinop(TCmpl _cmpl_) {
        setCmpl(_cmpl_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ACmplBinop((TCmpl) cloneNode(this._cmpl_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseACmplBinop(this);
    }

    public TCmpl getCmpl() {
        return this._cmpl_;
    }

    public void setCmpl(TCmpl node) {
        if (this._cmpl_ != null) {
            this._cmpl_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._cmpl_ = node;
    }

    public String toString() {
        return toString(this._cmpl_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._cmpl_ == child) {
            this._cmpl_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._cmpl_ == oldChild) {
            setCmpl((TCmpl) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
