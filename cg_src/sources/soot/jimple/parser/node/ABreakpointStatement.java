package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ABreakpointStatement.class */
public final class ABreakpointStatement extends PStatement {
    private TBreakpoint _breakpoint_;
    private TSemicolon _semicolon_;

    public ABreakpointStatement() {
    }

    public ABreakpointStatement(TBreakpoint _breakpoint_, TSemicolon _semicolon_) {
        setBreakpoint(_breakpoint_);
        setSemicolon(_semicolon_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ABreakpointStatement((TBreakpoint) cloneNode(this._breakpoint_), (TSemicolon) cloneNode(this._semicolon_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseABreakpointStatement(this);
    }

    public TBreakpoint getBreakpoint() {
        return this._breakpoint_;
    }

    public void setBreakpoint(TBreakpoint node) {
        if (this._breakpoint_ != null) {
            this._breakpoint_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._breakpoint_ = node;
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
        return toString(this._breakpoint_) + toString(this._semicolon_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._breakpoint_ == child) {
            this._breakpoint_ = null;
        } else if (this._semicolon_ == child) {
            this._semicolon_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._breakpoint_ == oldChild) {
            setBreakpoint((TBreakpoint) newChild);
        } else if (this._semicolon_ == oldChild) {
            setSemicolon((TSemicolon) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
