package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/AMultiParameterList.class */
public final class AMultiParameterList extends PParameterList {
    private PParameter _parameter_;
    private TComma _comma_;
    private PParameterList _parameterList_;

    public AMultiParameterList() {
    }

    public AMultiParameterList(PParameter _parameter_, TComma _comma_, PParameterList _parameterList_) {
        setParameter(_parameter_);
        setComma(_comma_);
        setParameterList(_parameterList_);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new AMultiParameterList((PParameter) cloneNode(this._parameter_), (TComma) cloneNode(this._comma_), (PParameterList) cloneNode(this._parameterList_));
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseAMultiParameterList(this);
    }

    public PParameter getParameter() {
        return this._parameter_;
    }

    public void setParameter(PParameter node) {
        if (this._parameter_ != null) {
            this._parameter_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._parameter_ = node;
    }

    public TComma getComma() {
        return this._comma_;
    }

    public void setComma(TComma node) {
        if (this._comma_ != null) {
            this._comma_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._comma_ = node;
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

    public String toString() {
        return toString(this._parameter_) + toString(this._comma_) + toString(this._parameterList_);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.jimple.parser.node.Node
    public void removeChild(Node child) {
        if (this._parameter_ == child) {
            this._parameter_ = null;
        } else if (this._comma_ == child) {
            this._comma_ = null;
        } else if (this._parameterList_ == child) {
            this._parameterList_ = null;
        } else {
            throw new RuntimeException("Not a child.");
        }
    }

    @Override // soot.jimple.parser.node.Node
    void replaceChild(Node oldChild, Node newChild) {
        if (this._parameter_ == oldChild) {
            setParameter((PParameter) newChild);
        } else if (this._comma_ == oldChild) {
            setComma((TComma) newChild);
        } else if (this._parameterList_ == oldChild) {
            setParameterList((PParameterList) newChild);
        } else {
            throw new RuntimeException("Not a child.");
        }
    }
}
