package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AThrowsClause.class */
public final class AThrowsClause extends PThrowsClause {
    private TThrows _throws_;
    private PClassNameList _classNameList_;

    public AThrowsClause() {
    }

    public AThrowsClause(TThrows _throws_, PClassNameList _classNameList_) {
        setThrows(_throws_);
        setClassNameList(_classNameList_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AThrowsClause((TThrows) cloneNode(this._throws_), (PClassNameList) cloneNode(this._classNameList_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAThrowsClause(this);
    }

    public TThrows getThrows() {
        return this._throws_;
    }

    public void setThrows(TThrows node) {
        if (this._throws_ != null) {
            this._throws_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._throws_ = node;
    }

    public PClassNameList getClassNameList() {
        return this._classNameList_;
    }

    public void setClassNameList(PClassNameList node) {
        if (this._classNameList_ != null) {
            this._classNameList_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._classNameList_ = node;
    }

    public String toString() {
        return toString(this._throws_) + toString(this._classNameList_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._throws_ == child) {
            this._throws_ = null;
        } else if (this._classNameList_ == child) {
            this._classNameList_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._throws_ == oldChild) {
            setThrows((TThrows) newChild);
        } else if (this._classNameList_ == oldChild) {
            setClassNameList((PClassNameList) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
