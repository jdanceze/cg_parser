package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AEntermonitorStatement.class */
public final class AEntermonitorStatement extends PStatement {
    private TEntermonitor _entermonitor_;
    private PImmediate _immediate_;
    private TSemicolon _semicolon_;

    public AEntermonitorStatement() {
    }

    public AEntermonitorStatement(TEntermonitor _entermonitor_, PImmediate _immediate_, TSemicolon _semicolon_) {
        setEntermonitor(_entermonitor_);
        setImmediate(_immediate_);
        setSemicolon(_semicolon_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AEntermonitorStatement((TEntermonitor) cloneNode(this._entermonitor_), (PImmediate) cloneNode(this._immediate_), (TSemicolon) cloneNode(this._semicolon_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAEntermonitorStatement(this);
    }

    public TEntermonitor getEntermonitor() {
        return this._entermonitor_;
    }

    public void setEntermonitor(TEntermonitor node) {
        if (this._entermonitor_ != null) {
            this._entermonitor_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._entermonitor_ = node;
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
        return toString(this._entermonitor_) + toString(this._immediate_) + toString(this._semicolon_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._entermonitor_ == child) {
            this._entermonitor_ = null;
        } else if (this._immediate_ == child) {
            this._immediate_ = null;
        } else if (this._semicolon_ == child) {
            this._semicolon_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._entermonitor_ == oldChild) {
            setEntermonitor((TEntermonitor) newChild);
        } else if (this._immediate_ == oldChild) {
            setImmediate((PImmediate) newChild);
        } else if (this._semicolon_ == oldChild) {
            setSemicolon((TSemicolon) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
