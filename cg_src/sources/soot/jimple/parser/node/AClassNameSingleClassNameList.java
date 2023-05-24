package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AClassNameSingleClassNameList.class */
public final class AClassNameSingleClassNameList extends PClassNameList {
    private PClassName _className_;

    public AClassNameSingleClassNameList() {
    }

    public AClassNameSingleClassNameList(PClassName _className_) {
        setClassName(_className_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AClassNameSingleClassNameList((PClassName) cloneNode(this._className_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAClassNameSingleClassNameList(this);
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
        return toString(this._className_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._className_ == child) {
            this._className_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._className_ == oldChild) {
            setClassName((PClassName) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
