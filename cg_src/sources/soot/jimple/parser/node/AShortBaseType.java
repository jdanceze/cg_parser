package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AShortBaseType.class */
public final class AShortBaseType extends PBaseType {
    private TShort _short_;

    public AShortBaseType() {
    }

    public AShortBaseType(TShort _short_) {
        setShort(_short_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AShortBaseType((TShort) cloneNode(this._short_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAShortBaseType(this);
    }

    public TShort getShort() {
        return this._short_;
    }

    public void setShort(TShort node) {
        if (this._short_ != null) {
            this._short_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._short_ = node;
    }

    public String toString() {
        return toString(this._short_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._short_ == child) {
            this._short_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._short_ == oldChild) {
            setShort((TShort) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
