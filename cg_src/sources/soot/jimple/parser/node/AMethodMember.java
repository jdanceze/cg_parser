package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AMethodMember.class */
public final class AMethodMember extends PMember {
    private final LinkedList<PModifier> _modifier_ = new LinkedList<>();
    private PType _type_;
    private PName _name_;
    private TLParen _lParen_;
    private PParameterList _parameterList_;
    private TRParen _rParen_;
    private PThrowsClause _throwsClause_;
    private PMethodBody _methodBody_;

    public AMethodMember() {
    }

    public AMethodMember(List<?> _modifier_, PType _type_, PName _name_, TLParen _lParen_, PParameterList _parameterList_, TRParen _rParen_, PThrowsClause _throwsClause_, PMethodBody _methodBody_) {
        setModifier(_modifier_);
        setType(_type_);
        setName(_name_);
        setLParen(_lParen_);
        setParameterList(_parameterList_);
        setRParen(_rParen_);
        setThrowsClause(_throwsClause_);
        setMethodBody(_methodBody_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AMethodMember(cloneList(this._modifier_), (PType) cloneNode(this._type_), (PName) cloneNode(this._name_), (TLParen) cloneNode(this._lParen_), (PParameterList) cloneNode(this._parameterList_), (TRParen) cloneNode(this._rParen_), (PThrowsClause) cloneNode(this._throwsClause_), (PMethodBody) cloneNode(this._methodBody_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAMethodMember(this);
    }

    public LinkedList<PModifier> getModifier() {
        return this._modifier_;
    }

    public void setModifier(List<?> list) {
        Iterator<PModifier> it = this._modifier_.iterator();
        while (it.hasNext()) {
            it.next().parent(null);
        }
        this._modifier_.clear();
        for (Object obj_e : list) {
            PModifier e = (PModifier) obj_e;
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
            this._modifier_.add(e);
        }
    }

    public PType getType() {
        return this._type_;
    }

    public void setType(PType node) {
        if (this._type_ != null) {
            this._type_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._type_ = node;
    }

    public PName getName() {
        return this._name_;
    }

    public void setName(PName node) {
        if (this._name_ != null) {
            this._name_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._name_ = node;
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

    public PParameterList getParameterList() {
        return this._parameterList_;
    }

    public void setParameterList(PParameterList node) {
        if (this._parameterList_ != null) {
            this._parameterList_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._parameterList_ = node;
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

    public PThrowsClause getThrowsClause() {
        return this._throwsClause_;
    }

    public void setThrowsClause(PThrowsClause node) {
        if (this._throwsClause_ != null) {
            this._throwsClause_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._throwsClause_ = node;
    }

    public PMethodBody getMethodBody() {
        return this._methodBody_;
    }

    public void setMethodBody(PMethodBody node) {
        if (this._methodBody_ != null) {
            this._methodBody_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._methodBody_ = node;
    }

    public String toString() {
        return toString(this._modifier_) + toString(this._type_) + toString(this._name_) + toString(this._lParen_) + toString(this._parameterList_) + toString(this._rParen_) + toString(this._throwsClause_) + toString(this._methodBody_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._modifier_.remove(child)) {
            return;
        }
        if (this._type_ == child) {
            this._type_ = null;
        } else if (this._name_ == child) {
            this._name_ = null;
        } else if (this._lParen_ == child) {
            this._lParen_ = null;
        } else if (this._parameterList_ == child) {
            this._parameterList_ = null;
        } else if (this._rParen_ == child) {
            this._rParen_ = null;
        } else if (this._throwsClause_ == child) {
            this._throwsClause_ = null;
        } else if (this._methodBody_ == child) {
            this._methodBody_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        ListIterator<PModifier> i = this._modifier_.listIterator();
        while (i.hasNext()) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PModifier) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (this._type_ == oldChild) {
            setType((PType) newChild);
        } else if (this._name_ == oldChild) {
            setName((PName) newChild);
        } else if (this._lParen_ == oldChild) {
            setLParen((TLParen) newChild);
        } else if (this._parameterList_ == oldChild) {
            setParameterList((PParameterList) newChild);
        } else if (this._rParen_ == oldChild) {
            setRParen((TRParen) newChild);
        } else if (this._throwsClause_ == oldChild) {
            setThrowsClause((PThrowsClause) newChild);
        } else if (this._methodBody_ == oldChild) {
            setMethodBody((PMethodBody) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
