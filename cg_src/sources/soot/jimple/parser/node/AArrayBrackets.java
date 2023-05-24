package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AArrayBrackets.class */
public final class AArrayBrackets extends PArrayBrackets {
    private TLBracket _lBracket_;
    private TRBracket _rBracket_;

    public AArrayBrackets() {
    }

    public AArrayBrackets(TLBracket _lBracket_, TRBracket _rBracket_) {
        setLBracket(_lBracket_);
        setRBracket(_rBracket_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AArrayBrackets((TLBracket) cloneNode(this._lBracket_), (TRBracket) cloneNode(this._rBracket_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAArrayBrackets(this);
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
        return toString(this._lBracket_) + toString(this._rBracket_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._lBracket_ == child) {
            this._lBracket_ = null;
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
        } else if (this._rBracket_ == oldChild) {
            setRBracket((TRBracket) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
