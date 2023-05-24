package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AMultiNewExpr.class */
public final class AMultiNewExpr extends PNewExpr {
    private TNewmultiarray _newmultiarray_;
    private TLParen _lParen_;
    private PBaseType _baseType_;
    private TRParen _rParen_;
    private final LinkedList<PArrayDescriptor> _arrayDescriptor_ = new LinkedList<>();

    public AMultiNewExpr() {
    }

    public AMultiNewExpr(TNewmultiarray _newmultiarray_, TLParen _lParen_, PBaseType _baseType_, TRParen _rParen_, List<?> _arrayDescriptor_) {
        setNewmultiarray(_newmultiarray_);
        setLParen(_lParen_);
        setBaseType(_baseType_);
        setRParen(_rParen_);
        setArrayDescriptor(_arrayDescriptor_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AMultiNewExpr((TNewmultiarray) cloneNode(this._newmultiarray_), (TLParen) cloneNode(this._lParen_), (PBaseType) cloneNode(this._baseType_), (TRParen) cloneNode(this._rParen_), cloneList(this._arrayDescriptor_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAMultiNewExpr(this);
    }

    public TNewmultiarray getNewmultiarray() {
        return this._newmultiarray_;
    }

    public void setNewmultiarray(TNewmultiarray node) {
        if (this._newmultiarray_ != null) {
            this._newmultiarray_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._newmultiarray_ = node;
    }

    public TLParen getLParen() {
        return this._lParen_;
    }

    public void setLParen(TLParen node) {
        if (this._lParen_ != null) {
            this._lParen_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._lParen_ = node;
    }

    public PBaseType getBaseType() {
        return this._baseType_;
    }

    public void setBaseType(PBaseType node) {
        if (this._baseType_ != null) {
            this._baseType_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._baseType_ = node;
    }

    public TRParen getRParen() {
        return this._rParen_;
    }

    public void setRParen(TRParen node) {
        if (this._rParen_ != null) {
            this._rParen_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._rParen_ = node;
    }

    public LinkedList<PArrayDescriptor> getArrayDescriptor() {
        return this._arrayDescriptor_;
    }

    public void setArrayDescriptor(List<?> list) {
        Iterator<PArrayDescriptor> it = this._arrayDescriptor_.iterator();
        while (it.hasNext()) {
            it.next().parent(null);
        }
        this._arrayDescriptor_.clear();
        for (Object obj_e : list) {
            PArrayDescriptor e = (PArrayDescriptor) obj_e;
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
            this._arrayDescriptor_.add(e);
        }
    }

    public String toString() {
        return toString(this._newmultiarray_) + toString(this._lParen_) + toString(this._baseType_) + toString(this._rParen_) + toString(this._arrayDescriptor_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._newmultiarray_ == child) {
            this._newmultiarray_ = null;
        } else if (this._lParen_ == child) {
            this._lParen_ = null;
        } else if (this._baseType_ == child) {
            this._baseType_ = null;
        } else if (this._rParen_ == child) {
            this._rParen_ = null;
        } else if (this._arrayDescriptor_.remove(child)) {
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._newmultiarray_ == oldChild) {
            setNewmultiarray((TNewmultiarray) newChild);
        } else if (this._lParen_ == oldChild) {
            setLParen((TLParen) newChild);
        } else if (this._baseType_ == oldChild) {
            setBaseType((PBaseType) newChild);
        } else if (this._rParen_ == oldChild) {
            setRParen((TRParen) newChild);
        } else {
            ListIterator<PArrayDescriptor> i = this._arrayDescriptor_.listIterator();
            while (i.hasNext()) {
                if (i.next() == oldChild) {
                    if (newChild != null) {
                        i.set((PArrayDescriptor) newChild);
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
}
