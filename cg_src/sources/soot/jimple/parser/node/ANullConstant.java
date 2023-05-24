package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ANullConstant.class */
public final class ANullConstant extends PConstant {
    private TNull _null_;

    public ANullConstant() {
    }

    public ANullConstant(TNull _null_) {
        setNull(_null_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ANullConstant((TNull) cloneNode(this._null_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseANullConstant(this);
    }

    public TNull getNull() {
        return this._null_;
    }

    public void setNull(TNull node) {
        if (this._null_ != null) {
            this._null_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._null_ = node;
    }

    public String toString() {
        return toString(this._null_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._null_ == child) {
            this._null_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._null_ == oldChild) {
            setNull((TNull) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
