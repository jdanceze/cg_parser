package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ABooleanBaseType.class */
public final class ABooleanBaseType extends PBaseType {
    private TBoolean _boolean_;

    public ABooleanBaseType() {
    }

    public ABooleanBaseType(TBoolean _boolean_) {
        setBoolean(_boolean_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ABooleanBaseType((TBoolean) cloneNode(this._boolean_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseABooleanBaseType(this);
    }

    public TBoolean getBoolean() {
        return this._boolean_;
    }

    public void setBoolean(TBoolean node) {
        if (this._boolean_ != null) {
            this._boolean_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._boolean_ = node;
    }

    public String toString() {
        return toString(this._boolean_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._boolean_ == child) {
            this._boolean_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._boolean_ == oldChild) {
            setBoolean((TBoolean) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
