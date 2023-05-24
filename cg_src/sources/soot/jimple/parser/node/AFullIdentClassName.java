package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AFullIdentClassName.class */
public final class AFullIdentClassName extends PClassName {
    private TFullIdentifier _fullIdentifier_;

    public AFullIdentClassName() {
    }

    public AFullIdentClassName(TFullIdentifier _fullIdentifier_) {
        setFullIdentifier(_fullIdentifier_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AFullIdentClassName((TFullIdentifier) cloneNode(this._fullIdentifier_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAFullIdentClassName(this);
    }

    public TFullIdentifier getFullIdentifier() {
        return this._fullIdentifier_;
    }

    public void setFullIdentifier(TFullIdentifier node) {
        if (this._fullIdentifier_ != null) {
            this._fullIdentifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._fullIdentifier_ = node;
    }

    public String toString() {
        return toString(this._fullIdentifier_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._fullIdentifier_ == child) {
            this._fullIdentifier_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._fullIdentifier_ == oldChild) {
            setFullIdentifier((TFullIdentifier) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
