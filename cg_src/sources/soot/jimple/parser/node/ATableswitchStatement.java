package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/ATableswitchStatement.class */
public final class ATableswitchStatement extends PStatement {
    private TTableswitch _tableswitch_;
    private TLParen _lParen_;
    private PImmediate _immediate_;
    private TRParen _rParen_;
    private TLBrace _lBrace_;
    private final LinkedList<PCaseStmt> _caseStmt_ = new LinkedList<>();
    private TRBrace _rBrace_;
    private TSemicolon _semicolon_;

    public ATableswitchStatement() {
    }

    public ATableswitchStatement(TTableswitch _tableswitch_, TLParen _lParen_, PImmediate _immediate_, TRParen _rParen_, TLBrace _lBrace_, List<?> _caseStmt_, TRBrace _rBrace_, TSemicolon _semicolon_) {
        setTableswitch(_tableswitch_);
        setLParen(_lParen_);
        setImmediate(_immediate_);
        setRParen(_rParen_);
        setLBrace(_lBrace_);
        setCaseStmt(_caseStmt_);
        setRBrace(_rBrace_);
        setSemicolon(_semicolon_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new ATableswitchStatement((TTableswitch) cloneNode(this._tableswitch_), (TLParen) cloneNode(this._lParen_), (PImmediate) cloneNode(this._immediate_), (TRParen) cloneNode(this._rParen_), (TLBrace) cloneNode(this._lBrace_), cloneList(this._caseStmt_), (TRBrace) cloneNode(this._rBrace_), (TSemicolon) cloneNode(this._semicolon_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseATableswitchStatement(this);
    }

    public TTableswitch getTableswitch() {
        return this._tableswitch_;
    }

    public void setTableswitch(TTableswitch node) {
        if (this._tableswitch_ != null) {
            this._tableswitch_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tableswitch_ = node;
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

    public PImmediate getImmediate() {
        return this._immediate_;
    }

    public void setImmediate(PImmediate node) {
        if (this._immediate_ != null) {
            this._immediate_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._immediate_ = node;
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

    public LinkedList<PCaseStmt> getCaseStmt() {
        return this._caseStmt_;
    }

    public void setCaseStmt(List<?> list) {
        Iterator<PCaseStmt> it = this._caseStmt_.iterator();
        while (it.hasNext()) {
            it.next().parent(null);
        }
        this._caseStmt_.clear();
        for (Object obj_e : list) {
            PCaseStmt e = (PCaseStmt) obj_e;
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
            this._caseStmt_.add(e);
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
        return toString(this._tableswitch_) + toString(this._lParen_) + toString(this._immediate_) + toString(this._rParen_) + toString(this._lBrace_) + toString(this._caseStmt_) + toString(this._rBrace_) + toString(this._semicolon_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._tableswitch_ == child) {
            this._tableswitch_ = null;
        } else if (this._lParen_ == child) {
            this._lParen_ = null;
        } else if (this._immediate_ == child) {
            this._immediate_ = null;
        } else if (this._rParen_ == child) {
            this._rParen_ = null;
        } else if (this._lBrace_ == child) {
            this._lBrace_ = null;
        } else if (this._caseStmt_.remove(child)) {
        } else {
            if (this._rBrace_ == child) {
                this._rBrace_ = null;
            } else if (this._semicolon_ == child) {
                this._semicolon_ = null;
            } else {
                throw new RuntimeException("Not a child.");
            }
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._tableswitch_ == oldChild) {
            setTableswitch((TTableswitch) newChild);
        } else if (this._lParen_ == oldChild) {
            setLParen((TLParen) newChild);
        } else if (this._immediate_ == oldChild) {
            setImmediate((PImmediate) newChild);
        } else if (this._rParen_ == oldChild) {
            setRParen((TRParen) newChild);
        } else if (this._lBrace_ == oldChild) {
            setLBrace((TLBrace) newChild);
        } else {
            ListIterator<PCaseStmt> i = this._caseStmt_.listIterator();
            while (i.hasNext()) {
                if (i.next() == oldChild) {
                    if (newChild != null) {
                        i.set((PCaseStmt) newChild);
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
            } else if (this._semicolon_ == oldChild) {
                setSemicolon((TSemicolon) newChild);
            } else {
                throw new RuntimeException("Not a child.");
            }
        }
    }
}
