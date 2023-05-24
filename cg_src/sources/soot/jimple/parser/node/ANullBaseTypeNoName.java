package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ANullBaseTypeNoName.class */
public final class ANullBaseTypeNoName extends PBaseTypeNoName {
    private TNullType _nullType_;

    public ANullBaseTypeNoName() {
    }

    public ANullBaseTypeNoName(TNullType _nullType_) {
        setNullType(_nullType_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ANullBaseTypeNoName((TNullType) cloneNode(this._nullType_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseANullBaseTypeNoName(this);
    }

    public TNullType getNullType() {
        return this._nullType_;
    }

    public void setNullType(TNullType node) {
        if (this._nullType_ != null) {
            this._nullType_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._nullType_ = node;
    }

    public String toString() {
        return toString(this._nullType_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._nullType_ == child) {
            this._nullType_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._nullType_ == oldChild) {
            setNullType((TNullType) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
