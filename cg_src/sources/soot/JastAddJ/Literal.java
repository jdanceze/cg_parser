package soot.JastAddJ;

import beaver.Symbol;
import javax.resource.spi.work.WorkException;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Literal.class */
public abstract class Literal extends PrimaryExpr implements Cloneable {
    protected String tokenString_LITERAL;
    public int LITERALstart;
    public int LITERALend;
    protected boolean constant_computed = false;
    protected Constant constant_value;

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.constant_computed = false;
        this.constant_value = null;
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public Literal clone() throws CloneNotSupportedException {
        Literal node = (Literal) super.clone();
        node.constant_computed = false;
        node.constant_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    public static Literal buildBooleanLiteral(boolean value) {
        return new BooleanLiteral(value ? "true" : "false");
    }

    public static Literal buildStringLiteral(String value) {
        return new StringLiteral(value);
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(getLITERAL());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String escape(String s) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case '\b':
                    result.append("\\b");
                    break;
                case '\t':
                    result.append("\\t");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                case '\f':
                    result.append("\\f");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case '\"':
                    result.append("\\\"");
                    break;
                case '\'':
                    result.append("\\'");
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                default:
                    int value = s.charAt(i);
                    if (value < 32 || value > 126) {
                        result.append(asEscape(value));
                        break;
                    } else {
                        result.append(s.charAt(i));
                        break;
                    }
            }
        }
        return result.toString();
    }

    protected static String asEscape(int value) {
        StringBuffer s = new StringBuffer("\\u");
        String hex = Integer.toHexString(value);
        for (int i = 0; i < 4 - hex.length(); i++) {
            s.append(WorkException.UNDEFINED);
        }
        s.append(hex);
        return s.toString();
    }

    public Literal() {
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public Literal(String p0) {
        setLITERAL(p0);
    }

    public Literal(Symbol p0) {
        setLITERAL(p0);
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setLITERAL(String value) {
        this.tokenString_LITERAL = value;
    }

    public void setLITERAL(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setLITERAL is only valid for String lexemes");
        }
        this.tokenString_LITERAL = (String) symbol.value;
        this.LITERALstart = symbol.getStart();
        this.LITERALend = symbol.getEnd();
    }

    public String getLITERAL() {
        return this.tokenString_LITERAL != null ? this.tokenString_LITERAL : "";
    }

    public static Literal buildDoubleLiteral(double value) {
        String digits = Double.toString(value);
        NumericLiteral lit = new DoubleLiteral(digits);
        lit.setDigits(digits);
        lit.setKind(0);
        return lit;
    }

    public static Literal buildFloatLiteral(float value) {
        String digits = Float.toString(value);
        NumericLiteral lit = new FloatingPointLiteral(digits);
        lit.setDigits(digits);
        lit.setKind(0);
        return lit;
    }

    public static Literal buildIntegerLiteral(int value) {
        String digits = Integer.toHexString(value);
        NumericLiteral lit = new IntegerLiteral("0x" + digits);
        lit.setDigits(digits.toLowerCase());
        lit.setKind(1);
        return lit;
    }

    public static Literal buildLongLiteral(long value) {
        String digits = Long.toHexString(value);
        NumericLiteral lit = new LongLiteral("0x" + digits);
        lit.setDigits(digits.toLowerCase());
        lit.setKind(1);
        return lit;
    }

    @Override // soot.JastAddJ.Expr
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
        throw new UnsupportedOperationException("ConstantExpression operation constant not supported for type " + getClass().getName());
    }

    @Override // soot.JastAddJ.Expr
    public boolean isConstant() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.ASTNode
    public String dumpString() {
        state();
        return String.valueOf(getClass().getName()) + " [" + getLITERAL() + "]";
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
