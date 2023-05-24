package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AMethodSignature.class */
public final class AMethodSignature extends PMethodSignature {
    private TCmplt _cmplt_;
    private PClassName _className_;
    private TColon _first_;
    private PType _type_;
    private PName _methodName_;
    private TLParen _lParen_;
    private PParameterList _parameterList_;
    private TRParen _rParen_;
    private TCmpgt _cmpgt_;

    public AMethodSignature() {
    }

    public AMethodSignature(TCmplt _cmplt_, PClassName _className_, TColon _first_, PType _type_, PName _methodName_, TLParen _lParen_, PParameterList _parameterList_, TRParen _rParen_, TCmpgt _cmpgt_) {
        setCmplt(_cmplt_);
        setClassName(_className_);
        setFirst(_first_);
        setType(_type_);
        setMethodName(_methodName_);
        setLParen(_lParen_);
        setParameterList(_parameterList_);
        setRParen(_rParen_);
        setCmpgt(_cmpgt_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AMethodSignature((TCmplt) cloneNode(this._cmplt_), (PClassName) cloneNode(this._className_), (TColon) cloneNode(this._first_), (PType) cloneNode(this._type_), (PName) cloneNode(this._methodName_), (TLParen) cloneNode(this._lParen_), (PParameterList) cloneNode(this._parameterList_), (TRParen) cloneNode(this._rParen_), (TCmpgt) cloneNode(this._cmpgt_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAMethodSignature(this);
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

    public PClassName getClassName() {
        return this._className_;
    }

    public void setClassName(PClassName node) {
        if (this._className_ != null) {
            this._className_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._className_ = node;
    }

    public TColon getFirst() {
        return this._first_;
    }

    public void setFirst(TColon node) {
        if (this._first_ != null) {
            this._first_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._first_ = node;
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

    public PName getMethodName() {
        return this._methodName_;
    }

    public void setMethodName(PName node) {
        if (this._methodName_ != null) {
            this._methodName_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._methodName_ = node;
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
        return toString(this._cmplt_) + toString(this._className_) + toString(this._first_) + toString(this._type_) + toString(this._methodName_) + toString(this._lParen_) + toString(this._parameterList_) + toString(this._rParen_) + toString(this._cmpgt_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._cmplt_ == child) {
            this._cmplt_ = null;
        } else if (this._className_ == child) {
            this._className_ = null;
        } else if (this._first_ == child) {
            this._first_ = null;
        } else if (this._type_ == child) {
            this._type_ = null;
        } else if (this._methodName_ == child) {
            this._methodName_ = null;
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
        } else if (this._className_ == oldChild) {
            setClassName((PClassName) newChild);
        } else if (this._first_ == oldChild) {
            setFirst((TColon) newChild);
        } else if (this._type_ == oldChild) {
            setType((PType) newChild);
        } else if (this._methodName_ == oldChild) {
            setMethodName((PName) newChild);
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
