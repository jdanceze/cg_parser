package polyglot.ast;

import org.apache.commons.cli.HelpFormatter;
import polyglot.util.Enum;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Unary.class */
public interface Unary extends Expr {
    public static final Operator BIT_NOT = new Operator("~", true);
    public static final Operator NEG = new Operator(HelpFormatter.DEFAULT_OPT_PREFIX, true);
    public static final Operator POST_INC = new Operator("++", false);
    public static final Operator POST_DEC = new Operator(HelpFormatter.DEFAULT_LONG_OPT_PREFIX, false);
    public static final Operator PRE_INC = new Operator("++", true);
    public static final Operator PRE_DEC = new Operator(HelpFormatter.DEFAULT_LONG_OPT_PREFIX, true);
    public static final Operator POS = new Operator("+", true);
    public static final Operator NOT = new Operator("!", true);

    Expr expr();

    Unary expr(Expr expr);

    Operator operator();

    Unary operator(Operator operator);

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Unary$Operator.class */
    public static class Operator extends Enum {
        boolean prefix;
        String name;

        public Operator(String name, boolean prefix) {
            super(new StringBuffer().append(name).append(prefix ? "" : "post").toString());
            this.name = name;
            this.prefix = prefix;
        }

        public boolean isPrefix() {
            return this.prefix;
        }

        @Override // polyglot.util.Enum
        public String toString() {
            return this.name;
        }
    }
}
