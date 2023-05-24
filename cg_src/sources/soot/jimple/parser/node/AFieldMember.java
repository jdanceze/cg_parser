package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AFieldMember.class */
public final class AFieldMember extends PMember {
    private final LinkedList<PModifier> _modifier_ = new LinkedList<>();
    private PType _type_;
    private PName _name_;
    private TSemicolon _semicolon_;

    public AFieldMember() {
    }

    public AFieldMember(List<?> _modifier_, PType _type_, PName _name_, TSemicolon _semicolon_) {
        setModifier(_modifier_);
        setType(_type_);
        setName(_name_);
        setSemicolon(_semicolon_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AFieldMember(cloneList(this._modifier_), (PType) cloneNode(this._type_), (PName) cloneNode(this._name_), (TSemicolon) cloneNode(this._semicolon_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAFieldMember(this);
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
        return toString(this._modifier_) + toString(this._type_) + toString(this._name_) + toString(this._semicolon_);
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
        } else if (this._semicolon_ == child) {
            this._semicolon_ = null;
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
        } else if (this._semicolon_ == oldChild) {
            setSemicolon((TSemicolon) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
