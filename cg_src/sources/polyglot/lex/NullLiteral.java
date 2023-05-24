package polyglot.lex;

import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/lex/NullLiteral.class */
public class NullLiteral extends Literal {
    public NullLiteral(Position position, int sym) {
        super(position, sym);
    }

    public String toString() {
        return "literal null";
    }
}
