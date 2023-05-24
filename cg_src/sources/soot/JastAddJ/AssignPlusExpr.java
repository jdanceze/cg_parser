package soot.JastAddJ;

import soot.Local;
import soot.Scene;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/AssignPlusExpr.class */
public class AssignPlusExpr extends AssignAdditiveExpr implements Cloneable {
    @Override // soot.JastAddJ.AssignAdditiveExpr, soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.AssignAdditiveExpr, soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.AssignAdditiveExpr, soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public AssignPlusExpr clone() throws CloneNotSupportedException {
        AssignPlusExpr node = (AssignPlusExpr) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            AssignPlusExpr node = clone();
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

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (!getDest().isVariable()) {
            error("left hand side is not a variable");
        } else if (getSource().type().isUnknown() || getDest().type().isUnknown()) {
        } else {
            if (getDest().type().isString() && !getSource().type().isVoid()) {
                return;
            }
            if (getSource().type().isBoolean() || getDest().type().isBoolean()) {
                error("Operator + does not operate on boolean types");
            } else if (getSource().type().isPrimitive() && getDest().type().isPrimitive()) {
            } else {
                error("can not assign " + getDest() + " of type " + getDest().type().typeName() + " a value of type " + sourceType().typeName());
            }
        }
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr
    public Value eval(Body b) {
        TypeDecl dest = getDest().type();
        TypeDecl source = getSource().type();
        if (dest.isString()) {
            Value lvalue = getDest().eval(b);
            Value v = asImmediate(b, lvalue);
            Local local = b.newTemp(b.newNewExpr(lookupType("java.lang", "StringBuffer").sootRef(), this));
            b.setLine(this);
            b.add(b.newInvokeStmt(b.newSpecialInvokeExpr(local, Scene.v().getMethod("<java.lang.StringBuffer: void <init>(java.lang.String)>").makeRef(), v, this), this));
            Local rightResult = b.newTemp(b.newVirtualInvokeExpr(local, lookupType("java.lang", "StringBuffer").methodWithArgs("append", new TypeDecl[]{source.stringPromotion()}).sootRef(), asImmediate(b, getSource().eval(b)), this));
            Local result = b.newTemp(b.newVirtualInvokeExpr(rightResult, Scene.v().getMethod("<java.lang.StringBuffer: java.lang.String toString()>").makeRef(), this));
            Value v2 = lvalue instanceof Local ? lvalue : (Value) lvalue.clone();
            getDest().emitStore(b, v2, result, this);
            return result;
        }
        return super.eval(b);
    }

    @Override // soot.JastAddJ.AssignExpr
    public Value createAssignOp(Body b, Value fst, Value snd) {
        return b.newAddExpr(asImmediate(b, fst), asImmediate(b, snd), this);
    }

    public AssignPlusExpr() {
    }

    @Override // soot.JastAddJ.AssignAdditiveExpr, soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public AssignPlusExpr(Expr p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.AssignAdditiveExpr, soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.AssignAdditiveExpr, soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.AssignAdditiveExpr, soot.JastAddJ.AssignExpr
    public void setDest(Expr node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.AssignAdditiveExpr, soot.JastAddJ.AssignExpr
    public Expr getDest() {
        return (Expr) getChild(0);
    }

    @Override // soot.JastAddJ.AssignAdditiveExpr, soot.JastAddJ.AssignExpr
    public Expr getDestNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.AssignAdditiveExpr, soot.JastAddJ.AssignExpr
    public void setSource(Expr node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.AssignAdditiveExpr, soot.JastAddJ.AssignExpr
    public Expr getSource() {
        return (Expr) getChild(1);
    }

    @Override // soot.JastAddJ.AssignAdditiveExpr, soot.JastAddJ.AssignExpr
    public Expr getSourceNoTransform() {
        return (Expr) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.AssignExpr
    public String printOp() {
        state();
        return " += ";
    }

    @Override // soot.JastAddJ.AssignExpr
    public TypeDecl sourceType() {
        state();
        TypeDecl left = getDest().type();
        TypeDecl right = getSource().type();
        if (!left.isString() && !right.isString()) {
            return super.sourceType();
        }
        if (left.isVoid() || right.isVoid()) {
            return unknownType();
        }
        return left.isString() ? left : right;
    }

    @Override // soot.JastAddJ.AssignAdditiveExpr, soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
