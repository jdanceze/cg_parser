package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AClassNameMultiClassNameList.class */
public final class AClassNameMultiClassNameList extends PClassNameList {
    private PClassName _className_;
    private TComma _comma_;
    private PClassNameList _classNameList_;

    public AClassNameMultiClassNameList() {
    }

    public AClassNameMultiClassNameList(PClassName _className_, TComma _comma_, PClassNameList _classNameList_) {
        setClassName(_className_);
        setComma(_comma_);
        setClassNameList(_classNameList_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AClassNameMultiClassNameList((PClassName) cloneNode(this._className_), (TComma) cloneNode(this._comma_), (PClassNameList) cloneNode(this._classNameList_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAClassNameMultiClassNameList(this);
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

    public TComma getComma() {
        return this._comma_;
    }

    public void setComma(TComma node) {
        if (this._comma_ != null) {
            this._comma_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._comma_ = node;
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
        return toString(this._className_) + toString(this._comma_) + toString(this._classNameList_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._className_ == child) {
            this._className_ = null;
        } else if (this._comma_ == child) {
            this._comma_ = null;
        } else if (this._classNameList_ == child) {
            this._classNameList_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._className_ == oldChild) {
            setClassName((PClassName) newChild);
        } else if (this._comma_ == oldChild) {
            setComma((TComma) newChild);
        } else if (this._classNameList_ == oldChild) {
            setClassNameList((PClassNameList) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
