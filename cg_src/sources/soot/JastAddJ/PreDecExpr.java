package soot.JastAddJ;

import org.apache.commons.cli.HelpFormatter;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/PreDecExpr.class */
public class PreDecExpr extends Unary implements Cloneable {
    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public PreDecExpr clone() throws CloneNotSupportedException {
        PreDecExpr node = (PreDecExpr) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            PreDecExpr node = clone();
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

    @Override // soot.JastAddJ.ASTNode
    public void definiteAssignment() {
        Variable v;
        if (getOperand().isVariable() && (v = getOperand().varDecl()) != null && v.isFinal()) {
            error("++ and -- can not be applied to final variable " + v);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.ASTNode
    public boolean checkDUeverywhere(Variable v) {
        if (getOperand().isVariable() && getOperand().varDecl() == v && !isDAbefore(v)) {
            return false;
        }
        return super.checkDUeverywhere(v);
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (!getOperand().isVariable()) {
            error("prefix decrement expression only work on variables");
        } else if (!getOperand().type().isNumericType()) {
            error("unary decrement only operates on numeric types");
        }
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr
    public Value eval(Body b) {
        return emitPrefix(b, -1);
    }

    public PreDecExpr() {
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public PreDecExpr(Expr p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.Unary
    public void setOperand(Expr node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.Unary
    public Expr getOperand() {
        return (Expr) getChild(0);
    }

    @Override // soot.JastAddJ.Unary
    public Expr getOperandNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.Unary
    public String printPreOp() {
        state();
        return HelpFormatter.DEFAULT_LONG_OPT_PREFIX;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
        if (caller == getOperandNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isDest(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
        if (caller == getOperandNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isIncOrDec(this, caller);
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
