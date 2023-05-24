package polyglot.ext.jl.qq;

import java.util.List;
import polyglot.lex.Token;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/qq/QQListToken.class */
public class QQListToken extends Token {
    List list;

    public QQListToken(Position position, List list, int sym) {
        super(position, sym);
        this.list = list;
    }

    public List list() {
        return this.list;
    }

    public String toString() {
        return new StringBuffer().append("qq(").append(this.list).append(")").toString();
    }
}
