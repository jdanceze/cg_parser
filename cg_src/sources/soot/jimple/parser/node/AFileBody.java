package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AFileBody.class */
public final class AFileBody extends PFileBody {
    private TLBrace _lBrace_;
    private final LinkedList<PMember> _member_ = new LinkedList<>();
    private TRBrace _rBrace_;

    public AFileBody() {
    }

    public AFileBody(TLBrace _lBrace_, List<?> _member_, TRBrace _rBrace_) {
        setLBrace(_lBrace_);
        setMember(_member_);
        setRBrace(_rBrace_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AFileBody((TLBrace) cloneNode(this._lBrace_), cloneList(this._member_), (TRBrace) cloneNode(this._rBrace_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAFileBody(this);
    }

    public TLBrace getLBrace() {
        return this._lBrace_;
    }

    public void setLBrace(TLBrace node) {
        if (this._lBrace_ != null) {
            this._lBrace_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._lBrace_ = node;
    }

    public LinkedList<PMember> getMember() {
        return this._member_;
    }

    public void setMember(List<?> list) {
        Iterator<PMember> it = this._member_.iterator();
        while (it.hasNext()) {
            it.next().parent(null);
        }
        this._member_.clear();
        for (Object obj_e : list) {
            PMember e = (PMember) obj_e;
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
            this._member_.add(e);
        }
    }

    public TRBrace getRBrace() {
        return this._rBrace_;
    }

    public void setRBrace(TRBrace node) {
        if (this._rBrace_ != null) {
            this._rBrace_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._rBrace_ = node;
    }

    public String toString() {
        return toString(this._lBrace_) + toString(this._member_) + toString(this._rBrace_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._lBrace_ == child) {
            this._lBrace_ = null;
        } else if (this._member_.remove(child)) {
        } else {
            if (this._rBrace_ == child) {
                this._rBrace_ = null;
                return;
            }
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._lBrace_ == oldChild) {
            setLBrace((TLBrace) newChild);
            return;
        }
        ListIterator<PMember> i = this._member_.listIterator();
        while (i.hasNext()) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PMember) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (this._rBrace_ == oldChild) {
            setRBrace((TRBrace) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
