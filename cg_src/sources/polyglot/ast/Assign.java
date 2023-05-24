package polyglot.ast;

import polyglot.util.Enum;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Assign.class */
public interface Assign extends Expr {
    public static final Operator ASSIGN = new Operator("=");
    public static final Operator ADD_ASSIGN = new Operator("+=");
    public static final Operator SUB_ASSIGN = new Operator("-=");
    public static final Operator MUL_ASSIGN = new Operator("*=");
    public static final Operator DIV_ASSIGN = new Operator("/=");
    public static final Operator MOD_ASSIGN = new Operator("%=");
    public static final Operator BIT_AND_ASSIGN = new Operator("&=");
    public static final Operator BIT_OR_ASSIGN = new Operator("|=");
    public static final Operator BIT_XOR_ASSIGN = new Operator("^=");
    public static final Operator SHL_ASSIGN = new Operator("<<=");
    public static final Operator SHR_ASSIGN = new Operator(">>=");
    public static final Operator USHR_ASSIGN = new Operator(">>>=");

    Expr left();

    Assign left(Expr expr);

    Operator operator();

    Assign operator(Operator operator);

    Expr right();

    Assign right(Expr expr);

    boolean throwsArithmeticException();

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Assign$Operator.class */
    public static class Operator extends Enum {
        public Operator(String name) {
            super(name);
        }
    }
}
