package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ATransientModifier.class */
public final class ATransientModifier extends PModifier {
    private TTransient _transient_;

    public ATransientModifier() {
    }

    public ATransientModifier(TTransient _transient_) {
        setTransient(_transient_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ATransientModifier((TTransient) cloneNode(this._transient_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseATransientModifier(this);
    }

    public TTransient getTransient() {
        return this._transient_;
    }

    public void setTransient(TTransient node) {
        if (this._transient_ != null) {
            this._transient_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._transient_ = node;
    }

    public String toString() {
        return toString(this._transient_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._transient_ == child) {
            this._transient_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._transient_ == oldChild) {
            setTransient((TTransient) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
