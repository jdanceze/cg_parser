package polyglot.lex;

import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/lex/NumericLiteral.class */
public abstract class NumericLiteral extends Literal {
    Number val;

    public NumericLiteral(Position position, int sym) {
        super(position, sym);
    }

    public Number getValue() {
        return this.val;
    }

    public String toString() {
        return new StringBuffer().append("numeric literal ").append(this.val.toString()).toString();
    }
}
