package polyglot.lex;

import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/lex/StringLiteral.class */
public class StringLiteral extends Literal {
    String val;

    public StringLiteral(Position position, String s, int sym) {
        super(position, sym);
        this.val = s;
    }

    public String getValue() {
        return this.val;
    }

    public String toString() {
        return new StringBuffer().append("string literal \"").append(Token.escape(this.val)).append("\"").toString();
    }
}
