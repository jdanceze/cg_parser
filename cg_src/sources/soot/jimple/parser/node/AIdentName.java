package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AIdentName.class */
public final class AIdentName extends PName {
    private TIdentifier _identifier_;

    public AIdentName() {
    }

    public AIdentName(TIdentifier _identifier_) {
        setIdentifier(_identifier_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AIdentName((TIdentifier) cloneNode(this._identifier_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAIdentName(this);
    }

    public TIdentifier getIdentifier() {
        return this._identifier_;
    }

    public void setIdentifier(TIdentifier node) {
        if (this._identifier_ != null) {
            this._identifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier_ = node;
    }

    public String toString() {
        return toString(this._identifier_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._identifier_ == child) {
            this._identifier_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._identifier_ == oldChild) {
            setIdentifier((TIdentifier) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
