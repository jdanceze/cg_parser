package soot.JastAddJ;

import beaver.Symbol;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/NumericLiteral.class */
public class NumericLiteral extends Literal implements Cloneable {
    public static final int DECIMAL = 0;
    public static final int HEXADECIMAL = 1;
    public static final int OCTAL = 2;
    public static final int BINARY = 3;
    private boolean whole;
    private boolean fraction;
    private boolean exponent;
    private boolean floating;
    private boolean isFloat;
    private boolean isLong;
    protected TypeDecl type_value;
    protected String digits = "";
    protected int kind = 0;
    private StringBuffer buf = new StringBuffer();
    private int idx = 0;
    protected boolean type_computed = false;

    @Override // soot.JastAddJ.Literal, soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.Literal, soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Literal, soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public NumericLiteral clone() throws CloneNotSupportedException {
        NumericLiteral node = (NumericLiteral) super.clone();
        node.type_computed = false;
        node.type_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            NumericLiteral node = clone();
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
    @Override // soot.JastAddJ.ASTNode
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

    public void setDigits(String digits) {
        this.digits = digits;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    private String name() {
        String name;
        switch (this.kind) {
            case 0:
                name = "decimal";
                break;
            case 1:
                name = "hexadecimal";
                break;
            case 2:
                name = "octal";
                break;
            case 3:
            default:
                name = "binary";
                break;
        }
        if (this.floating) {
            return String.valueOf(name) + " floating point";
        }
        return name;
    }

    private void pushChar() {
        StringBuffer stringBuffer = this.buf;
        String literal = getLITERAL();
        int i = this.idx;
        this.idx = i + 1;
        stringBuffer.append(literal.charAt(i));
    }

    private void skip(int n) {
        this.idx += n;
    }

    private boolean have(int n) {
        return getLITERAL().length() >= this.idx + n;
    }

    private char peek(int n) {
        return getLITERAL().charAt(this.idx + n);
    }

    private static final boolean isDecimalDigit(char c) {
        if (c != '_') {
            return c >= '0' && c <= '9';
        }
        return true;
    }

    private static final boolean isHexadecimalDigit(char c) {
        if (c != '_') {
            if (c < '0' || c > '9') {
                if (c < 'a' || c > 'f') {
                    return c >= 'A' && c <= 'F';
                }
                return true;
            }
            return true;
        }
        return true;
    }

    private static final boolean isBinaryDigit(char c) {
        return c == '_' || c == '0' || c == '1';
    }

    private static final boolean isUnderscore(char c) {
        return c == '_';
    }

    public Literal parse() {
        Literal literal;
        if (getLITERAL().length() == 0) {
            throw new IllegalStateException("Empty NumericLiteral");
        }
        this.kind = classifyLiteral();
        if (!this.floating) {
            literal = parseDigits();
        } else {
            literal = parseFractionPart();
        }
        literal.setStart(this.LITERALstart);
        literal.setEnd(this.LITERALend);
        return literal;
    }

    private int classifyLiteral() {
        if (peek(0) == '.') {
            this.floating = true;
            return 0;
        } else if (peek(0) != '0' || !have(2)) {
            return 0;
        } else {
            if (peek(1) == 'x' || peek(1) == 'X') {
                skip(2);
                return 1;
            } else if (peek(1) == 'b' || peek(1) == 'B') {
                skip(2);
                return 3;
            } else {
                return 0;
            }
        }
    }

    private boolean misplacedUnderscore() {
        if (this.idx == 0 || this.idx + 1 == getLITERAL().length()) {
            return true;
        }
        switch (this.kind) {
            case 0:
                return (isDecimalDigit(peek(-1)) && isDecimalDigit(peek(1))) ? false : true;
            case 1:
                return (isHexadecimalDigit(peek(-1)) && isHexadecimalDigit(peek(1))) ? false : true;
            case 2:
            default:
                throw new IllegalStateException("Unexpected literal kind");
            case 3:
                return (isBinaryDigit(peek(-1)) && isBinaryDigit(peek(1))) ? false : true;
        }
    }

    private Literal syntaxError(String msg) {
        String err = "in " + name() + " literal \"" + getLITERAL() + "\": " + msg;
        return new IllegalLiteral(err);
    }

    private Literal unexpectedCharacter(char c) {
        return syntaxError("unexpected character '" + c + "'; not a valid digit");
    }

    private String getLiteralString() {
        return this.buf.toString().toLowerCase();
    }

    private Literal buildLiteral() {
        NumericLiteral literal;
        setDigits(this.buf.toString().toLowerCase());
        if (!this.floating) {
            if (!this.whole) {
                return syntaxError("at least one digit is required");
            }
            if (this.kind == 0 && this.digits.charAt(0) == '0') {
                this.kind = 2;
                for (int idx = 1; idx < this.digits.length(); idx++) {
                    char c = this.digits.charAt(idx);
                    if (c < '0' || c > '7') {
                        return unexpectedCharacter(c);
                    }
                }
            }
            if (this.isLong) {
                literal = new LongLiteral(getLITERAL());
            } else {
                literal = new IntegerLiteral(getLITERAL());
            }
        } else if (this.kind == 1 && !this.exponent) {
            return syntaxError("exponent is required");
        } else {
            if (!this.whole && !this.fraction) {
                return syntaxError("at least one digit is required in either the whole or fraction part");
            }
            if (this.kind == 1) {
                this.digits = "0x" + this.digits;
            }
            if (this.isFloat) {
                literal = new FloatingPointLiteral(getLITERAL());
            } else {
                literal = new DoubleLiteral(getLITERAL());
            }
        }
        literal.setDigits(getDigits());
        literal.setKind(getKind());
        return literal;
    }

    private Literal parseDigits() {
        while (have(1)) {
            char c = peek(0);
            switch (c) {
                case '.':
                    if (this.kind != 0 && this.kind != 1) {
                        return unexpectedCharacter(c);
                    }
                    return parseFractionPart();
                case 'D':
                case 'd':
                    break;
                case 'F':
                case 'f':
                    if (this.kind == 3) {
                        return unexpectedCharacter(c);
                    }
                    this.isFloat = true;
                    break;
                case 'L':
                case 'l':
                    if (have(2)) {
                        return syntaxError("extra digits/characters after suffix " + c);
                    }
                    this.isLong = true;
                    skip(1);
                    continue;
                case '_':
                    if (misplacedUnderscore()) {
                        return syntaxError("misplaced underscore - underscores may only be used within sequences of digits");
                    }
                    skip(1);
                    continue;
                default:
                    switch (this.kind) {
                        case 0:
                            if (c == 'e' || c == 'E') {
                                return parseExponentPart();
                            }
                            if (c != 'f' && c != 'F') {
                                if (c == 'd' || c == 'D') {
                                    if (have(2)) {
                                        return syntaxError("extra digits/characters after type suffix " + c);
                                    }
                                    this.floating = true;
                                    skip(1);
                                    break;
                                } else if (!isDecimalDigit(c)) {
                                    return unexpectedCharacter(c);
                                } else {
                                    this.whole = true;
                                    pushChar();
                                    break;
                                }
                            } else if (have(2)) {
                                return syntaxError("extra digits/characters after type suffix " + c);
                            } else {
                                this.floating = true;
                                this.isFloat = true;
                                skip(1);
                                continue;
                            }
                            break;
                        case 1:
                            if (c == 'p' || c == 'P') {
                                return parseExponentPart();
                            }
                            if (!isHexadecimalDigit(c)) {
                                return unexpectedCharacter(c);
                            }
                            this.whole = true;
                            pushChar();
                            continue;
                        case 2:
                        default:
                            continue;
                        case 3:
                            if (!isBinaryDigit(c)) {
                                return unexpectedCharacter(c);
                            }
                            this.whole = true;
                            pushChar();
                            continue;
                    }
                    break;
            }
            if (this.kind == 3) {
                return unexpectedCharacter(c);
            }
            if (this.kind != 1) {
                if (have(2)) {
                    return syntaxError("extra digits/characters after type suffix " + c);
                }
                this.floating = true;
                skip(1);
            } else {
                this.whole = true;
                pushChar();
            }
        }
        return buildLiteral();
    }

    private Literal parseFractionPart() {
        this.floating = true;
        pushChar();
        while (have(1)) {
            char c = peek(0);
            switch (c) {
                case '.':
                    return syntaxError("multiple decimal periods are not allowed");
                case '_':
                    if (misplacedUnderscore()) {
                        return syntaxError("misplaced underscore - underscores may only be used as separators within sequences of valid digits");
                    }
                    skip(1);
                    break;
                default:
                    if (this.kind == 0) {
                        if (c == 'e' || c == 'E') {
                            return parseExponentPart();
                        }
                        if (c != 'f' && c != 'F') {
                            if (c == 'd' || c == 'D') {
                                if (have(2)) {
                                    return syntaxError("extra digits/characters after type suffix " + c);
                                }
                                this.floating = true;
                                skip(1);
                                break;
                            } else if (!isDecimalDigit(c)) {
                                return unexpectedCharacter(c);
                            } else {
                                pushChar();
                                this.fraction = true;
                                break;
                            }
                        } else if (have(2)) {
                            return syntaxError("extra digits/characters after type suffix " + c);
                        } else {
                            this.floating = true;
                            this.isFloat = true;
                            skip(1);
                            break;
                        }
                    } else if (c == 'p' || c == 'P') {
                        return parseExponentPart();
                    } else {
                        if (!isHexadecimalDigit(c)) {
                            return unexpectedCharacter(c);
                        }
                        this.fraction = true;
                        pushChar();
                        break;
                    }
            }
        }
        return buildLiteral();
    }

    private Literal parseExponentPart() {
        this.floating = true;
        pushChar();
        if (have(1) && (peek(0) == '+' || peek(0) == '-')) {
            pushChar();
        }
        while (have(1)) {
            char c = peek(0);
            switch (c) {
                case '+':
                case '-':
                    return syntaxError("exponent sign character is only allowed as the first character of the exponent part of a floating point literal");
                case '.':
                    return syntaxError("multiple decimal periods are not allowed");
                case 'D':
                case 'd':
                    break;
                case 'F':
                case 'f':
                    this.isFloat = true;
                    break;
                case 'P':
                case 'p':
                    return syntaxError("multiple exponent specifiers are not allowed");
                case '_':
                    if (misplacedUnderscore()) {
                        return syntaxError("misplaced underscore - underscores may only be used as separators within sequences of valid digits");
                    }
                    skip(1);
                    continue;
                default:
                    if (!isDecimalDigit(c)) {
                        return unexpectedCharacter(c);
                    }
                    pushChar();
                    this.exponent = true;
                    continue;
            }
            if (have(2)) {
                return syntaxError("extra digits/characters after type suffix " + c);
            }
            skip(1);
        }
        return buildLiteral();
    }

    public NumericLiteral() {
    }

    @Override // soot.JastAddJ.Literal, soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public NumericLiteral(String p0) {
        setLITERAL(p0);
    }

    public NumericLiteral(Symbol p0) {
        setLITERAL(p0);
    }

    @Override // soot.JastAddJ.Literal, soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.Literal, soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
    }

    @Override // soot.JastAddJ.Literal
    public void setLITERAL(String value) {
        this.tokenString_LITERAL = value;
    }

    @Override // soot.JastAddJ.Literal
    public void setLITERAL(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setLITERAL is only valid for String lexemes");
        }
        this.tokenString_LITERAL = (String) symbol.value;
        this.LITERALstart = symbol.getStart();
        this.LITERALend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.Literal
    public String getLITERAL() {
        return this.tokenString_LITERAL != null ? this.tokenString_LITERAL : "";
    }

    public long parseLong() {
        state();
        switch (getKind()) {
            case 0:
            default:
                return parseLongDecimal();
            case 1:
                return parseLongHexadecimal();
            case 2:
                return parseLongOctal();
            case 3:
                return parseLongBinary();
        }
    }

    public long parseLongHexadecimal() {
        state();
        long val = 0;
        if (this.digits.length() > 16) {
            for (int i = 0; i < this.digits.length() - 16; i++) {
                if (this.digits.charAt(i) != '0') {
                    throw new NumberFormatException("");
                }
            }
        }
        for (int i2 = 0; i2 < this.digits.length(); i2++) {
            int c = this.digits.charAt(i2);
            val = (val * 16) + ((c >= 97 && c <= 102) ? (c - 97) + 10 : c - 48);
        }
        return val;
    }

    public long parseLongOctal() {
        state();
        long val = 0;
        if (this.digits.length() > 21) {
            for (int i = 0; i < this.digits.length() - 21; i++) {
                if (i == (this.digits.length() - 21) - 1) {
                    if (this.digits.charAt(i) != '0' && this.digits.charAt(i) != '1') {
                        throw new NumberFormatException("");
                    }
                } else if (this.digits.charAt(i) != '0') {
                    throw new NumberFormatException("");
                }
            }
        }
        for (int i2 = 0; i2 < this.digits.length(); i2++) {
            int c = this.digits.charAt(i2) - '0';
            val = (val * 8) + c;
        }
        return val;
    }

    public long parseLongBinary() {
        state();
        long val = 0;
        if (this.digits.length() > 64) {
            for (int i = 0; i < this.digits.length() - 64; i++) {
                if (this.digits.charAt(i) != '0') {
                    throw new NumberFormatException("");
                }
            }
        }
        for (int i2 = 0; i2 < this.digits.length(); i2++) {
            if (this.digits.charAt(i2) == '1') {
                val |= 1 << ((this.digits.length() - i2) - 1);
            }
        }
        return val;
    }

    public long parseLongDecimal() {
        state();
        long val = 0;
        int i = 0;
        while (i < this.digits.length()) {
            long prev = val;
            int c = this.digits.charAt(i);
            if (c < 48 || c > 57) {
                throw new NumberFormatException("");
            }
            val = (val * 10) + (c - 48);
            if (val < prev) {
                boolean negMinValue = i == this.digits.length() - 1 && isNegative() && val == Long.MIN_VALUE;
                if (!negMinValue) {
                    throw new NumberFormatException("");
                }
            }
            i++;
        }
        if (val == Long.MIN_VALUE) {
            return val;
        }
        if (val < 0) {
            throw new NumberFormatException("");
        }
        return isNegative() ? -val : val;
    }

    public boolean needsRewrite() {
        state();
        return true;
    }

    public boolean isNegative() {
        state();
        return getLITERAL().charAt(0) == '-';
    }

    public String getDigits() {
        state();
        return this.digits;
    }

    public int getKind() {
        state();
        return this.kind;
    }

    public int getRadix() {
        state();
        switch (this.kind) {
            case 0:
            default:
                return 10;
            case 1:
                return 16;
            case 2:
                return 8;
            case 3:
                return 2;
        }
    }

    public boolean isDecimal() {
        state();
        return this.kind == 0;
    }

    public boolean isHex() {
        state();
        return this.kind == 1;
    }

    public boolean isOctal() {
        state();
        return this.kind == 2;
    }

    public boolean isBinary() {
        state();
        return this.kind == 3;
    }

    @Override // soot.JastAddJ.Expr
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
        return unknownType();
    }

    @Override // soot.JastAddJ.Literal, soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        if (needsRewrite()) {
            state().duringLiterals++;
            ASTNode result = rewriteRule0();
            state().duringLiterals--;
            return result;
        }
        return super.rewriteTo();
    }

    private Literal rewriteRule0() {
        return parse();
    }
}
