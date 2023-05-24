package polyglot.ext.jl.ast;

import polyglot.ast.CharLit;
import polyglot.ast.Node;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.util.StringUtil;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/CharLit_c.class */
public class CharLit_c extends NumLit_c implements CharLit {
    public CharLit_c(Position pos, char value) {
        super(pos, value);
    }

    @Override // polyglot.ast.CharLit
    public char value() {
        return (char) longValue();
    }

    @Override // polyglot.ast.CharLit
    public CharLit value(char value) {
        CharLit_c n = (CharLit_c) copy();
        n.value = value;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        return type(tc.typeSystem().Char());
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append("'").append(StringUtil.escape((char) this.value)).append("'").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("'");
        w.write(StringUtil.escape((char) this.value));
        w.write("'");
    }

    @Override // polyglot.ext.jl.ast.Lit_c, polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Object constantValue() {
        return new Character((char) this.value);
    }
}
