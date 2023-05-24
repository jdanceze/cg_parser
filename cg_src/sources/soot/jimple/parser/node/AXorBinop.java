package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AXorBinop.class */
public final class AXorBinop extends PBinop {
    private TXor _xor_;

    public AXorBinop() {
    }

    public AXorBinop(TXor _xor_) {
        setXor(_xor_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AXorBinop((TXor) cloneNode(this._xor_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAXorBinop(this);
    }

    public TXor getXor() {
        return this._xor_;
    }

    public void setXor(TXor node) {
        if (this._xor_ != null) {
            this._xor_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._xor_ = node;
    }

    public String toString() {
        return toString(this._xor_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._xor_ == child) {
            this._xor_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._xor_ == oldChild) {
            setXor((TXor) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
