package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ADoubleBaseTypeNoName.class */
public final class ADoubleBaseTypeNoName extends PBaseTypeNoName {
    private TDouble _double_;

    public ADoubleBaseTypeNoName() {
    }

    public ADoubleBaseTypeNoName(TDouble _double_) {
        setDouble(_double_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ADoubleBaseTypeNoName((TDouble) cloneNode(this._double_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseADoubleBaseTypeNoName(this);
    }

    public TDouble getDouble() {
        return this._double_;
    }

    public void setDouble(TDouble node) {
        if (this._double_ != null) {
            this._double_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._double_ = node;
    }

    public String toString() {
        return toString(this._double_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._double_ == child) {
            this._double_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._double_ == oldChild) {
            setDouble((TDouble) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
