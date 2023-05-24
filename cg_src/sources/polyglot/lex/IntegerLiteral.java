package polyglot.lex;

import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/lex/IntegerLiteral.class */
public class IntegerLiteral extends NumericLiteral {
    public IntegerLiteral(Position position, int i, int sym) {
        super(position, sym);
        this.val = new Integer(i);
    }
}
