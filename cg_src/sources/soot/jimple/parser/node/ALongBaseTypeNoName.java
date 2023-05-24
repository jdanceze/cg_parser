package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ALongBaseTypeNoName.class */
public final class ALongBaseTypeNoName extends PBaseTypeNoName {
    private TLong _long_;

    public ALongBaseTypeNoName() {
    }

    public ALongBaseTypeNoName(TLong _long_) {
        setLong(_long_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ALongBaseTypeNoName((TLong) cloneNode(this._long_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseALongBaseTypeNoName(this);
    }

    public TLong getLong() {
        return this._long_;
    }

    public void setLong(TLong node) {
        if (this._long_ != null) {
            this._long_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._long_ = node;
    }

    public String toString() {
        return toString(this._long_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._long_ == child) {
            this._long_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._long_ == oldChild) {
            setLong((TLong) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
