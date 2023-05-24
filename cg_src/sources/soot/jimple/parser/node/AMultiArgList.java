package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AMultiArgList.class */
public final class AMultiArgList extends PArgList {
    private PImmediate _immediate_;
    private TComma _comma_;
    private PArgList _argList_;

    public AMultiArgList() {
    }

    public AMultiArgList(PImmediate _immediate_, TComma _comma_, PArgList _argList_) {
        setImmediate(_immediate_);
        setComma(_comma_);
        setArgList(_argList_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AMultiArgList((PImmediate) cloneNode(this._immediate_), (TComma) cloneNode(this._comma_), (PArgList) cloneNode(this._argList_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAMultiArgList(this);
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

    public PArgList getArgList() {
        return this._argList_;
    }

    public void setArgList(PArgList node) {
        if (this._argList_ != null) {
            this._argList_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._argList_ = node;
    }

    public String toString() {
        return toString(this._immediate_) + toString(this._comma_) + toString(this._argList_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._immediate_ == child) {
            this._immediate_ = null;
        } else if (this._comma_ == child) {
            this._comma_ = null;
        } else if (this._argList_ == child) {
            this._argList_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._immediate_ == oldChild) {
            setImmediate((PImmediate) newChild);
        } else if (this._comma_ == oldChild) {
            setComma((TComma) newChild);
        } else if (this._argList_ == oldChild) {
            setArgList((PArgList) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
