package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AUnopExpr.class */
public final class AUnopExpr extends PUnopExpr {
    private PUnop _unop_;
    private PImmediate _immediate_;

    public AUnopExpr() {
    }

    public AUnopExpr(PUnop _unop_, PImmediate _immediate_) {
        setUnop(_unop_);
        setImmediate(_immediate_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AUnopExpr((PUnop) cloneNode(this._unop_), (PImmediate) cloneNode(this._immediate_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAUnopExpr(this);
    }

    public PUnop getUnop() {
        return this._unop_;
    }

    public void setUnop(PUnop node) {
        if (this._unop_ != null) {
            this._unop_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._unop_ = node;
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

    public String toString() {
        return toString(this._unop_) + toString(this._immediate_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._unop_ == child) {
            this._unop_ = null;
        } else if (this._immediate_ == child) {
            this._immediate_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._unop_ == oldChild) {
            setUnop((PUnop) newChild);
        } else if (this._immediate_ == oldChild) {
            setImmediate((PImmediate) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
