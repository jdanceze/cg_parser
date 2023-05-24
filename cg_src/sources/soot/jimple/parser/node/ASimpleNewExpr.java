package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ASimpleNewExpr.class */
public final class ASimpleNewExpr extends PNewExpr {
    private TNew _new_;
    private PBaseType _baseType_;

    public ASimpleNewExpr() {
    }

    public ASimpleNewExpr(TNew _new_, PBaseType _baseType_) {
        setNew(_new_);
        setBaseType(_baseType_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ASimpleNewExpr((TNew) cloneNode(this._new_), (PBaseType) cloneNode(this._baseType_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseASimpleNewExpr(this);
    }

    public TNew getNew() {
        return this._new_;
    }

    public void setNew(TNew node) {
        if (this._new_ != null) {
            this._new_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._new_ = node;
    }

    public PBaseType getBaseType() {
        return this._baseType_;
    }

    public void setBaseType(PBaseType node) {
        if (this._baseType_ != null) {
            this._baseType_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._baseType_ = node;
    }

    public String toString() {
        return toString(this._new_) + toString(this._baseType_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._new_ == child) {
            this._new_ = null;
        } else if (this._baseType_ == child) {
            this._baseType_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._new_ == oldChild) {
            setNew((TNew) newChild);
        } else if (this._baseType_ == oldChild) {
            setBaseType((PBaseType) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
