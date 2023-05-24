package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AFloatBaseTypeNoName.class */
public final class AFloatBaseTypeNoName extends PBaseTypeNoName {
    private TFloat _float_;

    public AFloatBaseTypeNoName() {
    }

    public AFloatBaseTypeNoName(TFloat _float_) {
        setFloat(_float_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AFloatBaseTypeNoName((TFloat) cloneNode(this._float_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAFloatBaseTypeNoName(this);
    }

    public TFloat getFloat() {
        return this._float_;
    }

    public void setFloat(TFloat node) {
        if (this._float_ != null) {
            this._float_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._float_ = node;
    }

    public String toString() {
        return toString(this._float_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._float_ == child) {
            this._float_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._float_ == oldChild) {
            setFloat((TFloat) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
