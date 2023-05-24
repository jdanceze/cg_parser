package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ANonvoidJimpleType.class */
public final class ANonvoidJimpleType extends PJimpleType {
    private PNonvoidType _nonvoidType_;

    public ANonvoidJimpleType() {
    }

    public ANonvoidJimpleType(PNonvoidType _nonvoidType_) {
        setNonvoidType(_nonvoidType_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ANonvoidJimpleType((PNonvoidType) cloneNode(this._nonvoidType_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseANonvoidJimpleType(this);
    }

    public PNonvoidType getNonvoidType() {
        return this._nonvoidType_;
    }

    public void setNonvoidType(PNonvoidType node) {
        if (this._nonvoidType_ != null) {
            this._nonvoidType_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._nonvoidType_ = node;
    }

    public String toString() {
        return toString(this._nonvoidType_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._nonvoidType_ == child) {
            this._nonvoidType_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._nonvoidType_ == oldChild) {
            setNonvoidType((PNonvoidType) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
