package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AInterfaceNonstaticInvoke.class */
public final class AInterfaceNonstaticInvoke extends PNonstaticInvoke {
    private TInterfaceinvoke _interfaceinvoke_;

    public AInterfaceNonstaticInvoke() {
    }

    public AInterfaceNonstaticInvoke(TInterfaceinvoke _interfaceinvoke_) {
        setInterfaceinvoke(_interfaceinvoke_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AInterfaceNonstaticInvoke((TInterfaceinvoke) cloneNode(this._interfaceinvoke_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAInterfaceNonstaticInvoke(this);
    }

    public TInterfaceinvoke getInterfaceinvoke() {
        return this._interfaceinvoke_;
    }

    public void setInterfaceinvoke(TInterfaceinvoke node) {
        if (this._interfaceinvoke_ != null) {
            this._interfaceinvoke_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._interfaceinvoke_ = node;
    }

    public String toString() {
        return toString(this._interfaceinvoke_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._interfaceinvoke_ == child) {
            this._interfaceinvoke_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._interfaceinvoke_ == oldChild) {
            setInterfaceinvoke((TInterfaceinvoke) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
