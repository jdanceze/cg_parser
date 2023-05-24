package soot.JastAddJ;

import org.apache.commons.cli.HelpFormatter;
import soot.JastAddJ.ASTNode;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/MinusExpr.class */
public class MinusExpr extends Unary implements Cloneable {
    protected boolean type_computed = false;
    protected TypeDecl type_value;

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public MinusExpr clone() throws CloneNotSupportedException {
        MinusExpr node = (MinusExpr) super.clone();
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
            MinusExpr node = clone();
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
    public void typeCheck() {
        if (!getOperand().type().isNumericType()) {
            error("unary minus only operates on numeric types");
        }
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr
    public Value eval(Body b) {
        return b.newNegExpr(asImmediate(b, getOperand().eval(b)), this);
    }

    public MinusExpr() {
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public MinusExpr(Expr p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
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

    @Override // soot.JastAddJ.Expr
    public Constant constant() {
        state();
        return type().minus(getOperand().constant());
    }

    @Override // soot.JastAddJ.Expr
    public boolean isConstant() {
        state();
        return getOperand().isConstant();
    }

    @Override // soot.JastAddJ.Unary
    public String printPreOp() {
        state();
        return HelpFormatter.DEFAULT_OPT_PREFIX;
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr
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
        return getOperand().type().unaryNumericPromotion();
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        if ((getOperand() instanceof IntegerLiteral) && ((IntegerLiteral) getOperand()).isDecimal() && getOperand().isPositive()) {
            state().duringLiterals++;
            ASTNode result = rewriteRule0();
            state().duringLiterals--;
            return result;
        } else if ((getOperand() instanceof LongLiteral) && ((LongLiteral) getOperand()).isDecimal() && getOperand().isPositive()) {
            state().duringLiterals++;
            ASTNode result2 = rewriteRule1();
            state().duringLiterals--;
            return result2;
        } else {
            return super.rewriteTo();
        }
    }

    private IntegerLiteral rewriteRule0() {
        IntegerLiteral original = (IntegerLiteral) getOperand();
        IntegerLiteral literal = new IntegerLiteral(HelpFormatter.DEFAULT_OPT_PREFIX + original.getLITERAL());
        literal.setDigits(original.getDigits());
        literal.setKind(original.getKind());
        return literal;
    }

    private LongLiteral rewriteRule1() {
        LongLiteral original = (LongLiteral) getOperand();
        LongLiteral literal = new LongLiteral(HelpFormatter.DEFAULT_OPT_PREFIX + original.getLITERAL());
        literal.setDigits(original.getDigits());
        literal.setKind(original.getKind());
        return literal;
    }
}
