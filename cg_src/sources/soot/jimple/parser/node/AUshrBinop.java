package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AUshrBinop.class */
public final class AUshrBinop extends PBinop {
    private TUshr _ushr_;

    public AUshrBinop() {
    }

    public AUshrBinop(TUshr _ushr_) {
        setUshr(_ushr_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AUshrBinop((TUshr) cloneNode(this._ushr_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAUshrBinop(this);
    }

    public TUshr getUshr() {
        return this._ushr_;
    }

    public void setUshr(TUshr node) {
        if (this._ushr_ != null) {
            this._ushr_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._ushr_ = node;
    }

    public String toString() {
        return toString(this._ushr_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._ushr_ == child) {
            this._ushr_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._ushr_ == oldChild) {
            setUshr((TUshr) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
