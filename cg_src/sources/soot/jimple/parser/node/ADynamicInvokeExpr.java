package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ADynamicInvokeExpr.class */
public final class ADynamicInvokeExpr extends PInvokeExpr {
    private TDynamicinvoke _dynamicinvoke_;
    private TStringConstant _stringConstant_;
    private PUnnamedMethodSignature _dynmethod_;
    private TLParen _firstl_;
    private PArgList _dynargs_;
    private TRParen _firstr_;
    private PMethodSignature _bsm_;
    private TLParen _lParen_;
    private PArgList _staticargs_;
    private TRParen _rParen_;

    public ADynamicInvokeExpr() {
    }

    public ADynamicInvokeExpr(TDynamicinvoke _dynamicinvoke_, TStringConstant _stringConstant_, PUnnamedMethodSignature _dynmethod_, TLParen _firstl_, PArgList _dynargs_, TRParen _firstr_, PMethodSignature _bsm_, TLParen _lParen_, PArgList _staticargs_, TRParen _rParen_) {
        setDynamicinvoke(_dynamicinvoke_);
        setStringConstant(_stringConstant_);
        setDynmethod(_dynmethod_);
        setFirstl(_firstl_);
        setDynargs(_dynargs_);
        setFirstr(_firstr_);
        setBsm(_bsm_);
        setLParen(_lParen_);
        setStaticargs(_staticargs_);
        setRParen(_rParen_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ADynamicInvokeExpr((TDynamicinvoke) cloneNode(this._dynamicinvoke_), (TStringConstant) cloneNode(this._stringConstant_), (PUnnamedMethodSignature) cloneNode(this._dynmethod_), (TLParen) cloneNode(this._firstl_), (PArgList) cloneNode(this._dynargs_), (TRParen) cloneNode(this._firstr_), (PMethodSignature) cloneNode(this._bsm_), (TLParen) cloneNode(this._lParen_), (PArgList) cloneNode(this._staticargs_), (TRParen) cloneNode(this._rParen_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseADynamicInvokeExpr(this);
    }

    public TDynamicinvoke getDynamicinvoke() {
        return this._dynamicinvoke_;
    }

    public void setDynamicinvoke(TDynamicinvoke node) {
        if (this._dynamicinvoke_ != null) {
            this._dynamicinvoke_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._dynamicinvoke_ = node;
    }

    public TStringConstant getStringConstant() {
        return this._stringConstant_;
    }

    public void setStringConstant(TStringConstant node) {
        if (this._stringConstant_ != null) {
            this._stringConstant_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._stringConstant_ = node;
    }

    public PUnnamedMethodSignature getDynmethod() {
        return this._dynmethod_;
    }

    public void setDynmethod(PUnnamedMethodSignature node) {
        if (this._dynmethod_ != null) {
            this._dynmethod_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._dynmethod_ = node;
    }

    public TLParen getFirstl() {
        return this._firstl_;
    }

    public void setFirstl(TLParen node) {
        if (this._firstl_ != null) {
            this._firstl_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._firstl_ = node;
    }

    public PArgList getDynargs() {
        return this._dynargs_;
    }

    public void setDynargs(PArgList node) {
        if (this._dynargs_ != null) {
            this._dynargs_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._dynargs_ = node;
    }

    public TRParen getFirstr() {
        return this._firstr_;
    }

    public void setFirstr(TRParen node) {
        if (this._firstr_ != null) {
            this._firstr_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._firstr_ = node;
    }

    public PMethodSignature getBsm() {
        return this._bsm_;
    }

    public void setBsm(PMethodSignature node) {
        if (this._bsm_ != null) {
            this._bsm_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._bsm_ = node;
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

    public PArgList getStaticargs() {
        return this._staticargs_;
    }

    public void setStaticargs(PArgList node) {
        if (this._staticargs_ != null) {
            this._staticargs_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._staticargs_ = node;
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
        return toString(this._dynamicinvoke_) + toString(this._stringConstant_) + toString(this._dynmethod_) + toString(this._firstl_) + toString(this._dynargs_) + toString(this._firstr_) + toString(this._bsm_) + toString(this._lParen_) + toString(this._staticargs_) + toString(this._rParen_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._dynamicinvoke_ == child) {
            this._dynamicinvoke_ = null;
        } else if (this._stringConstant_ == child) {
            this._stringConstant_ = null;
        } else if (this._dynmethod_ == child) {
            this._dynmethod_ = null;
        } else if (this._firstl_ == child) {
            this._firstl_ = null;
        } else if (this._dynargs_ == child) {
            this._dynargs_ = null;
        } else if (this._firstr_ == child) {
            this._firstr_ = null;
        } else if (this._bsm_ == child) {
            this._bsm_ = null;
        } else if (this._lParen_ == child) {
            this._lParen_ = null;
        } else if (this._staticargs_ == child) {
            this._staticargs_ = null;
        } else if (this._rParen_ == child) {
            this._rParen_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._dynamicinvoke_ == oldChild) {
            setDynamicinvoke((TDynamicinvoke) newChild);
        } else if (this._stringConstant_ == oldChild) {
            setStringConstant((TStringConstant) newChild);
        } else if (this._dynmethod_ == oldChild) {
            setDynmethod((PUnnamedMethodSignature) newChild);
        } else if (this._firstl_ == oldChild) {
            setFirstl((TLParen) newChild);
        } else if (this._dynargs_ == oldChild) {
            setDynargs((PArgList) newChild);
        } else if (this._firstr_ == oldChild) {
            setFirstr((TRParen) newChild);
        } else if (this._bsm_ == oldChild) {
            setBsm((PMethodSignature) newChild);
        } else if (this._lParen_ == oldChild) {
            setLParen((TLParen) newChild);
        } else if (this._staticargs_ == oldChild) {
            setStaticargs((PArgList) newChild);
        } else if (this._rParen_ == oldChild) {
            setRParen((TRParen) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
