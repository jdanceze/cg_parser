package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AImmediateExpression.class */
public final class AImmediateExpression extends PExpression {
    private PImmediate _immediate_;

    public AImmediateExpression() {
    }

    public AImmediateExpression(PImmediate _immediate_) {
        setImmediate(_immediate_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AImmediateExpression((PImmediate) cloneNode(this._immediate_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAImmediateExpression(this);
    }

    public PImmediate getImmediate() {
        return this._immediate_;
    }

    public void setImmediate(PImmediate node) {
        if (this._immediate_ != null) {
            this._immediate_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._immediate_ = node;
    }

    public String toString() {
        return toString(this._immediate_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._immediate_ == child) {
            this._immediate_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._immediate_ == oldChild) {
            setImmediate((PImmediate) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
