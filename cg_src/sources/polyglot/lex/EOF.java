package polyglot.lex;

import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/lex/EOF.class */
public class EOF extends Token {
    public EOF(Position position, int sym) {
        super(position, sym);
    }

    public String toString() {
        return "end of file";
    }
}
