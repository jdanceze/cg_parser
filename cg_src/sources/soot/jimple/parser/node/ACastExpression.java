package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ACastExpression.class */
public final class ACastExpression extends PExpression {
    private TLParen _lParen_;
    private PNonvoidType _nonvoidType_;
    private TRParen _rParen_;
    private PImmediate _immediate_;

    public ACastExpression() {
    }

    public ACastExpression(TLParen _lParen_, PNonvoidType _nonvoidType_, TRParen _rParen_, PImmediate _immediate_) {
        setLParen(_lParen_);
        setNonvoidType(_nonvoidType_);
        setRParen(_rParen_);
        setImmediate(_immediate_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ACastExpression((TLParen) cloneNode(this._lParen_), (PNonvoidType) cloneNode(this._nonvoidType_), (TRParen) cloneNode(this._rParen_), (PImmediate) cloneNode(this._immediate_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseACastExpression(this);
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

    public PImmediate getImmediate() {
        return this._immediate_;
    }

    public void setImmediate(PImmediate node) {
        if (this._immediate_ != null) {
            this._immediate_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._immediate_ = node;
    }

    public String toString() {
        return toString(this._lParen_) + toString(this._nonvoidType_) + toString(this._rParen_) + toString(this._immediate_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._lParen_ == child) {
            this._lParen_ = null;
        } else if (this._nonvoidType_ == child) {
            this._nonvoidType_ = null;
        } else if (this._rParen_ == child) {
            this._rParen_ = null;
        } else if (this._immediate_ == child) {
            this._immediate_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._lParen_ == oldChild) {
            setLParen((TLParen) newChild);
        } else if (this._nonvoidType_ == oldChild) {
            setNonvoidType((PNonvoidType) newChild);
        } else if (this._rParen_ == oldChild) {
            setRParen((TRParen) newChild);
        } else if (this._immediate_ == oldChild) {
            setImmediate((PImmediate) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
