package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ACaseStmt.class */
public final class ACaseStmt extends PCaseStmt {
    private PCaseLabel _caseLabel_;
    private TColon _colon_;
    private PGotoStmt _gotoStmt_;

    public ACaseStmt() {
    }

    public ACaseStmt(PCaseLabel _caseLabel_, TColon _colon_, PGotoStmt _gotoStmt_) {
        setCaseLabel(_caseLabel_);
        setColon(_colon_);
        setGotoStmt(_gotoStmt_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ACaseStmt((PCaseLabel) cloneNode(this._caseLabel_), (TColon) cloneNode(this._colon_), (PGotoStmt) cloneNode(this._gotoStmt_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseACaseStmt(this);
    }

    public PCaseLabel getCaseLabel() {
        return this._caseLabel_;
    }

    public void setCaseLabel(PCaseLabel node) {
        if (this._caseLabel_ != null) {
            this._caseLabel_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._caseLabel_ = node;
    }

    public TColon getColon() {
        return this._colon_;
    }

    public void setColon(TColon node) {
        if (this._colon_ != null) {
            this._colon_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._colon_ = node;
    }

    public PGotoStmt getGotoStmt() {
        return this._gotoStmt_;
    }

    public void setGotoStmt(PGotoStmt node) {
        if (this._gotoStmt_ != null) {
            this._gotoStmt_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._gotoStmt_ = node;
    }

    public String toString() {
        return toString(this._caseLabel_) + toString(this._colon_) + toString(this._gotoStmt_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._caseLabel_ == child) {
            this._caseLabel_ = null;
        } else if (this._colon_ == child) {
            this._colon_ = null;
        } else if (this._gotoStmt_ == child) {
            this._gotoStmt_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._caseLabel_ == oldChild) {
            setCaseLabel((PCaseLabel) newChild);
        } else if (this._colon_ == oldChild) {
            setColon((TColon) newChild);
        } else if (this._gotoStmt_ == oldChild) {
            setGotoStmt((PGotoStmt) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
