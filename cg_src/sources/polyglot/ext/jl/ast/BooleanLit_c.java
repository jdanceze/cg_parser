package polyglot.ext.jl.ast;

import polyglot.ast.BooleanLit;
import polyglot.ast.Node;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/BooleanLit_c.class */
public class BooleanLit_c extends Lit_c implements BooleanLit {
    protected boolean value;

    public BooleanLit_c(Position pos, boolean value) {
        super(pos);
        this.value = value;
    }

    @Override // polyglot.ast.BooleanLit
    public boolean value() {
        return this.value;
    }

    @Override // polyglot.ast.BooleanLit
    public BooleanLit value(boolean value) {
        BooleanLit_c n = (BooleanLit_c) copy();
        n.value = value;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        return type(tc.typeSystem().Boolean());
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write(String.valueOf(this.value));
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(value ").append(this.value).append(")").toString());
        w.end();
    }

    @Override // polyglot.ext.jl.ast.Lit_c, polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Object constantValue() {
        return Boolean.valueOf(this.value);
    }
}
