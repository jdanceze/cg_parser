package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AArrayReference.class */
public final class AArrayReference extends PReference {
    private PArrayRef _arrayRef_;

    public AArrayReference() {
    }

    public AArrayReference(PArrayRef _arrayRef_) {
        setArrayRef(_arrayRef_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AArrayReference((PArrayRef) cloneNode(this._arrayRef_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAArrayReference(this);
    }

    public PArrayRef getArrayRef() {
        return this._arrayRef_;
    }

    public void setArrayRef(PArrayRef node) {
        if (this._arrayRef_ != null) {
            this._arrayRef_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._arrayRef_ = node;
    }

    public String toString() {
        return toString(this._arrayRef_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._arrayRef_ == child) {
            this._arrayRef_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._arrayRef_ == oldChild) {
            setArrayRef((PArrayRef) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
