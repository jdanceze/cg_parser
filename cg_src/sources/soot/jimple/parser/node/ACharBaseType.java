package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ACharBaseType.class */
public final class ACharBaseType extends PBaseType {
    private TChar _char_;

    public ACharBaseType() {
    }

    public ACharBaseType(TChar _char_) {
        setChar(_char_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ACharBaseType((TChar) cloneNode(this._char_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseACharBaseType(this);
    }

    public TChar getChar() {
        return this._char_;
    }

    public void setChar(TChar node) {
        if (this._char_ != null) {
            this._char_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._char_ = node;
    }

    public String toString() {
        return toString(this._char_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._char_ == child) {
            this._char_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._char_ == oldChild) {
            setChar((TChar) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
