package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AVolatileModifier.class */
public final class AVolatileModifier extends PModifier {
    private TVolatile _volatile_;

    public AVolatileModifier() {
    }

    public AVolatileModifier(TVolatile _volatile_) {
        setVolatile(_volatile_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AVolatileModifier((TVolatile) cloneNode(this._volatile_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAVolatileModifier(this);
    }

    public TVolatile getVolatile() {
        return this._volatile_;
    }

    public void setVolatile(TVolatile node) {
        if (this._volatile_ != null) {
            this._volatile_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._volatile_ = node;
    }

    public String toString() {
        return toString(this._volatile_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._volatile_ == child) {
            this._volatile_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._volatile_ == oldChild) {
            setVolatile((TVolatile) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
