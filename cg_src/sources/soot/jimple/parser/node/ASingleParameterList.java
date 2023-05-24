package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ASingleParameterList.class */
public final class ASingleParameterList extends PParameterList {
    private PParameter _parameter_;

    public ASingleParameterList() {
    }

    public ASingleParameterList(PParameter _parameter_) {
        setParameter(_parameter_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ASingleParameterList((PParameter) cloneNode(this._parameter_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseASingleParameterList(this);
    }

    public PParameter getParameter() {
        return this._parameter_;
    }

    public void setParameter(PParameter node) {
        if (this._parameter_ != null) {
            this._parameter_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._parameter_ = node;
    }

    public String toString() {
        return toString(this._parameter_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._parameter_ == child) {
            this._parameter_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._parameter_ == oldChild) {
            setParameter((PParameter) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
