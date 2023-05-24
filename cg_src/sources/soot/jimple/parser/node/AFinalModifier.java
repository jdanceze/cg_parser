package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AFinalModifier.class */
public final class AFinalModifier extends PModifier {
    private TFinal _final_;

    public AFinalModifier() {
    }

    public AFinalModifier(TFinal _final_) {
        setFinal(_final_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AFinalModifier((TFinal) cloneNode(this._final_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAFinalModifier(this);
    }

    public TFinal getFinal() {
        return this._final_;
    }

    public void setFinal(TFinal node) {
        if (this._final_ != null) {
            this._final_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._final_ = node;
    }

    public String toString() {
        return toString(this._final_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._final_ == child) {
            this._final_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._final_ == oldChild) {
            setFinal((TFinal) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
