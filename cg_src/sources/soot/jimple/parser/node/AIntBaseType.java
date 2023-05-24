package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AIntBaseType.class */
public final class AIntBaseType extends PBaseType {
    private TInt _int_;

    public AIntBaseType() {
    }

    public AIntBaseType(TInt _int_) {
        setInt(_int_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AIntBaseType((TInt) cloneNode(this._int_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAIntBaseType(this);
    }

    public TInt getInt() {
        return this._int_;
    }

    public void setInt(TInt node) {
        if (this._int_ != null) {
            this._int_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._int_ = node;
    }

    public String toString() {
        return toString(this._int_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._int_ == child) {
            this._int_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._int_ == oldChild) {
            setInt((TInt) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
