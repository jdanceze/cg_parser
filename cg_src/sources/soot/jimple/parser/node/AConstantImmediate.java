package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AConstantImmediate.class */
public final class AConstantImmediate extends PImmediate {
    private PConstant _constant_;

    public AConstantImmediate() {
    }

    public AConstantImmediate(PConstant _constant_) {
        setConstant(_constant_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AConstantImmediate((PConstant) cloneNode(this._constant_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAConstantImmediate(this);
    }

    public PConstant getConstant() {
        return this._constant_;
    }

    public void setConstant(PConstant node) {
        if (this._constant_ != null) {
            this._constant_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._constant_ = node;
    }

    public String toString() {
        return toString(this._constant_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._constant_ == child) {
            this._constant_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._constant_ == oldChild) {
            setConstant((PConstant) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
