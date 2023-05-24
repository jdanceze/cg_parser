package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ADefaultCaseLabel.class */
public final class ADefaultCaseLabel extends PCaseLabel {
    private TDefault _default_;

    public ADefaultCaseLabel() {
    }

    public ADefaultCaseLabel(TDefault _default_) {
        setDefault(_default_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ADefaultCaseLabel((TDefault) cloneNode(this._default_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseADefaultCaseLabel(this);
    }

    public TDefault getDefault() {
        return this._default_;
    }

    public void setDefault(TDefault node) {
        if (this._default_ != null) {
            this._default_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._default_ = node;
    }

    public String toString() {
        return toString(this._default_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._default_ == child) {
            this._default_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._default_ == oldChild) {
            setDefault((TDefault) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
