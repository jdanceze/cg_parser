package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AAbstractModifier.class */
public final class AAbstractModifier extends PModifier {
    private TAbstract _abstract_;

    public AAbstractModifier() {
    }

    public AAbstractModifier(TAbstract _abstract_) {
        setAbstract(_abstract_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AAbstractModifier((TAbstract) cloneNode(this._abstract_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAAbstractModifier(this);
    }

    public TAbstract getAbstract() {
        return this._abstract_;
    }

    public void setAbstract(TAbstract node) {
        if (this._abstract_ != null) {
            this._abstract_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._abstract_ = node;
    }

    public String toString() {
        return toString(this._abstract_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._abstract_ == child) {
            this._abstract_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._abstract_ == oldChild) {
            setAbstract((TAbstract) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
