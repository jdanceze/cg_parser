package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ALabelStatement.class */
public final class ALabelStatement extends PStatement {
    private PLabelName _labelName_;
    private TColon _colon_;

    public ALabelStatement() {
    }

    public ALabelStatement(PLabelName _labelName_, TColon _colon_) {
        setLabelName(_labelName_);
        setColon(_colon_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ALabelStatement((PLabelName) cloneNode(this._labelName_), (TColon) cloneNode(this._colon_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseALabelStatement(this);
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

    public String toString() {
        return toString(this._labelName_) + toString(this._colon_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._labelName_ == child) {
            this._labelName_ = null;
        } else if (this._colon_ == child) {
            this._colon_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._labelName_ == oldChild) {
            setLabelName((PLabelName) newChild);
        } else if (this._colon_ == oldChild) {
            setColon((TColon) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
