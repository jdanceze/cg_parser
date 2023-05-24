package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AGotoStmt.class */
public final class AGotoStmt extends PGotoStmt {
    private TGoto _goto_;
    private PLabelName _labelName_;
    private TSemicolon _semicolon_;

    public AGotoStmt() {
    }

    public AGotoStmt(TGoto _goto_, PLabelName _labelName_, TSemicolon _semicolon_) {
        setGoto(_goto_);
        setLabelName(_labelName_);
        setSemicolon(_semicolon_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AGotoStmt((TGoto) cloneNode(this._goto_), (PLabelName) cloneNode(this._labelName_), (TSemicolon) cloneNode(this._semicolon_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAGotoStmt(this);
    }

    public TGoto getGoto() {
        return this._goto_;
    }

    public void setGoto(TGoto node) {
        if (this._goto_ != null) {
            this._goto_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._goto_ = node;
    }

    public PLabelName getLabelName() {
        return this._labelName_;
    }

    public void setLabelName(PLabelName node) {
        if (this._labelName_ != null) {
            this._labelName_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._labelName_ = node;
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
        return toString(this._goto_) + toString(this._labelName_) + toString(this._semicolon_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._goto_ == child) {
            this._goto_ = null;
        } else if (this._labelName_ == child) {
            this._labelName_ = null;
        } else if (this._semicolon_ == child) {
            this._semicolon_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._goto_ == oldChild) {
            setGoto((TGoto) newChild);
        } else if (this._labelName_ == oldChild) {
            setLabelName((PLabelName) newChild);
        } else if (this._semicolon_ == oldChild) {
            setSemicolon((TSemicolon) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
