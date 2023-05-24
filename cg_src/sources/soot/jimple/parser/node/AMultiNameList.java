package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AMultiNameList.class */
public final class AMultiNameList extends PNameList {
    private PName _name_;
    private TComma _comma_;
    private PNameList _nameList_;

    public AMultiNameList() {
    }

    public AMultiNameList(PName _name_, TComma _comma_, PNameList _nameList_) {
        setName(_name_);
        setComma(_comma_);
        setNameList(_nameList_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AMultiNameList((PName) cloneNode(this._name_), (TComma) cloneNode(this._comma_), (PNameList) cloneNode(this._nameList_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAMultiNameList(this);
    }

    public PName getName() {
        return this._name_;
    }

    public void setName(PName node) {
        if (this._name_ != null) {
            this._name_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._name_ = node;
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

    public PNameList getNameList() {
        return this._nameList_;
    }

    public void setNameList(PNameList node) {
        if (this._nameList_ != null) {
            this._nameList_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._nameList_ = node;
    }

    public String toString() {
        return toString(this._name_) + toString(this._comma_) + toString(this._nameList_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._name_ == child) {
            this._name_ = null;
        } else if (this._comma_ == child) {
            this._comma_ = null;
        } else if (this._nameList_ == child) {
            this._nameList_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._name_ == oldChild) {
            setName((PName) newChild);
        } else if (this._comma_ == oldChild) {
            setComma((TComma) newChild);
        } else if (this._nameList_ == oldChild) {
            setNameList((PNameList) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
