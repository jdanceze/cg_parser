package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ALocalName.class */
public final class ALocalName extends PLocalName {
    private PName _name_;

    public ALocalName() {
    }

    public ALocalName(PName _name_) {
        setName(_name_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ALocalName((PName) cloneNode(this._name_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseALocalName(this);
    }

    public PName getName() {
        return this._name_;
    }

    public void setName(PName node) {
        if (this._name_ != null) {
            this._name_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._name_ = node;
    }

    public String toString() {
        return toString(this._name_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._name_ == child) {
            this._name_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._name_ == oldChild) {
            setName((PName) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
