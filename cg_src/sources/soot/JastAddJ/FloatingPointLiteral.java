package soot.JastAddJ;

import beaver.Symbol;
import soot.JastAddJ.ASTNode;
import soot.Value;
import soot.jimple.FloatConstant;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/FloatingPointLiteral.class */
public class FloatingPointLiteral extends NumericLiteral implements Cloneable {
    protected TypeDecl type_value;
    protected boolean isZero_value;
    protected Constant constant_value;
    protected boolean type_computed = false;
    protected boolean isZero_computed = false;
    protected boolean constant_computed = false;

    @Override // soot.JastAddJ.NumericLiteral, soot.JastAddJ.Literal, soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
        this.isZero_computed = false;
        this.constant_computed = false;
        this.constant_value = null;
    }

    @Override // soot.JastAddJ.NumericLiteral, soot.JastAddJ.Literal, soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.NumericLiteral, soot.JastAddJ.Literal, soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public FloatingPointLiteral clone() throws CloneNotSupportedException {
        FloatingPointLiteral node = (FloatingPointLiteral) super.clone();
        node.type_computed = false;
        node.type_value = null;
        node.isZero_computed = false;
        node.constant_computed = false;
        node.constant_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.NumericLiteral, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            FloatingPointLiteral node = clone();
            node.parent = null;
            if (this.children != null) {
                node.children = (ASTNode[]) this.children.clone();
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.NumericLiteral, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> fullCopy() {
        ASTNode<ASTNode> copy = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    copy.setChild(child.fullCopy(), i);
                }
            }
        }
        return copy;
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (!isZero() && constant().floatValue() == 0.0f) {
            error("It is an error for nonzero floating-point " + getLITERAL() + " to round to zero");
        }
        if (constant().floatValue() == Float.NEGATIVE_INFINITY || constant().floatValue() == Float.POSITIVE_INFINITY) {
            error("It is an error for floating-point " + getLITERAL() + " to round to an infinity");
        }
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        return FloatConstant.v(constant().floatValue());
    }

    public FloatingPointLiteral() {
    }

    @Override // soot.JastAddJ.NumericLiteral, soot.JastAddJ.Literal, soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public FloatingPointLiteral(String p0) {
        setLITERAL(p0);
    }

    public FloatingPointLiteral(Symbol p0) {
        setLITERAL(p0);
    }

    @Override // soot.JastAddJ.NumericLiteral, soot.JastAddJ.Literal, soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.NumericLiteral, soot.JastAddJ.Literal, soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
    }

    @Override // soot.JastAddJ.NumericLiteral, soot.JastAddJ.Literal
    public void setLITERAL(String value) {
        this.tokenString_LITERAL = value;
    }

    @Override // soot.JastAddJ.NumericLiteral, soot.JastAddJ.Literal
    public void setLITERAL(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setLITERAL is only valid for String lexemes");
        }
        this.tokenString_LITERAL = (String) symbol.value;
        this.LITERALstart = symbol.getStart();
        this.LITERALend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.NumericLiteral, soot.JastAddJ.Literal
    public String getLITERAL() {
        return this.tokenString_LITERAL != null ? this.tokenString_LITERAL : "";
    }

    @Override // soot.JastAddJ.Literal, soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        super.toString(s);
    }

    @Override // soot.JastAddJ.NumericLiteral, soot.JastAddJ.Expr
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
        return typeFloat();
    }

    public boolean isZero() {
        if (this.isZero_computed) {
            return this.isZero_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isZero_value = isZero_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isZero_computed = true;
        }
        return this.isZero_value;
    }

    private boolean isZero_compute() {
        char c;
        for (int i = 0; i < this.digits.length() && (c = this.digits.charAt(i)) != 'e' && c != 'p'; i++) {
            if (c != '0' && c != '.') {
                return false;
            }
        }
        return true;
    }

    @Override // soot.JastAddJ.Literal, soot.JastAddJ.Expr
    public Constant constant() {
        if (this.constant_computed) {
            return this.constant_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.constant_value = constant_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.constant_computed = true;
        }
        return this.constant_value;
    }

    private Constant constant_compute() {
        try {
            return Constant.create(Float.parseFloat(getDigits()));
        } catch (NumberFormatException e) {
            Constant c = Constant.create(0.0f);
            c.error = true;
            return c;
        }
    }

    @Override // soot.JastAddJ.NumericLiteral
    public boolean needsRewrite() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.NumericLiteral, soot.JastAddJ.Literal, soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
