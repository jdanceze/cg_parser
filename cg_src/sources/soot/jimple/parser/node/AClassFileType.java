package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AClassFileType.class */
public final class AClassFileType extends PFileType {
    private TClass _theclass_;

    public AClassFileType() {
    }

    public AClassFileType(TClass _theclass_) {
        setTheclass(_theclass_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AClassFileType((TClass) cloneNode(this._theclass_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAClassFileType(this);
    }

    public TClass getTheclass() {
        return this._theclass_;
    }

    public void setTheclass(TClass node) {
        if (this._theclass_ != null) {
            this._theclass_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._theclass_ = node;
    }

    public String toString() {
        return toString(this._theclass_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._theclass_ == child) {
            this._theclass_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._theclass_ == oldChild) {
            setTheclass((TClass) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
