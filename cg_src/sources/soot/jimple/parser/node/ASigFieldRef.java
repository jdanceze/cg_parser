package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ASigFieldRef.class */
public final class ASigFieldRef extends PFieldRef {
    private PFieldSignature _fieldSignature_;

    public ASigFieldRef() {
    }

    public ASigFieldRef(PFieldSignature _fieldSignature_) {
        setFieldSignature(_fieldSignature_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ASigFieldRef((PFieldSignature) cloneNode(this._fieldSignature_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseASigFieldRef(this);
    }

    public PFieldSignature getFieldSignature() {
        return this._fieldSignature_;
    }

    public void setFieldSignature(PFieldSignature node) {
        if (this._fieldSignature_ != null) {
            this._fieldSignature_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._fieldSignature_ = node;
    }

    public String toString() {
        return toString(this._fieldSignature_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._fieldSignature_ == child) {
            this._fieldSignature_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._fieldSignature_ == oldChild) {
            setFieldSignature((PFieldSignature) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
