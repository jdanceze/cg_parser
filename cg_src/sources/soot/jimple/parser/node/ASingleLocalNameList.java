package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ASingleLocalNameList.class */
public final class ASingleLocalNameList extends PLocalNameList {
    private PLocalName _localName_;

    public ASingleLocalNameList() {
    }

    public ASingleLocalNameList(PLocalName _localName_) {
        setLocalName(_localName_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ASingleLocalNameList((PLocalName) cloneNode(this._localName_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseASingleLocalNameList(this);
    }

    public PLocalName getLocalName() {
        return this._localName_;
    }

    public void setLocalName(PLocalName node) {
        if (this._localName_ != null) {
            this._localName_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._localName_ = node;
    }

    public String toString() {
        return toString(this._localName_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._localName_ == child) {
            this._localName_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._localName_ == oldChild) {
            setLocalName((PLocalName) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
