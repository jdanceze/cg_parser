package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AMultiLocalNameList.class */
public final class AMultiLocalNameList extends PLocalNameList {
    private PLocalName _localName_;
    private TComma _comma_;
    private PLocalNameList _localNameList_;

    public AMultiLocalNameList() {
    }

    public AMultiLocalNameList(PLocalName _localName_, TComma _comma_, PLocalNameList _localNameList_) {
        setLocalName(_localName_);
        setComma(_comma_);
        setLocalNameList(_localNameList_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AMultiLocalNameList((PLocalName) cloneNode(this._localName_), (TComma) cloneNode(this._comma_), (PLocalNameList) cloneNode(this._localNameList_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAMultiLocalNameList(this);
    }

    public PLocalName getLocalName() {
        return this._localName_;
    }

    public void setLocalName(PLocalName node) {
        if (this._localName_ != null) {
            this._localName_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._localName_ = node;
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

    public PLocalNameList getLocalNameList() {
        return this._localNameList_;
    }

    public void setLocalNameList(PLocalNameList node) {
        if (this._localNameList_ != null) {
            this._localNameList_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._localNameList_ = node;
    }

    public String toString() {
        return toString(this._localName_) + toString(this._comma_) + toString(this._localNameList_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._localName_ == child) {
            this._localName_ = null;
        } else if (this._comma_ == child) {
            this._comma_ = null;
        } else if (this._localNameList_ == child) {
            this._localNameList_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._localName_ == oldChild) {
            setLocalName((PLocalName) newChild);
        } else if (this._comma_ == oldChild) {
            setComma((TComma) newChild);
        } else if (this._localNameList_ == oldChild) {
            setLocalNameList((PLocalNameList) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
