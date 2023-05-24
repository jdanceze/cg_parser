package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AFile.class */
public final class AFile extends PFile {
    private final LinkedList<PModifier> _modifier_ = new LinkedList<>();
    private PFileType _fileType_;
    private PClassName _className_;
    private PExtendsClause _extendsClause_;
    private PImplementsClause _implementsClause_;
    private PFileBody _fileBody_;

    public AFile() {
    }

    public AFile(List<?> _modifier_, PFileType _fileType_, PClassName _className_, PExtendsClause _extendsClause_, PImplementsClause _implementsClause_, PFileBody _fileBody_) {
        setModifier(_modifier_);
        setFileType(_fileType_);
        setClassName(_className_);
        setExtendsClause(_extendsClause_);
        setImplementsClause(_implementsClause_);
        setFileBody(_fileBody_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AFile(cloneList(this._modifier_), (PFileType) cloneNode(this._fileType_), (PClassName) cloneNode(this._className_), (PExtendsClause) cloneNode(this._extendsClause_), (PImplementsClause) cloneNode(this._implementsClause_), (PFileBody) cloneNode(this._fileBody_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAFile(this);
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

    public PFileType getFileType() {
        return this._fileType_;
    }

    public void setFileType(PFileType node) {
        if (this._fileType_ != null) {
            this._fileType_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._fileType_ = node;
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

    public PExtendsClause getExtendsClause() {
        return this._extendsClause_;
    }

    public void setExtendsClause(PExtendsClause node) {
        if (this._extendsClause_ != null) {
            this._extendsClause_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._extendsClause_ = node;
    }

    public PImplementsClause getImplementsClause() {
        return this._implementsClause_;
    }

    public void setImplementsClause(PImplementsClause node) {
        if (this._implementsClause_ != null) {
            this._implementsClause_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._implementsClause_ = node;
    }

    public PFileBody getFileBody() {
        return this._fileBody_;
    }

    public void setFileBody(PFileBody node) {
        if (this._fileBody_ != null) {
            this._fileBody_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._fileBody_ = node;
    }

    public String toString() {
        return toString(this._modifier_) + toString(this._fileType_) + toString(this._className_) + toString(this._extendsClause_) + toString(this._implementsClause_) + toString(this._fileBody_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._modifier_.remove(child)) {
            return;
        }
        if (this._fileType_ == child) {
            this._fileType_ = null;
        } else if (this._className_ == child) {
            this._className_ = null;
        } else if (this._extendsClause_ == child) {
            this._extendsClause_ = null;
        } else if (this._implementsClause_ == child) {
            this._implementsClause_ = null;
        } else if (this._fileBody_ == child) {
            this._fileBody_ = null;
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
        if (this._fileType_ == oldChild) {
            setFileType((PFileType) newChild);
        } else if (this._className_ == oldChild) {
            setClassName((PClassName) newChild);
        } else if (this._extendsClause_ == oldChild) {
            setExtendsClause((PExtendsClause) newChild);
        } else if (this._implementsClause_ == oldChild) {
            setImplementsClause((PImplementsClause) newChild);
        } else if (this._fileBody_ == oldChild) {
            setFileBody((PFileBody) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
