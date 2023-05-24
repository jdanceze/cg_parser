package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/APublicModifier.class */
public final class APublicModifier extends PModifier {
    private TPublic _public_;

    public APublicModifier() {
    }

    public APublicModifier(TPublic _public_) {
        setPublic(_public_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new APublicModifier((TPublic) cloneNode(this._public_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAPublicModifier(this);
    }

    public TPublic getPublic() {
        return this._public_;
    }

    public void setPublic(TPublic node) {
        if (this._public_ != null) {
            this._public_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._public_ = node;
    }

    public String toString() {
        return toString(this._public_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._public_ == child) {
            this._public_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._public_ == oldChild) {
            setPublic((TPublic) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
