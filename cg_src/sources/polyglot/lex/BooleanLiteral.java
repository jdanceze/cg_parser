package polyglot.lex;

import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/lex/BooleanLiteral.class */
public class BooleanLiteral extends Literal {
    Boolean val;

    public BooleanLiteral(Position position, boolean b, int sym) {
        super(position, sym);
        this.val = Boolean.valueOf(b);
    }

    public Boolean getValue() {
        return this.val;
    }

    public String toString() {
        return new StringBuffer().append("boolean literal ").append(this.val.toString()).toString();
    }
}
