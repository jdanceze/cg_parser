package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AIdentityNoTypeStatement.class */
public final class AIdentityNoTypeStatement extends PStatement {
    private PLocalName _localName_;
    private TColonEquals _colonEquals_;
    private TAtIdentifier _atIdentifier_;
    private TSemicolon _semicolon_;

    public AIdentityNoTypeStatement() {
    }

    public AIdentityNoTypeStatement(PLocalName _localName_, TColonEquals _colonEquals_, TAtIdentifier _atIdentifier_, TSemicolon _semicolon_) {
        setLocalName(_localName_);
        setColonEquals(_colonEquals_);
        setAtIdentifier(_atIdentifier_);
        setSemicolon(_semicolon_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AIdentityNoTypeStatement((PLocalName) cloneNode(this._localName_), (TColonEquals) cloneNode(this._colonEquals_), (TAtIdentifier) cloneNode(this._atIdentifier_), (TSemicolon) cloneNode(this._semicolon_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAIdentityNoTypeStatement(this);
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

    public TColonEquals getColonEquals() {
        return this._colonEquals_;
    }

    public void setColonEquals(TColonEquals node) {
        if (this._colonEquals_ != null) {
            this._colonEquals_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._colonEquals_ = node;
    }

    public TAtIdentifier getAtIdentifier() {
        return this._atIdentifier_;
    }

    public void setAtIdentifier(TAtIdentifier node) {
        if (this._atIdentifier_ != null) {
            this._atIdentifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._atIdentifier_ = node;
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
        return toString(this._localName_) + toString(this._colonEquals_) + toString(this._atIdentifier_) + toString(this._semicolon_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._localName_ == child) {
            this._localName_ = null;
        } else if (this._colonEquals_ == child) {
            this._colonEquals_ = null;
        } else if (this._atIdentifier_ == child) {
            this._atIdentifier_ = null;
        } else if (this._semicolon_ == child) {
            this._semicolon_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._localName_ == oldChild) {
            setLocalName((PLocalName) newChild);
        } else if (this._colonEquals_ == oldChild) {
            setColonEquals((TColonEquals) newChild);
        } else if (this._atIdentifier_ == oldChild) {
            setAtIdentifier((TAtIdentifier) newChild);
        } else if (this._semicolon_ == oldChild) {
            setSemicolon((TSemicolon) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
