package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AProtectedModifier.class */
public final class AProtectedModifier extends PModifier {
    private TProtected _protected_;

    public AProtectedModifier() {
    }

    public AProtectedModifier(TProtected _protected_) {
        setProtected(_protected_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AProtectedModifier((TProtected) cloneNode(this._protected_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAProtectedModifier(this);
    }

    public TProtected getProtected() {
        return this._protected_;
    }

    public void setProtected(TProtected node) {
        if (this._protected_ != null) {
            this._protected_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._protected_ = node;
    }

    public String toString() {
        return toString(this._protected_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._protected_ == child) {
            this._protected_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._protected_ == oldChild) {
            setProtected((TProtected) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
