package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AFullMethodBody.class */
public final class AFullMethodBody extends PMethodBody {
    private TLBrace _lBrace_;
    private final LinkedList<PDeclaration> _declaration_ = new LinkedList<>();
    private final LinkedList<PStatement> _statement_ = new LinkedList<>();
    private final LinkedList<PCatchClause> _catchClause_ = new LinkedList<>();
    private TRBrace _rBrace_;

    public AFullMethodBody() {
    }

    public AFullMethodBody(TLBrace _lBrace_, List<?> _declaration_, List<?> _statement_, List<?> _catchClause_, TRBrace _rBrace_) {
        setLBrace(_lBrace_);
        setDeclaration(_declaration_);
        setStatement(_statement_);
        setCatchClause(_catchClause_);
        setRBrace(_rBrace_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AFullMethodBody((TLBrace) cloneNode(this._lBrace_), cloneList(this._declaration_), cloneList(this._statement_), cloneList(this._catchClause_), (TRBrace) cloneNode(this._rBrace_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAFullMethodBody(this);
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

    public LinkedList<PDeclaration> getDeclaration() {
        return this._declaration_;
    }

    public void setDeclaration(List<?> list) {
        Iterator<PDeclaration> it = this._declaration_.iterator();
        while (it.hasNext()) {
            it.next().parent(null);
        }
        this._declaration_.clear();
        for (Object obj_e : list) {
            PDeclaration e = (PDeclaration) obj_e;
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
            this._declaration_.add(e);
        }
    }

    public LinkedList<PStatement> getStatement() {
        return this._statement_;
    }

    public void setStatement(List<?> list) {
        Iterator<PStatement> it = this._statement_.iterator();
        while (it.hasNext()) {
            it.next().parent(null);
        }
        this._statement_.clear();
        for (Object obj_e : list) {
            PStatement e = (PStatement) obj_e;
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
            this._statement_.add(e);
        }
    }

    public LinkedList<PCatchClause> getCatchClause() {
        return this._catchClause_;
    }

    public void setCatchClause(List<?> list) {
        Iterator<PCatchClause> it = this._catchClause_.iterator();
        while (it.hasNext()) {
            it.next().parent(null);
        }
        this._catchClause_.clear();
        for (Object obj_e : list) {
            PCatchClause e = (PCatchClause) obj_e;
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
            this._catchClause_.add(e);
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
        return toString(this._lBrace_) + toString(this._declaration_) + toString(this._statement_) + toString(this._catchClause_) + toString(this._rBrace_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._lBrace_ == child) {
            this._lBrace_ = null;
        } else if (this._declaration_.remove(child) || this._statement_.remove(child) || this._catchClause_.remove(child)) {
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
        ListIterator<PDeclaration> i = this._declaration_.listIterator();
        while (i.hasNext()) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PDeclaration) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        ListIterator<PStatement> i2 = this._statement_.listIterator();
        while (i2.hasNext()) {
            if (i2.next() == oldChild) {
                if (newChild != null) {
                    i2.set((PStatement) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i2.remove();
                oldChild.parent(null);
                return;
            }
        }
        ListIterator<PCatchClause> i3 = this._catchClause_.listIterator();
        while (i3.hasNext()) {
            if (i3.next() == oldChild) {
                if (newChild != null) {
                    i3.set((PCatchClause) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i3.remove();
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
