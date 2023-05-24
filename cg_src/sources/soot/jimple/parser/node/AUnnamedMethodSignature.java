package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AUnnamedMethodSignature.class */
public final class AUnnamedMethodSignature extends PUnnamedMethodSignature {
    private TCmplt _cmplt_;
    private PType _type_;
    private TLParen _lParen_;
    private PParameterList _parameterList_;
    private TRParen _rParen_;
    private TCmpgt _cmpgt_;

    public AUnnamedMethodSignature() {
    }

    public AUnnamedMethodSignature(TCmplt _cmplt_, PType _type_, TLParen _lParen_, PParameterList _parameterList_, TRParen _rParen_, TCmpgt _cmpgt_) {
        setCmplt(_cmplt_);
        setType(_type_);
        setLParen(_lParen_);
        setParameterList(_parameterList_);
        setRParen(_rParen_);
        setCmpgt(_cmpgt_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AUnnamedMethodSignature((TCmplt) cloneNode(this._cmplt_), (PType) cloneNode(this._type_), (TLParen) cloneNode(this._lParen_), (PParameterList) cloneNode(this._parameterList_), (TRParen) cloneNode(this._rParen_), (TCmpgt) cloneNode(this._cmpgt_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAUnnamedMethodSignature(this);
    }

    public TCmplt getCmplt() {
        return this._cmplt_;
    }

    public void setCmplt(TCmplt node) {
        if (this._cmplt_ != null) {
            this._cmplt_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._cmplt_ = node;
    }

    public PType getType() {
        return this._type_;
    }

    public void setType(PType node) {
        if (this._type_ != null) {
            this._type_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._type_ = node;
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

    public PParameterList getParameterList() {
        return this._parameterList_;
    }

    public void setParameterList(PParameterList node) {
        if (this._parameterList_ != null) {
            this._parameterList_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._parameterList_ = node;
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

    public TCmpgt getCmpgt() {
        return this._cmpgt_;
    }

    public void setCmpgt(TCmpgt node) {
        if (this._cmpgt_ != null) {
            this._cmpgt_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._cmpgt_ = node;
    }

    public String toString() {
        return toString(this._cmplt_) + toString(this._type_) + toString(this._lParen_) + toString(this._parameterList_) + toString(this._rParen_) + toString(this._cmpgt_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._cmplt_ == child) {
            this._cmplt_ = null;
        } else if (this._type_ == child) {
            this._type_ = null;
        } else if (this._lParen_ == child) {
            this._lParen_ = null;
        } else if (this._parameterList_ == child) {
            this._parameterList_ = null;
        } else if (this._rParen_ == child) {
            this._rParen_ = null;
        } else if (this._cmpgt_ == child) {
            this._cmpgt_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._cmplt_ == oldChild) {
            setCmplt((TCmplt) newChild);
        } else if (this._type_ == oldChild) {
            setType((PType) newChild);
        } else if (this._lParen_ == oldChild) {
            setLParen((TLParen) newChild);
        } else if (this._parameterList_ == oldChild) {
            setParameterList((PParameterList) newChild);
        } else if (this._rParen_ == oldChild) {
            setRParen((TRParen) newChild);
        } else if (this._cmpgt_ == oldChild) {
            setCmpgt((TCmpgt) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
