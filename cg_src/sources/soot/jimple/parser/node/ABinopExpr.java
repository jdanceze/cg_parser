package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ABinopExpr.class */
public final class ABinopExpr extends PBinopExpr {
    private PImmediate _left_;
    private PBinop _binop_;
    private PImmediate _right_;

    public ABinopExpr() {
    }

    public ABinopExpr(PImmediate _left_, PBinop _binop_, PImmediate _right_) {
        setLeft(_left_);
        setBinop(_binop_);
        setRight(_right_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ABinopExpr((PImmediate) cloneNode(this._left_), (PBinop) cloneNode(this._binop_), (PImmediate) cloneNode(this._right_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseABinopExpr(this);
    }

    public PImmediate getLeft() {
        return this._left_;
    }

    public void setLeft(PImmediate node) {
        if (this._left_ != null) {
            this._left_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._left_ = node;
    }

    public PBinop getBinop() {
        return this._binop_;
    }

    public void setBinop(PBinop node) {
        if (this._binop_ != null) {
            this._binop_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._binop_ = node;
    }

    public PImmediate getRight() {
        return this._right_;
    }

    public void setRight(PImmediate node) {
        if (this._right_ != null) {
            this._right_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._right_ = node;
    }

    public String toString() {
        return toString(this._left_) + toString(this._binop_) + toString(this._right_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._left_ == child) {
            this._left_ = null;
        } else if (this._binop_ == child) {
            this._binop_ = null;
        } else if (this._right_ == child) {
            this._right_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._left_ == oldChild) {
            setLeft((PImmediate) newChild);
        } else if (this._binop_ == oldChild) {
            setBinop((PBinop) newChild);
        } else if (this._right_ == oldChild) {
            setRight((PImmediate) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
