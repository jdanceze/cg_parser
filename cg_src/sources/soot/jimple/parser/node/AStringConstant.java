package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AStringConstant.class */
public final class AStringConstant extends PConstant {
    private TStringConstant _stringConstant_;

    public AStringConstant() {
    }

    public AStringConstant(TStringConstant _stringConstant_) {
        setStringConstant(_stringConstant_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AStringConstant((TStringConstant) cloneNode(this._stringConstant_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAStringConstant(this);
    }

    public TStringConstant getStringConstant() {
        return this._stringConstant_;
    }

    public void setStringConstant(TStringConstant node) {
        if (this._stringConstant_ != null) {
            this._stringConstant_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._stringConstant_ = node;
    }

    public String toString() {
        return toString(this._stringConstant_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._stringConstant_ == child) {
            this._stringConstant_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._stringConstant_ == oldChild) {
            setStringConstant((TStringConstant) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
