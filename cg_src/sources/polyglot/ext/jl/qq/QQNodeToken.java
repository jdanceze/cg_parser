package polyglot.ext.jl.qq;

import polyglot.ast.Node;
import polyglot.ext.jl.Topics;
import polyglot.lex.Token;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/qq/QQNodeToken.class */
public class QQNodeToken extends Token {
    Node node;

    public QQNodeToken(Position position, Node node, int sym) {
        super(position, sym);
        this.node = node;
    }

    public Node node() {
        return this.node;
    }

    public String toString() {
        return new StringBuffer().append(Topics.qq).append(symbol()).append("(").append(this.node).append(")").toString();
    }
}
