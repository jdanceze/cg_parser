package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ANativeModifier.class */
public final class ANativeModifier extends PModifier {
    private TNative _native_;

    public ANativeModifier() {
    }

    public ANativeModifier(TNative _native_) {
        setNative(_native_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ANativeModifier((TNative) cloneNode(this._native_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseANativeModifier(this);
    }

    public TNative getNative() {
        return this._native_;
    }

    public void setNative(TNative node) {
        if (this._native_ != null) {
            this._native_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._native_ = node;
    }

    public String toString() {
        return toString(this._native_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._native_ == child) {
            this._native_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._native_ == oldChild) {
            setNative((TNative) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
