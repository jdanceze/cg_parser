package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AArrayNewExpr.class */
public final class AArrayNewExpr extends PNewExpr {
    private TNewarray _newarray_;
    private TLParen _lParen_;
    private PNonvoidType _nonvoidType_;
    private TRParen _rParen_;
    private PFixedArrayDescriptor _fixedArrayDescriptor_;

    public AArrayNewExpr() {
    }

    public AArrayNewExpr(TNewarray _newarray_, TLParen _lParen_, PNonvoidType _nonvoidType_, TRParen _rParen_, PFixedArrayDescriptor _fixedArrayDescriptor_) {
        setNewarray(_newarray_);
        setLParen(_lParen_);
        setNonvoidType(_nonvoidType_);
        setRParen(_rParen_);
        setFixedArrayDescriptor(_fixedArrayDescriptor_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AArrayNewExpr((TNewarray) cloneNode(this._newarray_), (TLParen) cloneNode(this._lParen_), (PNonvoidType) cloneNode(this._nonvoidType_), (TRParen) cloneNode(this._rParen_), (PFixedArrayDescriptor) cloneNode(this._fixedArrayDescriptor_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAArrayNewExpr(this);
    }

    public TNewarray getNewarray() {
        return this._newarray_;
    }

    public void setNewarray(TNewarray node) {
        if (this._newarray_ != null) {
            this._newarray_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._newarray_ = node;
    }

    public TLParen getLParen() {
        return this._lParen_;
    }

    public void setLParen(TLParen node) {
        if (this._lParen_ != null) {
            this._lParen_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._lParen_ = node;
    }

    public PNonvoidType getNonvoidType() {
        return this._nonvoidType_;
    }

    public void setNonvoidType(PNonvoidType node) {
        if (this._nonvoidType_ != null) {
            this._nonvoidType_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._nonvoidType_ = node;
    }

    public TRParen getRParen() {
        return this._rParen_;
    }

    public void setRParen(TRParen node) {
        if (this._rParen_ != null) {
            this._rParen_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._rParen_ = node;
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
        return toString(this._newarray_) + toString(this._lParen_) + toString(this._nonvoidType_) + toString(this._rParen_) + toString(this._fixedArrayDescriptor_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._newarray_ == child) {
            this._newarray_ = null;
        } else if (this._lParen_ == child) {
            this._lParen_ = null;
        } else if (this._nonvoidType_ == child) {
            this._nonvoidType_ = null;
        } else if (this._rParen_ == child) {
            this._rParen_ = null;
        } else if (this._fixedArrayDescriptor_ == child) {
            this._fixedArrayDescriptor_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._newarray_ == oldChild) {
            setNewarray((TNewarray) newChild);
        } else if (this._lParen_ == oldChild) {
            setLParen((TLParen) newChild);
        } else if (this._nonvoidType_ == oldChild) {
            setNonvoidType((PNonvoidType) newChild);
        } else if (this._rParen_ == oldChild) {
            setRParen((TRParen) newChild);
        } else if (this._fixedArrayDescriptor_ == oldChild) {
            setFixedArrayDescriptor((PFixedArrayDescriptor) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
