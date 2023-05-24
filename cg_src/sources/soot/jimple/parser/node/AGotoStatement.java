package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AGotoStatement.class */
public final class AGotoStatement extends PStatement {
    private PGotoStmt _gotoStmt_;

    public AGotoStatement() {
    }

    public AGotoStatement(PGotoStmt _gotoStmt_) {
        setGotoStmt(_gotoStmt_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AGotoStatement((PGotoStmt) cloneNode(this._gotoStmt_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAGotoStatement(this);
    }

    public PGotoStmt getGotoStmt() {
        return this._gotoStmt_;
    }

    public void setGotoStmt(PGotoStmt node) {
        if (this._gotoStmt_ != null) {
            this._gotoStmt_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._gotoStmt_ = node;
    }

    public String toString() {
        return toString(this._gotoStmt_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._gotoStmt_ == child) {
            this._gotoStmt_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._gotoStmt_ == oldChild) {
            setGotoStmt((PGotoStmt) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
