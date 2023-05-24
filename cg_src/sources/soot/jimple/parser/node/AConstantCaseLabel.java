package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AConstantCaseLabel.class */
public final class AConstantCaseLabel extends PCaseLabel {
    private TCase _case_;
    private TMinus _minus_;
    private TIntegerConstant _integerConstant_;

    public AConstantCaseLabel() {
    }

    public AConstantCaseLabel(TCase _case_, TMinus _minus_, TIntegerConstant _integerConstant_) {
        setCase(_case_);
        setMinus(_minus_);
        setIntegerConstant(_integerConstant_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AConstantCaseLabel((TCase) cloneNode(this._case_), (TMinus) cloneNode(this._minus_), (TIntegerConstant) cloneNode(this._integerConstant_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAConstantCaseLabel(this);
    }

    public TCase getCase() {
        return this._case_;
    }

    public void setCase(TCase node) {
        if (this._case_ != null) {
            this._case_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._case_ = node;
    }

    public TMinus getMinus() {
        return this._minus_;
    }

    public void setMinus(TMinus node) {
        if (this._minus_ != null) {
            this._minus_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._minus_ = node;
    }

    public TIntegerConstant getIntegerConstant() {
        return this._integerConstant_;
    }

    public void setIntegerConstant(TIntegerConstant node) {
        if (this._integerConstant_ != null) {
            this._integerConstant_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._integerConstant_ = node;
    }

    public String toString() {
        return toString(this._case_) + toString(this._minus_) + toString(this._integerConstant_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._case_ == child) {
            this._case_ = null;
        } else if (this._minus_ == child) {
            this._minus_ = null;
        } else if (this._integerConstant_ == child) {
            this._integerConstant_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._case_ == oldChild) {
            setCase((TCase) newChild);
        } else if (this._minus_ == oldChild) {
            setMinus((TMinus) newChild);
        } else if (this._integerConstant_ == oldChild) {
            setIntegerConstant((TIntegerConstant) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
