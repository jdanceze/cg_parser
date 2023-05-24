package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AVoidType.class */
public final class AVoidType extends PType {
    private TVoid _void_;

    public AVoidType() {
    }

    public AVoidType(TVoid _void_) {
        setVoid(_void_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AVoidType((TVoid) cloneNode(this._void_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAVoidType(this);
    }

    public TVoid getVoid() {
        return this._void_;
    }

    public void setVoid(TVoid node) {
        if (this._void_ != null) {
            this._void_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._void_ = node;
    }

    public String toString() {
        return toString(this._void_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._void_ == child) {
            this._void_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._void_ == oldChild) {
            setVoid((TVoid) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
