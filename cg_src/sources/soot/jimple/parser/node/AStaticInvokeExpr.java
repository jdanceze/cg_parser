package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AStaticInvokeExpr.class */
public final class AStaticInvokeExpr extends PInvokeExpr {
    private TStaticinvoke _staticinvoke_;
    private PMethodSignature _methodSignature_;
    private TLParen _lParen_;
    private PArgList _argList_;
    private TRParen _rParen_;

    public AStaticInvokeExpr() {
    }

    public AStaticInvokeExpr(TStaticinvoke _staticinvoke_, PMethodSignature _methodSignature_, TLParen _lParen_, PArgList _argList_, TRParen _rParen_) {
        setStaticinvoke(_staticinvoke_);
        setMethodSignature(_methodSignature_);
        setLParen(_lParen_);
        setArgList(_argList_);
        setRParen(_rParen_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AStaticInvokeExpr((TStaticinvoke) cloneNode(this._staticinvoke_), (PMethodSignature) cloneNode(this._methodSignature_), (TLParen) cloneNode(this._lParen_), (PArgList) cloneNode(this._argList_), (TRParen) cloneNode(this._rParen_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAStaticInvokeExpr(this);
    }

    public TStaticinvoke getStaticinvoke() {
        return this._staticinvoke_;
    }

    public void setStaticinvoke(TStaticinvoke node) {
        if (this._staticinvoke_ != null) {
            this._staticinvoke_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._staticinvoke_ = node;
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
        return toString(this._staticinvoke_) + toString(this._methodSignature_) + toString(this._lParen_) + toString(this._argList_) + toString(this._rParen_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._staticinvoke_ == child) {
            this._staticinvoke_ = null;
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
        if (this._staticinvoke_ == oldChild) {
            setStaticinvoke((TStaticinvoke) newChild);
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
