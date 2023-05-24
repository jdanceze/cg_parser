package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AArrayDescriptor.class */
public final class AArrayDescriptor extends PArrayDescriptor {
    private TLBracket _lBracket_;
    private PImmediate _immediate_;
    private TRBracket _rBracket_;

    public AArrayDescriptor() {
    }

    public AArrayDescriptor(TLBracket _lBracket_, PImmediate _immediate_, TRBracket _rBracket_) {
        setLBracket(_lBracket_);
        setImmediate(_immediate_);
        setRBracket(_rBracket_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AArrayDescriptor((TLBracket) cloneNode(this._lBracket_), (PImmediate) cloneNode(this._immediate_), (TRBracket) cloneNode(this._rBracket_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAArrayDescriptor(this);
    }

    public TLBracket getLBracket() {
        return this._lBracket_;
    }

    public void setLBracket(TLBracket node) {
        if (this._lBracket_ != null) {
            this._lBracket_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._lBracket_ = node;
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

    public TRBracket getRBracket() {
        return this._rBracket_;
    }

    public void setRBracket(TRBracket node) {
        if (this._rBracket_ != null) {
            this._rBracket_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._rBracket_ = node;
    }

    public String toString() {
        return toString(this._lBracket_) + toString(this._immediate_) + toString(this._rBracket_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._lBracket_ == child) {
            this._lBracket_ = null;
        } else if (this._immediate_ == child) {
            this._immediate_ = null;
        } else if (this._rBracket_ == child) {
            this._rBracket_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._lBracket_ == oldChild) {
            setLBracket((TLBracket) newChild);
        } else if (this._immediate_ == oldChild) {
            setImmediate((PImmediate) newChild);
        } else if (this._rBracket_ == oldChild) {
            setRBracket((TRBracket) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
