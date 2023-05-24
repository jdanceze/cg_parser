package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ALocalFieldRef.class */
public final class ALocalFieldRef extends PFieldRef {
    private PLocalName _localName_;
    private TDot _dot_;
    private PFieldSignature _fieldSignature_;

    public ALocalFieldRef() {
    }

    public ALocalFieldRef(PLocalName _localName_, TDot _dot_, PFieldSignature _fieldSignature_) {
        setLocalName(_localName_);
        setDot(_dot_);
        setFieldSignature(_fieldSignature_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ALocalFieldRef((PLocalName) cloneNode(this._localName_), (TDot) cloneNode(this._dot_), (PFieldSignature) cloneNode(this._fieldSignature_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseALocalFieldRef(this);
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

    public TDot getDot() {
        return this._dot_;
    }

    public void setDot(TDot node) {
        if (this._dot_ != null) {
            this._dot_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._dot_ = node;
    }

    public PFieldSignature getFieldSignature() {
        return this._fieldSignature_;
    }

    public void setFieldSignature(PFieldSignature node) {
        if (this._fieldSignature_ != null) {
            this._fieldSignature_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._fieldSignature_ = node;
    }

    public String toString() {
        return toString(this._localName_) + toString(this._dot_) + toString(this._fieldSignature_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._localName_ == child) {
            this._localName_ = null;
        } else if (this._dot_ == child) {
            this._dot_ = null;
        } else if (this._fieldSignature_ == child) {
            this._fieldSignature_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._localName_ == oldChild) {
            setLocalName((PLocalName) newChild);
        } else if (this._dot_ == oldChild) {
            setDot((TDot) newChild);
        } else if (this._fieldSignature_ == oldChild) {
            setFieldSignature((PFieldSignature) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
