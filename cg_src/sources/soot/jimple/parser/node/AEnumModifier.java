package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AEnumModifier.class */
public final class AEnumModifier extends PModifier {
    private TEnum _enum_;

    public AEnumModifier() {
    }

    public AEnumModifier(TEnum _enum_) {
        setEnum(_enum_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AEnumModifier((TEnum) cloneNode(this._enum_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAEnumModifier(this);
    }

    public TEnum getEnum() {
        return this._enum_;
    }

    public void setEnum(TEnum node) {
        if (this._enum_ != null) {
            this._enum_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._enum_ = node;
    }

    public String toString() {
        return toString(this._enum_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._enum_ == child) {
            this._enum_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._enum_ == oldChild) {
            setEnum((TEnum) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
