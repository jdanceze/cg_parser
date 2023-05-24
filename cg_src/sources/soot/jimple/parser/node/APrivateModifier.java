package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/APrivateModifier.class */
public final class APrivateModifier extends PModifier {
    private TPrivate _private_;

    public APrivateModifier() {
    }

    public APrivateModifier(TPrivate _private_) {
        setPrivate(_private_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new APrivateModifier((TPrivate) cloneNode(this._private_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAPrivateModifier(this);
    }

    public TPrivate getPrivate() {
        return this._private_;
    }

    public void setPrivate(TPrivate node) {
        if (this._private_ != null) {
            this._private_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._private_ = node;
    }

    public String toString() {
        return toString(this._private_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._private_ == child) {
            this._private_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._private_ == oldChild) {
            setPrivate((TPrivate) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
