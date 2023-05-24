package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AUnknownJimpleType.class */
public final class AUnknownJimpleType extends PJimpleType {
    private TUnknown _unknown_;

    public AUnknownJimpleType() {
    }

    public AUnknownJimpleType(TUnknown _unknown_) {
        setUnknown(_unknown_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AUnknownJimpleType((TUnknown) cloneNode(this._unknown_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAUnknownJimpleType(this);
    }

    public TUnknown getUnknown() {
        return this._unknown_;
    }

    public void setUnknown(TUnknown node) {
        if (this._unknown_ != null) {
            this._unknown_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._unknown_ = node;
    }

    public String toString() {
        return toString(this._unknown_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._unknown_ == child) {
            this._unknown_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._unknown_ == oldChild) {
            setUnknown((TUnknown) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
