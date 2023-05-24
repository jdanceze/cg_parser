package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AQuotedClassName.class */
public final class AQuotedClassName extends PClassName {
    private TQuotedName _quotedName_;

    public AQuotedClassName() {
    }

    public AQuotedClassName(TQuotedName _quotedName_) {
        setQuotedName(_quotedName_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AQuotedClassName((TQuotedName) cloneNode(this._quotedName_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAQuotedClassName(this);
    }

    public TQuotedName getQuotedName() {
        return this._quotedName_;
    }

    public void setQuotedName(TQuotedName node) {
        if (this._quotedName_ != null) {
            this._quotedName_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._quotedName_ = node;
    }

    public String toString() {
        return toString(this._quotedName_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._quotedName_ == child) {
            this._quotedName_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._quotedName_ == oldChild) {
            setQuotedName((TQuotedName) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
