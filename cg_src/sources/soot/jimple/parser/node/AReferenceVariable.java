package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AReferenceVariable.class */
public final class AReferenceVariable extends PVariable {
    private PReference _reference_;

    public AReferenceVariable() {
    }

    public AReferenceVariable(PReference _reference_) {
        setReference(_reference_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AReferenceVariable((PReference) cloneNode(this._reference_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAReferenceVariable(this);
    }

    public PReference getReference() {
        return this._reference_;
    }

    public void setReference(PReference node) {
        if (this._reference_ != null) {
            this._reference_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._reference_ = node;
    }

    public String toString() {
        return toString(this._reference_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._reference_ == child) {
            this._reference_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._reference_ == oldChild) {
            setReference((PReference) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
