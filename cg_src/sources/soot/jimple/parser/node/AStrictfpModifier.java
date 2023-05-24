package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AStrictfpModifier.class */
public final class AStrictfpModifier extends PModifier {
    private TStrictfp _strictfp_;

    public AStrictfpModifier() {
    }

    public AStrictfpModifier(TStrictfp _strictfp_) {
        setStrictfp(_strictfp_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AStrictfpModifier((TStrictfp) cloneNode(this._strictfp_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAStrictfpModifier(this);
    }

    public TStrictfp getStrictfp() {
        return this._strictfp_;
    }

    public void setStrictfp(TStrictfp node) {
        if (this._strictfp_ != null) {
            this._strictfp_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._strictfp_ = node;
    }

    public String toString() {
        return toString(this._strictfp_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._strictfp_ == child) {
            this._strictfp_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._strictfp_ == oldChild) {
            setStrictfp((TStrictfp) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
