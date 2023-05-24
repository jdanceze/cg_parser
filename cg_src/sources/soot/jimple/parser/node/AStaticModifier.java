package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AStaticModifier.class */
public final class AStaticModifier extends PModifier {
    private TStatic _static_;

    public AStaticModifier() {
    }

    public AStaticModifier(TStatic _static_) {
        setStatic(_static_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AStaticModifier((TStatic) cloneNode(this._static_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAStaticModifier(this);
    }

    public TStatic getStatic() {
        return this._static_;
    }

    public void setStatic(TStatic node) {
        if (this._static_ != null) {
            this._static_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._static_ = node;
    }

    public String toString() {
        return toString(this._static_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._static_ == child) {
            this._static_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._static_ == oldChild) {
            setStatic((TStatic) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
