package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ANonstaticInvokeExpr.class */
public final class ANonstaticInvokeExpr extends PInvokeExpr {
    private PNonstaticInvoke _nonstaticInvoke_;
    private PLocalName _localName_;
    private TDot _dot_;
    private PMethodSignature _methodSignature_;
    private TLParen _lParen_;
    private PArgList _argList_;
    private TRParen _rParen_;

    public ANonstaticInvokeExpr() {
    }

    public ANonstaticInvokeExpr(PNonstaticInvoke _nonstaticInvoke_, PLocalName _localName_, TDot _dot_, PMethodSignature _methodSignature_, TLParen _lParen_, PArgList _argList_, TRParen _rParen_) {
        setNonstaticInvoke(_nonstaticInvoke_);
        setLocalName(_localName_);
        setDot(_dot_);
        setMethodSignature(_methodSignature_);
        setLParen(_lParen_);
        setArgList(_argList_);
        setRParen(_rParen_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ANonstaticInvokeExpr((PNonstaticInvoke) cloneNode(this._nonstaticInvoke_), (PLocalName) cloneNode(this._localName_), (TDot) cloneNode(this._dot_), (PMethodSignature) cloneNode(this._methodSignature_), (TLParen) cloneNode(this._lParen_), (PArgList) cloneNode(this._argList_), (TRParen) cloneNode(this._rParen_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseANonstaticInvokeExpr(this);
    }

    public PNonstaticInvoke getNonstaticInvoke() {
        return this._nonstaticInvoke_;
    }

    public void setNonstaticInvoke(PNonstaticInvoke node) {
        if (this._nonstaticInvoke_ != null) {
            this._nonstaticInvoke_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._nonstaticInvoke_ = node;
    }

    public PLocalName getLocalName() {
        return this._localName_;
    }

    public void setLocalName(PLocalName node) {
        if (this._localName_ != null) {
            this._localName_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._localName_ = node;
    }

    public TDot getDot() {
        return this._dot_;
    }

    public void setDot(TDot node) {
        if (this._dot_ != null) {
            this._dot_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._dot_ = node;
    }

    public PMethodSignature getMethodSignature() {
        return this._methodSignature_;
    }

    public void setMethodSignature(PMethodSignature node) {
        if (this._methodSignature_ != null) {
            this._methodSignature_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._methodSignature_ = node;
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

    public PArgList getArgList() {
        return this._argList_;
    }

    public void setArgList(PArgList node) {
        if (this._argList_ != null) {
            this._argList_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._argList_ = node;
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

    public String toString() {
        return toString(this._nonstaticInvoke_) + toString(this._localName_) + toString(this._dot_) + toString(this._methodSignature_) + toString(this._lParen_) + toString(this._argList_) + toString(this._rParen_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._nonstaticInvoke_ == child) {
            this._nonstaticInvoke_ = null;
        } else if (this._localName_ == child) {
            this._localName_ = null;
        } else if (this._dot_ == child) {
            this._dot_ = null;
        } else if (this._methodSignature_ == child) {
            this._methodSignature_ = null;
        } else if (this._lParen_ == child) {
            this._lParen_ = null;
        } else if (this._argList_ == child) {
            this._argList_ = null;
        } else if (this._rParen_ == child) {
            this._rParen_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._nonstaticInvoke_ == oldChild) {
            setNonstaticInvoke((PNonstaticInvoke) newChild);
        } else if (this._localName_ == oldChild) {
            setLocalName((PLocalName) newChild);
        } else if (this._dot_ == oldChild) {
            setDot((TDot) newChild);
        } else if (this._methodSignature_ == oldChild) {
            setMethodSignature((PMethodSignature) newChild);
        } else if (this._lParen_ == oldChild) {
            setLParen((TLParen) newChild);
        } else if (this._argList_ == oldChild) {
            setArgList((PArgList) newChild);
        } else if (this._rParen_ == oldChild) {
            setRParen((TRParen) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
