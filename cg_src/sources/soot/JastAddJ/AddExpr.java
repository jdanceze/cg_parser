package soot.JastAddJ;

import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Scene;
import soot.Value;
import soot.jimple.StringConstant;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/AddExpr.class */
public class AddExpr extends AdditiveExpr implements Cloneable {
    protected boolean type_computed = false;
    protected TypeDecl type_value;

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public AddExpr clone() throws CloneNotSupportedException {
        AddExpr node = (AddExpr) super.clone();
        node.type_computed = false;
        node.type_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            AddExpr node = clone();
            node.parent = null;
            if (this.children != null) {
                node.children = (ASTNode[]) this.children.clone();
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: fullCopy */
    public ASTNode<ASTNode> fullCopy2() {
        ASTNode<ASTNode> copy2 = copy2();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    copy2.setChild(child.fullCopy(), i);
                }
            }
        }
        return copy2;
    }

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.ASTNode
    public void typeCheck() {
        TypeDecl left = getLeftOperand().type();
        TypeDecl right = getRightOperand().type();
        if (!left.isString() && !right.isString()) {
            super.typeCheck();
        } else if (left.isVoid()) {
            error("The type void of the left hand side is not numeric");
        } else if (right.isVoid()) {
            error("The type void of the right hand side is not numeric");
        }
    }

    @Override // soot.JastAddJ.Binary
    public Value emitOperation(Body b, Value left, Value right) {
        return asLocal(b, b.newAddExpr(asImmediate(b, left), asImmediate(b, right), this));
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr
    public Value eval(Body b) {
        Local v;
        if (type().isString() && isConstant()) {
            return StringConstant.v(constant().stringValue());
        }
        if (isStringAdd()) {
            if (firstStringAddPart()) {
                v = b.newTemp(b.newNewExpr(lookupType("java.lang", "StringBuffer").sootRef(), this));
                b.setLine(this);
                b.add(b.newInvokeStmt(b.newSpecialInvokeExpr(v, Scene.v().getMethod("<java.lang.StringBuffer: void <init>()>").makeRef(), this), this));
                b.setLine(this);
                b.add(b.newInvokeStmt(b.newVirtualInvokeExpr(v, lookupType("java.lang", "StringBuffer").methodWithArgs("append", new TypeDecl[]{getLeftOperand().type().stringPromotion()}).sootRef(), asImmediate(b, getLeftOperand().eval(b)), this), this));
            } else {
                v = (Local) getLeftOperand().eval(b);
            }
            b.setLine(this);
            b.add(b.newInvokeStmt(b.newVirtualInvokeExpr(v, lookupType("java.lang", "StringBuffer").methodWithArgs("append", new TypeDecl[]{getRightOperand().type().stringPromotion()}).sootRef(), asImmediate(b, getRightOperand().eval(b)), this), this));
            if (lastStringAddPart()) {
                return b.newTemp(b.newVirtualInvokeExpr(v, Scene.v().getMethod("<java.lang.StringBuffer: java.lang.String toString()>").makeRef(), this));
            }
            return v;
        }
        return b.newAddExpr(b.newTemp(getLeftOperand().type().emitCastTo(b, getLeftOperand(), type())), asImmediate(b, getRightOperand().type().emitCastTo(b, getRightOperand(), type())), this);
    }

    public AddExpr() {
    }

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public AddExpr(Expr p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public void setLeftOperand(Expr node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public Expr getLeftOperand() {
        return (Expr) getChild(0);
    }

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public Expr getLeftOperandNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public void setRightOperand(Expr node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public Expr getRightOperand() {
        return (Expr) getChild(1);
    }

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public Expr getRightOperandNoTransform() {
        return (Expr) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.Expr
    public Constant constant() {
        state();
        return type().add(getLeftOperand().constant(), getRightOperand().constant());
    }

    @Override // soot.JastAddJ.Binary
    public String printOp() {
        state();
        return " + ";
    }

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.Expr
    public TypeDecl type() {
        if (this.type_computed) {
            return this.type_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.type_value = type_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.type_computed = true;
        }
        return this.type_value;
    }

    private TypeDecl type_compute() {
        TypeDecl left = getLeftOperand().type();
        TypeDecl right = getRightOperand().type();
        if (!left.isString() && !right.isString()) {
            return super.type();
        }
        if (left.isVoid() || right.isVoid()) {
            return unknownType();
        }
        return left.isString() ? left : right;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean isStringAdd() {
        state();
        return type().isString() && !isConstant();
    }

    public boolean firstStringAddPart() {
        state();
        return type().isString() && !getLeftOperand().isStringAdd();
    }

    public boolean lastStringAddPart() {
        state();
        return !getParent().isStringAdd();
    }

    @Override // soot.JastAddJ.AdditiveExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
