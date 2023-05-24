package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AInstanceofExpression.class */
public final class AInstanceofExpression extends PExpression {
    private PImmediate _immediate_;
    private TInstanceof _instanceof_;
    private PNonvoidType _nonvoidType_;

    public AInstanceofExpression() {
    }

    public AInstanceofExpression(PImmediate _immediate_, TInstanceof _instanceof_, PNonvoidType _nonvoidType_) {
        setImmediate(_immediate_);
        setInstanceof(_instanceof_);
        setNonvoidType(_nonvoidType_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AInstanceofExpression((PImmediate) cloneNode(this._immediate_), (TInstanceof) cloneNode(this._instanceof_), (PNonvoidType) cloneNode(this._nonvoidType_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAInstanceofExpression(this);
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

    public TInstanceof getInstanceof() {
        return this._instanceof_;
    }

    public void setInstanceof(TInstanceof node) {
        if (this._instanceof_ != null) {
            this._instanceof_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._instanceof_ = node;
    }

    public PNonvoidType getNonvoidType() {
        return this._nonvoidType_;
    }

    public void setNonvoidType(PNonvoidType node) {
        if (this._nonvoidType_ != null) {
            this._nonvoidType_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._nonvoidType_ = node;
    }

    public String toString() {
        return toString(this._immediate_) + toString(this._instanceof_) + toString(this._nonvoidType_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._immediate_ == child) {
            this._immediate_ = null;
        } else if (this._instanceof_ == child) {
            this._instanceof_ = null;
        } else if (this._nonvoidType_ == child) {
            this._nonvoidType_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._immediate_ == oldChild) {
            setImmediate((PImmediate) newChild);
        } else if (this._instanceof_ == oldChild) {
            setInstanceof((TInstanceof) newChild);
        } else if (this._nonvoidType_ == oldChild) {
            setNonvoidType((PNonvoidType) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
