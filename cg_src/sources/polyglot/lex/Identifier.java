package polyglot.lex;

import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/lex/Identifier.class */
public class Identifier extends Token {
    String identifier;

    public Identifier(Position position, String identifier, int sym) {
        super(position, sym);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String toString() {
        return new StringBuffer().append("identifier \"").append(this.identifier).append("\"").toString();
    }
}
