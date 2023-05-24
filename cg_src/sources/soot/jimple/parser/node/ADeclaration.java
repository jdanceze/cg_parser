package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ADeclaration.class */
public final class ADeclaration extends PDeclaration {
    private PJimpleType _jimpleType_;
    private PLocalNameList _localNameList_;
    private TSemicolon _semicolon_;

    public ADeclaration() {
    }

    public ADeclaration(PJimpleType _jimpleType_, PLocalNameList _localNameList_, TSemicolon _semicolon_) {
        setJimpleType(_jimpleType_);
        setLocalNameList(_localNameList_);
        setSemicolon(_semicolon_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ADeclaration((PJimpleType) cloneNode(this._jimpleType_), (PLocalNameList) cloneNode(this._localNameList_), (TSemicolon) cloneNode(this._semicolon_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseADeclaration(this);
    }

    public PJimpleType getJimpleType() {
        return this._jimpleType_;
    }

    public void setJimpleType(PJimpleType node) {
        if (this._jimpleType_ != null) {
            this._jimpleType_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._jimpleType_ = node;
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
        return toString(this._jimpleType_) + toString(this._localNameList_) + toString(this._semicolon_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._jimpleType_ == child) {
            this._jimpleType_ = null;
        } else if (this._localNameList_ == child) {
            this._localNameList_ = null;
        } else if (this._semicolon_ == child) {
            this._semicolon_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._jimpleType_ == oldChild) {
            setJimpleType((PJimpleType) newChild);
        } else if (this._localNameList_ == oldChild) {
            setLocalNameList((PLocalNameList) newChild);
        } else if (this._semicolon_ == oldChild) {
            setSemicolon((TSemicolon) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
