package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/APlusBinop.class */
public final class APlusBinop extends PBinop {
    private TPlus _plus_;

    public APlusBinop() {
    }

    public APlusBinop(TPlus _plus_) {
        setPlus(_plus_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new APlusBinop((TPlus) cloneNode(this._plus_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAPlusBinop(this);
    }

    public TPlus getPlus() {
        return this._plus_;
    }

    public void setPlus(TPlus node) {
        if (this._plus_ != null) {
            this._plus_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._plus_ = node;
    }

    public String toString() {
        return toString(this._plus_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._plus_ == child) {
            this._plus_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._plus_ == oldChild) {
            setPlus((TPlus) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
