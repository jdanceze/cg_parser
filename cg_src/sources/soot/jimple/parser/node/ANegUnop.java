package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ANegUnop.class */
public final class ANegUnop extends PUnop {
    private TNeg _neg_;

    public ANegUnop() {
    }

    public ANegUnop(TNeg _neg_) {
        setNeg(_neg_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ANegUnop((TNeg) cloneNode(this._neg_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseANegUnop(this);
    }

    public TNeg getNeg() {
        return this._neg_;
    }

    public void setNeg(TNeg node) {
        if (this._neg_ != null) {
            this._neg_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._neg_ = node;
    }

    public String toString() {
        return toString(this._neg_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._neg_ == child) {
            this._neg_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._neg_ == oldChild) {
            setNeg((TNeg) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
