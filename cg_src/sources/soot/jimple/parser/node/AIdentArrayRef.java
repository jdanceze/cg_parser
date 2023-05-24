package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AIdentArrayRef.class */
public final class AIdentArrayRef extends PArrayRef {
    private TIdentifier _identifier_;
    private PFixedArrayDescriptor _fixedArrayDescriptor_;

    public AIdentArrayRef() {
    }

    public AIdentArrayRef(TIdentifier _identifier_, PFixedArrayDescriptor _fixedArrayDescriptor_) {
        setIdentifier(_identifier_);
        setFixedArrayDescriptor(_fixedArrayDescriptor_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AIdentArrayRef((TIdentifier) cloneNode(this._identifier_), (PFixedArrayDescriptor) cloneNode(this._fixedArrayDescriptor_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAIdentArrayRef(this);
    }

    public TIdentifier getIdentifier() {
        return this._identifier_;
    }

    public void setIdentifier(TIdentifier node) {
        if (this._identifier_ != null) {
            this._identifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier_ = node;
    }

    public PFixedArrayDescriptor getFixedArrayDescriptor() {
        return this._fixedArrayDescriptor_;
    }

    public void setFixedArrayDescriptor(PFixedArrayDescriptor node) {
        if (this._fixedArrayDescriptor_ != null) {
            this._fixedArrayDescriptor_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._fixedArrayDescriptor_ = node;
    }

    public String toString() {
        return toString(this._identifier_) + toString(this._fixedArrayDescriptor_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._identifier_ == child) {
            this._identifier_ = null;
        } else if (this._fixedArrayDescriptor_ == child) {
            this._fixedArrayDescriptor_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._identifier_ == oldChild) {
            setIdentifier((TIdentifier) newChild);
        } else if (this._fixedArrayDescriptor_ == oldChild) {
            setFixedArrayDescriptor((PFixedArrayDescriptor) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
