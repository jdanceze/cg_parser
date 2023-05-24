package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AFieldReference.class */
public final class AFieldReference extends PReference {
    private PFieldRef _fieldRef_;

    public AFieldReference() {
    }

    public AFieldReference(PFieldRef _fieldRef_) {
        setFieldRef(_fieldRef_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AFieldReference((PFieldRef) cloneNode(this._fieldRef_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAFieldReference(this);
    }

    public PFieldRef getFieldRef() {
        return this._fieldRef_;
    }

    public void setFieldRef(PFieldRef node) {
        if (this._fieldRef_ != null) {
            this._fieldRef_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._fieldRef_ = node;
    }

    public String toString() {
        return toString(this._fieldRef_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._fieldRef_ == child) {
            this._fieldRef_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._fieldRef_ == oldChild) {
            setFieldRef((PFieldRef) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
