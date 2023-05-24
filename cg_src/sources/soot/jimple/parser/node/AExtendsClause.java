package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AExtendsClause.class */
public final class AExtendsClause extends PExtendsClause {
    private TExtends _extends_;
    private PClassName _className_;

    public AExtendsClause() {
    }

    public AExtendsClause(TExtends _extends_, PClassName _className_) {
        setExtends(_extends_);
        setClassName(_className_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AExtendsClause((TExtends) cloneNode(this._extends_), (PClassName) cloneNode(this._className_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAExtendsClause(this);
    }

    public TExtends getExtends() {
        return this._extends_;
    }

    public void setExtends(TExtends node) {
        if (this._extends_ != null) {
            this._extends_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._extends_ = node;
    }

    public PClassName getClassName() {
        return this._className_;
    }

    public void setClassName(PClassName node) {
        if (this._className_ != null) {
            this._className_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._className_ = node;
    }

    public String toString() {
        return toString(this._extends_) + toString(this._className_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._extends_ == child) {
            this._extends_ = null;
        } else if (this._className_ == child) {
            this._className_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._extends_ == oldChild) {
            setExtends((TExtends) newChild);
        } else if (this._className_ == oldChild) {
            setClassName((PClassName) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
