package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AAnnotationModifier.class */
public final class AAnnotationModifier extends PModifier {
    private TAnnotation _annotation_;

    public AAnnotationModifier() {
    }

    public AAnnotationModifier(TAnnotation _annotation_) {
        setAnnotation(_annotation_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AAnnotationModifier((TAnnotation) cloneNode(this._annotation_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAAnnotationModifier(this);
    }

    public TAnnotation getAnnotation() {
        return this._annotation_;
    }

    public void setAnnotation(TAnnotation node) {
        if (this._annotation_ != null) {
            this._annotation_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._annotation_ = node;
    }

    public String toString() {
        return toString(this._annotation_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._annotation_ == child) {
            this._annotation_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._annotation_ == oldChild) {
            setAnnotation((TAnnotation) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
