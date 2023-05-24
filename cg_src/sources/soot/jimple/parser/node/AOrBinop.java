package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AOrBinop.class */
public final class AOrBinop extends PBinop {
    private TOr _or_;

    public AOrBinop() {
    }

    public AOrBinop(TOr _or_) {
        setOr(_or_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AOrBinop((TOr) cloneNode(this._or_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAOrBinop(this);
    }

    public TOr getOr() {
        return this._or_;
    }

    public void setOr(TOr node) {
        if (this._or_ != null) {
            this._or_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._or_ = node;
    }

    public String toString() {
        return toString(this._or_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._or_ == child) {
            this._or_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._or_ == oldChild) {
            setOr((TOr) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
