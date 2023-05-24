package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AShrBinop.class */
public final class AShrBinop extends PBinop {
    private TShr _shr_;

    public AShrBinop() {
    }

    public AShrBinop(TShr _shr_) {
        setShr(_shr_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AShrBinop((TShr) cloneNode(this._shr_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAShrBinop(this);
    }

    public TShr getShr() {
        return this._shr_;
    }

    public void setShr(TShr node) {
        if (this._shr_ != null) {
            this._shr_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._shr_ = node;
    }

    public String toString() {
        return toString(this._shr_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._shr_ == child) {
            this._shr_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._shr_ == oldChild) {
            setShr((TShr) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
