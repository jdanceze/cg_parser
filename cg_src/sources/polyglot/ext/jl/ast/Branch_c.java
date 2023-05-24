package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.List;
import polyglot.ast.Branch;
import polyglot.ast.Term;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.PrettyPrinter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Branch_c.class */
public class Branch_c extends Stmt_c implements Branch {
    protected Branch.Kind kind;
    protected String label;

    public Branch_c(Position pos, Branch.Kind kind, String label) {
        super(pos);
        this.kind = kind;
        this.label = label;
    }

    @Override // polyglot.ast.Branch
    public Branch.Kind kind() {
        return this.kind;
    }

    @Override // polyglot.ast.Branch
    public Branch kind(Branch.Kind kind) {
        Branch_c n = (Branch_c) copy();
        n.kind = kind;
        return n;
    }

    @Override // polyglot.ast.Branch
    public String label() {
        return this.label;
    }

    @Override // polyglot.ast.Branch
    public Branch label(String label) {
        Branch_c n = (Branch_c) copy();
        n.label = label;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.kind.toString()).append(this.label != null ? new StringBuffer().append(Instruction.argsep).append(this.label).toString() : "").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write(this.kind.toString());
        if (this.label != null) {
            w.write(new StringBuffer().append(Instruction.argsep).append(this.label).toString());
        }
        w.write(";");
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitBranchTarget(this);
        return Collections.EMPTY_LIST;
    }
}
