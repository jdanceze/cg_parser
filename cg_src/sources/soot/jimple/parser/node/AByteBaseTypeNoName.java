package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AByteBaseTypeNoName.class */
public final class AByteBaseTypeNoName extends PBaseTypeNoName {
    private TByte _byte_;

    public AByteBaseTypeNoName() {
    }

    public AByteBaseTypeNoName(TByte _byte_) {
        setByte(_byte_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AByteBaseTypeNoName((TByte) cloneNode(this._byte_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAByteBaseTypeNoName(this);
    }

    public TByte getByte() {
        return this._byte_;
    }

    public void setByte(TByte node) {
        if (this._byte_ != null) {
            this._byte_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._byte_ = node;
    }

    public String toString() {
        return toString(this._byte_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._byte_ == child) {
            this._byte_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._byte_ == oldChild) {
            setByte((TByte) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
