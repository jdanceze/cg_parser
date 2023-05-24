package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AQuotedNonvoidType.class */
public final class AQuotedNonvoidType extends PNonvoidType {
    private TQuotedName _quotedName_;
    private final LinkedList<PArrayBrackets> _arrayBrackets_ = new LinkedList<>();

    public AQuotedNonvoidType() {
    }

    public AQuotedNonvoidType(TQuotedName _quotedName_, List<?> _arrayBrackets_) {
        setQuotedName(_quotedName_);
        setArrayBrackets(_arrayBrackets_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AQuotedNonvoidType((TQuotedName) cloneNode(this._quotedName_), cloneList(this._arrayBrackets_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAQuotedNonvoidType(this);
    }

    public TQuotedName getQuotedName() {
        return this._quotedName_;
    }

    public void setQuotedName(TQuotedName node) {
        if (this._quotedName_ != null) {
            this._quotedName_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._quotedName_ = node;
    }

    public LinkedList<PArrayBrackets> getArrayBrackets() {
        return this._arrayBrackets_;
    }

    public void setArrayBrackets(List<?> list) {
        Iterator<PArrayBrackets> it = this._arrayBrackets_.iterator();
        while (it.hasNext()) {
            it.next().parent(null);
        }
        this._arrayBrackets_.clear();
        for (Object obj_e : list) {
            PArrayBrackets e = (PArrayBrackets) obj_e;
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
            this._arrayBrackets_.add(e);
        }
    }

    public String toString() {
        return toString(this._quotedName_) + toString(this._arrayBrackets_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._quotedName_ == child) {
            this._quotedName_ = null;
        } else if (this._arrayBrackets_.remove(child)) {
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._quotedName_ == oldChild) {
            setQuotedName((TQuotedName) newChild);
            return;
        }
        ListIterator<PArrayBrackets> i = this._arrayBrackets_.listIterator();
        while (i.hasNext()) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PArrayBrackets) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        throw new RuntimeException("Not a child.");
    }
}
