package polyglot.ast;

import org.apache.commons.cli.HelpFormatter;
import polyglot.util.Enum;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Binary.class */
public interface Binary extends Expr {
    public static final Operator GT = new Operator(">", Precedence.RELATIONAL);
    public static final Operator LT = new Operator("<", Precedence.RELATIONAL);
    public static final Operator EQ = new Operator("==", Precedence.EQUAL);
    public static final Operator LE = new Operator("<=", Precedence.RELATIONAL);
    public static final Operator GE = new Operator(">=", Precedence.RELATIONAL);
    public static final Operator NE = new Operator("!=", Precedence.EQUAL);
    public static final Operator COND_OR = new Operator("||", Precedence.COND_OR);
    public static final Operator COND_AND = new Operator("&&", Precedence.COND_AND);
    public static final Operator ADD = new Operator("+", Precedence.ADD);
    public static final Operator SUB = new Operator(HelpFormatter.DEFAULT_OPT_PREFIX, Precedence.ADD);
    public static final Operator MUL = new Operator("*", Precedence.MUL);
    public static final Operator DIV = new Operator("/", Precedence.MUL);
    public static final Operator MOD = new Operator("%", Precedence.MUL);
    public static final Operator BIT_OR = new Operator("|", Precedence.BIT_OR);
    public static final Operator BIT_AND = new Operator("&", Precedence.BIT_AND);
    public static final Operator BIT_XOR = new Operator("^", Precedence.BIT_XOR);
    public static final Operator SHL = new Operator("<<", Precedence.SHIFT);
    public static final Operator SHR = new Operator(">>", Precedence.SHIFT);
    public static final Operator USHR = new Operator(">>>", Precedence.SHIFT);

    Expr left();

    Binary left(Expr expr);

    Operator operator();

    Binary operator(Operator operator);

    Expr right();

    Binary right(Expr expr);

    boolean throwsArithmeticException();

    Binary precedence(Precedence precedence);

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Binary$Operator.class */
    public static class Operator extends Enum {
        Precedence prec;

        public Operator(String name, Precedence prec) {
            super(name);
            this.prec = prec;
        }

        public Precedence precedence() {
            return this.prec;
        }
    }
}
