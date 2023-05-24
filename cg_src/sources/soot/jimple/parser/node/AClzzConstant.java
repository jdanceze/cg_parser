package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AClzzConstant.class */
public final class AClzzConstant extends PConstant {
    private TClass _id_;
    private TStringConstant _stringConstant_;

    public AClzzConstant() {
    }

    public AClzzConstant(TClass _id_, TStringConstant _stringConstant_) {
        setId(_id_);
        setStringConstant(_stringConstant_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AClzzConstant((TClass) cloneNode(this._id_), (TStringConstant) cloneNode(this._stringConstant_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAClzzConstant(this);
    }

    public TClass getId() {
        return this._id_;
    }

    public void setId(TClass node) {
        if (this._id_ != null) {
            this._id_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._id_ = node;
    }

    public TStringConstant getStringConstant() {
        return this._stringConstant_;
    }

    public void setStringConstant(TStringConstant node) {
        if (this._stringConstant_ != null) {
            this._stringConstant_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._stringConstant_ = node;
    }

    public String toString() {
        return toString(this._id_) + toString(this._stringConstant_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._id_ == child) {
            this._id_ = null;
        } else if (this._stringConstant_ == child) {
            this._stringConstant_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._id_ == oldChild) {
            setId((TClass) newChild);
        } else if (this._stringConstant_ == oldChild) {
            setStringConstant((TStringConstant) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
