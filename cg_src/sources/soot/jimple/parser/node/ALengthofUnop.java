package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ALengthofUnop.class */
public final class ALengthofUnop extends PUnop {
    private TLengthof _lengthof_;

    public ALengthofUnop() {
    }

    public ALengthofUnop(TLengthof _lengthof_) {
        setLengthof(_lengthof_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ALengthofUnop((TLengthof) cloneNode(this._lengthof_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseALengthofUnop(this);
    }

    public TLengthof getLengthof() {
        return this._lengthof_;
    }

    public void setLengthof(TLengthof node) {
        if (this._lengthof_ != null) {
            this._lengthof_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._lengthof_ = node;
    }

    public String toString() {
        return toString(this._lengthof_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._lengthof_ == child) {
            this._lengthof_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._lengthof_ == oldChild) {
            setLengthof((TLengthof) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
