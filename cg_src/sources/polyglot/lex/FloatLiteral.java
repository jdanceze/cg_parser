package polyglot.lex;

import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/lex/FloatLiteral.class */
public class FloatLiteral extends NumericLiteral {
    public FloatLiteral(Position position, float f, int sym) {
        super(position, sym);
        this.val = new Float(f);
    }
}
