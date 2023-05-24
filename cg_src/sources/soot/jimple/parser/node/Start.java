package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/Start.class */
public final class Start extends Node {
    private PFile _pFile_;
    private EOF _eof_;

    public Start() {
    }

    public Start(PFile _pFile_, EOF _eof_) {
        setPFile(_pFile_);
        setEOF(_eof_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new Start((PFile) cloneNode(this._pFile_), (EOF) cloneNode(this._eof_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseStart(this);
    }

    public PFile getPFile() {
        return this._pFile_;
    }

    public void setPFile(PFile node) {
        if (this._pFile_ != null) {
            this._pFile_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._pFile_ = node;
    }

    public EOF getEOF() {
        return this._eof_;
    }

    public void setEOF(EOF node) {
        if (this._eof_ != null) {
            this._eof_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._eof_ = node;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._pFile_ == child) {
            this._pFile_ = null;
        } else if (this._eof_ == child) {
            this._eof_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._pFile_ == oldChild) {
            setPFile((PFile) newChild);
        } else if (this._eof_ == oldChild) {
            setEOF((EOF) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    public String toString() {
        return toString(this._pFile_) + toString(this._eof_);
    }
}
