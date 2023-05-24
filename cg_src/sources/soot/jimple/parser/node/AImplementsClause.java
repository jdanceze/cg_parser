package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AImplementsClause.class */
public final class AImplementsClause extends PImplementsClause {
    private TImplements _implements_;
    private PClassNameList _classNameList_;

    public AImplementsClause() {
    }

    public AImplementsClause(TImplements _implements_, PClassNameList _classNameList_) {
        setImplements(_implements_);
        setClassNameList(_classNameList_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AImplementsClause((TImplements) cloneNode(this._implements_), (PClassNameList) cloneNode(this._classNameList_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAImplementsClause(this);
    }

    public TImplements getImplements() {
        return this._implements_;
    }

    public void setImplements(TImplements node) {
        if (this._implements_ != null) {
            this._implements_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._implements_ = node;
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
        return toString(this._implements_) + toString(this._classNameList_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._implements_ == child) {
            this._implements_ = null;
        } else if (this._classNameList_ == child) {
            this._classNameList_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._implements_ == oldChild) {
            setImplements((TImplements) newChild);
        } else if (this._classNameList_ == oldChild) {
            setClassNameList((PClassNameList) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
