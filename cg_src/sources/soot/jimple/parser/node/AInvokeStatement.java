package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AInvokeStatement.class */
public final class AInvokeStatement extends PStatement {
    private PInvokeExpr _invokeExpr_;
    private TSemicolon _semicolon_;

    public AInvokeStatement() {
    }

    public AInvokeStatement(PInvokeExpr _invokeExpr_, TSemicolon _semicolon_) {
        setInvokeExpr(_invokeExpr_);
        setSemicolon(_semicolon_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AInvokeStatement((PInvokeExpr) cloneNode(this._invokeExpr_), (TSemicolon) cloneNode(this._semicolon_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAInvokeStatement(this);
    }

    public PInvokeExpr getInvokeExpr() {
        return this._invokeExpr_;
    }

    public void setInvokeExpr(PInvokeExpr node) {
        if (this._invokeExpr_ != null) {
            this._invokeExpr_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._invokeExpr_ = node;
    }

    public TSemicolon getSemicolon() {
        return this._semicolon_;
    }

    public void setSemicolon(TSemicolon node) {
        if (this._semicolon_ != null) {
            this._semicolon_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._semicolon_ = node;
    }

    public String toString() {
        return toString(this._invokeExpr_) + toString(this._semicolon_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._invokeExpr_ == child) {
            this._invokeExpr_ = null;
        } else if (this._semicolon_ == child) {
            this._semicolon_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._invokeExpr_ == oldChild) {
            setInvokeExpr((PInvokeExpr) newChild);
        } else if (this._semicolon_ == oldChild) {
            setSemicolon((TSemicolon) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
