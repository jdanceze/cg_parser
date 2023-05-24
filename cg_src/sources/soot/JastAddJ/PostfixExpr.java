package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/PostfixExpr.class */
public abstract class PostfixExpr extends Unary implements Cloneable {
    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public PostfixExpr clone() throws CloneNotSupportedException {
        PostfixExpr node = (PostfixExpr) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
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
            error("postfix expressions only work on variables");
        } else if (!getOperand().type().isNumericType()) {
            error("postfix expressions only operates on numeric types");
        }
    }

    public PostfixExpr() {
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public PostfixExpr(Expr p0) {
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

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getOperandNoTransform()) {
            return NameType.EXPRESSION_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
