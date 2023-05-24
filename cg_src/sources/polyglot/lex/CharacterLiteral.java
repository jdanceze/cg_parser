package polyglot.lex;

import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/lex/CharacterLiteral.class */
public class CharacterLiteral extends Literal {
    Character val;

    public CharacterLiteral(Position position, char c, int sym) {
        super(position, sym);
        this.val = new Character(c);
    }

    public Character getValue() {
        return this.val;
    }

    public String getEscapedValue() {
        return Token.escape(String.valueOf(this.val));
    }

    public String toString() {
        return new StringBuffer().append("char literal '").append(getEscapedValue()).append("'").toString();
    }
}
