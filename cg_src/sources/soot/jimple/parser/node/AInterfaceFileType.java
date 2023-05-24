package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AInterfaceFileType.class */
public final class AInterfaceFileType extends PFileType {
    private TInterface _interface_;

    public AInterfaceFileType() {
    }

    public AInterfaceFileType(TInterface _interface_) {
        setInterface(_interface_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AInterfaceFileType((TInterface) cloneNode(this._interface_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAInterfaceFileType(this);
    }

    public TInterface getInterface() {
        return this._interface_;
    }

    public void setInterface(TInterface node) {
        if (this._interface_ != null) {
            this._interface_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._interface_ = node;
    }

    public String toString() {
        return toString(this._interface_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._interface_ == child) {
            this._interface_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._interface_ == oldChild) {
            setInterface((TInterface) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
