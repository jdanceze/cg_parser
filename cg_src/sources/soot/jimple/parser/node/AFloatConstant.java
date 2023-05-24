package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AFloatConstant.class */
public final class AFloatConstant extends PConstant {
    private TMinus _minus_;
    private TFloatConstant _floatConstant_;

    public AFloatConstant() {
    }

    public AFloatConstant(TMinus _minus_, TFloatConstant _floatConstant_) {
        setMinus(_minus_);
        setFloatConstant(_floatConstant_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AFloatConstant((TMinus) cloneNode(this._minus_), (TFloatConstant) cloneNode(this._floatConstant_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAFloatConstant(this);
    }

    public TMinus getMinus() {
        return this._minus_;
    }

    public void setMinus(TMinus node) {
        if (this._minus_ != null) {
            this._minus_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._minus_ = node;
    }

    public TFloatConstant getFloatConstant() {
        return this._floatConstant_;
    }

    public void setFloatConstant(TFloatConstant node) {
        if (this._floatConstant_ != null) {
            this._floatConstant_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._floatConstant_ = node;
    }

    public String toString() {
        return toString(this._minus_) + toString(this._floatConstant_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._minus_ == child) {
            this._minus_ = null;
        } else if (this._floatConstant_ == child) {
            this._floatConstant_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._minus_ == oldChild) {
            setMinus((TMinus) newChild);
        } else if (this._floatConstant_ == oldChild) {
            setFloatConstant((TFloatConstant) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
