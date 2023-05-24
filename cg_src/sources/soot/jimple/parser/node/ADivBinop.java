package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ADivBinop.class */
public final class ADivBinop extends PBinop {
    private TDiv _div_;

    public ADivBinop() {
    }

    public ADivBinop(TDiv _div_) {
        setDiv(_div_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ADivBinop((TDiv) cloneNode(this._div_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseADivBinop(this);
    }

    public TDiv getDiv() {
        return this._div_;
    }

    public void setDiv(TDiv node) {
        if (this._div_ != null) {
            this._div_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._div_ = node;
    }

    public String toString() {
        return toString(this._div_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._div_ == child) {
            this._div_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._div_ == oldChild) {
            setDiv((TDiv) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
