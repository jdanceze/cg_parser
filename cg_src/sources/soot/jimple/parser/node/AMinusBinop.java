package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AMinusBinop.class */
public final class AMinusBinop extends PBinop {
    private TMinus _minus_;

    public AMinusBinop() {
    }

    public AMinusBinop(TMinus _minus_) {
        setMinus(_minus_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AMinusBinop((TMinus) cloneNode(this._minus_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAMinusBinop(this);
    }

    public TMinus getMinus() {
        return this._minus_;
    }

    public void setMinus(TMinus node) {
        if (this._minus_ != null) {
            this._minus_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._minus_ = node;
    }

    public String toString() {
        return toString(this._minus_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._minus_ == child) {
            this._minus_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._minus_ == oldChild) {
            setMinus((TMinus) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
