package polyglot.lex;

import java_cup.runtime.Symbol;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/lex/Token.class */
public abstract class Token {
    Position position;
    int symbol;

    public Token(Position position, int symbol) {
        this.position = position;
        this.symbol = symbol;
    }

    public Position getPosition() {
        return this.position;
    }

    public Symbol symbol() {
        return new Symbol(this.symbol, this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String escape(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case 11:
                default:
                    if (s.charAt(i) < ' ' || (s.charAt(i) > '~' && s.charAt(i) < 255)) {
                        sb.append(new StringBuffer().append("\\").append(Integer.toOctalString(s.charAt(i))).toString());
                        break;
                    } else {
                        sb.append(s.charAt(i));
                        break;
                    }
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
            }
        }
        return sb.toString();
    }
}
