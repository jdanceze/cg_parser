package polyglot.lex;

import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/lex/Operator.class */
public class Operator extends Token {
    String which;

    public Operator(Position position, String which, int sym) {
        super(position, sym);
        this.which = which;
    }

    public String toString() {
        return new StringBuffer().append("operator ").append(this.which).toString();
    }
}
