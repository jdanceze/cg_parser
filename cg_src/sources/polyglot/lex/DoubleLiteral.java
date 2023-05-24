package polyglot.lex;

import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/lex/DoubleLiteral.class */
public class DoubleLiteral extends NumericLiteral {
    public DoubleLiteral(Position position, double d, int sym) {
        super(position, sym);
        this.val = new Double(d);
    }
}
