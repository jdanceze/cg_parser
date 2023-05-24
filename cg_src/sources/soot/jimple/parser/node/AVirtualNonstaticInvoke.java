package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AVirtualNonstaticInvoke.class */
public final class AVirtualNonstaticInvoke extends PNonstaticInvoke {
    private TVirtualinvoke _virtualinvoke_;

    public AVirtualNonstaticInvoke() {
    }

    public AVirtualNonstaticInvoke(TVirtualinvoke _virtualinvoke_) {
        setVirtualinvoke(_virtualinvoke_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AVirtualNonstaticInvoke((TVirtualinvoke) cloneNode(this._virtualinvoke_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAVirtualNonstaticInvoke(this);
    }

    public TVirtualinvoke getVirtualinvoke() {
        return this._virtualinvoke_;
    }

    public void setVirtualinvoke(TVirtualinvoke node) {
        if (this._virtualinvoke_ != null) {
            this._virtualinvoke_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._virtualinvoke_ = node;
    }

    public String toString() {
        return toString(this._virtualinvoke_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._virtualinvoke_ == child) {
            this._virtualinvoke_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._virtualinvoke_ == oldChild) {
            setVirtualinvoke((TVirtualinvoke) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
