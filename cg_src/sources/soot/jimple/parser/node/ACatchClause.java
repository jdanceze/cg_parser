package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ACatchClause.class */
public final class ACatchClause extends PCatchClause {
    private TCatch _catch_;
    private PClassName _name_;
    private TFrom _from_;
    private PLabelName _fromLabel_;
    private TTo _to_;
    private PLabelName _toLabel_;
    private TWith _with_;
    private PLabelName _withLabel_;
    private TSemicolon _semicolon_;

    public ACatchClause() {
    }

    public ACatchClause(TCatch _catch_, PClassName _name_, TFrom _from_, PLabelName _fromLabel_, TTo _to_, PLabelName _toLabel_, TWith _with_, PLabelName _withLabel_, TSemicolon _semicolon_) {
        setCatch(_catch_);
        setName(_name_);
        setFrom(_from_);
        setFromLabel(_fromLabel_);
        setTo(_to_);
        setToLabel(_toLabel_);
        setWith(_with_);
        setWithLabel(_withLabel_);
        setSemicolon(_semicolon_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ACatchClause((TCatch) cloneNode(this._catch_), (PClassName) cloneNode(this._name_), (TFrom) cloneNode(this._from_), (PLabelName) cloneNode(this._fromLabel_), (TTo) cloneNode(this._to_), (PLabelName) cloneNode(this._toLabel_), (TWith) cloneNode(this._with_), (PLabelName) cloneNode(this._withLabel_), (TSemicolon) cloneNode(this._semicolon_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseACatchClause(this);
    }

    public TCatch getCatch() {
        return this._catch_;
    }

    public void setCatch(TCatch node) {
        if (this._catch_ != null) {
            this._catch_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._catch_ = node;
    }

    public PClassName getName() {
        return this._name_;
    }

    public void setName(PClassName node) {
        if (this._name_ != null) {
            this._name_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._name_ = node;
    }

    public TFrom getFrom() {
        return this._from_;
    }

    public void setFrom(TFrom node) {
        if (this._from_ != null) {
            this._from_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._from_ = node;
    }

    public PLabelName getFromLabel() {
        return this._fromLabel_;
    }

    public void setFromLabel(PLabelName node) {
        if (this._fromLabel_ != null) {
            this._fromLabel_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._fromLabel_ = node;
    }

    public TTo getTo() {
        return this._to_;
    }

    public void setTo(TTo node) {
        if (this._to_ != null) {
            this._to_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._to_ = node;
    }

    public PLabelName getToLabel() {
        return this._toLabel_;
    }

    public void setToLabel(PLabelName node) {
        if (this._toLabel_ != null) {
            this._toLabel_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._toLabel_ = node;
    }

    public TWith getWith() {
        return this._with_;
    }

    public void setWith(TWith node) {
        if (this._with_ != null) {
            this._with_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._with_ = node;
    }

    public PLabelName getWithLabel() {
        return this._withLabel_;
    }

    public void setWithLabel(PLabelName node) {
        if (this._withLabel_ != null) {
            this._withLabel_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._withLabel_ = node;
    }

    public TSemicolon getSemicolon() {
        return this._semicolon_;
    }

    public void setSemicolon(TSemicolon node) {
        if (this._semicolon_ != null) {
            this._semicolon_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._semicolon_ = node;
    }

    public String toString() {
        return toString(this._catch_) + toString(this._name_) + toString(this._from_) + toString(this._fromLabel_) + toString(this._to_) + toString(this._toLabel_) + toString(this._with_) + toString(this._withLabel_) + toString(this._semicolon_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._catch_ == child) {
            this._catch_ = null;
        } else if (this._name_ == child) {
            this._name_ = null;
        } else if (this._from_ == child) {
            this._from_ = null;
        } else if (this._fromLabel_ == child) {
            this._fromLabel_ = null;
        } else if (this._to_ == child) {
            this._to_ = null;
        } else if (this._toLabel_ == child) {
            this._toLabel_ = null;
        } else if (this._with_ == child) {
            this._with_ = null;
        } else if (this._withLabel_ == child) {
            this._withLabel_ = null;
        } else if (this._semicolon_ == child) {
            this._semicolon_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._catch_ == oldChild) {
            setCatch((TCatch) newChild);
        } else if (this._name_ == oldChild) {
            setName((PClassName) newChild);
        } else if (this._from_ == oldChild) {
            setFrom((TFrom) newChild);
        } else if (this._fromLabel_ == oldChild) {
            setFromLabel((PLabelName) newChild);
        } else if (this._to_ == oldChild) {
            setTo((TTo) newChild);
        } else if (this._toLabel_ == oldChild) {
            setToLabel((PLabelName) newChild);
        } else if (this._with_ == oldChild) {
            setWith((TWith) newChild);
        } else if (this._withLabel_ == oldChild) {
            setWithLabel((PLabelName) newChild);
        } else if (this._semicolon_ == oldChild) {
            setSemicolon((TSemicolon) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
