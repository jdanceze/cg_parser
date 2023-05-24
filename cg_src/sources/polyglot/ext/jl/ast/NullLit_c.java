package polyglot.ext.jl.ast;

import polyglot.ast.Node;
import polyglot.ast.NullLit;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/NullLit_c.class */
public class NullLit_c extends Lit_c implements NullLit {
    public NullLit_c(Position pos) {
        super(pos);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) {
        return type(tc.typeSystem().Null());
    }

    public Object objValue() {
        return null;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return Jimple.NULL;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write(Jimple.NULL);
    }

    @Override // polyglot.ext.jl.ast.Lit_c, polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Object constantValue() {
        return null;
    }
}
