package polyglot.parse;

import polyglot.ast.Expr;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/parse/VarDeclarator.class */
public class VarDeclarator {
    public Position pos;
    public String name;
    public int dims = 0;
    public Expr init = null;

    public VarDeclarator(Position pos, String name) {
        this.pos = pos;
        this.name = name;
    }

    public Position position() {
        return this.pos;
    }
}
