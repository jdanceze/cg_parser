package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AAndBinop.class */
public final class AAndBinop extends PBinop {
    private TAnd _and_;

    public AAndBinop() {
    }

    public AAndBinop(TAnd _and_) {
        setAnd(_and_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AAndBinop((TAnd) cloneNode(this._and_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAAndBinop(this);
    }

    public TAnd getAnd() {
        return this._and_;
    }

    public void setAnd(TAnd node) {
        if (this._and_ != null) {
            this._and_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._and_ = node;
    }

    public String toString() {
        return toString(this._and_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._and_ == child) {
            this._and_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._and_ == oldChild) {
            setAnd((TAnd) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
