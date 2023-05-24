package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ASynchronizedModifier.class */
public final class ASynchronizedModifier extends PModifier {
    private TSynchronized _synchronized_;

    public ASynchronizedModifier() {
    }

    public ASynchronizedModifier(TSynchronized _synchronized_) {
        setSynchronized(_synchronized_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ASynchronizedModifier((TSynchronized) cloneNode(this._synchronized_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseASynchronizedModifier(this);
    }

    public TSynchronized getSynchronized() {
        return this._synchronized_;
    }

    public void setSynchronized(TSynchronized node) {
        if (this._synchronized_ != null) {
            this._synchronized_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._synchronized_ = node;
    }

    public String toString() {
        return toString(this._synchronized_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._synchronized_ == child) {
            this._synchronized_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._synchronized_ == oldChild) {
            setSynchronized((TSynchronized) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
