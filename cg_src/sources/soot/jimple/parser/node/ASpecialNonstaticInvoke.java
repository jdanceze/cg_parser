package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ASpecialNonstaticInvoke.class */
public final class ASpecialNonstaticInvoke extends PNonstaticInvoke {
    private TSpecialinvoke _specialinvoke_;

    public ASpecialNonstaticInvoke() {
    }

    public ASpecialNonstaticInvoke(TSpecialinvoke _specialinvoke_) {
        setSpecialinvoke(_specialinvoke_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ASpecialNonstaticInvoke((TSpecialinvoke) cloneNode(this._specialinvoke_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseASpecialNonstaticInvoke(this);
    }

    public TSpecialinvoke getSpecialinvoke() {
        return this._specialinvoke_;
    }

    public void setSpecialinvoke(TSpecialinvoke node) {
        if (this._specialinvoke_ != null) {
            this._specialinvoke_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._specialinvoke_ = node;
    }

    public String toString() {
        return toString(this._specialinvoke_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._specialinvoke_ == child) {
            this._specialinvoke_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._specialinvoke_ == oldChild) {
            setSpecialinvoke((TSpecialinvoke) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
