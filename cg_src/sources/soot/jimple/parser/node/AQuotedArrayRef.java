package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AQuotedArrayRef.class */
public final class AQuotedArrayRef extends PArrayRef {
    private TQuotedName _quotedName_;
    private PFixedArrayDescriptor _fixedArrayDescriptor_;

    public AQuotedArrayRef() {
    }

    public AQuotedArrayRef(TQuotedName _quotedName_, PFixedArrayDescriptor _fixedArrayDescriptor_) {
        setQuotedName(_quotedName_);
        setFixedArrayDescriptor(_fixedArrayDescriptor_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AQuotedArrayRef((TQuotedName) cloneNode(this._quotedName_), (PFixedArrayDescriptor) cloneNode(this._fixedArrayDescriptor_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAQuotedArrayRef(this);
    }

    public TQuotedName getQuotedName() {
        return this._quotedName_;
    }

    public void setQuotedName(TQuotedName node) {
        if (this._quotedName_ != null) {
            this._quotedName_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._quotedName_ = node;
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
        return toString(this._quotedName_) + toString(this._fixedArrayDescriptor_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._quotedName_ == child) {
            this._quotedName_ = null;
        } else if (this._fixedArrayDescriptor_ == child) {
            this._fixedArrayDescriptor_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._quotedName_ == oldChild) {
            setQuotedName((TQuotedName) newChild);
        } else if (this._fixedArrayDescriptor_ == oldChild) {
            setFixedArrayDescriptor((PFixedArrayDescriptor) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
